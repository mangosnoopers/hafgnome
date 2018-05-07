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
import edu.cornell.gdiac.mangosnoops.Menus.SettingsMenu;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;
import edu.cornell.gdiac.mangosnoops.hudentity.Wheel;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Class for reading player keyboard input.
 */
public class InputController {
	// Fields to manage game state
	/**Settings menu for full screen fields**/
	private SettingsMenu settings;
	/** Whether the reset button was pressed. */
	protected boolean resetPressed;
	/** Whether the exit button was pressed. */
	protected boolean exitPressed;
	/** Whether the left mouse button was clicked. */
	private boolean mouseClicked;
	/**Last recorded state of the mouse button **/
	private boolean prevMouseClicked;
	/** Vector location of click */
	private Vector2 clickPos;
	/** Change in x of the input */
	private float dx;
	/** Change in y of the input */
	private float dy;
	/** Keyboard inputted number */
	private int numKeyPressed;
	/** Whether or not keyboard turn was inputted */
	private boolean turnPressed;
	/** Whether the pause button was pressed or not **/
	private boolean pausePressed;
	/**
	 * Creates a new input controller.
	 */
	public InputController(SettingsMenu settings) {
		this.settings = settings;
	}

	/**
	 * @return the change in x of the input.
	 */
	public float getDX() { return dx; }

	/**
	 * @return the change in x of the input.
	 */
	public float getDY() { return dy; }

	/**
	 * @return The current mouse position
	 */
	public Vector2 getClickPos() { return clickPos; }

	/**
	 * @return The current mouse position
	 */
	public Vector2 getHoverPos() { return new Vector2(Gdx.input.getX(),Gdx.input.getY()); }

	/**
	 * @return true if the reset button was pressed.
	 */
	public boolean didReset() {
		return resetPressed;
	}

	/**
	 * @return true if the exit button was pressed.
	 */
	public boolean didExit() {
		return exitPressed;
	}

	/**
	 * @return true if left click is being pressed
	 */
	public boolean isMousePressed() { return mouseClicked; }

	/**
	 * @return true if the mouse was clicked on the last recorded instance
	 */
	public boolean isPrevMousePressed(){ return prevMouseClicked; }

	/**
	 * @return true if a turn was inputted by keyboard
	 */
	public boolean isTurnPressed(){ return turnPressed; }

	/**
	 * @return the keyboard number input. If a number was not inputted,
	 * return -1.
	 */
	public int getNumKeyPressed() {
		return numKeyPressed;
	}

	/**
	 * @return true if the pause button was pressed
	 */
	public boolean pressedPause(){ return pausePressed; }


	/**
	 * Reads the input for the player and converts the result into game logic.
	 */
	public void readInput() {
		//Full screen/Escape Full Screen
		if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
			settings.setFullScreen(!settings.isFullScreen());
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.graphics.setWindowedMode((int)Image.getScreenWidth(),(int)Image.getScreenHeight());
		}

		//Exit Screen
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			Gdx.app.exit();
		}

		//Read input for Gameplay
		prevMouseClicked = mouseClicked;
		resetPressed = Gdx.input.isKeyPressed(Input.Keys.R);
		mouseClicked = (Gdx.input.isButtonPressed(Input.Buttons.LEFT));
		pausePressed = (Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) ;

		//Process number input for SAT Question
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
			numKeyPressed = 0;
		} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
			numKeyPressed = 1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
			numKeyPressed = 2;
		} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
			numKeyPressed = 3;
		} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
			numKeyPressed = 4;
		} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
			numKeyPressed = 5;
		} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
			numKeyPressed = 6;
		} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_7)) {
			numKeyPressed = 7;
		} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
			numKeyPressed = 8;
		} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
			numKeyPressed = 9;
		} else {
			numKeyPressed = -1;
		}
		//Process mouse input
		if (mouseClicked) {
			clickPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			dx = Gdx.input.getDeltaX();
			dy = Gdx.input.getDeltaY();
			//System.out.println(mouseClicked);
			//            System.out.println("Mouse at: "+ clickPos);
		} else {
			clickPos = null;
		}
		turnPressed = false;
		//Process WASD/Arrow input (for turning wheel)
        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			turnPressed = true;
            if(dx > 10){
                dx -= 4;
            } else if(dx > 0) {
                dx = dx*1.03f;
            } else {
                dx = 6;
            }
        } else if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			turnPressed = true;
            if (dx < -10) {
                dx -= 4;
            } else if(dx < 0) {
                dx = dx*1.03f;
            } else {
                dx = -6;
            }
        } else if(!mouseClicked) {
            dx = 0;
        }
	}

}
