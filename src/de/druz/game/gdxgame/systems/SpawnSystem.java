package de.druz.game.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;

import de.druz.game.gdxgame.components.Spawner;

	
public class SpawnSystem extends DelayedEntityProcessingSystem {
	@Mapper
	ComponentMapper<Spawner> sm;
	
	public SpawnSystem(float defaultDelay) {
		super(Aspect.getAspectForAll(Spawner.class), defaultDelay);
	}
	
	@Override
	protected void processDelta(Entity e, float accumulatedDelta) {
		Spawner spawn = sm.get(e);
		spawn.delay -= accumulatedDelta;
	}
	
	@Override
	protected void processExpired(Entity e) {
		Spawner spawner = sm.get(e);
		spawner.spawn(world).addToWorld();
		spawner.delay = spawner.interval;
		offerDelay(spawner.delay);
	}
	
	@Override
	protected float getRemainingDelay(Entity e) {
		Spawner spawn = sm.get(e);
		return spawn.delay;
	}

	@Override
	protected void inserted(Entity e) {
		super.inserted(e);
	}
}
