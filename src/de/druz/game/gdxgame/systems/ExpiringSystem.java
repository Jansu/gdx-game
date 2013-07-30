package de.druz.game.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;

import de.druz.game.gdxgame.components.Expires;

	
public class ExpiringSystem extends DelayedEntityProcessingSystem {
	@Mapper
	ComponentMapper<Expires> em;
	
	
	public ExpiringSystem(float defaultDelay) {
		super(Aspect.getAspectForAll(Expires.class), defaultDelay);
	}
	
	@Override
	protected void processDelta(Entity e, float accumulatedDelta) {
		Expires expires = em.get(e);
		expires.delay -= accumulatedDelta;
//		System.out.println("accumulatedDelta " + accumulatedDelta);
	}
	
	@Override
	protected void processExpired(Entity e) {
//		System.out.println("expired " + e.getId());
		e.deleteFromWorld();
	}
	
	@Override
	protected float getRemainingDelay(Entity e) {
		Expires expires = em.get(e);
		return expires.delay;
	}

//	@Override
	public void offerDelay(float delay) {
//		System.out.println("delay " + delay + " - getRemainingTimeUntilProcessing() " + getRemainingTimeUntilProcessing());
		if(!running || delay < this.delay) {
			restart(delay);
		}
	}

	@Override
	protected void inserted(Entity e) {
		Expires expires = em.get(e);
		expires.delay += acc;
		if(expires.delay > 0) {
			offerDelay(expires.delay);
		}
	}
}
