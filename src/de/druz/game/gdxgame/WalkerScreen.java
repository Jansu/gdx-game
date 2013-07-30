package de.druz.game.gdxgame;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.druz.game.gdxgame.components.RandomNameType;
import de.druz.game.gdxgame.systems.ExpiringSystem;
import de.druz.game.gdxgame.systems.PhysicsSystem;
import de.druz.game.gdxgame.systems.PlayerInputSystem;
import de.druz.game.gdxgame.systems.RandomNameSystem;
import de.druz.game.gdxgame.systems.SoundSystem;
import de.druz.game.gdxgame.systems.SpawnSystem;
import de.druz.game.gdxgame.systems.render.Box2dDebugRendererSystem;
import de.druz.game.gdxgame.systems.render.DebugOutputRenderSystem;

public class WalkerScreen implements Screen {

	private OrthographicCamera camera;
	private World world;
	private com.badlogic.gdx.physics.box2d.World physicsWorld;
	private Box2dDebugRendererSystem box2dDebugRenderSys;

	public WalkerScreen() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		camera.translate(0.5f, -(h/w)/2);
//		camera = new OrthographicCamera(w/100, h/100);

		world = new World();

		world.setManager(new GroupManager());

		this.physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -10), true);
		world.setSystem(new PlayerInputSystem(camera));
		world.setSystem(new PhysicsSystem(physicsWorld));
		world.setSystem(new ExpiringSystem(5.0f));
		world.setSystem(new SoundSystem());

//		spriteRenderSystem = world.setSystem(new SpriteRenderSystem(camera), true);
		box2dDebugRenderSys = world.setSystem(new Box2dDebugRendererSystem(physicsWorld, camera));
		world.setSystem(new RandomNameSystem());
		world.setSystem(new DebugOutputRenderSystem(camera));
		world.setSystem(new SpawnSystem(1.0f));

		world.initialize();
		
		EntityFactory.createGameNameSpawner(world).addToWorld();
//		EntityFactory.createPersonNameSpawner(world, RandomNameType.MALE).addToWorld();
//		EntityFactory.createPersonNameSpawner(world, RandomNameType.FEMALE).addToWorld();
		
//		EntityFactory.createPlayer(world, 0, 0).addToWorld();
		
	}
	
	@Override
	public void render(float delta) {		
		world.setDelta(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		world.process();
//		box2dDebugRenderSys.process();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
//		batch.dispose();
//		texture.dispose();
	}

}
