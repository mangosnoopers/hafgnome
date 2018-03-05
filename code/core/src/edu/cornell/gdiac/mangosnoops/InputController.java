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
import edu.cornell.gdiac.mangosnoops.entity.Wheel;
import edu.cornell.gdiac.util.*;

/**
 * Class for reading player keyboard input.
 */
public class InputController {
    // Constants
    /** Factor to translate an angle to left/right movement */
    private static final float ANGLE_TO_LR = 7.0f;
    /** Default angle from which to view the road */
    private static final float DEFAULT_VIEW = 1.54f;

	// Fields to manage game state
	/** Whether the left mouse button was clicked. */
	private boolean mouseClicked;
    /** Vector location of first click */
    private Vector2 firstClick;

    // Wheel controls
    /** The wheel used for user control */
    private Wheel w;
	/** The left/right movement of the player's view -- left is negative */
	private float movement = 0.0f;

    /**
     * Creates a new input controller.
     */
    public InputController() {
    }

	/**
	 * Returns the amount of sideways movement. 
	 */
	public float getMovement() { return movement; }

    /**
     * Set the wheel used for input controls.
     */
    public void setWheel(Wheel w) { this.w = w; }

    /**
     * Returns true if the mouse is positioned inside the area of the wheel.
     *
     * @param p the vector giving the mouse's (x,y) screen coordinates
     */
	private boolean inWheelArea(Vector2 p) {
        return true;
	}

    /**
     * Processes the player clicking and dragging on the wheel. The user must
     * have already clicked on the wheel (i.e. firstClick is not null).
     *
     * This method sets the angle to turn the wheel by, and the left/right shift
     * in the player's view.
     */
	private void processWheelTurn() {
	    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

	        // bounce back to center if cursor leaves wheel
	        if (!inWheelArea(new Vector2(Gdx.input.getX(), Gdx.input.getY()))) {
	            w.setAng(0.0f);
	            movement = 0.0f; //TODO: change this to smt that makes sense
	            return;
            }

            // otherwise change wheel angle and lateral screen movement
            w.setAng(w.getAng() - Gdx.input.getDeltaX());
	        movement = w.getAng() / ANGLE_TO_LR;
        }

    }

	/**
	 * Reads the input for the player and converts the result into game logic.
	 */
	public void readInput() {
		mouseClicked = (Gdx.input.isButtonPressed(Input.Buttons.LEFT));
        if (mouseClicked) {
            firstClick = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        } else {
            firstClick = null;
        }

		// player clicked the wheel
		if (w != null && mouseClicked && inWheelArea(firstClick)) {
		    processWheelTurn();
        }
	}

}
