package de.druz.game.gdxgame;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.druz.game.gdxgame.components.DebugOutput;
import de.druz.game.gdxgame.components.Expires;
import de.druz.game.gdxgame.components.Physics;
import de.druz.game.gdxgame.components.RandomName;
import de.druz.game.gdxgame.components.RandomNameType;
import de.druz.game.gdxgame.components.SoundComponent;
import de.druz.game.gdxgame.components.Spawner;
import de.druz.game.gdxgame.physics.RunnerFactory;
import de.druz.game.gdxgame.physics.SimpleFixtureFactory;
import de.druz.game.gdxgame.physics.WalkerFactory;

public class EntityFactory {

	public static Entity createGameNameSpawner(World world) {
		Entity e = world.createEntity();
		Spawner spawner = new Spawner(1f);
//		{
//			@Override
//			public Entity spawn(World world) {
//				System.out.println("CREATING GAME NAME");
//				return createGameName(world);
//			}
//		};
//		spawner.spawn(world);
//		System.out.println("spawn called");
		e.addComponent(spawner);
		return e;
	}

	public static Entity createPersonNameSpawner(World world, final RandomNameType nameType) {
		Entity e = world.createEntity();
		// doesn't seem to work with inline method
		e.addComponent(new Spawner(2f) {
			@Override
			public Entity spawn(World world) {
				return createPerson(world, nameType);
			}
		});
		return e;
	}
	
	public static Entity createGameName(World world) {
		Entity e = world.createEntity();
		
		RandomName nameComp = new RandomName(RandomNameType.GAME);
		Expires expiresComp = new Expires(20f);
		e.addComponent(nameComp);
		e.addComponent(expiresComp);
		e.addComponent(new DebugOutput(nameComp));
//		e.addComponent(new DebugOutput(nameComp, expiresComp));
		
		return e;
	}
	
	public static Entity createPerson(World world, RandomNameType nameType) {
		Entity e = world.createEntity();
		
		RandomName nameComp = new RandomName(nameType);
		e.addComponent(nameComp);
		e.addComponent(new Expires(20f));
		e.addComponent(new DebugOutput(nameComp));
		
		return e;
	}
	
	public static Entity createBody(World world, Vector3 vec1, Vector3 vec2, BodyType bodyType) {
		Entity e = world.createEntity();
		BodyDef def = new BodyDef();
		def.type = bodyType;
		Vector3 target = vec1.lerp(vec2, 0.5f);
		def.position.x = target.x;
		def.position.y = target.y;
        
		e.addComponent(new Physics(def, new SimpleFixtureFactory(vec1, vec2)));
		e.addComponent(new Expires(10));
		return e;
	}
	
	public static Entity createSound(World world, String file) {
		return createSound(world, file, 1.0f, 1.0f, 1.0f);
	}

	public static Entity createSound(World world, String file, float volume, float pitch, float pan) {
		Entity e = world.createEntity();
		
		SoundComponent sound = new SoundComponent(file);
		sound.volume = volume;
		sound.pitch = pitch;
		sound.pan = pan;
		e.addComponent(sound);

		e.addComponent(new Expires(5.0f));
		
		return e;
	}

	public static Entity createWalker(World world, Vector3 touchDownVector, Vector3 touchUpVector, BodyType bodyType) {
		Entity e = world.createEntity();
		BodyDef def = new BodyDef();
		def.type = bodyType;
		Vector3 target = touchDownVector.lerp(touchUpVector, 0.5f);
		def.position.x = target.x;
		def.position.y = target.y;
        
		e.addComponent(new Physics(def, new WalkerFactory(touchDownVector, touchUpVector)));
		e.addComponent(new Expires(10));
		return e;
	}

	public static Entity createRunner(World world, Vector3 touchDownVector, Vector3 touchUpVector, BodyType bodyType) {
		Entity e = world.createEntity();
		BodyDef def = new BodyDef();
		def.type = bodyType;
		Vector3 target = touchDownVector.lerp(touchUpVector, 0.5f);
		def.position.x = target.x;
		def.position.y = target.y;
        
		e.addComponent(new Physics(def, new RunnerFactory(touchDownVector, touchUpVector)));
		e.addComponent(new Expires(10));
		return e;
	}
}
