package de.druz.game.gdxgame.physics;

import java.util.List;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

import de.druz.game.gdxgame.components.Physics;

public class RunnerFactory extends SimpleFixtureFactory {

	public RunnerFactory(Vector3 vec1, Vector3 vec2) {
		super(vec1, vec2);
	}

	@Override
	public List<Body> createFixtures(World world, Physics physics, Entity e) {
		List<Body> bodies = super.createFixtures(world, physics, e);
		Body torso = bodies.get(0);
		
        PolygonShape torsoShape = new PolygonShape();
        float widthHalf = Math.max(0.0001f, Math.abs(vec1.x-vec2.x));
		float heightHalf = Math.max(0.0001f, Math.abs(vec1.y-vec2.y));
		torsoShape.setAsBox(widthHalf, heightHalf);
		
		torso.createFixture(torsoShape, 5.0f);
		
		Vector2 relHipAnchor = new Vector2(0, -heightHalf * 0.95f);
		Vector2 hipAnchor = torso.getWorldPoint(relHipAnchor);
		
		System.out.println(relHipAnchor);
		
		CircleShape hipCircle = new CircleShape();
		hipCircle.setRadius(widthHalf);
		hipCircle.setPosition(hipAnchor);
		
		FixtureDef fd = new FixtureDef();
		fd.density = 1.0f;
		fd.shape = hipCircle;
		fd.filter.groupIndex = -1;
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.position.set(relHipAnchor);
		Body hip = torso.getWorld().createBody(bd);
		hip.createFixture(fd);

		JointDef jDef = new JointDef();
		jDef.type = JointType.WheelJoint;
		RevoluteJointDef jd = new RevoluteJointDef();
		WheelJointDef wd = new WheelJointDef();
		wd.initialize(torso, hip, hipAnchor, new Vector2(0.0f, 1.0f));
		wd.collideConnected = false;
		wd.maxMotorTorque = 400.0f;
		wd.enableMotor = true;
		wd.motorSpeed = 1.0f;
//		Joint motorJoint = (RevoluteJoint) torso.getWorld().createJoint(jd);

		bodies.add(torso);
		// TODO add all bodies
		return bodies;
	}

}
