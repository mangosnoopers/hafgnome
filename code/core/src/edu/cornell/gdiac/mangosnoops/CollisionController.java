/*
 * CollisionController.java
 *
 * This controller implements basic collision detection as described in
 * the instructions.  All objects in this game are treated as circles,
 * and a collision happens when circles intersect.
 *
 * This controller is EXTREMELY ineffecient.  To improve its performance,
 * you will need to use collision cells, as described in the instructions.
 * You should not need to modify any method other than the constructor
 * and processCollisions.  However, you will need to add your own methods.
 *
 * This is the only file that you need to modify as the first part of
 * the lab. 
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import edu.cornell.gdiac.mangosnoops.entity.*;

/**
 * Controller implementing simple game physics.
 */
public class CollisionController {
	/** TODO: Maybe delete? Window height */
	private static final float WINDOW_HEIGHT = 600;
	/** TODO: Maybe delete? */
	/** A factor to determine the area of the wheel a gnome is allowed to hit */
	private static final float WHEEL_SAFE_AREA = 100.0f;

	// These cannot be modified after the controller is constructed.
	// If these change, make a new constructor.
	/** Width of the collision geometry */
	private float width;
	/** Height of the collision geometry */
	private float height;

	/**
	 * Returns width of the game window (necessary to detect out of bounds)
	 *
	 * @return width of the game window
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Returns height of the game window (necessary to detect out of bounds)
	 *
	 * @return height of the game window
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Creates a CollisionController for the given screen dimensions.
	 *
	 * @param width   Width of the screen 
	 * @param height  Height of the screen 
	 */
	public CollisionController(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * This is the main (incredibly unoptimized) collision detetection method.
	 *
	 * @param gnomez List of live gnomes to check
	 * @param yonda  Player's car
	 */
	public void processCollisions(Array<Gnome> gnomez, Car yonda) {
		processBounds(yonda);
		for (Gnome g : gnomez) {
			handleCollision(yonda, g);
		}
	}


	/**
	 * Check if a GameObject is out of bounds and take action.
	 *
	 * Obviously an object off-screen is out of bounds.
	 *
	 * @param c      Player's car
	 */
	private void processBounds(Car c) {
		// TODO: commented this out to get game to run, car is null rn
		//if(c.getX() < 0 || c.getX() > width ) c.setDestroyed(true);
	}

	/**
	 * Collide a gnome with a car.
	 */
	private void handleCollision(Car c, Gnome g) {
		/** TODO: commenting this out to get game 2 run
		 *throw new java.lang.UnsupportedOperationException();
		 */
	}

	/** TODO: delete this, janky stuff used for gameplay prototype
	 */
	public void processCollisions(Array<Gnome> gnomez, Wheel w) {
		for (Gnome g : gnomez) {
			handleCollision(w, g);
		}
	}

	/** TODO: probably delete this for final, used for gameplay prototype
	 *  Collide a gnome with the steering wheel.
	 */
	private void handleCollision(Wheel w, Gnome g) {
		float gx = g.getDrawCoords().x;
		float gy = g.getDrawCoords().y;
		Vector2 cen = w.getCenter();
		Texture wsprite = w.getWheelSprite();

		// make wheel inactive if it collides with gnome
		if(gx > cen.x - wsprite.getWidth()/2.0f + WHEEL_SAFE_AREA
				&& gx < cen.x + wsprite.getWidth()/2.0f - WHEEL_SAFE_AREA
		   		&& gy > cen.y - wsprite.getHeight()/2.0f + WHEEL_SAFE_AREA
				&& gy < cen.y + wsprite.getHeight()/2.0f - WHEEL_SAFE_AREA*2.0f) {
			w.setActive(false);
		}
	}
}