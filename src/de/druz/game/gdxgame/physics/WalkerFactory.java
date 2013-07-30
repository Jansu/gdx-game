package de.druz.game.gdxgame.physics;

import java.util.ArrayList;
import java.util.List;

import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import de.druz.game.gdxgame.components.Physics;

public class WalkerFactory extends SimpleFixtureFactory {
	private static final long CHASSIS_TAG = 1;
	private static final long WHEEL_TAG = 2;

	private static final long MOTOR_TAG = 8;

	Vector2 m_offset = new Vector2();
	Body m_chassis;
	Body m_wheel;
	RevoluteJoint m_motorJoint;
	boolean m_motorOn;
	float m_motorSpeed;

	public Long getTag(Body argBody) {
		if (argBody == m_chassis) {
			return CHASSIS_TAG;
		} else if (argBody == m_wheel) {
			return WHEEL_TAG;
		}
		return null;
	}

	public Long getTag(Joint argJoint) {
		if (argJoint == m_motorJoint) {
			return MOTOR_TAG;
		}
		return null;
	}

	public void processBody(Body argBody, Long argTag) {
		if (argTag == CHASSIS_TAG) {
			m_chassis = argBody;
		} else if (argTag == WHEEL_TAG) {
			m_wheel = argBody;
		}
	}

	public void processJoint(Joint argJoint, Long argTag) {
		if (argTag == MOTOR_TAG) {
			m_motorJoint = (RevoluteJoint) argJoint;
			m_motorOn = m_motorJoint.isMotorEnabled();
		}
	}

	private List<Body> createLeg(Body body, float s, Vector2 wheelAnchor) {
		List<Body> bodies = new ArrayList<Body>();
		
		Vector2 p1 = new Vector2(5.4f * s, -6.1f);
		Vector2 p2 = new Vector2(7.2f * s, -1.2f);
		Vector2 p3 = new Vector2(4.3f * s, -1.9f);
		Vector2 p4 = new Vector2(3.1f * s, 0.8f);
		Vector2 p5 = new Vector2(6.0f * s, 1.5f);
		Vector2 p6 = new Vector2(2.5f * s, 3.7f);

		FixtureDef fd1 = new FixtureDef();
		FixtureDef fd2 = new FixtureDef();
		fd1.filter.groupIndex = -1;
		fd2.filter.groupIndex = -1;
		fd1.density = 1.0f;
		fd2.density = 1.0f;

		PolygonShape poly1 = new PolygonShape();
		PolygonShape poly2 = new PolygonShape();

		if (s > 0.0f) {
			Vector2[] vertices = new Vector2[3];

			vertices[0] = p1;
			vertices[1] = p2;
			vertices[2] = p3;
			poly1.set(vertices);

			vertices[0] = new Vector2();
			vertices[1] = p5.sub(p4);
			vertices[2] = p6.sub(p4);
			poly2.set(vertices);
		} else {
			Vector2[] vertices = new Vector2[3];

			vertices[0] = p1;
			vertices[1] = p3;
			vertices[2] = p2;
			poly1.set(vertices);

			vertices[0] = new Vector2();
			vertices[1] = p6.sub(p4);
			vertices[2] = p5.sub(p4);
			poly2.set(vertices);
		}

		fd1.shape = poly1;
		fd2.shape = poly2;

		BodyDef bd1 = new BodyDef(), bd2 = new BodyDef();
		bd1.type = BodyType.DynamicBody;
		bd2.type = BodyType.DynamicBody;
		bd1.position.x = m_offset.x;
		bd1.position.y = m_offset.y;
		Vector2 temp = p4.add(m_offset);
		bd2.position.x = temp.x;
		bd2.position.y = temp.y;

		bd1.angularDamping = 10.0f;
		bd2.angularDamping = 10.0f;

		Body body1 = body.getWorld().createBody(bd1);
		Body body2 = body.getWorld().createBody(bd2);

		body1.createFixture(fd1);
		body2.createFixture(fd2);

		DistanceJointDef djd = new DistanceJointDef();

		// Using a soft distance constraint can reduce some jitter.
		// It also makes the structure seem a bit more fluid by
		// acting like a suspension system.
		djd.dampingRatio = 0.5f;
		djd.frequencyHz = 10.0f;

		djd.initialize(body1, body2, p2.add(m_offset), p5.add(m_offset));
		body.getWorld().createJoint(djd);

		djd.initialize(body1, body2, p3.add(m_offset), p4.add(m_offset));
		body.getWorld().createJoint(djd);

		djd.initialize(body1, m_wheel, p3.add(m_offset),
				wheelAnchor.add(m_offset));
		body.getWorld().createJoint(djd);

		djd.initialize(body2, m_wheel, p6.add(m_offset),
				wheelAnchor.add(m_offset));
		body.getWorld().createJoint(djd);

		RevoluteJointDef rjd = new RevoluteJointDef();

		rjd.initialize(body2, m_chassis, p4.add(m_offset));
		body.getWorld().createJoint(rjd);
		
		bodies.add(body1);
		bodies.add(body2);
		return bodies ;
	}

	public WalkerFactory(Vector3 vec1, Vector3 vec2) {
		super(vec1, vec2);
	}

	@Override
	public List<Body> createFixtures(World world, Physics physics, Entity e) {
		List<Body> bodies = super.createFixtures(world, physics, e);
		Body body = bodies.get(0);
//		PolygonShape box = new PolygonShape();
//		float width = Math.max(0.0001f, Math.abs(vec1.x - vec2.x));
//		float height = Math.max(0.0001f, Math.abs(vec1.y - vec2.y));
//		box.setAsBox(width, height);
//
//		body.createFixture(box, 5.0f);
		
		m_offset.set(0.0f, 8.0f);
		m_motorSpeed = 2.0f;
		m_motorOn = true;
		Vector2 pivot = new Vector2(0.0f, 0.8f);

		// Chassis
		{
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(2.5f, 1.0f);

			FixtureDef sd = new FixtureDef();
			sd.density = 1.0f;
			sd.shape = shape;
			sd.filter.groupIndex = -1;
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.position.set(pivot).add(m_offset);
			m_chassis = body.getWorld().createBody(bd);
			m_chassis.createFixture(sd);
			
			bodies.add(m_chassis);
		}

		{
			CircleShape shape = new CircleShape();
			shape.setRadius(1.6f);

			FixtureDef sd = new FixtureDef();
			sd.density = 1.0f;
			sd.shape = shape;
			sd.filter.groupIndex = -1;
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.position.set(pivot).add(m_offset);
			m_wheel = body.getWorld().createBody(bd);
			m_wheel.createFixture(sd);
			
			bodies.add(m_wheel);
		}

		{
			RevoluteJointDef jd = new RevoluteJointDef();

			jd.initialize(m_wheel, m_chassis, pivot.add(m_offset));
			jd.collideConnected = false;
			jd.motorSpeed = m_motorSpeed;
			jd.maxMotorTorque = 400.0f;
			jd.enableMotor = m_motorOn;
			m_motorJoint = (RevoluteJoint) body.getWorld().createJoint(jd);
		}

		Vector2 wheelAnchor;

		wheelAnchor = pivot.add(new Vector2(0.0f, -0.8f));

		bodies.addAll(createLeg(body, -1.0f, wheelAnchor));
		bodies.addAll(createLeg(body, 1.0f, wheelAnchor));

		m_wheel.setTransform(m_wheel.getPosition(),
				120.0f * MathUtils.PI / 180.0f);
		bodies.addAll(createLeg(body, -1.0f, wheelAnchor));
		bodies.addAll(createLeg(body, 1.0f, wheelAnchor));

		m_wheel.setTransform(m_wheel.getPosition(), -120.0f * MathUtils.PI
				/ 180.0f);
		bodies.addAll(createLeg(body, -1.0f, wheelAnchor));
		bodies.addAll(createLeg(body, 1.0f, wheelAnchor));

		return bodies;
	}

}
