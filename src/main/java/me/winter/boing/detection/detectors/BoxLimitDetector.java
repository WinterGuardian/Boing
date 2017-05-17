package me.winter.boing.detection.detectors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import me.winter.boing.Collision;
import me.winter.boing.detection.PooledDetector;
import me.winter.boing.DynamicBody;
import me.winter.boing.shapes.Box;
import me.winter.boing.shapes.Limit;

import static com.badlogic.gdx.math.Vector2.Zero;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static me.winter.boing.util.FloatUtil.areEqual;
import static me.winter.boing.util.FloatUtil.isGreaterOrEqual;
import static me.winter.boing.util.FloatUtil.isSmallerOrEqual;

/**
 * Detects collisions between an Axis Aligned Bounding Box and a Limit
 * <p>
 * Created by Alexander Winter on 2017-04-12.
 */
public class BoxLimitDetector extends PooledDetector<Box, Limit>
{
	public BoxLimitDetector(Pool<Collision> collisionPool)
	{
		super(collisionPool);
	}

	@Override
	public Collision collides(Box boxA, Limit limitB)
	{
		Vector2 vecA = boxA.getBody() instanceof DynamicBody
				? ((DynamicBody)boxA.getBody()).getMovement()
				: Zero;

		Vector2 vecB = limitB.getBody() instanceof DynamicBody
				? ((DynamicBody)limitB.getBody()).getMovement()
				: Zero;

		float ax = boxA.getAbsX(); //abs X for a
		float ay = boxA.getAbsY(); //abs Y for a
		float bx = limitB.getAbsX(); //abs X for b
		float by = limitB.getAbsY(); //abs Y for b
		float hsA; //half size of A (as a Limit)

		float nx = -limitB.normal.x; //normal X
		float ny = -limitB.normal.y; //normal Y

		float pax = boxA.getPrevAbsX(); //previous x for A
		float pay = boxA.getPrevAbsY(); //previous y for A

		if(abs(limitB.normal.x) > abs(limitB.normal.y)) //if collision is more horizontal than vertical
		{
			ax += nx * boxA.width / 2; //extends to side
			pax += nx * boxA.width / 2;
			hsA = boxA.height / 2;
		}
		else
		{
			ay += ny * boxA.height / 2; //extend to top/bottom
			pay += ny * boxA.height / 2;
			hsA = boxA.width / 2;
		}

		if(!isGreaterOrEqual(ax * nx + ay * ny, bx * nx + by * ny)) //if limitB with his velocity isn't after boxA with his velocity
			return null; //no collision

		float pbx = limitB.getPrevAbsX(); //previous x for B
		float pby = limitB.getPrevAbsY(); //previous y for B

		if(!isSmallerOrEqual(pax * nx + pay * ny, pbx * nx + pby * ny)) //if limitB isn't before boxA
			return null; //no collision


		float diff = (pbx - pax) * nx + (pby - pay) * ny;
		float vecDiff = (vecB.x - vecA.x) * nx + (vecB.y - vecA.y) * ny;

		float midpoint = vecDiff != 0 ? (diff + vecDiff) / vecDiff : 0f;

		float mxA = ax - vecA.x * midpoint; //midpoint x for A
		float myA = ay - vecA.y * midpoint; //midpoint y for A
		float mxB = bx - vecB.x * midpoint; //midpoint x for B
		float myB = by - vecB.y * midpoint; //midpoint y for B

		float hsB = limitB.size / 2; //half size for B

		float limitA1 = -ny * (mxA + hsA) + nx * (myA + hsA);
		float limitA2 = -ny * (mxA - hsA) + nx * (myA - hsA);
		float limitB1 = -ny * (mxB + hsB) + nx * (myB + hsB);
		float limitB2 = -ny * (mxB - hsB) + nx * (myB - hsB);

		float surface = min(max(limitA1, limitA2), max(limitB1, limitB2)) //minimum of the maximums
				- max(min(limitA1, limitA2), min(limitB1, limitB2)); //maximum of the minimums

		if(areEqual(surface, 0))
		{
			float sizeDiff = (hsB + hsA) * nx + (hsB + hsA) * ny;

			midpoint = vecDiff != 0 ? (diff + vecDiff + sizeDiff) / vecDiff : 0f;

			mxA = ax - vecA.x * midpoint; //midpoint x for A
			myA = ay - vecA.y * midpoint; //midpoint y for A
			mxB = bx - vecB.x * midpoint; //midpoint x for B
			myB = by - vecB.y * midpoint; //midpoint y for B

			limitA1 = -ny * (mxA + hsA) + nx * (myA + hsA);
			limitA2 = -ny * (mxA - hsA) + nx * (myA - hsA);
			limitB1 = -ny * (mxB + hsB) + nx * (myB + hsB);
			limitB2 = -ny * (mxB - hsB) + nx * (myB - hsB);

			surface = min(max(limitA1, limitA2), max(limitB1, limitB2)) //minimum of the maximums
					- max(min(limitA1, limitA2), min(limitB1, limitB2)); //maximum of the minimums

			if(isSmallerOrEqual(surface, 0))
				return null;

			surface = 0f;
		}
		else if(surface < 0)
			return null;

		Collision collision = collisionPool.obtain();

		collision.colliderA = boxA;
		collision.colliderB = limitB;

		collision.normalA.set(nx, ny);
		collision.normalB.set(limitB.normal);
		collision.setImpactVelocities(boxA.getBody(), limitB.getBody());
		collision.penetration = -((bx - ax) * nx + (by - ay) * ny);

		collision.contactSurface = surface;

		return collision;
	}
}