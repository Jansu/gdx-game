package de.druz.game.gdxgame.systems.render;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dDebugRendererSystem extends VoidEntitySystem {
	private OrthographicCamera camera;
	private World physWorld;
	private Box2DDebugRenderer renderer;
	
	public Box2dDebugRendererSystem(World physWorld, OrthographicCamera camera) {
		this.physWorld = physWorld;
		this.renderer = new Box2DDebugRenderer();
		this.camera = camera;
	}

	@Override
	protected void processSystem() {
//		System.out.println("camera.position " + camera.position);
		renderer.render(physWorld, camera.combined);
	}
}
