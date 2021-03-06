package me.winter.boing.test.detection;

import me.winter.boing.colliders.Box;
import me.winter.boing.colliders.Circle;
import me.winter.boing.impl.DynamicBodyImpl;
import me.winter.boing.impl.PhysicsWorldImpl;
import me.winter.boing.resolver.CollisionResolver;
import me.winter.boing.util.Counter;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2017-04-26.
 */
@Ignore
public class BoxCircleDetectionTest
{
	@Test
	public void simpleStaticBoxCircle()
	{
		Counter collisionCount = new Counter(0);

		CollisionResolver resolver = (w, c) -> {
			collisionCount.value++;
			return true;
		};
		PhysicsWorldImpl world = new PhysicsWorldImpl(resolver);

		DynamicBodyImpl solidImpl = new DynamicBodyImpl(1f);
		solidImpl.getPosition().set(0, 0);
		solidImpl.addCollider(new Box(solidImpl, 0, 0, 20, 20));
		world.add(solidImpl);

		DynamicBodyImpl solidImpl2 = new DynamicBodyImpl(1f);
		solidImpl2.getPosition().set(0, 0);
		solidImpl2.addCollider(new Circle(solidImpl2, 0, 0, 10));
		world.add(solidImpl2);

		assertEquals(0, collisionCount.value);

		world.step(1f);
		assertEquals(1, collisionCount.value);

		world.step(1f);
		assertEquals(2, collisionCount.value);

		solidImpl2.getPosition().set(100, 100);
		world.step(1f);
		assertEquals(2, collisionCount.value);

		solidImpl2.getPosition().set(25, 0);
		world.step(1f);
		assertEquals(2, collisionCount.value);

		solidImpl2.getPosition().set(0, 25);
		world.step(1f);
		assertEquals(2, collisionCount.value);

		solidImpl2.getPosition().set(15, 15);
		world.step(1f);
		assertEquals(3, collisionCount.value);

		solidImpl2.getPosition().set(-15, -15);
		world.step(1f);
		assertEquals(4, collisionCount.value);
	}

	@Test
	public void circleCrashingIntoBox()
	{
		Counter collisionCount = new Counter(0);

		CollisionResolver resolver = (w, c) -> {
			collisionCount.value++;
			return true;
		};
		PhysicsWorldImpl world = new PhysicsWorldImpl(resolver);

		DynamicBodyImpl solidImpl = new DynamicBodyImpl(1f);
		solidImpl.getPosition().set(0, 0);
		solidImpl.addCollider(new Circle(solidImpl, 0, 0, 10));
		world.add(solidImpl);

		DynamicBodyImpl solidImpl2 = new DynamicBodyImpl(1f);
		solidImpl2.getPosition().set(50, 0);
		solidImpl2.addCollider(new Box(solidImpl2, 0, 0, 20, 20));
		solidImpl2.getVelocity().set(-16, 0);
		world.add(solidImpl2);

		assertEquals(0, collisionCount.value); //-10 - 10, 40 - 60

		world.step(1f);
		assertEquals(0, collisionCount.value); //-10 - 10, 24 - 44

		world.step(1f);
		assertEquals(1, collisionCount.value); //-10 - 10, 8 - 28
	}

	@Test
	public void boxTouchingCircleCollision()
	{
		Counter collisionCount = new Counter(0);

		CollisionResolver resolver = (w, c) -> {
			collisionCount.value++;
			return true;
		};
		PhysicsWorldImpl world = new PhysicsWorldImpl(resolver);

		DynamicBodyImpl solidImpl = new DynamicBodyImpl(1f);
		solidImpl.getPosition().set(0, 0);
		solidImpl.addCollider(new Box(solidImpl, 0, 0, 20, 20));
		world.add(solidImpl);

		DynamicBodyImpl solidImpl2 = new DynamicBodyImpl(1f);
		solidImpl2.getPosition().set(20, 0);
		solidImpl2.addCollider(new Circle(solidImpl2, 0, 0, 10));
		world.add(solidImpl2);

		assertEquals(0, collisionCount.value);

		world.step(1f);
		assertEquals(1, collisionCount.value);
	}

	@Test
	public void boxAndCircleCrashing()
	{
		Counter collisionCount = new Counter(0);

		CollisionResolver resolver = (w, c) -> {
			collisionCount.value++;
			return true;
		};
		PhysicsWorldImpl world = new PhysicsWorldImpl(resolver);

		DynamicBodyImpl solidImpl = new DynamicBodyImpl(1f);
		solidImpl.getPosition().set(0, 0);
		solidImpl.addCollider(new Box(solidImpl, 0, 0, 20, 20));
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
