package de.druz.game.gdxgame.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import de.druz.game.gdxgame.components.SoundComponent;

public class SoundSystem extends EntityProcessingSystem {
	@Mapper
	private ComponentMapper<SoundComponent> soundMapper;

	public SoundSystem() {
		super(Aspect.getAspectForAll(SoundComponent.class));
	}

	@Override
	public void initialize() {

	}

	@Override
	protected void process(Entity e) {
//		world.deleteEntity(e);
	}

	@Override
	protected void inserted(Entity e) {
		SoundComponent soundComp = soundMapper.get(e);

		Sound sound = Gdx.audio.newSound(Gdx.files.internal("data/sound/hit.wav"));
//		System.out.println("playing sound with " + soundComp);
		sound.play(soundComp.volume, soundComp.pitch, soundComp.pan);
	}

	@Override
	protected void removed(Entity e) {
		super.removed(e);
		// TODO proper sound disposal / file management
//		SoundComponent soundComp = soundMapper.get(e);
//
//		Sound sound = Gdx.audio.newSound(Gdx.files.internal("data/sound/hit.wav"));
		
	}

}
