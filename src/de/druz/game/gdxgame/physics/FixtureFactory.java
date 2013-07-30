package de.druz.game.gdxgame.physics;

import java.util.List;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import de.druz.game.gdxgame.components.Physics;

public interface FixtureFactory {

	List<Body> createFixtures(World world, Physics physics, Entity e);
	
}
