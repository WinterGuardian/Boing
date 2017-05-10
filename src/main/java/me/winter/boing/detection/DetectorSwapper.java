package me.winter.boing.detection;

import com.badlogic.gdx.math.Vector2;
import me.winter.boing.Collision;
import me.winter.boing.shapes.Collider;

/**
 * Represents a CollisionDetector acting as the opposite of the specified CollisionDetector.
 * <p>
 * If the specified is a BoxCircleDetector, this detector will act as CircleBoxDetector.
 * <p>
 * Created by Alexander Winter on 2017-04-12.
 */
public class DetectorSwapper<A extends Collider, B extends Collider> implements CollisionDetector<A, B>
{
	private CollisionDetector<B, A> detector;

	public DetectorSwapper(CollisionDetector<B, A> detector)
	{
		this.detector = detector;
	}

	@Override
	public Collision collides(A shapeA, B shapeB)
	{
		Collision collision = detector.collides(shapeB, shapeA);

		if(collision == null)
			return null;

		float tmpX = collision.normalA.x;
		float tmpY = collision.normalA.y;
		collision.normalA.set(collision.normalB);
		collision.normalB.set(tmpX, tmpY);

		tmpX = collision.impactVelA.x;
		tmpY = collision.impactVelA.y;
		collision.impactVelA.set(collision.impactVelB);
		collision.impactVelB.set(tmpX, tmpY);

		collision.colliderA = shapeA;
		collision.colliderB = shapeB;
		return collision;
	}
}