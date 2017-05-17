package me.winter.boing.shapes;

import me.winter.boing.Body;
import me.winter.boing.DynamicBody;

/**
 * Abstract implementation of a Collider. Has a relative
 * location and keeps a reference to its body.
 * <p>
 * Created by Alexander Winter on 2017-04-10.
 */
public abstract class AbstractCollider implements Collider
{
	protected Body body;
	protected float x, y;

	private Object tag;

	public AbstractCollider(Body body, float x, float y)
	{
		this.body = body;
		this.x = x;
		this.y = y;
	}

	@Override
	public Body getBody()
	{
		return body;
	}

	@Override
	public float getAbsX()
	{
		return x + body.getPosition().x;
	}

	@Override
	public float getAbsY()
	{
		return y + body.getPosition().y;
	}

	@Override
	public float getPrevAbsX()
	{
		if(body instanceof DynamicBody)
			return x + ((DynamicBody)body).getPreviousPos().x;
		return getAbsX();
	}

	@Override
	public float getPrevAbsY()
	{
		if(body instanceof DynamicBody)
			return y + ((DynamicBody)body).getPreviousPos().y;
		return getAbsY();
	}

	@Override
	public Object getTag()
	{
		return tag;
	}

	@Override
	public void setTag(Object tag)
	{
		this.tag = tag;
	}
}
