/*
 * Bullet.java
 *
 * This is a passive model, and this model does very little by itself.  
 * The CollisionController does most of the hard work.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package edu.cornell.gdiac.mangosnoops.entity;

import edu.cornell.gdiac.mangosnoops.*;

/**
 * Model class for bullets fired by the ship.
 */
public class Bullet extends GameObject {
	/**
	 * Returns the type of this object.
	 *
	 * We use this instead of runtime-typing for performance reasons.
	 *
	 * @return the type of this object.
	 */
	public ObjectType getType() {
		return ObjectType.BULLET;
	}

	/**
	 * Initialize bullet with trivial starting position.
	 */
	public Bullet() {
	}
}