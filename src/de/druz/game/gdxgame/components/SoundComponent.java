package de.druz.game.gdxgame.components;

import com.artemis.Component;

public class SoundComponent extends Component {

	@Override
	public String toString() {
		return "SoundComponent [file=" + file + ", volume=" + volume
				+ ", pitch=" + pitch + ", pan=" + pan + "]";
	}

	public String file;
	public float volume = 1.0f;
	public float pitch = 1.0f;
	public float pan = 1.0f; 

	public SoundComponent(String file) {
		super();
		this.file = file;
	}
	
}
