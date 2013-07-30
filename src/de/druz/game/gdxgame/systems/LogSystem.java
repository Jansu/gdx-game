package de.druz.game.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;

import de.druz.game.gdxgame.components.Logs;

public class LogSystem extends DelayedEntityProcessingSystem{
	@Mapper
	ComponentMapper<Logs> lm;

	public LogSystem(float defaultDelay) {
		super(Aspect.getAspectForAll(Logs.class), defaultDelay);
	}
	
	@Override
	protected void processDelta(Entity e, float accumulatedDelta) {
		Logs logs = lm.get(e);
		logs.delay -= accumulatedDelta;
	}
	
	@Override
	protected void processExpired(Entity e) {
		Logs logs = lm.get(e);
		
		System.out.println(logs.logEntry);
		
		logs.delay = logs.interval;
		offerDelay(logs.delay);
	}
	
	@Override
	protected float getRemainingDelay(Entity e) {
		Logs logs = lm.get(e);
		return logs.delay;
	}

	@Override
	protected void inserted(Entity e) {
		super.inserted(e);
	}

}
