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
    // Constants
    /** Wheel inner radius */
    private static final float WHEEL_INNER = 3.0f;
    /** Wheel outer radius */
    private static final float WHEEL_OUTER = 5.0f;
    /** Factor to translate an angle to left/right movement */
    private static final float ANGLE_TO_LR = 3.0f;

	// Fields to manage game state
	/** Whether the left mouse button was clicked. */
	private boolean mouseClicked;
    /** Vector location of first click */
    private Vector2 firstClick;

    /** The angle used to rotate the wheel, in radians */
    private float theta = 0.0f;
	/** The left/right movement of the player's view -- left is negative */
	private float movement = 0.0f;


	/**
	 * Returns the amount of sideways movement. 
	 *
	 * -1 = left, 1 = right, 0 = still
	 *
	 * @return the amount of sideways movement. 
	 */
	public float getMovement() { return movement; }

	/**
	 * Returns the angle used to rotate the wheel. If this angle is 0, the
     * wheel is still.
	 */
	public float getWheelRotation() { return theta; }

	/**
	 * Creates a new input controller
	 */
	public InputController() {
	}

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
	    float origTheta = theta;
	    if (mouseClicked && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//            Vector2 endPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
//            theta += endPosition.angle(firstClick);
            theta += Gdx.input.getDeltaX();

	        // bounce back if cursor leaves wheel
	        if (!inWheelArea(new Vector2(Gdx.input.getX(), Gdx.input.getY()))) {
	            theta = origTheta;
	            movement = theta / ANGLE_TO_LR;
	            return;
            }
        }

        // when mouse is let go, set theta and movement
//        Vector2 endPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
//	    theta = endPosition.angle(firstClick);
//	    movement = theta / ANGLE_TO_LR;
    }

	/**
	 * Reads the input for the player and converts the result into game logic.
	 */
	public void readInput() {
		mouseClicked = (Gdx.input.isButtonPressed(Input.Buttons.LEFT));
		firstClick = mouseCoords();

		// player clicked the wheel
		if (mouseClicked && inWheelArea(firstClick)) {
		    processWheelTurn();
        }
	}

    /**
     * Returns a vector giving the location of the first mouse click, or null
     * if the mouse was not clicked.
     */
    private Vector2 mouseCoords() {
        if (mouseClicked) {
            return new Vector2(Gdx.input.getX(), Gdx.input.getY());
        }
        return null;
    }
}
