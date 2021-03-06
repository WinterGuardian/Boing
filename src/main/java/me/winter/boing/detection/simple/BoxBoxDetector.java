package me.winter.boing.detection.simple;

import com.badlogic.gdx.utils.Pool;
import me.winter.boing.Collision;
import me.winter.boing.CollisionDynamicVariable;
import me.winter.boing.PhysicsWorld;
import me.winter.boing.colliders.Box;
import me.winter.boing.detection.PooledDetector;

import static java.lang.Math.abs;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2017-05-23.
 */
public class BoxBoxDetector extends PooledDetector<Box, Box>
{
	public BoxBoxDetector(Pool<Collision> collisionPool)
	{
		super(collisionPool);
	}

	@Override
	public Collision collides(PhysicsWorld world, Box shapeA, Box shapeB)
	{
		float dx = shapeB.getAbsX() - shapeA.getAbsX();
		float dy = shapeB.getAbsY() - shapeA.getAbsY();

		CollisionDynamicVariable peneX = (cA, cB) -> ((Box)cA).width / 2 + ((Box)cB).width / 2 - abs(cB.getAbsX() - cA.getAbsX());
		CollisionDynamicVariable peneY = (cA, cB) -> ((Box)cA).height / 2 + ((Box)cB).height / 2 - abs(cB.getAbsY() - cA.getAbsY());

		float peneXVal = peneX.getValue(shapeA, shapeB);
		float peneYVal = peneY.getValue(shapeA, shapeB);

		if(peneXVal < 0 || peneYVal < 0)
			return null;

		Collision collision = collisionPool.obtain();

		if(peneXVal < peneYVal)
		{
			collision.normal.set(dx < 0 ? -1 : 1, 0);
			collision.penetration = peneX;
			collision.contactSurface = peneY;
		}
		else
		{
			collision.normal.set(0, dy < 0 ? -1 : 1);
			collision.penetration = peneY;
			collision.contactSurface = peneX;
		}

		collision.colliderA = shapeA;
		collision.colliderB = shapeB;
		collision.setImpactVelocities(shapeA.getBody(), shapeB.getBody());

		return collision;
	}
}
