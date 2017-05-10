package me.winter.boing.test.detection;

import me.winter.boing.Collision;
import me.winter.boing.impl.WorldImpl;
import me.winter.boing.resolver.CollisionResolver;
import me.winter.boing.shapes.Circle;
import me.winter.boing.impl.DynamicBodyImpl;
import me.winter.boing.test.util.MutableInt;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2017-04-26.
 */
public class CircleCircleDetectionTest
{
	@Test
	public void simpleStaticCircleCircle()
	{
		MutableInt collisionCount = new MutableInt(0);

		CollisionResolver resolver = c -> collisionCount.value++;
		WorldImpl world = new WorldImpl(resolver);

		DynamicBodyImpl solidImpl = new DynamicBodyImpl(1f);
		solidImpl.getPosition().set(0, 0);
		solidImpl.addCollider(new Circle(solidImpl, 0, 0, 12.5f));
		world.add(solidImpl);

		DynamicBodyImpl solidImpl2 = new DynamicBodyImpl(1f);
		solidImpl2.getPosition().set(0, 0);
		solidImpl2.addCollider(new Circle(solidImpl2, 0, 0, 12.5f));
		world.add(solidImpl2);

		assertEquals(0, collisionCount.value);

		world.step(1f);
		assertEquals(1, collisionCount.value);

		world.step(1f);
		assertEquals(2, collisionCount.value);

		solidImpl2.getPosition().set(100, 100);
		world.step(1f);
		assertEquals(2, collisionCount.value);

		solidImpl2.getPosition().set(30, 0);
		world.step(1f);
		assertEquals(2, collisionCount.value);

		solidImpl2.getPosition().set(0, 30);
		world.step(1f);
		assertEquals(2, collisionCount.value);

		solidImpl2.getPosition().set(16, 16);
		world.step(1f);
		assertEquals(3, collisionCount.value);

		solidImpl2.getPosition().set(-16, -16);
		world.step(1f);
		assertEquals(4, collisionCount.value);
	}

	@Test
	public void circleCrashingIntoCircle()
	{
		MutableInt collisionCount = new MutableInt(0);

		CollisionResolver resolver = c -> collisionCount.value++;
		WorldImpl world = new WorldImpl(resolver);

		DynamicBodyImpl solidImpl = new DynamicBodyImpl(1f);
		solidImpl.getPosition().set(0, 0);
		solidImpl.addCollider(new Circle(solidImpl, 0, 0, 10));
		world.add(solidImpl);

		DynamicBodyImpl solidImpl2 = new DynamicBodyImpl(1f);
		solidImpl2.getPosition().set(50, 0);
		solidImpl2.addCollider(new Circle(solidImpl2, 0, 0, 10));
		solidImpl2.getVelocity().set(-16, 0);
		world.add(solidImpl2);

		assertEquals(0, collisionCount.value);

		world.step(1f);
		assertEquals(0, collisionCount.value);

		world.step(1f);
		assertEquals(1, collisionCount.value);
	}

	@Test
	public void circleTouchingCircleCollision()
	{
		MutableInt collisionCount = new MutableInt(0);

		CollisionResolver resolver = c -> collisionCount.value++;
		WorldImpl world = new WorldImpl(resolver);

		DynamicBodyImpl solidImpl = new DynamicBodyImpl(1f);
		solidImpl.getPosition().set(0, 0);
		solidImpl.addCollider(new Circle(solidImpl, 0, 0, 10));
		world.add(solidImpl);

		DynamicBodyImpl solidImpl2 = new DynamicBodyImpl(1f);
		solidImpl2.getPosition().set(20, 0);
		solidImpl2.addCollider(new Circle(solidImpl2, 0, 0, 10));
		world.add(solidImpl2);

		DynamicBodyImpl solidImpl3 = new DynamicBodyImpl(1f);
		solidImpl3.getPosition().set(0, 20);
		solidImpl3.addCollider(new Circle(solidImpl3, 0, 0, 10));
		world.add(solidImpl3);

		assertEquals(0, collisionCount.value);

		world.step(1f);
		assertEquals(2, collisionCount.value);
	}

	@Test
	public void circlesCrashing()
	{
		MutableInt collisionCount = new MutableInt(0);

		CollisionResolver resolver = c -> collisionCount.value++;
		WorldImpl world = new WorldImpl(resolver);

		DynamicBodyImpl solidImpl = new DynamicBodyImpl(1f);
		solidImpl.getPosition().set(0, 0);
		solidImpl.addCollider(new Circle(solidImpl, 0, 0, 10));
		solidImpl.getVelocity().set(8, 0);
		world.add(solidImpl);

		DynamicBodyImpl solidImpl2 = new DynamicBodyImpl(1f);
		solidImpl2.getPosition().set(50, 0);
		solidImpl2.addCollider(new Circle(solidImpl2, 0, 0, 10));
		solidImpl2.getVelocity().set(-8, 0);
		world.add(solidImpl2);

		assertEquals(0, collisionCount.value);

		world.step(1f);
		assertEquals(0, collisionCount.value);

		world.step(1f);
		assertEquals(1, collisionCount.value);
	}
}