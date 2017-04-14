package me.winter.boing.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2017-04-13.
 */
public class VectorUtil
{
	private VectorUtil() {}

	public static Vector2 divide(Vector2 vec, float scalar)
	{
		vec.x /= scalar;
		vec.y /= scalar;
		return vec;
	}

	public static Vector2 divide(Vector2 vec, float x, float y)
	{
		vec.x /= x;
		vec.y /= y;
		return vec;
	}

	public static Vector2 divide(Vector2 vec, Vector2 divider)
	{
		vec.x /= divider.x;
		vec.y /= divider.y;
		return vec;
	}

	public static Vector3 divide(Vector3 vec, float scalar)
	{
		vec.x /= scalar;
		vec.y /= scalar;
		vec.z /= scalar;
		return vec;
	}

	public static Vector3 divide(Vector3 vec, float x, float y, float z)
	{
		vec.x /= x;
		vec.y /= y;
		vec.z /= z;
		return vec;
	}

	public static Vector3 divide(Vector3 vec, Vector3 divider)
	{
		vec.x /= divider.x;
		vec.y /= divider.y;
		vec.z /= divider.z;
		return vec;
	}
}