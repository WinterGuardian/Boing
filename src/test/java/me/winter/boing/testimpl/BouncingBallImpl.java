package me.winter.boing.testimpl;

import me.winter.boing.Collision;
import me.winter.boing.impl.DynamicBodyImpl;

import static me.winter.boing.util.VelocityUtil.reflect;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2017-04-10.
 */
public class BouncingBallImpl extends DynamicBodyImpl
{
	public BouncingBallImpl()
	{
		super();
	}

	public BouncingBallImpl(float weight)
	{
		super(weight);
	}

	@Override
	public void notifyCollision(Collision collision)
	{
		reflect(getVelocity(), collision.normal);

		getVelocity().add(collision.impactVelB);
		//getVelocity().scl(1f - collision.weightRatio);
	}
}
