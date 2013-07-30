package de.druz.game.gdxgame.components;

import com.artemis.Component;

public class Expires extends Component implements Outputable {
	/**
	 * @param delay in sec
	 */
	public Expires(float delay) {
		super();
		this.delay = delay;
	}

	public float delay;

	@Override
	public String output() {
		return " delay: " + delay;
	}
}
