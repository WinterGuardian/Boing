package me.winter.boing.simulation;

import com.badlogic.gdx.math.Vector2;
import me.winter.boing.colliders.Box;
import me.winter.boing.colliders.Limit;
import me.winter.boing.impl.BodyImpl;
import me.winter.boing.resolver.ReplaceResolver;
import me.winter.boing.testimpl.BouncingBallImpl;
import me.winter.boing.testimpl.GravityAffected;
import me.winter.boing.testimpl.PlayerImpl;
import me.winter.boing.testimpl.TestWorldImpl;
import me.winter.boing.util.WorldSimulation;
import org.junit.Test;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2017-07-15.
 */
public class GlitchSimulation
{
	@Test
	public void cornerGlitch()
	{
		TestWorldImpl world = new TestWorldImpl(new ReplaceResolver());

		BouncingBallImpl ballImpl = new BouncingBallImpl();

		ballImpl.getPosition().set(50, 50);
		ballImpl.addCollider(new Limit(ballImpl, 25, 0, new Vector2(1, 0), 50));
		ballImpl.addCollider(new Limit(ballImpl, 0, 25, new Vector2(0, 1), 50));
		ballImpl.getVelocity().set(50, 50);

		world.add(ballImpl);

		BouncingBallImpl ballImpl2 = new BouncingBallImpl();

		ballImpl2.getPosition().set(500, 500);
		ballImpl2.addCollider(new Limit(ballImpl2, -25, 0, new Vector2(-1, 0), 50));
		ballImpl2.addCollider(new Limit(ballImpl2, 0, -25, new Vector2(0, -1), 50));
		ballImpl2.getVelocity().set(-100, -100);

		world.add(ballImpl2);

		new WorldSimulation(world, 60f).start();
	}

	@Test
	public void boxCornerGlitch()
	{
		TestWorldImpl world = new TestWorldImpl(new ReplaceResolver());

		BouncingBallImpl ballImpl = new BouncingBallImpl();

		ballImpl.getPosition().set(50, 50);
		ballImpl.addCollider(new Box(ballImpl, 0, 0, 50, 50));
		ballImpl.getVelocity().set(50, 50);

		world.add(ballImpl);

		BouncingBallImpl ballImpl2 = new BouncingBallImpl();

		ballImpl2.getPosition().set(500, 500);
		ballImpl2.addCollider(new Box(ballImpl2, 0, 0, 50, 50));
		ballImpl2.getVelocity().set(-100, -100);

		world.add(ballImpl2);

		new WorldSimulation(world, 60f).start();
	}

	@Test
	public void boxLimitCornerGlitch()
	{
		TestWorldImpl world = new TestWorldImpl(new ReplaceResolver());

		BouncingBallImpl ballImpl = new BouncingBallImpl();

		ballImpl.getPosition().set(50, 50);
		ballImpl.addCollider(new Box(ballImpl, 0, 0, 50, 50));
		ballImpl.getVelocity().set(50, 50);

		world.add(ballImpl);

		BouncingBallImpl ballImpl2 = new BouncingBallImpl();

		ballImpl2.getPosition().set(500, 500);
		ballImpl2.addCollider(new Limit(ballImpl2, -25, 0, new Vector2(-1, 0), 50));
		ballImpl2.addCollider(new Limit(ballImpl2, 0, -25, new Vector2(0, -1), 50));
		ballImpl2.getVelocity().set(-100, -100);

		world.add(ballImpl2);

		new WorldSimulation(world, 60f).start();
	}

	@Test
	public void passingThroughBottomWhilePushingOnWall()
	{
		TestWorldImpl world = new TestWorldImpl(new ReplaceResolver());


		PlayerImpl player = new PlayerImpl();
		player.getPosition().set(400, 200);
		player.addCollider(new Box(player, 0, 25, 40, 50));
		world.add(player);

		BodyImpl ground = new BodyImpl();
		ground.getPosition().set(400, 175);
		ground.addCollider(new Box(ground, 0, 0, 200, 50));
		world.add(ground);

		BodyImpl wall = new BodyImpl();
		wall.getPosition().set(450, 200);
		wall.addCollider(new Box(wall, 0, 40, 60, 80));
		world.add(wall);

		GravityAffected pushable = new GravityAffected();
		pushable.getPosition().set(425, 280);
		pushable.addCollider(new Box(pushable, 0, 15, 30, 30));
		world.add(pushable);

		new WorldSimulation(world, 60f).start();
	}
}