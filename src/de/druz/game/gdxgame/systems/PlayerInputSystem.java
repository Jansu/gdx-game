package de.druz.game.gdxgame.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.druz.game.gdxgame.EntityFactory;
import de.druz.game.gdxgame.components.Physics;

public class PlayerInputSystem extends VoidEntitySystem implements InputProcessor {
	private static final float FACTOR_CAMERA_MOVE = 0.05f;
	private static final float VerticalThrusters = 200;
	private static final float VerticalMaxSpeed = 200;
	
	private @Mapper ComponentMapper<Physics> pm;
	
	protected OrthographicCamera camera;
	protected Vector3 touchDownVector;
	protected Vector3 touchUpVector;
	protected boolean left = false;
	protected boolean right = false;
	protected boolean up = false;
	protected boolean down = false;
	
	private BodyType bodyType = BodyType.StaticBody;
	private boolean walker = false;
	private boolean runner = false;
	
	public PlayerInputSystem(OrthographicCamera camera) {
//		super(Aspect.getAspectForAll(Player.class, Physics.class));
		this.camera = camera;
		this.touchDownVector = new Vector3();
		this.touchUpVector = new Vector3();
	}
	
	@Override
	protected void initialize() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	protected void processSystem() {
		float changeX = 0.0f;
		float changeY = 0.0f;
		if(up) {
			changeY += FACTOR_CAMERA_MOVE * camera.zoom;
		}
		if(down) {
			camera.position.y -= FACTOR_CAMERA_MOVE * camera.zoom;
		}
		if(right) {
			camera.position.x += FACTOR_CAMERA_MOVE * camera.zoom;
		}
		if(left) {
			camera.position.x -= FACTOR_CAMERA_MOVE * camera.zoom;
		}
//		if (changeX != 0.0f || changeY != 0.0f) {
			camera.translate(changeX, changeY);
			camera.update();
//		}
	}

	@Override
	public boolean keyDown(int keycode) {
		walker = false;
		runner = false;
		if(keycode == Input.Keys.NUM_1) {
			bodyType = BodyType.StaticBody;
		} else if(keycode == Input.Keys.NUM_2) {
			bodyType = BodyType.DynamicBody;
		} else if(keycode == Input.Keys.NUM_3) {
			bodyType = BodyType.KinematicBody;
		} else if(keycode == Input.Keys.NUM_4) {
			bodyType = BodyType.DynamicBody;
			walker = true;
		} else if(keycode == Input.Keys.NUM_5) {
			bodyType = BodyType.DynamicBody;
			runner  = true;
		} else if(keycode == Input.Keys.A) {
			left = true;
		} else if(keycode == Input.Keys.D) {
			right = true;
		} else if(keycode == Input.Keys.W) {
			up = true;
		} else if(keycode == Input.Keys.S) {
			down = true;
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.A) {
			left = false;
		}
		else if(keycode == Input.Keys.D) {
			right = false;
		}
		else if(keycode == Input.Keys.W) {
			up = false;
		}
		else if(keycode == Input.Keys.S) {
			down = false;
		}
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if(button == Input.Buttons.LEFT) {
			touchDownVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchDownVector);
//			System.out.println(String.format("touchDown %s", touchDownVector));
			
			EntityFactory.createGameName(world).addToWorld();
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(button == Input.Buttons.LEFT) {
			touchUpVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchUpVector);
//			System.out.println(String.format("touch up %s", touchUpVector));
			if (walker) {
				EntityFactory.createWalker(world, touchDownVector, touchUpVector, bodyType).addToWorld();
			} else if (runner) {
				EntityFactory.createRunner(world, touchDownVector, touchUpVector, bodyType).addToWorld();

				EntityFactory.createGameNameSpawner(world).addToWorld();
			} else {
				EntityFactory.createBody(world, touchDownVector, touchUpVector, bodyType).addToWorld();
			}
		}
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
//		System.out.println("camera zoom "+camera.zoom);
		camera.zoom += amount/10f;
		if (0 > camera.zoom) {
			camera.zoom = 0;
		}
		camera.update();
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
}
