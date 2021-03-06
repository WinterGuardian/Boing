package me.winter.boing.detection.continuous;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import me.winter.boing.Collision;
import me.winter.boing.PhysicsWorld;
import me.winter.boing.colliders.Limit;
import me.winter.boing.detection.PooledDetector;

import static java.lang.Math.signum;
import static me.winter.boing.util.FloatUtil.DEFAULT_ULPS;
import static me.winter.boing.util.FloatUtil.areEqual;
import static me.winter.boing.util.FloatUtil.getGreatestULP;
import static me.winter.boing.util.FloatUtil.isGreaterOrEqual;
import static me.winter.boing.util.FloatUtil.isSmallerOrEqual;

/**
 * Detects collisions between 2 Limits. This is the only
 * continous collision detector since limits do not have any area.
 * <p>
 * Created by Alexander Winter on 2017-04-12.
 */
public class LimitLimitDetector extends PooledDetector<Limit, Limit>
{
	public LimitLimitDetector(Pool<Collision> collisionPool)
	{
		super(collisionPool);
	}

	@Override
	public Collision collides(PhysicsWorld world, Limit limitA, Limit limitB)
	{
		if(!areEqual(limitA.normal.dot(limitB.normal), -1))
			return null;

		final float normalX = limitA.normal.x; // normal X
		final float normalY = limitA.normal.y; // normal Y

		// position of the limit
		final float posAx = limitA.getAbsX();
		final float posAy = limitA.getAbsY();

		// same for b
		final float posBx = limitB.getAbsX();
		final float posBy = limitB.getAbsY();

		Vector2 movA = limitA.getMovement(world);
		Vector2 movB = limitB.getMovement(world);

		// movement of the bodies seen from each other
		float vecAx = movA.x, vecAy = movA.y, vecBx = movB.x, vecBy = movB.y;

		Vector2 shiftA = limitA.getCollisionShifting(world);
		Vector2 shiftB = limitB.getCollisionShifting(world);

		// compare values to check if collision occurs, if the other is getting away from the first,
		// his velocity is subtracted to see if any collision could occur in the case where the one getting away gets pushed back on the first
		float compAx = posAx, compAy = posAy, compBx = posBx, compBy = posBy;

		// if the velocity is not going along the normal (going against the direction limit is pointing at)
		if(signum(normalX) != signum(vecAx))
			// remove the velocity to feel the other body as it isn't moving
			compAx -= vecAx;

		// per component
		if(signum(normalY) != signum(vecAy))
			compAy -= vecAy;

		// same for B, B's normal is the opposite of A
		if(signum(-normalX) != signum(vecBx))
			compBx -= vecBx;

		if(signum(-normalY) != signum(vecBy))
			compBy -= vecBy;

		// if collision shifting is going along it's normal
		if(signum(normalX) == signum(shiftA.x))
			// then expect it to be pushed to there this frame too
			// (we assume its getting pushed by something)
			vecAx += shiftA.x;
		// else, collision shifting is going in the other direction
		// ignoring it is safer since we don't know for sure if
		// it will get pushed this frame too or if the pusher is B
		// (in the case were the pusher is B, this collision must happen
		// for the pushing not to drop)

		// collision shifting is per component
		if(signum(normalY) == signum(shiftA.y))
			vecAy += shiftA.y;

		// same for B, B's normal is the opposite of A
		if(signum(-normalX) == signum(shiftB.x))
			vecBx += shiftB.x;

		if(signum(-normalY) == signum(shiftB.y))
			vecBy += shiftB.y;

		// note that from this point, vec[AB][xy] and comp[AB][xy] are effectively final
		final float epsilon = DEFAULT_ULPS * getGreatestULP(posAx, posAy, posBx, posBy, vecAx, vecAy, vecBx, vecBy, limitA.size, limitB.size);

		// if limitB with his movement isn't after limitA with his movement
		// (aka the limits are still facing each other after having moved)
		if(!isGreaterOrEqual(compAx * normalX + compAy * normalY, compBx * normalX + compBy * normalY, epsilon))
			return null;

		// 'previous' position is assumed to be the current position minus
		// the fake movement we just assumed. This fake previous position is
		// used to make sure no collision drops by collision shitfing
		final float prevAx = posAx - vecAx;
		final float prevAy = posAy - vecAy;
		final float prevBx = posBx - vecBx;
		final float prevBy = posBy - vecBy;

		// if limitB isn't before limitA
		// (aka the limits at previous positions are not even facing each other)
		if(!isSmallerOrEqual(prevAx * normalX + prevAy * normalY, prevBx * normalX + prevBy * normalY, epsilon)) // if limitB isn't before limitA
			return null; // no collision

		final float hsizeA = limitA.size / 2; // half size for A
		final float hsizeB = limitB.size / 2; // half size for B

		// difference between their previous positions
		final float diff = (prevAx - prevBx) * normalX + (prevAy - prevBy) * normalY;

		// difference in their velocities
		final float vecDiff = (vecBx - vecAx) * normalX + (vecBy - vecAy) * normalY;

		// the "time" or ratio of dispatchment at which the the limits touch
		// if vecDiff is 0 they are moving at the same rate so any point would do
		final float collisionTime = vecDiff != 0 ? diff / vecDiff : 0f;

		// points at which the limits touch (or collide)
		final float collAx = prevAx + vecAx * collisionTime;
		final float collAy = prevAy + vecAy * collisionTime;
		final float collBx = prevBx + vecBx * collisionTime;
		final float collBy = prevBy + vecBy * collisionTime;

		// contact surface, used to detect if limits are on the same plane and
		// to fullfill the collision report
		// get surface contact at mid point
		final float surface = BoxBoxDetector.getContactSurface(collAx, collAy, hsizeA, collBx, collBy, hsizeB, normalX, normalY);

		// if 0, it might be a corner corner case
		if(!areEqual(surface, 0) && surface < 0)
			return null;

		// making the object that is used to report the collision
		Collision collision = collisionPool.obtain();

		collision.normal.set(normalX, normalY);

		collision.penetration = (cA, cB) -> getPenetration((Limit)cA, (Limit)cB);

		// boing v2 priority algorithm
		// collision.priority = surface * getPenetration(compAx, compAy, compBx, compBy, normalX, normalY);
		collision.priority = surface * getPenetration(limitA, limitB);
		// System.out.println(collision.priority);

		// re-get the position from the original calculation
		// since we are in a CollisionDynamicVariable, posAx, posAy etc. might
		// be outdated (cached values)
		collision.contactSurface = (cA, cB) -> getContactSurface((Limit)cA, (Limit)cB);

		collision.colliderA = limitA;
		collision.colliderB = limitB;
		collision.setImpactVelocities(limitA.getBody(), limitB.getBody());

		return collision;
	}

	public static float getPenetration(Limit limitA, Limit limitB)
	{
		return (limitA.getAbsX() - limitB.getAbsX()) * limitA.normal.x + (limitA.getAbsY() - limitB.getAbsY()) * limitA.normal.y;
	}

	public static float getPenetration(float ax, float ay, float bx, float by, float nx, float ny)
	{
		return (ax - bx) * nx + (ay - by) * ny;
	}

	public static float getContactSurface(Limit limitA, Limit limitB)
	{
		return BoxBoxDetector.getContactSurface(limitA.getAbsX(), limitA.getAbsY(), limitA.size / 2, limitB.getAbsX(), limitB.getAbsY(), limitB.size / 2, limitA.normal.x, limitA.normal.y);
	}
}
