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
import com.badlogic.gdx.math.*;
import edu.cornell.gdiac.util.*;

/**
 * Class for reading player keyboard input.
 */
public class InputController {
    // Wheel constants: TODO UPDATE THESE
    /** Wheel inner radius */
    private static final Vector2 WHEEL_INNER = new Vector2(3.0f,3.0f);
    /** Wheel outer radius */
    private static final Vector2 WHEEL_OUTER = new Vector2(5.0f,5.0f);

	// Fields to manage game state
	/** Whether the left mouse button was clicked. */
	protected boolean mouseClicked;
	/** The left/right movement of the player this turn -- left is negative */
	private float movement = 0.0f;
    /** Whether the mouse has been dragged while clicking. */
	private boolean mouseDragged;


	/**
	 * Returns the amount of sideways movement. 
	 *
	 * -1 = left, 1 = right, 0 = still
	 *
	 * @return the amount of sideways movement. 
	 */
	public float getMovement() { return movement; }

	/**
	 * Returns true if the left mouse button was clicked.
	 */
	public boolean didClickLeft() { return mouseClicked; }

	/**
	 * Creates a new input controller
	 */
	public InputController() {
	}

    /**
     * Returns true if the mouse is positioned inside the area of the wheel.
     * @return
     */
	private boolean inWheelArea() {
        return false;
	}

	/**
	 * Reads the input for the player and converts the result into game logic.
	 */
	public void readInput() {
		mouseClicked = (Gdx.input.isButtonPressed(Input.Buttons.LEFT));

		if (inWheelArea()) {
		    return;
		    // do things
        }
	}

    public Vector2 mouseCoords() {
        if (mouseClicked) {
            return new Vector2(Gdx.input.getX(), Gdx.input.getY());
        }
        return null;
    }
}
