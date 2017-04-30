package me.winter.boing.physics.shapes;

import com.badlogic.gdx.math.Vector2;
import me.winter.boing.physics.Body;

/**
 * An axis aligned limit collider. Basically a segment or an edge.
 * <p>
 * Created by Alexander Winter on 2017-04-12.
 */
public class Limit extends AbstractCollider
{
	public Vector2 normal;
	public float size;

	public Limit(Body body, float x, float y, Vector2 normal, float size)
	{
		super(body, x, y);

		if(normal.x * normal.y != 0)
			throw new IllegalArgumentException("normal has to be axis aligned !");

		this.normal = normal;
		this.size = size;
	}
}
