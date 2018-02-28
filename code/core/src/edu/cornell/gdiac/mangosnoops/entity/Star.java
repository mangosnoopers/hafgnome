/*
 * Star.java
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
import com.badlogic.gdx.graphics.*;

/**
 * Model class for stars caused by shell explosions.
 */
public class Star extends GameObject {
	/** Mean life-expectancy of a star */
	private static final int STAR_AGE = 30;
	/** Variance of star ages */
	private static final int AGE_RANGE = 10;

	/** Current age of star.  Deleted when reach 0. */
	private int age;
	/** Current angle of star, as they can rotate */
	private float angle;

	/**
	 * Returns the type of this object.
	 *
	 * We use this instead of runtime-typing for performance reasons.
	 *
	 * @return the type of this object.
	 */
	public ObjectType getType() {
		return ObjectType.STAR;
	}
	
	/**
	 * Returns the current angle of this star 
	 *
	 * @return the current angle of this star 
	 */
	public float getAngle() {
		return angle;
	}
	
	/**
	 * Initialize star with trivial starting position.
	 */
	public Star() {
		// Stars die over time
		age = RandomController.rollInt(STAR_AGE - AGE_RANGE, STAR_AGE + AGE_RANGE);
	}

	/**
	 * Updates the age and angle of this star.
	 *
	 * @param delta Number of seconds since last animation frame
	 */
	public void update(float delta) {
		// Call superclass's update
		super.update(delta);
		
		// Decrease time until death; die if it's time
		if (--age == 0) {
			destroyed = true;
		}
		
		// Compute a new angle of rotation.
		angle = (float)(delta*1000 % (8 * Math.PI)); // MAGIC NUMBERS
	}
	

	/**
	 * Draws this object to the canvas
	 *
	 * There is only one drawing pass in this application, so you can draw the objects 
	 * in any order.
	 *
	 * @param canvas The drawing context
	 */
	public void draw(GameCanvas canvas) {
		canvas.draw(animator, Color.WHITE, origin.x, origin.y, position.x, position.y, angle, 1, 1);
	}
	
}
