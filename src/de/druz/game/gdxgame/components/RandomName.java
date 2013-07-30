package de.druz.game.gdxgame.components;

import com.artemis.Component;

public class RandomName extends Component implements Outputable {

	public String name;
	public RandomNameType type;

	public RandomName(RandomNameType type) {
		super();
		this.type = type;
	}

	@Override
	public String output() {
		return name;
	}
}
