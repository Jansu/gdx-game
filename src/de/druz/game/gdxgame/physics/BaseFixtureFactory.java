package de.druz.game.gdxgame.physics;

import java.util.ArrayList;
import java.util.List;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import de.druz.game.gdxgame.components.Physics;

public abstract class BaseFixtureFactory implements FixtureFactory {

	@Override
	public List<Body> createFixtures(World world, Physics physics, Entity e) {
		ArrayList<Body> bodies = new ArrayList<Body>();
		Body body = world.createBody(physics.getDef());
		body.setUserData(e);
		bodies.add(body);
		return bodies;
	}
}
