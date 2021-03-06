package me.winter.boing.simulation;

import me.winter.boing.Collision;
import me.winter.boing.colliders.Box;
import me.winter.boing.colliders.Limit;
import me.winter.boing.impl.BodyImpl;
import me.winter.boing.impl.DynamicBodyImpl;
import me.winter.boing.resolver.ReplaceResolver;
import me.winter.boing.simulation.simulator.BoingSimulator;
import me.winter.boing.testimpl.TestPhysicsWorldImpl;
import org.junit.Ignore;
import org.junit.Test;

import static me.winter.boing.util.SleepUtil.sleep;
import static me.winter.boing.util.VectorUtil.DOWN;
import static me.winter.boing.util.VectorUtil.LEFT;
import static me.winter.boing.util.VectorUtil.RIGHT;
import static me.winter.boing.util.VectorUtil.UP;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2017-06-05.
 */
@Ignore
public class StepByStepSimulation
{
	@Test
	public void fourFrameStackStabilizationTest()
	{
		TestPhysicsWorldImpl world = new TestPhysicsWorldImpl(new ReplaceResolver());

		DynamicBodyImpl pusher = new DynamicBodyImpl();
		pusher.getPosition().set(0, 100);
		pusher.getVelocity().set(100, 0);
		pusher.addCollider(new Limit(pusher, 0, 0, RIGHT, 100));
		world.add(pusher);

		DynamicBodyImpl pushed = new DynamicBodyImpl();

		pushed.getPosition().set(100, 100);
		pushed.addCollider(new Limit(pushed, -50, 0, LEFT, 100));
		pushed.addCollider(new Limit(pushed, 50, 0, RIGHT, 100));

		pushed.addCollider(new Limit(pushed, 0, -50, DOWN, 100));
		pushed.addCollider(new Limit(pushed, 0, 50, UP, 100));

		world.add(pushed);

		BodyImpl wall = new BodyImpl();
		wall.getPosition().set(220, 100);
		wall.addCollider(new Limit(wall, 0, 0, LEFT, 100));
		wall.getColliders()[0].setTag("WALL");
		world.add(wall);

		new BoingSimulator(world, 1f).start(true);
	}

	@Test
	public void pushChain()
	{
		TestPhysicsWorldImpl world = new TestPhysicsWorldImpl(new ReplaceResolver());

		DynamicBodyImpl pusher = new DynamicBodyImpl(5f);
		pusher.getPosition().set(0, 100);
		pusher.getVelocity().set(30, 0);
		pusher.addCollider(new Limit(pusher, 0, 0, RIGHT, 100));
		pusher.getColliders()[0].setTag("PUSHER");
		world.add(pusher);

		DynamicBodyImpl pushed = new DynamicBodyImpl();

		pushed.getPosition().set(100, 100);
		pushed.addCollider(new Limit(pushed, -50, 0, LEFT, 100));
		pushed.addCollider(new Limit(pushed, 50, 0, RIGHT, 100));

		pushed.addCollider(new Limit(pushed, 0, -50, DOWN, 100));
		pushed.addCollider(new Limit(pushed, 0, 50, UP, 100));

		world.add(pushed);

		DynamicBodyImpl pushed2 = new DynamicBodyImpl();
		pushed2.getPosition().set(220, 100);
		pushed2.addCollider(new Limit(pushed2, 0, 0, LEFT, 100));
		pushed2.getColliders()[0].setTag("PUSHED2");
		world.add(pushed2);


		new BoingSimulator(world, 1f).start(true);
	}

	@Test
	public void perpetuallyFallingGroundCaseWithoutWalls()
	{
		TestPhysicsWorldImpl world = new TestPhysicsWorldImpl(new ReplaceResolver());

		DynamicBodyImpl playerLike = new DynamicBodyImpl(5f);
		playerLike.getPosition().set(400, 200);
		playerLike.getVelocity().set(25, -25);
		playerLike.addCollider(new Box(playerLike, 0, 50, 50, 100));
		playerLike.getColliders()[0].setTag("PLAYER");
		world.add(playerLike);

		DynamicBodyImpl boxLike = new DynamicBodyImpl();
		boxLike.getPosition().set(450, 225);
		boxLike.getVelocity().set(0, -25);
		boxLike.addCollider(new Box(boxLike, 0, 0, 50, 50));
		world.add(boxLike);

		for(int i = 0; i < 3; i++)
		{
			DynamicBodyImpl fallingGround = new DynamicBodyImpl();
			fallingGround.getPosition().set(400 + i * 50, 175);
			fallingGround.getVelocity().set(0, -25);
			fallingGround.addCollider(new Box(fallingGround, 0, 0, 50, 50));
			world.add(fallingGround);
		}

		BodyImpl ground = new BodyImpl();
		ground.getPosition().set(450, 150);
		ground.addCollider(new Limit(ground, 0, 0, UP, 150));
		world.add(ground);

		new BoingSimulator(world, 1f).start(true);
	}


	@Test
	public void perpetuallyFallingGroundCase()
	{
		TestPhysicsWorldImpl world = new TestPhysicsWorldImpl(new ReplaceResolver());

		DynamicBodyImpl playerLike = new DynamicBodyImpl(5f);
		playerLike.getPosition().set(400, 200);
		playerLike.getVelocity().set(25, -25);
		playerLike.addCollider(new Box(playerLike, 0, 50, 50, 100));
		playerLike.getColliders()[0].setTag("PLAYER");
		world.add(playerLike);

		DynamicBodyImpl boxLike = new DynamicBodyImpl();
		boxLike.getPosition().set(450, 225);
		boxLike.getVelocity().set(0, -25);
		boxLike.addCollider(new Box(boxLike, 0, 0, 50, 50));
		world.add(boxLike);

		for(int i = 0; i < 4; i++)
		{
			DynamicBodyImpl fallingGround = new DynamicBodyImpl();
			fallingGround.getPosition().set(350 + i * 50, 175);
			fallingGround.getVelocity().set(0, -25);
			fallingGround.addCollider(new Box(fallingGround, 0, 0, 50, 50));
			world.add(fallingGround);
		}

		BodyImpl ground = new BodyImpl();
		ground.getPosition().set(425, 150);
		ground.addCollider(new Limit(ground, 0, 0, UP, 200));
		world.add(ground);

		BodyImpl wall = new BodyImpl();
		wall.getPosition().set(525, 200);
		wall.addCollider(new Limit(wall, 0, 0, LEFT, 100));
		world.add(wall);

		BodyImpl wall2 = new BodyImpl();
		wall2.getPosition().set(325, 200);
		wall2.addCollider(new Limit(wall2, 0, 0, RIGHT, 100));
		world.add(wall2);

		new BoingSimulator(world, 1f).start(true);
	}

	@Test
	public void minimalPerpetuallyFallingGroundCase()
	{
		TestPhysicsWorldImpl world = new TestPhysicsWorldImpl(new ReplaceResolver());

		DynamicBodyImpl boxLike = new DynamicBodyImpl();
		boxLike.getPosition().set(400, 225);
		boxLike.getVelocity().set(25, -25);
		boxLike.addCollider(new Box(boxLike, 0, 0, 50, 50));
		world.add(boxLike);

		for(int i = 0; i < 2; i++)
		{
			DynamicBodyImpl fallingGround = new DynamicBodyImpl();
			fallingGround.getPosition().set(400 + i * 50, 175);
			fallingGround.getVelocity().set(0, -25);
			fallingGround.addCollider(new Box(fallingGround, 0, 0, 50, 50));
			world.add(fallingGround);
		}

		BodyImpl ground = new BodyImpl();
		ground.getPosition().set(425, 150);
		ground.addCollider(new Limit(ground, 0, 0, UP, 100));
		world.add(ground);

		new BoingSimulator(world, 1f).start(true);
	}

	@Test
	public void minimalPerpetuallyFallingGroundCaseInLimits()
	{
		TestPhysicsWorldImpl world = new TestPhysicsWorldImpl(new ReplaceResolver());

		DynamicBodyImpl boxLike = new DynamicBodyImpl();
		boxLike.getPosition().set(400, 225);
		boxLike.getVelocity().set(25, -25);
		boxLike.addCollider(new Limit(boxLike, 0, 25, UP, 50));
		Limit downLimit = new Limit(boxLike, 0, -25, DOWN, 50);
		downLimit.setTag("DOWN_LIMIT");
		boxLike.addCollider(downLimit);
		boxLike.addCollider(new Limit(boxLike, -25, 0, LEFT, 50));
		boxLike.addCollider(new Limit(boxLike, 25, 0, RIGHT, 50));
		world.add(boxLike);

		for(int i = 0; i < 2; i++)
		{
			DynamicBodyImpl fallingGround = new DynamicBodyImpl();
			fallingGround.getPosition().set(400 + i * 50, 175);
			fallingGround.getVelocity().set(0, -25);
			Limit upLimit = new Limit(fallingGround, 0, 25, UP, 50);
			if(i == 0)
				upLimit.setTag("UP_LIMIT");
			fallingGround.addCollider(upLimit);
			fallingGround.addCollider(new Limit(fallingGround, 0, -25, DOWN, 50));
			fallingGround.addCollider(new Limit(fallingGround, -25, 0, LEFT, 50));
			fallingGround.addCollider(new Limit(fallingGround, 25, 0, RIGHT, 50));
			world.add(fallingGround);
		}

		BodyImpl ground = new BodyImpl();
		ground.getPosition().set(425, 150);
		ground.addCollider(new Limit(ground, 0, 0, UP, 100));
		world.add(ground);

		new BoingSimulator(world, 1f).start(true);
	}

	@Test
	public void minimalPerpetuallyFallingGroundCaseInLimitsGhostCollision()
	{
		TestPhysicsWorldImpl world = new TestPhysicsWorldImpl(new ReplaceResolver());

		DynamicBodyImpl boxLike = new DynamicBodyImpl() {
			@Override
			public void notifyCollision(Collision collision)
			{
				displayAndWait();
			}
		};
		boxLike.getPosition().set(400, 225);
		boxLike.getVelocity().set(20, -20);
		boxLike.addCollider(new Limit(boxLike, 0, 25, UP, 50));
		boxLike.addCollider(new Limit(boxLike, 0, -25, DOWN, 50));
		boxLike.addCollider(new Limit(boxLike, -25, 0, LEFT, 50));
		boxLike.addCollider(new Limit(boxLike, 25, 0, RIGHT, 50));

		boxLike.getColliders()[0].setTag("UP_LIMIT of top box");
		boxLike.getColliders()[1].setTag("DOWN_LIMIT of top box");
		boxLike.getColliders()[2].setTag("LEFT_LIMIT of top box");
		boxLike.getColliders()[3].setTag("RIGHT_LIMIT of top box");

		world.add(boxLike);

		for(int i = 0; i < 2; i++)
		{
			DynamicBodyImpl fallingGround = new DynamicBodyImpl() {
				@Override
				public void notifyCollision(Collision collision)
				{
					displayAndWait();
				}
			};

			fallingGround.getPosition().set(400 + i * 50, 175);
			fallingGround.getVelocity().set(0, -25);
			fallingGround.addCollider(new Limit(fallingGround, 0, 25, UP, 50));
			fallingGround.addCollider(new Limit(fallingGround, 0, -25, DOWN, 50));
			fallingGround.addCollider(new Limit(fallingGround, -25, 0, LEFT, 50));
			fallingGround.addCollider(new Limit(fallingGround, 25, 0, RIGHT, 50));

			fallingGround.getColliders()[0].setTag("UP_LIMIT of " + (i == 0 ? "left" : "right") + " box");
			fallingGround.getColliders()[1].setTag("DOWN_LIMIT of " + (i == 0 ? "left" : "right") + " box");
			fallingGround.getColliders()[2].setTag("LEFT_LIMIT of " + (i == 0 ? "left" : "right") + " box");
			fallingGround.getColliders()[3].setTag("RIGHT_LIMIT of " + (i == 0 ? "left" : "right") + " box");

			world.add(fallingGround);
		}

		BodyImpl ground = new BodyImpl() {
			@Override
			public void notifyCollision(Collision collision)
			{
				displayAndWait();
			}
		};
		ground.getPosition().set(425, 150);
		ground.addCollider(new Limit(ground, 0, 0, UP, 100));
		world.add(ground);

		new BoingSimulator(world, 1f).start(true);
	}

	@Test
	public void minimalPerpetuallyFallingGroundCaseInLimitsGhostCollisionNoSideMoving()
	{
		TestPhysicsWorldImpl world = new TestPhysicsWorldImpl(new ReplaceResolver());

		DynamicBodyImpl boxLike = new DynamicBodyImpl();
		boxLike.getPosition().set(425, 225);
		boxLike.getVelocity().set(0, -20);
		boxLike.addCollider(new Limit(boxLike, 0, 25, UP, 50));
		Limit downLimit = new Limit(boxLike, 0, -25, DOWN, 50);
		downLimit.setTag("DOWN_LIMIT");
		boxLike.addCollider(downLimit);
		boxLike.addCollider(new Limit(boxLike, -25, 0, LEFT, 50));
		boxLike.addCollider(new Limit(boxLike, 25, 0, RIGHT, 50));
		world.add(boxLike);

		for(int i = 0; i < 2; i++)
		{
			DynamicBodyImpl fallingGround = new DynamicBodyImpl();
			fallingGround.getPosition().set(400 + i * 50, 175);
			fallingGround.getVelocity().set(0, -25);
			Limit upLimit = new Limit(fallingGround, 0, 25, UP, 50);
			if(i == 0)
				upLimit.setTag("UP_LIMIT");
			fallingGround.addCollider(upLimit);
			fallingGround.addCollider(new Limit(fallingGround, 0, -25, DOWN, 50));
			fallingGround.addCollider(new Limit(fallingGround, -25, 0, LEFT, 50));
			fallingGround.addCollider(new Limit(fallingGround, 25, 0, RIGHT, 50));
			world.add(fallingGround);
		}

		BodyImpl ground = new BodyImpl();
		ground.getPosition().set(425, 150);
		ground.addCollider(new Limit(ground, 0, 0, UP, 100));
		world.add(ground);

		new BoingSimulator(world, 1f).start(true);
	}

	private void displayAndWait()
	{
		BoingSimulator.__last.forceRepaint();
		sleep(2000);
	}
}