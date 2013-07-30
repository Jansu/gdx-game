package de.druz.game.gdxgame.systems;

import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import de.druz.game.gdxgame.EntityFactory;
import de.druz.game.gdxgame.components.Physics;
import de.druz.game.gdxgame.components.Transform;

public class PhysicsSystem extends IntervalEntitySystem implements ContactListener {
	private static final int VELOCITY_ITERATIONS = 8;
	private static final int POSITION_ITERATIONS = 3;
	
	private ComponentMapper<Physics> physicsMapper;
	private ComponentMapper<Transform> transformMapper;
	private World physicsWorld;
	private boolean playSound = false;

	public PhysicsSystem(World physicsWorld) {
		super(Aspect.getAspectForAll(Physics.class), 0.020f);
		this.physicsWorld = physicsWorld;
	}

	@Override
	public void initialize() {
		physicsMapper = world.getMapper(Physics.class);
		transformMapper = world.getMapper(Transform.class);

		physicsWorld.setContactListener(this);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		physicsWorld.step(0.020f, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}
	
	@Override
	protected void inserted(Entity e) {
		Physics collidable = e.getComponent(Physics.class);
		List<Body> bodies = collidable.getFixtureFactory().createFixtures(physicsWorld, collidable, e);
		collidable.setBodies(bodies);
	}

	@Override
	protected void removed(Entity e) {
		Physics collidable = e.getComponent(Physics.class);
		for (Body body: collidable.getBodies()) {
			physicsWorld.destroyBody(body);
		}
	}

	@Override
	public void beginContact(Contact contact) {
//		Entity e1 = (Entity)contact.getFixtureA().getUserData();
//		Entity e2 = (Entity)contact.getFixtureB().getUserData();
//		
//		ImmutableBag<String> groupsA = world.getManager(GroupManager.class).getGroups(e1);
//		ImmutableBag<String> groupsB = world.getManager(GroupManager.class).getGroups(e2);
		
		playSound = contact.getFixtureA().getFilterData().groupIndex == contact.getFixtureB().getFilterData().groupIndex;
		
//		if (groupsA.contains("bla")) {
//			
//		}
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		if (playSound) {
			float[] impulses = impulse.getNormalImpulses();
			float maxImpulse = 0.0f;
			for (float i : impulses) {
				maxImpulse = Math.max(maxImpulse, i);
			}
//			float mass = Math.max(contact.getFixtureA().getBody().getMass(), contact.getFixtureB().getBody().getMass());
//			EntityFactory.createSound(world, "hit.wav", maxImpulse, pitchFromMass(mass), 1.0f).addToWorld();

			float massA = contact.getFixtureA().getBody().getMass();
			float massB = contact.getFixtureB().getBody().getMass();
			EntityFactory.createSound(world, "hit.wav", maxImpulse, pitchFromMass(massA), 1.0f).addToWorld();
			EntityFactory.createSound(world, "hit.wav", maxImpulse, pitchFromMass(massB), 1.0f).addToWorld();			
			playSound = false;
		}
	}

	private float pitchFromMass(float mass) {
		float pitch = 1/mass;
		return Math.max(0.1f, pitch);
//		return Math.min(2.0f, Math.max(0.5f, pitch));
	}



}
