/*
 * GameMode.java
 *
 * This is the primary class file for running the game.  You should study this file for
 * ideas on how to structure your own root class. This class follows a 
 * model-view-controller pattern fairly strictly.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;

import edu.cornell.gdiac.util.*;

/**
 * The primary controller class for the game.
 *
 * While GDXRoot is the root class, it delegates all of the work to the player mode
 * classes. This is the player mode class for running the game. In initializes all 
 * of the other classes in the game and hooks them together.  It also provides the
 * basic game loop (update-draw).
 */
public class GameMode implements Screen {
	/** 
 	 * Track the current state of the game for the update loop.
 	 */
	public enum GameState {
		/** Before the game has started */
		INTRO,
		/** While we are playing the game */
		PLAY,
		/** When the ships is dead (but shells still work) */
		OVER
	}

	// LEVEL - TODO: Don't just hardcode the JSON url in lol
	private static String LEVEL_JSON = "temp.json";

	// GRAPHICS AND SOUND RESOURCES
	/** The file for the background image to scroll */
	private static String BKGD_FILE = "images/background.png";
	/** The font file to use for scores */
	private static String FONT_FILE = "fonts/ComicSans.ttf";

	/** The file for the road image */
	private static String ROAD_FILE = "images/road.png";

	private static String CLOUDS_FILE = "images/clouds.png";

	private static String SKY_FILE = "images/sky.png";

	// Loaded assets
	/** The background image for the game */
	private Texture background;
	/** The font for giving messages to the player */
	private BitmapFont displayFont;
	private int FONT_SIZE = 24;

	/** Track all loaded assets (for unloading purposes) */
	private Array<String> assets;

	/** A pixel map of the original road image, to be mapped to the
	 * pseudo-3d view */
	private Pixmap roadMap;

	private Texture clouds;

	private Texture sky;


	/** 
	 * Preloads the assets for this game.
	 * 
	 * The asset manager for LibGDX is asynchronous.  That means that you
	 * tell it what to load and then wait while it loads them.  This is 
	 * the first step: telling it what to load.
	 * 
	 * @param manager Reference to global asset manager.
	 */
	public void preLoadContent(AssetManager manager) {
		// Load the background.
		manager.load(BKGD_FILE,Texture.class);
		assets.add(BKGD_FILE);

		// Load the road asset
		manager.load(ROAD_FILE, Pixmap.class);

		// Load the clouds
		manager.load(CLOUDS_FILE, Texture.class);

		// Load sky
		manager.load(SKY_FILE, Texture.class);

		
		// Load the font
		FreetypeFontLoader.FreeTypeFontLoaderParameter size2Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		size2Params.fontFileName = FONT_FILE;
		size2Params.fontParameters.size = FONT_SIZE;
		manager.load(FONT_FILE, BitmapFont.class, size2Params);
		assets.add(FONT_FILE);
		
		// Preload gameplay content
		gameplayController.preLoadContent(manager,assets);
	}

	/** 
	 * Loads the assets for this game.
	 * 
	 * The asset manager for LibGDX is asynchronous.  That means that you
	 * tell it what to load and then wait while it loads them.  This is 
	 * the second step: extracting assets from the manager after it has
	 * finished loading them.
	 * 
	 * @param manager Reference to global asset manager.
	 */
	public void loadContent(AssetManager manager) {
		// Allocate the font
		if (manager.isLoaded(FONT_FILE)) {
			displayFont = manager.get(FONT_FILE,BitmapFont.class);
		} else {
			displayFont = null;
		}

		// Allocate the background
		if (manager.isLoaded(BKGD_FILE)) {
			background = manager.get(BKGD_FILE, Texture.class);
			background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}

		if (manager.isLoaded(ROAD_FILE)) {
			roadMap = manager.get(ROAD_FILE, Pixmap.class);
		}

		if (manager.isLoaded(CLOUDS_FILE)) {
			clouds = manager.get(CLOUDS_FILE, Texture.class);
		}

		if (manager.isLoaded(SKY_FILE)) {
			sky = manager.get(CLOUDS_FILE, Texture.class);
		}


		// Load gameplay content
		gameplayController.loadContent(manager);
	}

	/** 
	 * Unloads the assets for this game.
	 * 
	 * This method erases the static variables.  It also deletes the associated textures 
	 * from the asset manager.
	 * 
	 * @param manager Reference to global asset manager.
	 */
	public void unloadContent(AssetManager manager) {
    	for(String s : assets) {
    		if (manager.isLoaded(s)) {
    			manager.unload(s);
    		}
    	}
	}
	
	/// CONSTANTS
	
	/** Factor used to compute where we are in scrolling process */
	private static final float TIME_MODIFIER    = 0.06f;
	/** Offset for the shell counter message on the screen */
	private static final float COUNTER_OFFSET   = 5.0f;
	/** Offset for the game over message on the screen */
	private static final float GAME_OVER_OFFSET = 40.0f;
	
	/** Reference to drawing context to display graphics (VIEW CLASS) */
	private GameCanvas canvas;
	
	/** Reads input from keyboard or game pad (CONTROLLER CLASS) */
	private InputController inputController;
	/** Handle collision and physics (CONTROLLER CLASS) */
	private CollisionController collisionController;
	/** Constructs the game models and handle basic gameplay (CONTROLLER CLASS) */
	private GameplayController gameplayController;
	
	/** Variable to track the game state (SIMPLE FIELDS) */
	private GameState gameState;
	/** Variable to track total time played in milliseconds (SIMPLE FIELDS) */
	private float totalTime = 0;
	/** Whether or not this player mode is still active */
	private boolean active;
	
	/** Listener that will update the player mode when we are done */
	private ScreenListener listener;

	/**
	 * Creates a new game with the given drawing context.
	 *
	 * This constructor initializes the models and controllers for the game.  The
	 * view has already been initialized by the root class.
	 */
	public GameMode(GameCanvas canvas) {
		this.canvas = canvas;
		active = false;
		// Null out all pointers, 0 out all ints, etc.
		gameState = GameState.INTRO;
		assets = new Array<String>();

		// Create the controllers.
		inputController = new InputController();
		gameplayController = new GameplayController(new LevelObject(LEVEL_JSON));
		// YOU WILL NEED TO MODIFY THIS NEXT LINE
		collisionController = new CollisionController(canvas.getWidth(), canvas.getHeight());
	}
	
	/**
	 * Dispose of all (non-static) resources allocated to this mode.
	 */
	public void dispose() {
		inputController = null;
		gameplayController = null;
		collisionController = null;
		canvas = null;
	}
	
	
	/**
	 * Update the game state.
	 *
	 * We prefer to separate update and draw from one another as separate methods, instead
	 * of using the single render() method that LibGDX does.  We will talk about why we
	 * prefer this in lecture.
	 *
	 * @param delta Number of seconds since last animation frame
	 */
	private void update(float delta) {
		// Process the game input
		inputController.readInput();

		// Test whether to reset the game.
		switch (gameState) {
		case INTRO:
			gameState = GameState.PLAY;
			gameplayController.start(canvas.getWidth() / 2.0f, 0);
			break;
		case OVER:
			if (inputController.didReset()) {
				gameState = GameState.PLAY;
				gameplayController.reset();
				//TODO: Make the next two lines less sketch
				canvas.resetCam();
				inputController.resetMovement();
				gameplayController.start(canvas.getWidth() / 2.0f, 0);
			} else {
				play(delta);
			}
            break;
		case PLAY:
			play(delta);
			break;
		default:
			break;
		}
	}
	int done = 0;
	
	/**
	 * This method processes a single step in the game loop.
	 *
	 * @param delta Number of seconds since last animation frame
	 */
	protected void play(float delta) {
		// if no player is alive, declare game over
		/* TODO: commented this out to get game to run, car is null rn
		if (gameplayController.getCar().isDestroyed()) {
			gameState = GameState.OVER;
		}
		*/
		// TODO: only for gameplay prototype
		if (!gameplayController.getWheel().isActive()) {
            gameState = GameState.OVER;
        }

		// Update objects.
		gameplayController.resolveActions(inputController,delta);

		// Check for collisions
		totalTime += (delta*1000); // Seconds to milliseconds
		float offset =  canvas.getWidth() - (totalTime * TIME_MODIFIER) % canvas.getWidth();
		// TODO: changed this to wheel instead of car for gameplay prototype
		collisionController.processCollisions(gameplayController.getGnomez(),gameplayController.getWheel());

		// Clean up destroyed objects
		gameplayController.garbageCollect();
	}
	
	/**
	 * Draw the status of this player mode.
	 *
	 * We prefer to separate update and draw from one another as separate methods, instead
	 * of using the single render() method that LibGDX does.  We will talk about why we
	 * prefer this in lecture.
	 */
	private void draw(float delta) {
		float offset = -((totalTime * TIME_MODIFIER) % canvas.getWidth());
		canvas.begin();
		canvas.drawRoad(roadMap, 1.54f, inputController.getMovement());
		canvas.draw(clouds,200 , 400);
		// Draw the game objects
		canvas.drawGnomez(gameplayController.getGnomez(), 1.54f);
		inputController.setWheel(gameplayController.getWheel());
		gameplayController.getWheel().drawWheel(canvas);

		// Output a simple debugging message stating the number of shells on the screen
        // TODO: commented this out to get game to run, car is null rn
		//String message = "Current movement: "+gameplayController.getCar().getMovement();
		//canvas.drawText(message, displayFont, COUNTER_OFFSET, canvas.getHeight()-COUNTER_OFFSET);

		if (gameState == GameState.OVER) {
			canvas.drawTextCentered("GAME OVER",displayFont, GAME_OVER_OFFSET);
			canvas.drawTextCentered("Press R to restart",displayFont, GAME_OVER_OFFSET-40);
		}

		// car health TODO: change to not be wheel
        canvas.drawText("HEALTH: " + Math.max(gameplayController.getWheel().getHealth(), 0),
                        displayFont, 10.0f, canvas.getHeight() - 10.0f);

		// Flush information to the graphic buffer.
		canvas.end();
	}
	
	/**
	 * Called when the Screen is resized. 
	 *
	 * This can happen at any point during a non-paused state but will never happen 
	 * before a call to show().
	 *
	 * @param width  The new width in pixels
	 * @param height The new height in pixels
	 */
	public void resize(int width, int height) {
		// IGNORE FOR NOW
	}

	/**
	 * Called when the Screen should render itself.
	 *
	 * We defer to the other methods update() and draw().  However, it is VERY important
	 * that we only quit AFTER a draw.
	 *
	 * @param delta Number of seconds since last animation frame
	 */
	public void render(float delta) {
		if (active) {
			update(delta);
			draw(delta);
			if (inputController.didExit() && listener != null) {
				listener.exitScreen(this, 0);
			}
		}
	}

	/**
	 * Called when the Screen is paused.
	 * 
	 * This is usually when it's not active or visible on screen. An Application is 
	 * also paused before it is destroyed.
	 */
	public void pause() {
		// TODO Auto-generated method stub
	}

	/**
	 * Called when the Screen is resumed from a paused state.
	 *
	 * This is usually when it regains focus.
	 */
	public void resume() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Called when this screen becomes the current screen for a Game.
	 */
	public void show() {
		// Useless if called in outside animation loop
		active = true;
	}

	/**
	 * Called when this screen is no longer the current screen for a Game.
	 */
	public void hide() {
		// Useless if called in outside animation loop
		active = false;
	}

	/**
	 * Sets the ScreenListener for this mode
	 *
	 * The ScreenListener will respond to requests to quit.
	 */
	public void setScreenListener(ScreenListener listener) {
		this.listener = listener;
	}

}