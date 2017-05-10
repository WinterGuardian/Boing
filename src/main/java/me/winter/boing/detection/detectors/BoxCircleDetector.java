package me.winter.boing.detection.detectors;

import com.badlogic.gdx.utils.Pool;
import me.winter.boing.Collision;
import me.winter.boing.detection.PooledDetector;
import me.winter.boing.shapes.Box;
import me.winter.boing.shapes.Circle;
import me.winter.boing.util.VectorUtil;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static me.winter.boing.util.VectorUtil.divide;

/**
 * Detects collisions between an Axis Aligned Bounding Box and a Circle
 * <p>
 * Created by Alexander Winter on 2017-04-12.
 */
public class BoxCircleDetector extends PooledDetector<Box, Circle>
{
	public BoxCircleDetector(Pool<Collision> collisionPool)
	{
		super(collisionPool);
	}

	@Override
	public Collision collides(Box boxA, Circle circleB)
	{
		float dx = circleB.getAbsX() - boxA.getAbsX();
		float dy = circleB.getAbsY() - boxA.getAbsY();

		float absDx = abs(dx);
		float absDy = abs(dy);

		float halfW = boxA.width / 2;
		float halfH = boxA.height / 2;

		if(absDx > halfW + circleB.radius
				|| absDy > halfH + circleB.radius)
			return null;

		//stupid box box collision inside (inverted if)
		if(absDx > halfW && absDy > halfH //if it's not inside
		&& (absDx - halfW) * (absDx - halfW) + (absDy - halfH) * (absDy - halfH) > circleB.radius * circleB.radius) //and it's not in the middle
			return null;

		float closestX = clamp(dx, -halfW, halfW);
		float closestY = clamp(dy, -halfH, halfH);

		Collision collision = collisionPool.obtain();

		if(absDx - halfW > absDy - halfH)
		{
			closestX = signum(closestX) * halfW;

			collision.normalA.set(dx < 0 ? -1 : 1, 0);
			collision.normalB.set(closestX - dx, closestY - dy);
			float len = collision.normalB.len();
			collision.penetration = circleB.radius - len;
			VectorUtil.divide(collision.normalB, len);
		}
		else
		{
			closestY = signum(closestY) * halfH;

			collision.normalA.set(0, dy < 0 ? -1 : 1);
			collision.normalB.set(closestX - dx, closestY - dy);
			float len = collision.normalB.len();
			collision.penetration = circleB.radius - len;
			VectorUtil.divide(collision.normalB, len);
		}

		collision.contactSurface = 0;
		collision.colliderA = boxA;
		collision.colliderB = circleB;
		collision.setImpactVelocities(boxA.getBody(), circleB.getBody());

		return collision;
	}
}