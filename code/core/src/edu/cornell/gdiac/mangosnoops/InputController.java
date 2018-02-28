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
 * Class for reading player input. 
 *
 * This supports both a keyboard and X-Box controller. In previous solutions, we only 
 * detected the X-Box controller on start-up.  This class allows us to hot-swap in
 * a controller via the new XBox360Controller class.
 */
public class InputController {
	// Fields to manage game state
	/** Whether the reset button was pressed. */
	protected boolean resetPressed;
	/** Whether the flood button was pressed. */
	protected boolean floodPressed;
	/** Whether the exit button was pressed. */
	protected boolean exitPressed;
	/** Whether the fire button was pressed. */
	private boolean firePressed;
	/** How much did we move (left/right)? */
	private float offset;
	/** XBox Controller support */
	private XBox360Controller xbox;
	
	/**
	 * Returns the amount of sideways movement. 
	 *
	 * -1 = left, 1 = right, 0 = still
	 *
	 * @return the amount of sideways movement. 
	 */
	public float getMovement() {
		return offset;
	}

	/**
	 * Returns true if the fire button was pressed.
	 *
	 * @return true if the fire button was pressed.
	 */
	public boolean didFire() {
		return firePressed;
	}

	/**
	 * Returns true if the reset button was pressed.
	 *
	 * @return true if the reset button was pressed.
	 */
	public boolean didReset() {
		return resetPressed;
	}

	/**
	 * Returns true if the player wants to flood shells.
	 *
	 * @return true if the player wants to flood shells.
	 */
	public boolean didFlood() {
		return floodPressed;
	}

	/**
	 * Returns true if the exit button was pressed.
	 *
	 * @return true if the exit button was pressed.
	 */
	public boolean didExit() {
		return exitPressed;
	}
	
	/**
	 * Creates a new input controller
	 * 
	 * The input controller attempts to connect to the X-Box controller at device 0,
	 * if it exists.  Otherwise, it falls back to the keyboard control.
	 */
	public InputController() { 
		// If we have a game-pad for id, then use it.
		xbox = new XBox360Controller(0);
	}

	/**
	 * Reads the input for the player and converts the result into game logic.
	 */
	public void readInput() {
		// Check to see if a GamePad is connected
		if (xbox.isConnected()) {
			readGamepad();
			readKeyboard(true); // Read as a back-up
		} else {
			readKeyboard(false);
		}
	}

	/**
	 * Reads input from an X-Box controller connected to this computer.
	 */
	private void readGamepad() {

		resetPressed = xbox.getA();
		floodPressed = xbox.getRB();
		exitPressed  = xbox.getBack();

		// Increase animation frame, but only if trying to move
		offset = xbox.getLeftX();
		firePressed = xbox.getRightTrigger() > 0.6f;
	}

	/**
	 * Reads input from the keyboard.
	 *
	 * This controller reads from the keyboard regardless of whether or not an X-Box
	 * controller is connected.  However, if a controller is connected, this method
	 * gives priority to the X-Box controller.
	 *
	 * @param secondary true if the keyboard should give priority to a gamepad
	 */
	private void readKeyboard(boolean secondary) {
		// Give priority to gamepad results
		resetPressed = (secondary && resetPressed) || (Gdx.input.isKeyPressed(Input.Keys.R));
		floodPressed = (secondary && floodPressed) || (Gdx.input.isKeyPressed(Input.Keys.F));
		exitPressed  = (secondary && exitPressed) || (Gdx.input.isKeyPressed(Input.Keys.ESCAPE));

		offset = (secondary ? offset : 0.0f);
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			offset += 1.0f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			offset -= 1.0f;
		}
		firePressed =  (secondary && firePressed) || Gdx.input.isKeyPressed(Input.Keys.SPACE);
	}
}
