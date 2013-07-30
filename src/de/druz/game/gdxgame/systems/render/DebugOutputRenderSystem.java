package de.druz.game.gdxgame.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.druz.game.gdxgame.components.DebugOutput;

public class DebugOutputRenderSystem extends EntitySystem {
	@Mapper
	ComponentMapper<DebugOutput> dm;

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;

	public DebugOutputRenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(DebugOutput.class));
		this.camera = camera;
	}

	@Override
	protected void initialize() {
		batch = new SpriteBatch();
		
		Texture fontTexture = new Texture(Gdx.files.internal("data/font/normal_0.png"));
		fontTexture.setFilter(TextureFilter.Linear, TextureFilter.MipMapLinearLinear);
		TextureRegion fontRegion = new TextureRegion(fontTexture);
		font = new BitmapFont(Gdx.files.internal("data/font/normal.fnt"), fontRegion, false);
		font.setUseIntegerPositions(false);
		font.setScale(0.001f, 0.001f);
	}

	@Override
	protected void begin() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		StringBuilder out = new StringBuilder();
		for(int i = 0; entities.size() > i; i++) {
			process(entities.get(i), out);
		}
		font.drawMultiLine(batch, out, 0, 0);
	}

	protected void process(Entity e, StringBuilder out) {
		if(dm.has(e)) {
			out.append(dm.get(e).output()).append("\n");
//			System.out.println(dm.get(e).output());
		}
	}

	protected void end() {
		batch.end();
	}

	@Override
	protected void inserted(Entity e) {
		System.out.println(dm.get(e).output());
	}

	@Override
	protected void removed(Entity e) {
	}

}
