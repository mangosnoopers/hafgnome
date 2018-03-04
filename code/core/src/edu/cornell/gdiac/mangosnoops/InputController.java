/*
 * InputController.java
 *
 * This class buffers in input from the devices and converts it into its
 * semantic meaning. If your game had an option that allows the player to
 * remap the control keys, you would store this information in this class.
 * That way, the main GameEngine does not have to keep track of the current
 * key mapping.
 *
 * This class is a singleton for this application, but we have not designed
 * it as one.  That is to give you some extra functionality should you want
 * to add multiple ships.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.*;
import edu.cornell.gdiac.util.*;

/**
 * Class for reading player keyboard input.
 */
public class InputController {
	// Fields to manage game state
	/** Whether the wheel was pressed. */
	protected boolean wheelPressed;
	/** Whether the wheel was released. **/
	protected boolean wheelReleased;
	/** The left/right movement of the player this turn -- left is negative */
	private float movement = 0.0f;


	/**
	 * Returns the amount of sideways movement. 
	 *
	 * -1 = left, 1 = right, 0 = still
	 *
	 * @return the amount of sideways movement. 
	 */
	public float getMovement() {
		return movement;
	}

	/**
	 * Returns true if the wheel was pressed.
	 *
	 * @return true if the wheel was pressed.
	 */
	public boolean didPressWheel() {
		return wheelPressed;
	}

	/**
	 * Returns true if the wheel was released.
	 *
	 * @return true if the wheel was released.
	 */
	public boolean didReleaseWheel() {
		return wheelReleased;
	}

	/**
	 * Creates a new input controller
	 */
	public InputController() {
	}

	/**
	 * Reads the input for the player and converts the result into game logic.
	 */
	public void readInput() {
		wheelPressed = (Gdx.input.isButtonPressed(Input.Buttons.LEFT));
		//TODO : Read rotation input and change movement
	}

}
