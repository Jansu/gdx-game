package de.druz.game.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.EntitySystem;

public abstract class BaseEntitySystem extends EntitySystem {
	
	public BaseEntitySystem(Aspect aspect) {
		super(aspect);
	}

	protected void log(String string) {
		System.out.println(getSystemName() + ": " + string);
	}

	protected String getSystemName() {
		return this.getClass().getSimpleName();
	}
}
