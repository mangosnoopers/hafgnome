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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;
import edu.cornell.gdiac.mangosnoops.hudentity.Wheel;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Class for reading player keyboard input.
 */
public class InputController {
	// Fields to manage game state
	/** Whether the reset button was pressed. */
	protected boolean resetPressed;
	/** Whether the exit button was pressed. */
	protected boolean exitPressed;
	/** Whether the left mouse button was clicked. */
	private boolean mouseClicked;
    /** Vector location of click */
    private Vector2 clickPos;
    /** Change in x of the input */
    private float dx;
    /** Change in y of the input */
	private float dy;

	/**
     * Creates a new input controller.
     */
    public InputController() {
    }

	/**
	 * Returns the change in x of the input.
	 */
	public float getDX() { return dx; }

	/**
	 * Returns the change in x of the input.
	 */
	public float getDY() { return dy; }

	/**
	 * Returns the current mouse position.
	 */
	public Vector2 getClickPos() { return clickPos; }

	/**
	 * Returns true if the reset button was pressed.
	 *
	 * @return true if the reset button was pressed.
	 */
	public boolean didReset() {
		return resetPressed;
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
	 * Returns true if left click is being pressed
	 * @return true if left click is being pressed
	 */
	public boolean mousePressed() { return mouseClicked; }
	/**
	 * Reads the input for the player and converts the result into game logic.
	 */
	public void readInput() {
		resetPressed = (Gdx.input.isKeyPressed(Input.Keys.R));
		exitPressed  = (Gdx.input.isKeyPressed(Input.Keys.ESCAPE));
		mouseClicked = (Gdx.input.isButtonPressed(Input.Buttons.LEFT));
        if (mouseClicked) {
            clickPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            dx = Gdx.input.getDeltaX();
            dy = Gdx.input.getDeltaY();
			//System.out.println(mouseClicked);
            System.out.println("Mouse at: "+ clickPos);
        } else {
            clickPos = null;
        }
	}

}
