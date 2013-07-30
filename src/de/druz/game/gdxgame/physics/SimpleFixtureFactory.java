package de.druz.game.gdxgame.physics;

import java.util.List;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import de.druz.game.gdxgame.components.Physics;

public class SimpleFixtureFactory extends BaseFixtureFactory {

	protected Vector3 vec1;
	protected Vector3 vec2;

	public SimpleFixtureFactory(Vector3 vec1, Vector3 vec2) {
		super();
		this.vec1 = vec1;
		this.vec2 = vec2;
	}

	@Override
	public List<Body> createFixtures(World world, Physics physics, Entity e) {
		List<Body> bodies = super.createFixtures(world, physics, e);
		Body body = bodies.get(0);
		
		Shape shape = new PolygonShape();
		float width = Math.max(0.0001f, Math.abs(vec1.x-vec2.x));
		float height = Math.max(0.0001f, Math.abs(vec1.y-vec2.y));
		if (BodyType.DynamicBody.equals(body.getType())) {
			shape = new CircleShape();
			shape.setRadius(width);
		} else {
			((PolygonShape)shape).setAsBox(width, height);
		}
//		System.out.println("width " + width);
		
		body.createFixture(shape, 5.0f);
		
		return bodies;
	}
}
