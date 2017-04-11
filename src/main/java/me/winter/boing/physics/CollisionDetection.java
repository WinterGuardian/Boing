package me.winter.boing.physics;

import com.badlogic.gdx.utils.Pool;
import me.winter.boing.physics.shapes.AABB;
import me.winter.boing.physics.shapes.Circle;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2017-04-11.
 */
public class CollisionDetection
{
	private CollisionDetection() {} //static class

	public static Collision circleCircle(Circle circleA, Circle circleB, Pool<Collision> collisionPool)
	{
		float dx = circleA.getAbsX() - circleB.getAbsX();
		float dy = circleA.getAbsY() - circleB.getAbsY();

		float r = circleA.radius + circleB.radius;

		float dst2 = dx * dx + dy * dy;

		if(dst2 >= r * r)
			return null;

		Collision collision = collisionPool.obtain();

		collision.normalA.set(-dx, -dy);
		collision.normalB.set(dx, dy);

		collision.contact.set(-dx, -dy).scl(circleA.radius / r).add(circleA.getAbsX(), circleA.getAbsY());

		return collision;
	}

	public static Collision boxBox(AABB boxA, AABB boxB, Pool<Collision> collisionPool)
	{
		float dx = boxA.getAbsX() - boxB.getAbsX();
		float dy = boxA.getAbsY() - boxB.getAbsY();

		if(abs(dx) > boxA.width / 2 + boxB.width / 2 || abs(dy) > boxA.height / 2 + boxB.height / 2)
			return null;

		Collision collision = collisionPool.obtain();

		if(abs(dx) > abs(dy))
		{
			collision.normalA.set(-dx, 0);
			collision.normalB.set(dx, 0);
			collision.contact.set(signum(dx) * boxA.width / 2, 0).add(boxA.getAbsX(), boxB.getAbsY());
		}
		else
		{
			collision.normalA.set(-dy, 0);
			collision.normalB.set(dy, 0);
			collision.contact.set(0, signum(dy) * boxA.height / 2).add(boxA.getAbsX(), boxB.getAbsY());
		}

		return collision;
	}


	public static Collision boxCircle(AABB boxA, Circle circleB, Pool<Collision> collisionPool)
	{
		return null;
	}


	public static Collision circleBox(Circle circleA, AABB boxB, Pool<Collision> collisionPool)
	{
		return null;
	}
}
