package de.druz.game.gdxgame.components;


import java.util.List;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.druz.game.gdxgame.physics.FixtureFactory;

public class Physics extends Component {

	private List<Body> bodies;
	private BodyDef def;
	private FixtureFactory fixtureFactory;

	public Physics(BodyDef def, FixtureFactory fixFac){
		this.def = def;
		this.fixtureFactory = fixFac;
	}
	
	public FixtureFactory getFixtureFactory() {
		return fixtureFactory;
	}

	public void setFixtureFactory(FixtureFactory fixtureFactory) {
		this.fixtureFactory = fixtureFactory;
	}

	public BodyDef getDef() {
		return def;
	}

	public void setDef(BodyDef def) {
		this.def = def;
	}

	public float getX() {
		return bodies.get(0).getPosition().x;
	}
	
	public float getY() {
		return bodies.get(0).getPosition().y;
	}
	
	/**
	 * @return rotation as radians
	 */
	public float getRotation() {
		return bodies.get(0).getAngle();
	}
	
	public void setLocation(float x, float y) {
		bodies.get(0).getPosition().x = x;
		bodies.get(0).getPosition().y = y;
	}
	
	/**
	 * @param angle as radians
	 */
	public void addRotation(float angle) {
		bodies.get(0).getTransform().setRotation((bodies.get(0).getAngle()+angle));
	}
	
	public void applyForce(float xf, float yf) {
		bodies.get(0).applyForceToCenter(xf, yf);
	}
	
	public List<Body> getBodies() {
		return bodies;
	}

	public void setBodies(List<Body> bodies) {
		this.bodies = bodies;
	}

	public Body getBody() {
		return bodies.get(0);
	}

}
