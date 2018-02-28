/*
 * Ship.java
 *
 * This is a passive model, and this model does very little by itself.  
 * All of its work is done by the CollisionController or the 
 * GameplayController. 
 * 
 * This separation is very important for this class because it has a lot 
 * of interactions with other classes.  When a ship fires, it creates  
 * bullets. If did not move that behavior to the GameplayController,
 * then we would have to have a reference to the GameEngine in this
 * class. Tight coupling with the GameEngine is a very bad idea, so
 * we have separated this out.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package edu.cornell.gdiac.mangosnoops.entity;

import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

/**
 * Model class for the player ship.
 */
public class Ship extends GameObject {
	/// CONSTANTS
	/** Horizontal speed **/
	private static final float BEETLE_SPEED = 4.0f;
	/** How long between shots */
	private static final int COOLDOWN_TIME  = 8;
	/** Cooldown bonus if we don't fire */
	private static final int COOLDOWN_BONUS = 3;
	/** How fast we change frames (one frame per 4 calls to update) */
	private static final float ANIMATION_SPEED = 0.25f;
	/** The number of animation frames in our filmstrip */
	private static final int   NUM_ANIM_FRAMES = 2;
	//#region REMOVE ME
	/** Number of kills for a power up */
	private static final int POWER_KILL = 20;
	/** Length of time of power up */
	private static final int POWER_TIME = 600; // 5 seconds
	//#endregion
	
	/// ATTRIBUTES
	/** The left/right movement of the player this turn */
	private float movement = 0.0f;
	/** Whether this ship is currently firing */
	private boolean firing = false;
	/** How long before ship can fire again */
	private int cooldown;
	/** Current animation frame for this ship */
	private float animeframe;
	//#region REMOVE ME
	// Code to allow power-ups
	/** Whether we are currently powered up */
	private boolean powered = false;
	/** Number of kills so far (to acquire power-up) */
	private int killcount = 0;
	/** The current amount of time with the power-up */
	private int powertime;
	//#endregion
	
	/**
	 * Returns the type of this object.
	 *
	 * We use this instead of runtime-typing for performance reasons.
	 *
	 * @return the type of this object.
	 */
	public ObjectType getType() {
		return ObjectType.SHIP;
	}

	/**
	 * Returns the current player (left/right) movement input.
	 *
	 * @return the current player movement input.
	 */
	public float getMovement() {
		return movement;
	}
	
	/**
	 * Sets the current player (left/right) movement input.
	 *
	 * @param value the current player movement input.
	 */
	public void setMovement(float value) {
		movement = value;
	}
	
	/**
	 * Returns true if the ship is actively firing.
	 *
	 * @return true if the ship is actively firing.
	 */
	public boolean isFiring() {
		return firing && cooldown <= 0;
	}
	
	/**
	 * Sets whether the ship is actively firing.
	 *
	 * @param value whether the ship is actively firing.
	 */
	public void setFiring(boolean value) {
		firing = value;
	}
	
	/**
	 * Resets the cooldown so that the weapon can fire again.
	 *
	 * Since weapon fire is managed externally, we need this method to 
	 * reset the weapon after use. Otherwise, the player can fire 
	 * a continuous stream of bullets.
	 */
	public void resetCooldown() {
		cooldown = COOLDOWN_TIME;
	}

	//#region REMOVE ME
	/** 
	 * Returns true if the powerup is active.
	 *
	 * @return true if the powerup is active.
	 */
	public boolean isPowered() {
		return powered;
	}
	
	/**
	 * Acknowledges a kill to earn power ups
	 */
	public void registerKill() {
		if (!powered) {
			killcount++;
		}

		// Magic Numbers
		if (killcount == POWER_KILL) {
			powertime = POWER_TIME;
			powered   = true;
			killcount = 0;
		}
	}
	//#endregion
	
	/**
	 * Initialize a ship with trivial starting position.
	 */
	public Ship() {
		cooldown   = 0;
		animeframe = 0.0f;
	}
	
	public void setTexture(Texture texture) {
		animator = new FilmStrip(texture,1,2,2);
		radius = animator.getRegionHeight() / 2.0f;
		origin = new Vector2(animator.getRegionWidth()/2.0f, animator.getRegionHeight()/2.0f);
	}
	
	/**
	 * Updates the animation frame and position of this ship.
	 *
	 * Notice how little this method does.  It does not actively fire the weapon.  It 
	 * only manages the cooldown and indicates whether the weapon is currently firing. 
	 * The result of weapon fire is managed by the GameplayController.
	 *
	 * @param delta Number of seconds since last animation frame
	 */
	public void update(float delta) {
		// Call superclass's update
		super.update(delta);

		// Increase animation frame, but only if trying to move
		if (movement != 0.0f) {
			animeframe += ANIMATION_SPEED;
			if (animeframe >= NUM_ANIM_FRAMES) {
				animeframe -= NUM_ANIM_FRAMES;
			}
			position.x += movement * BEETLE_SPEED;
		}

		// Decrease time until ship can fire again
		if (cooldown > 0) {
			cooldown--;
		}

		if (!firing) {
			// Cool down faster when not holding space
			cooldown -= COOLDOWN_BONUS;
		}
		
		//#region REMOVE ME
		if (powered) {
			powertime--;
			if (powertime == 0) {
				powered = false;
			}
		}
		//#endregion
	}

	/**
	 * Draws this shell to the canvas
	 *
	 * There is only one drawing pass in this application, so you can draw the objects 
	 * in any order.
	 *
	 * @param canvas The drawing context
	 */
	public void draw(GameCanvas canvas) {
		float x = animator.getRegionWidth()/2.0f;
		float y = animator.getRegionHeight()/2.0f;
		animator.setFrame((int)animeframe);
		canvas.draw(animator, Color.WHITE, x, y, position.x, position.y, 0.0f, 1.0f, 1.f);
	}
	
}
