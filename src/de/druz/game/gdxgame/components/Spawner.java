package de.druz.game.gdxgame.components;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

import de.druz.game.gdxgame.EntityFactory;

public class Spawner extends Component {

	public float delay;
	public float interval;

	private RandomNameType type = RandomNameType.GAME;
	
	public Spawner(float interval) {
		this.interval = interval;
		this.delay = interval;
	}

	public Entity spawn(World world) {
		switch (type) {
		case GAME:
			type = RandomNameType.MALE;
			return EntityFactory.createGameName(world);
		case MALE:
			type = RandomNameType.FEMALE;
			return EntityFactory.createPerson(world, RandomNameType.MALE);
		case FEMALE:
			type = RandomNameType.GAME;
			return EntityFactory.createPerson(world, RandomNameType.FEMALE);
		}
		return null;
	}
	
}
