package de.druz.game.gdxgame.components;

import com.artemis.Component;

public class Logs extends Component {

	public float interval;
	public String logEntry;
	public float delay;
	
	public Logs(float interval) {
		super();
		this.interval = interval;
		this.delay = interval;
	}
}
