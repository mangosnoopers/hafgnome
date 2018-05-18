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
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;

import edu.cornell.gdiac.mangosnoops.Menus.SettingsMenu;
import edu.cornell.gdiac.mangosnoops.hudentity.Child;
import edu.cornell.gdiac.mangosnoops.roadentity.Enemy;
import edu.cornell.gdiac.mangosnoops.roadentity.Gnome;
import edu.cornell.gdiac.util.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.security.Key;

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
		/** While we are on the road */
		PLAY,
		/** When the ships is dead (but shells still work) */
		OVER,
		/** When the pause method is called **/
		PAUSED,
		/** When there's a cutscene */
		CUTSCENE
	}

	/** Cutscene files */

	// Suburb cutscene
	protected static final String CUTSCENE0_0 = "images/cutscenes/cutscene0_0.png";
	protected static final String CUTSCENE0_1 = "images/cutscenes/cutscene0_1.png";
	protected static final String CUTSCENE0_2 = "images/cutscenes/cutscene0_2.png";
	protected static final String CUTSCENE0_3 = "images/cutscenes/cutscene0_3.png";
	protected static final String CUTSCENE0_4 = "images/cutscenes/cutscene0_4.png";
	protected static final String CUTSCENE0_5 = "images/cutscenes/cutscene0_5.png";
	protected static final String CUTSCENE0_6 = "images/cutscenes/cutscene0_6.png";

	// Highway cutscene
    protected static final String CUTSCENE1_0 = "images/cutscenes/cutscene1_0.png";
	protected static final String CUTSCENE1_1 = "images/cutscenes/cutscene1_1.png";
	protected static final String CUTSCENE1_2 = "images/cutscenes/cutscene1_2.png";
	protected static final String CUTSCENE1_3 = "images/cutscenes/cutscene1_3.png";
	protected static final String CUTSCENE1_4 = "images/cutscenes/cutscene1_4.png";
	protected static final String CUTSCENE1_5 = "images/cutscenes/cutscene1_5.png";
	protected static final String CUTSCENE1_6 = "images/cutscenes/cutscene1_6.png";
	protected static final String CUTSCENE1_7 = "images/cutscenes/cutscene1_7.png";
	protected static final String CUTSCENE1_8 = "images/cutscenes/cutscene1_8.png";
	protected static final String CUTSCENE1_9 = "images/cutscenes/cutscene1_9.png";

	// Midwest cutscene
	protected static final String CUTSCENE2_0 = "images/cutscenes/cutscene2_0.png";
	protected static final String CUTSCENE2_1 = "images/cutscenes/cutscene2_1.png";
	protected static final String CUTSCENE2_2 = "images/cutscenes/cutscene2_2.png";
	protected static final String CUTSCENE2_3 = "images/cutscenes/cutscene2_3.png";

	// End cutscene
	protected static final String CUTSCENE3_0 = "images/cutscenes/cutscene3_0.png";
	protected static final String CUTSCENE3_1 = "images/cutscenes/cutscene3_1.png";
	protected static final String CUTSCENE3_2 = "images/cutscenes/cutscene3_2.png";
	protected static final String CUTSCENE3_3 = "images/cutscenes/cutscene3_3.png";
	protected static final String CUTSCENE3_4 = "images/cutscenes/cutscene3_4.png";
	protected static final String CUTSCENE3_5 = "images/cutscenes/cutscene3_5.png";

	// Mountains cutscene
	protected static final String CUTSCENE4_0 = "images/cutscenes/cutscene4_0.png";
	protected static final String CUTSCENE4_1 = "images/cutscenes/cutscene4_1.png";
	protected static final String CUTSCENE4_2 = "images/cutscenes/cutscene4_2.png";
	protected static final String CUTSCENE4_3 = "images/cutscenes/cutscene4_3.png";
	protected static final String CUTSCENE4_4 = "images/cutscenes/cutscene4_4.png";

	/** Cutscene textures */
	private Texture cutscene0_0Texture;
	private Texture cutscene0_1Texture;
	private Texture cutscene0_2Texture;
	private Texture cutscene0_3Texture;
	private Texture cutscene0_4Texture;
	private Texture cutscene0_5Texture;
	private Texture cutscene0_6Texture;

	private Texture cutscene1_0Texture;
	private Texture cutscene1_1Texture;
	private Texture cutscene1_2Texture;
	private Texture cutscene1_3Texture;
	private Texture cutscene1_4Texture;
	private Texture cutscene1_5Texture;
	private Texture cutscene1_6Texture;
	private Texture cutscene1_7Texture;
	private Texture cutscene1_8Texture;
	private Texture cutscene1_9Texture;

	private Texture cutscene2_0Texture;
	private Texture cutscene2_1Texture;
	private Texture cutscene2_2Texture;
	private Texture cutscene2_3Texture;

	private Texture cutscene3_0Texture;
	private Texture cutscene3_1Texture;
	private Texture cutscene3_2Texture;
	private Texture cutscene3_3Texture;
	private Texture cutscene3_4Texture;
	private Texture cutscene3_5Texture;

	private Texture cutscene4_0Texture;
	private Texture cutscene4_1Texture;
	private Texture cutscene4_2Texture;
	private Texture cutscene4_3Texture;
	private Texture cutscene4_4Texture;

	/** Background files */
	protected static final String SUBURB_BG = "images/suburb_background.png";
	protected static final String MOUNTAIN_BG = "images/mountains_background.png";
	protected static final String MIDWEST_BG = "images/midwest_background.png";
	protected static final String HIGHWAY_BG = "images/highway_background.png";

	/** Dimensions of the screen **/
	private static Vector2 SCREEN_DIMENSIONS;
	// GRAPHICS AND SOUND RESOURCES
	/** The font file to use for scores */
	private static String FONT_FILE = "fonts/Roadgeek 2005 Series E.ttf";
	/** Death Screen */
	private static final String DEATH_MODULE_FILE = "images/screen_death.png";
	/** Files for pause screen assets **/
	private static final String PAUSE_MENU_FILE = "images/PauseMenuAssets/pauseMenuBackground.png";
	private static final String PAUSE_RESUME_FILE = "images/PauseMenuAssets/pauseResume.png";
	private static final String PAUSE_RESTART_FILE = "images/PauseMenuAssets/pauseRestartLevel.png";
	private static final String PAUSE_OPTIONS_FILE = "images/PauseMenuAssets/pauseSettings.png";
	private static final String PAUSE_MAIN_MENU_FILE = "images/PauseMenuAssets/pauseMainMenu.png";
	private static final String PAUSE_EXIT_FILE = "images/PauseMenuAssets/pauseExit.png";
	private static final String PAUSE_EGG = "images/PauseMenuAssets/easterEgg.png";
	private static final String TIP_FLAMINGO_FILE = "images/restStopAssets/gameTips/flamingotip.png";
	private static final String TIP_VISOR_FILE = "images/restStopAssets/gameTips/visortip.png";
	private static final String TIP_GRILL_FILE = "images/restStopAssets/gameTips/grilltip.png";
	private static final String TIP_SAT_FILE = "images/restStopAssets/gameTips/sattip.png";
	// Loaded assets
	/** The background image for the game */
	private Texture background;
	/** The font for giving messages to the player */
	private BitmapFont displayFont;
	private static final int FONT_SIZE = 24;
	/** Track all loaded assets (for unloading purposes) */
	private Array<String> assets;

	/** Texture of the dash **/
	private Texture dash;
	/** Death Screen */
	private Texture deathModule;
	/** Pause Menu Textures **/
	private Texture pauseMenuTexture;
	private Texture pauseResumeButtonTexture;
	private Texture pauseRestartButtonTexture;
	private Texture pauseSettingsButtonTexture;
	private Texture pauseMainMenuButtonTexture;
	private Texture pauseExitButtonTexture;
	private Texture pauseEggTexture;
	/** Tips */
	private Texture tipFlamingoTex;
	private Texture tipVisorTex;
	private Texture tipGrillTex;
	private Texture tipSatTex;
	private Image tutorialModule;
	/** Counter for the game */
	private int counter;
	/** Tracker for global miles traversed in story mode of game TODO do something w this */
	private float globalMiles;
	/** Opacity of the overlay used to fade out into rest stop */
	private float fadeOutOpacity;
	/** Whether or not to exit to the rest stop */
	private boolean exitToRestStop;
	/** Whether or not to exit to the rest stop */
	public boolean exitToMainMenu = false;
	/** Fade delay */
	private int delay;

	/** Factor used to compute where we are in scrolling process */
	private static final float TIME_MODIFIER    = 0.06f;
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
	/** Handles all sound Output **/
	private SoundController soundController;
	/** Settings Menu **/
	private SettingsMenu settings;
	/** Variable to track the game state (SIMPLE FIELDS) */
	private GameState gameState;
	/** Variable to track total time played in milliseconds (SIMPLE FIELDS) */
	private float totalTime = 0;
	/** Whether or not this player mode is still active */
	private boolean active;
	/** Listener that will update the player mode when we are done */
	private ScreenListener listener;
	/** boolean denoting if screen if paused **/
	private int numTimesPaused;
	boolean restartedFromPause ;
	protected boolean exitFromPause;
	private Image pauseMenu;
	private Image pauseResumeButton;
	private Image pauseRestartButton;
	private Image pauseSettingsButton;
	private Image pauseMainMenuButton;
	private Image pauseExitButton;
	private Image pauseEgg;

	private int indexLevel; //used to put tutorial modules
	private boolean exitModule;
	/** Background textures */
	private Texture suburbBackgroundTexture;
	private Texture mountainBackgroundTexture;
	private Texture midwestBackgroundTexture;
	private Texture highwayBackgroundTexture;

	/**
	 * @return the current state of the game
	 */
	public GameState getGameState(){ return gameState; }

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

		// Load death module
		manager.load(DEATH_MODULE_FILE, Texture.class);
		assets.add(DEATH_MODULE_FILE);

		// Cutscenes
		manager.load(CUTSCENE0_0,Texture.class);
		assets.add(CUTSCENE0_0);
		manager.load(CUTSCENE0_1,Texture.class);
		assets.add(CUTSCENE0_1);
		manager.load(CUTSCENE0_2,Texture.class);
		assets.add(CUTSCENE0_2);
		manager.load(CUTSCENE0_3,Texture.class);
		assets.add(CUTSCENE0_3);
		manager.load(CUTSCENE0_4,Texture.class);
		assets.add(CUTSCENE0_4);
		manager.load(CUTSCENE0_5,Texture.class);
		assets.add(CUTSCENE0_5);
		manager.load(CUTSCENE0_6,Texture.class);
		assets.add(CUTSCENE0_6);

		manager.load(CUTSCENE1_0,Texture.class);
		assets.add(CUTSCENE1_0);
		manager.load(CUTSCENE1_1,Texture.class);
		assets.add(CUTSCENE1_1);
		manager.load(CUTSCENE1_2,Texture.class);
		assets.add(CUTSCENE1_2);
		manager.load(CUTSCENE1_3,Texture.class);
		assets.add(CUTSCENE1_3);
		manager.load(CUTSCENE1_4,Texture.class);
		assets.add(CUTSCENE1_4);
		manager.load(CUTSCENE1_5,Texture.class);
		assets.add(CUTSCENE1_5);
		manager.load(CUTSCENE1_6,Texture.class);
		assets.add(CUTSCENE1_6);
		manager.load(CUTSCENE1_7,Texture.class);
		assets.add(CUTSCENE1_7);
		manager.load(CUTSCENE1_8,Texture.class);
		assets.add(CUTSCENE1_8);
		manager.load(CUTSCENE1_9,Texture.class);
		assets.add(CUTSCENE1_9);

		manager.load(CUTSCENE2_0,Texture.class);
		assets.add(CUTSCENE2_0);
		manager.load(CUTSCENE2_1,Texture.class);
		assets.add(CUTSCENE2_1);
		manager.load(CUTSCENE2_2,Texture.class);
		assets.add(CUTSCENE2_2);
		manager.load(CUTSCENE2_3,Texture.class);
		assets.add(CUTSCENE2_3);

		manager.load(CUTSCENE3_0,Texture.class);
		assets.add(CUTSCENE3_0);
		manager.load(CUTSCENE3_1,Texture.class);
		assets.add(CUTSCENE3_1);
		manager.load(CUTSCENE3_2,Texture.class);
		assets.add(CUTSCENE3_2);
		manager.load(CUTSCENE3_3,Texture.class);
		assets.add(CUTSCENE3_3);
		manager.load(CUTSCENE3_4,Texture.class);
		assets.add(CUTSCENE3_4);
		manager.load(CUTSCENE3_5,Texture.class);
		assets.add(CUTSCENE3_5);

		manager.load(CUTSCENE4_0,Texture.class);
		assets.add(CUTSCENE4_0);
		manager.load(CUTSCENE4_1,Texture.class);
		assets.add(CUTSCENE4_1);
		manager.load(CUTSCENE4_2,Texture.class);
		assets.add(CUTSCENE4_2);
		manager.load(CUTSCENE4_3,Texture.class);
		assets.add(CUTSCENE4_3);
		manager.load(CUTSCENE4_4,Texture.class);
		assets.add(CUTSCENE4_4);

		// Backgrounds
		manager.load(SUBURB_BG,Texture.class);
		assets.add(SUBURB_BG);
		manager.load(MOUNTAIN_BG,Texture.class);
		assets.add(MOUNTAIN_BG);
		manager.load(HIGHWAY_BG,Texture.class);
		assets.add(HIGHWAY_BG);
		manager.load(MIDWEST_BG,Texture.class);
		assets.add(MIDWEST_BG);

		// Load pause menu assets
		manager.load(PAUSE_MENU_FILE, Texture.class);
		assets.add(PAUSE_MENU_FILE);
		manager.load(PAUSE_RESUME_FILE, Texture.class);
		assets.add(PAUSE_RESUME_FILE);
		manager.load(PAUSE_RESTART_FILE, Texture.class);
		assets.add(PAUSE_RESTART_FILE);
		manager.load(PAUSE_OPTIONS_FILE, Texture.class);
		assets.add(PAUSE_OPTIONS_FILE);
		manager.load(PAUSE_MAIN_MENU_FILE, Texture.class);
		assets.add(PAUSE_MAIN_MENU_FILE);
		manager.load(PAUSE_EXIT_FILE, Texture.class);
		assets.add(PAUSE_EXIT_FILE);
		manager.load(PAUSE_EXIT_FILE, Texture.class);
		assets.add(PAUSE_EXIT_FILE);
		manager.load(PAUSE_EGG, Texture.class);
		assets.add(PAUSE_EGG);

		manager.load(TIP_FLAMINGO_FILE, Texture.class);
		assets.add(TIP_FLAMINGO_FILE);
		manager.load(TIP_VISOR_FILE, Texture.class);
		assets.add(TIP_VISOR_FILE);
		manager.load(TIP_GRILL_FILE, Texture.class);
		assets.add(TIP_GRILL_FILE);
		manager.load(TIP_SAT_FILE, Texture.class);
		assets.add(TIP_SAT_FILE);
		//
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

		// Allocate assets
		if (manager.isLoaded(CUTSCENE4_0)) {
			cutscene4_0Texture = manager.get(CUTSCENE4_0, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE4_1)) {
			cutscene4_1Texture = manager.get(CUTSCENE4_1, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE4_2)) {
			cutscene4_2Texture = manager.get(CUTSCENE4_2, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE4_3)) {
			cutscene4_3Texture = manager.get(CUTSCENE4_3, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE4_4)) {
			cutscene4_4Texture = manager.get(CUTSCENE4_4, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE3_0)) {
			cutscene3_0Texture = manager.get(CUTSCENE3_0, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE3_1)) {
			cutscene3_1Texture = manager.get(CUTSCENE3_1, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE3_2)) {
			cutscene3_2Texture = manager.get(CUTSCENE3_2, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE3_3)) {
			cutscene3_3Texture = manager.get(CUTSCENE3_3, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE3_4)) {
			cutscene3_4Texture = manager.get(CUTSCENE3_4, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE3_5)) {
			cutscene3_5Texture = manager.get(CUTSCENE3_5, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE1_0)) {
			cutscene1_0Texture = manager.get(CUTSCENE1_0, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE1_1)) {
			cutscene1_1Texture = manager.get(CUTSCENE1_1, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE1_2)) {
			cutscene1_2Texture = manager.get(CUTSCENE1_2, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE1_3)) {
			cutscene1_3Texture = manager.get(CUTSCENE1_3, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE1_4)) {
			cutscene1_4Texture = manager.get(CUTSCENE1_4, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE1_5)) {
			cutscene1_5Texture = manager.get(CUTSCENE1_5, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE1_6)) {
			cutscene1_6Texture = manager.get(CUTSCENE1_6, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE1_7)) {
			cutscene1_7Texture = manager.get(CUTSCENE1_7, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE1_8)) {
			cutscene1_8Texture = manager.get(CUTSCENE1_8, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE1_9)) {
			cutscene1_9Texture = manager.get(CUTSCENE1_9, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE0_0)) {
			cutscene0_0Texture = manager.get(CUTSCENE0_0, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE0_1)) {
			cutscene0_1Texture = manager.get(CUTSCENE0_1, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE0_2)) {
			cutscene0_2Texture = manager.get(CUTSCENE0_2, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE0_3)) {
			cutscene0_3Texture = manager.get(CUTSCENE0_3, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE0_4)) {
			cutscene0_4Texture = manager.get(CUTSCENE0_4, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE0_5)) {
			cutscene0_5Texture = manager.get(CUTSCENE0_5, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE0_6)) {
			cutscene0_6Texture = manager.get(CUTSCENE0_6, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE2_0)) {
			cutscene2_0Texture = manager.get(CUTSCENE2_0, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE2_1)) {
			cutscene2_1Texture = manager.get(CUTSCENE2_1, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE2_2)) {
			cutscene2_2Texture = manager.get(CUTSCENE2_2, Texture.class);
		}
		if (manager.isLoaded(CUTSCENE2_3)) {
			cutscene2_3Texture = manager.get(CUTSCENE2_3, Texture.class);
		}
        if (manager.isLoaded(SUBURB_BG)) {
			suburbBackgroundTexture = manager.get(SUBURB_BG, Texture.class);
		}
		if (manager.isLoaded(MIDWEST_BG)) {
			midwestBackgroundTexture = manager.get(MIDWEST_BG, Texture.class);
		}
		if (manager.isLoaded(MOUNTAIN_BG)) {
			mountainBackgroundTexture = manager.get(MOUNTAIN_BG, Texture.class);
		}
		if (manager.isLoaded(HIGHWAY_BG)) {
			highwayBackgroundTexture = manager.get(HIGHWAY_BG, Texture.class);
		}
		if (manager.isLoaded(DEATH_MODULE_FILE)) {
			deathModule = manager.get(DEATH_MODULE_FILE, Texture.class);
		}
		if (manager.isLoaded(PAUSE_MENU_FILE)) {
			pauseMenuTexture = manager.get(PAUSE_MENU_FILE, Texture.class);
		}
		if (manager.isLoaded(PAUSE_RESUME_FILE)) {
			pauseResumeButtonTexture = manager.get(PAUSE_RESUME_FILE, Texture.class);
		}
		if (manager.isLoaded(PAUSE_RESTART_FILE)) {
			pauseRestartButtonTexture = manager.get(PAUSE_RESTART_FILE, Texture.class);
		}
		if (manager.isLoaded(PAUSE_OPTIONS_FILE)) {
			pauseSettingsButtonTexture = manager.get(PAUSE_OPTIONS_FILE, Texture.class);
		}
		if (manager.isLoaded(PAUSE_MAIN_MENU_FILE)) {
			pauseMainMenuButtonTexture = manager.get(PAUSE_MAIN_MENU_FILE, Texture.class);
		}
		if (manager.isLoaded(PAUSE_EXIT_FILE)) {
			pauseExitButtonTexture = manager.get(PAUSE_EXIT_FILE, Texture.class);
		}
		if (manager.isLoaded(PAUSE_EGG)) {
			pauseEggTexture = manager.get(PAUSE_EGG, Texture.class);
		}
		if (manager.isLoaded(TIP_FLAMINGO_FILE)) {
			tipFlamingoTex = manager.get(TIP_FLAMINGO_FILE, Texture.class);
		}
		if (manager.isLoaded(TIP_VISOR_FILE)) {
			tipVisorTex = manager.get(TIP_VISOR_FILE, Texture.class);
		}
		if (manager.isLoaded(TIP_GRILL_FILE)) {
			tipGrillTex = manager.get(TIP_GRILL_FILE, Texture.class);
		}
		if (manager.isLoaded(TIP_SAT_FILE)) {
			tipSatTex = manager.get(TIP_SAT_FILE, Texture.class);
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

	/** Get the player's inventory */
	public Inventory getInventory() { return gameplayController.getInventory(); }

	/** Set the player's inventory */
	public void setInventory(Inventory i) { gameplayController.setInventory(i); }

	private int currLevel;

	public void setCL(int newCL) {
		currLevel = newCL;
	}

	/**
	 * Creates a new game with the given drawing context.
	 *
	 * This constructor initializes the models and controllers for the game.  The
	 * view has already been initialized by the root class.
	 */
	public GameMode(int cL, GameCanvas canvas,SettingsMenu settings,SoundController soundController, String levelName) {
	    // TODO DO SOMETHING ELSE W THE EXCEPTIONS
	    try {
			SCREEN_DIMENSIONS = new Vector2(canvas.getWidth(),canvas.getHeight());
            this.canvas = canvas;
            active = false;
            fadeOutOpacity = 0.0f;
            delay = 0;
            System.out.println(cL);
            // Null out all pointers, 0 out all ints, etc.
			if (cL == 0 || cL == 8 || cL == 5 || cL == 10) {
			    gameState = GameState.CUTSCENE;
			} else {
				gameState = GameState.INTRO;
			}
            assets = new Array<String>();

			currLevel = cL;

            // Create the controllers.
			this.settings = settings;
			this.soundController = soundController;
            inputController = new InputController(settings);
            if (levelName.substring(0,3).equals("tut")) {
				gameplayController = new TutorialController(levelName, canvas, new LevelObject(levelName), Integer.parseInt(levelName.substring(3,4)), soundController);
			} else {
            	gameplayController = new NormalLevelController(levelName, canvas, new LevelObject(levelName), soundController);
            	indexLevel = Character.getNumericValue(levelName.charAt(levelName.indexOf("level") + 5));
			}
            collisionController = new CollisionController(canvas.getWidth(), canvas.getHeight(), soundController);
        } catch (IOException e) {
	        System.out.println(e.getMessage());
        } catch (InvalidFormatException e) {
			System.out.println(e.getMessage());
		}
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



	private float cutsceneDeltaSum = 0;
	private float CUTSCENE_0_SCENE_0_TIME = 5f;
	private float CUTSCENE_0_SCENE_1_TIME = 5f;
	private float CUTSCENE_0_SCENE_2_TIME = 5f;
	private float CUTSCENE_0_SCENE_3_TIME = 5f;
	private float CUTSCENE_0_SCENE_4_TIME = 5f;
	private float CUTSCENE_0_SCENE_5_TIME = 5f;
	private float CUTSCENE_0_SCENE_6_TIME = 5f;

	private float CUTSCENE_1_SCENE_0_TIME = 5f;
	private float CUTSCENE_1_SCENE_1_TIME = 5f;
	private float CUTSCENE_1_SCENE_2_TIME = 5f;
	private float CUTSCENE_1_SCENE_3_TIME = 5f;
	private float CUTSCENE_1_SCENE_4_TIME = 5f;
	private float CUTSCENE_1_SCENE_5_TIME = 5f;
	private float CUTSCENE_1_SCENE_6_TIME = 5f;

	private float CUTSCENE_2_SCENE_0_TIME = 5f;
	private float CUTSCENE_2_SCENE_1_TIME = 5f;
	private float CUTSCENE_2_SCENE_2_TIME = 5f;
	private float CUTSCENE_2_SCENE_3_TIME = 5f;

	private float CUTSCENE_3_SCENE_0_TIME = 5f;
	private float CUTSCENE_3_SCENE_1_TIME = 5f;
	private float CUTSCENE_3_SCENE_2_TIME = 5f;
	private float CUTSCENE_3_SCENE_3_TIME = 5f;
	private float CUTSCENE_3_SCENE_4_TIME = 5f;
	private float CUTSCENE_3_SCENE_5_TIME = 5f;

	private int cutSceneIndex = 0;

	public void displayCutScene4(float delta) {

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			gameState = GameState.INTRO;
		}
		cutsceneDeltaSum += delta;

		switch (cutSceneIndex) {
			case 0:
				if (cutsceneDeltaSum >= CUTSCENE_2_SCENE_0_TIME) {
					cutSceneIndex = 1;
					cutsceneDeltaSum = 0;
				}
				break;
            case 1:
                if (cutsceneDeltaSum >= CUTSCENE_2_SCENE_1_TIME) {
                	cutSceneIndex = 2;
                	cutsceneDeltaSum = 0;
				}
				break;
			case 2:
				if (cutsceneDeltaSum >= CUTSCENE_2_SCENE_2_TIME) {
					cutSceneIndex = 3;
					cutsceneDeltaSum = 0;
				}
				break;
			case 3:
				if (cutsceneDeltaSum >= CUTSCENE_2_SCENE_3_TIME) {
					cutSceneIndex = 4;
					cutsceneDeltaSum = 0;
				}
				break;
			case 4:
				if (cutsceneDeltaSum >= CUTSCENE_2_SCENE_3_TIME) {
					cutSceneIndex = 5;
					cutsceneDeltaSum = 0;
				}
				break;
			case 5:
				gameState = GameState.INTRO;
                break;
		}
	}

	public void displayCutScene2(float delta) {

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			gameState = GameState.INTRO;
		}

		cutsceneDeltaSum += delta;

		switch (cutSceneIndex) {
			case 0:
				if (cutsceneDeltaSum >= CUTSCENE_2_SCENE_0_TIME) {
					cutSceneIndex = 1;
					cutsceneDeltaSum = 0;
				}
				break;
            case 1:
                if (cutsceneDeltaSum >= CUTSCENE_2_SCENE_1_TIME) {
                	cutSceneIndex = 2;
                	cutsceneDeltaSum = 0;
				}
				break;
			case 2:
				if (cutsceneDeltaSum >= CUTSCENE_2_SCENE_2_TIME) {
					cutSceneIndex = 3;
					cutsceneDeltaSum = 0;
				}
				break;
			case 3:
				if (cutsceneDeltaSum >= CUTSCENE_2_SCENE_3_TIME) {
					cutSceneIndex = 4;
					cutsceneDeltaSum = 0;
				}
				break;
			case 4:
				gameState = GameState.INTRO;
                break;
		}



    }

    public void displayCutScene1(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			gameState = GameState.INTRO;
		}

		cutsceneDeltaSum += delta;

		switch (cutSceneIndex) {
			case 0:
				if (cutsceneDeltaSum >= CUTSCENE_1_SCENE_0_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 1;
					cutsceneDeltaSum = 0;
				}
				break;
            case 1:
                if (cutsceneDeltaSum >= CUTSCENE_1_SCENE_1_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                	cutSceneIndex = 2;
                	cutsceneDeltaSum = 0;
				}
				break;
			case 2:
				if (cutsceneDeltaSum >= CUTSCENE_1_SCENE_2_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 3;
					cutsceneDeltaSum = 0;
				}
				break;
			case 3:
				if (cutsceneDeltaSum >= CUTSCENE_1_SCENE_3_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 4;
					cutsceneDeltaSum = 0;
				}
				break;
			case 4:
				if (cutsceneDeltaSum >= CUTSCENE_1_SCENE_4_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 5;
					cutsceneDeltaSum = 0;
				}
				break;
			case 5:
				if (cutsceneDeltaSum >= CUTSCENE_1_SCENE_5_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 6;
					cutsceneDeltaSum = 0;
				}
				break;
			case 6:
				if (cutsceneDeltaSum >= CUTSCENE_1_SCENE_6_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 7;
					cutsceneDeltaSum = 0;
				}
				break;
			case 7:
				if (cutsceneDeltaSum >= CUTSCENE_1_SCENE_6_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 8;
					cutsceneDeltaSum = 0;
				}
				break;
			case 8:
				if (cutsceneDeltaSum >= CUTSCENE_1_SCENE_6_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 9;
					cutsceneDeltaSum = 0;
				}
				break;
			case 9:
				if (cutsceneDeltaSum >= CUTSCENE_1_SCENE_6_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 10;
					cutsceneDeltaSum = 0;
				}
				break;
			case 10:
				gameState = GameState.INTRO;
                break;
		}

	}

	public void playCutScene(int cutSceneNum) {
		currLevel = cutSceneNum;
		gameState = GameState.CUTSCENE;
	}

	public void displayCutScene3(float delta) {

		cutsceneDeltaSum += delta;

		switch (cutSceneIndex) {
			case 0:
				if (cutsceneDeltaSum >= CUTSCENE_3_SCENE_0_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 1;
					cutsceneDeltaSum = 0;
				}
				break;
            case 1:
                if (cutsceneDeltaSum >= CUTSCENE_3_SCENE_1_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                	cutSceneIndex = 2;
                	cutsceneDeltaSum = 0;
				}
				break;
			case 2:
				if (cutsceneDeltaSum >= CUTSCENE_3_SCENE_2_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 3;
					cutsceneDeltaSum = 0;
				}
				break;
			case 3:
				if (cutsceneDeltaSum >= CUTSCENE_3_SCENE_3_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 4;
					cutsceneDeltaSum = 0;
				}
				break;
			case 4:
				if (cutsceneDeltaSum >= CUTSCENE_3_SCENE_4_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 5;
					cutsceneDeltaSum = 0;
				}
				break;
			case 5:
				if (cutsceneDeltaSum >= CUTSCENE_3_SCENE_5_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 6;
					cutsceneDeltaSum = 0;
				}
				break;
			case 6:
			    exitToMainMenu = true;
                break;
		}

	}

	public void displayCutScene0(float delta) {

		cutsceneDeltaSum += delta;

		switch (cutSceneIndex) {
			case 0:
				if (cutsceneDeltaSum >= CUTSCENE_0_SCENE_0_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 1;
					cutsceneDeltaSum = 0;
				}
				break;
            case 1:
                if (cutsceneDeltaSum >= CUTSCENE_0_SCENE_1_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                	cutSceneIndex = 2;
                	cutsceneDeltaSum = 0;
				}
				break;
			case 2:
				if (cutsceneDeltaSum >= CUTSCENE_0_SCENE_2_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 3;
					cutsceneDeltaSum = 0;
				}
				break;
			case 3:
				if (cutsceneDeltaSum >= CUTSCENE_0_SCENE_3_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 4;
					cutsceneDeltaSum = 0;
				}
				break;
			case 4:
				if (cutsceneDeltaSum >= CUTSCENE_0_SCENE_4_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 5;
					cutsceneDeltaSum = 0;
				}
				break;
			case 5:
				if (cutsceneDeltaSum >= CUTSCENE_0_SCENE_5_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 6;
					cutsceneDeltaSum = 0;
				}
				break;
			case 6:
				if (cutsceneDeltaSum >= CUTSCENE_0_SCENE_6_TIME
						|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					cutSceneIndex = 7;
					cutsceneDeltaSum = 0;
				}
				break;
			case 7:
				gameState = GameState.INTRO;
                break;
		}




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
		//Ensure all images are properly drawn, scaled, and updated
		Image.updateScreenDimensions(canvas);
		// Process the game input
		inputController.readInput();
		// Test whether to reset the game.
//		try {
			switch (gameState) {
				case INTRO:
					gameState = GameState.PLAY;
					gameplayController.start(canvas.getWidth() / 2.0f, 0);
					break;
				case OVER:
					if (inputController.didReset() || restartedFromPause) {
						gameplayController.reset();
						soundController.reset();
						canvas.resetCam();
						gameState = GameState.PLAY;
						gameplayController.start(canvas.getWidth() / 2.0f, 0);
						numTimesPaused = 0;
					}
					if (exitFromPause) {
						gameplayController.reset();
						soundController.reset();
						canvas.resetCam();
						gameState = GameState.INTRO;
						numTimesPaused = 0;
					}
					break;
				case PLAY:
					if (!exitModule & (indexLevel == 1 || indexLevel == 2 || indexLevel == 3 || indexLevel == 4)) {
						play(delta);
						gameState = GameState.PAUSED;
						exitModule = false;
					}
					else{ play(delta); }
					break;
					case PAUSED:
							if (!exitModule && (indexLevel == 1 || indexLevel == 2 || indexLevel == 3 || indexLevel == 4)) {
								exitModule = inputController.isMousePressed();
								if (exitModule) gameState = GameState.PLAY;
							} else {
								pause_game();
							}
							break;
						case CUTSCENE:
							if (currLevel == 0) {
								displayCutScene0(delta);
							} else if (currLevel == 5) {
								displayCutScene2(delta);
							} else if (currLevel == 8) {
								displayCutScene1(delta);
							} else if (currLevel == 12) {
								displayCutScene3(delta);
							} else if (currLevel == 10) {
								displayCutScene4(delta);
							}
							break;
						default:
							break;
					}

//		} catch (Exception e) {
//			System.out.println("YOU SCREWED UP UPDATE YOU FOOL");
//			System.out.println(e);
//		}

	}

	/**
	 * This method processes a single step in the game loop.
	 *
	 * @param delta Number of seconds since last animation frame
	 */
	protected void play(float delta) {

		// Check if game is over
		if (gameplayController.getCar().isDestroyed()) {
			gameState = GameState.OVER;
		}

		// Check if game has been paused
		if(inputController.pressedPause()){
			gameState = GameState.PAUSED;
			numTimesPaused += 1;
		}
		// Update Based on input
		gameplayController.handleEvents(delta, gameplayController.getCar().getNed(), gameplayController.getCar().getNosh());
		gameplayController.resolveActions(inputController, delta);

		// Update child states TODO: idk
		gameplayController.resolveChildren(counter, gameplayController.getCar().getNed(),
				gameplayController.getCar().getNosh(), gameplayController.getRadio());

		// Check for collisions
		totalTime += (delta*1000); // Seconds to milliseconds
		float offset =  canvas.getWidth() - (totalTime * TIME_MODIFIER) % canvas.getWidth();
		collisionController.processCollisions(gameplayController.getEnemiez(),gameplayController.getCar(), gameplayController);

		// Play resulting sound
		soundController.play(gameplayController.getTouchscreen());

		// Clean up destroyed objects
		gameplayController.garbageCollect();

		// Set Camera
		canvas.setCameraXY(gameplayController.getCar().getPosition());

		// Update the counter
		if(counter == Integer.MAX_VALUE){
			counter = 0;
		}
		counter += 1;
	}

	/**
	 * This method is called on every loop that the game is paused
	 */
	private boolean wasHoveringOnButton;
	private boolean isHoveringOnButton;
	protected void pause_game() {
		isHoveringOnButton = false;
		// Check if game has been unpaused
		if (inputController.pressedPause()) {
			gameState = GameState.PLAY;
		}
		if(!settings.isShowing()) {
			// RESUME BUTTON
			if (pauseResumeButton != null && pauseResumeButton.inAreaWOriginScale(inputController.getHoverPos())) {
				pauseResumeButton.relativeScale = pauseResumeButton.ORIGINAL_SCALE * 1.08f;
				isHoveringOnButton = true;
				if (inputController.isMousePressed()) {
					soundController.playClick();
					pauseResumeButton.relativeScale = pauseResumeButton.ORIGINAL_SCALE;
					gameState = GameState.PLAY;
				}
			} else {
				pauseResumeButton.relativeScale = pauseResumeButton.ORIGINAL_SCALE;
				isHoveringOnButton = isHoveringOnButton || false;
			}
			// RESTART BUTTON
			if (pauseRestartButton != null && pauseRestartButton.inAreaWOriginScale(inputController.getHoverPos())) {
				pauseRestartButton.relativeScale = pauseRestartButton.ORIGINAL_SCALE * 1.08f;
				isHoveringOnButton = true;
				if (inputController.isMousePressed()) {
					soundController.playClick();
					pauseRestartButton.relativeScale = pauseRestartButton.ORIGINAL_SCALE;
					restartedFromPause = true;
					gameState = GameState.OVER;
				}
			} else {
				pauseRestartButton.relativeScale = pauseRestartButton.ORIGINAL_SCALE;
				isHoveringOnButton = isHoveringOnButton || false;
			}
			// SETTINGS BUTTON
			if (pauseSettingsButton != null && pauseSettingsButton.inAreaWOriginScale(inputController.getHoverPos())) {
				pauseSettingsButton.relativeScale = pauseSettingsButton.ORIGINAL_SCALE * 1.1f;
				isHoveringOnButton = true;
				if (inputController.isMousePressed()) {
					soundController.playClick();
					pauseSettingsButton.relativeScale = pauseSettingsButton.ORIGINAL_SCALE;
					settings.setShowing(true);
				}
			} else {
				pauseSettingsButton.relativeScale = pauseSettingsButton.ORIGINAL_SCALE;
				isHoveringOnButton = isHoveringOnButton || false;
			}
			// MAIN MENU BUTTON
			if (pauseMainMenuButton != null && pauseMainMenuButton.inAreaWOriginScale(inputController.getHoverPos())) {
				pauseMainMenuButton.relativeScale = pauseMainMenuButton.ORIGINAL_SCALE * 1.1f;
				isHoveringOnButton = true;
				if (inputController.isMousePressed()) {
					soundController.playClick();
					pauseMainMenuButton.relativeScale = pauseMainMenuButton.ORIGINAL_SCALE;
					gameState = GameState.OVER;
					exitFromPause = true;
				}
			} else {
				pauseMainMenuButton.relativeScale = pauseMainMenuButton.ORIGINAL_SCALE;
				isHoveringOnButton = isHoveringOnButton || false;
			}
			// EXIT GAME BUTTON
			if (pauseExitButton != null && pauseExitButton.inAreaWOriginScale(inputController.getHoverPos())) {
				pauseExitButton.relativeScale = pauseExitButton.ORIGINAL_SCALE * 1.1f;
				isHoveringOnButton = true;
				if (inputController.isMousePressed()) {
					soundController.playClick();
					pauseExitButton.relativeScale = pauseExitButton.ORIGINAL_SCALE;
					Gdx.app.exit();
				}
			} else {
				pauseExitButton.relativeScale = pauseExitButton.ORIGINAL_SCALE;
				isHoveringOnButton = isHoveringOnButton || false;
			}
			if((isHoveringOnButton != wasHoveringOnButton) && isHoveringOnButton){
				soundController.playHoverMouse();
			}
		} else{
			settings.update(inputController.getHoverPos(),soundController );
		}
		soundController.play(null);
		wasHoveringOnButton = isHoveringOnButton;
	}


/**
 * Draw the status of this player mode.
 *
 * We prefer to separate update and draw from one another as separate methods, instead
 * of using the single render() method that LibGDX does.  We will talk about why we
 * prefer this in lecture.
 */
	private void draw(float delta) {
		canvas.clearScreen();

		canvas.beginHUDDrawing();
		switch (gameplayController.getRegion()) {
			case SUBURBS:
				canvas.drawBackground(suburbBackgroundTexture, gameplayController.getRoad().getSpeedRatio());
				break;
			case HIGHWAY:
				canvas.drawBackground(highwayBackgroundTexture, gameplayController.getRoad().getSpeedRatio());
				break;
			case MIDWEST:
				canvas.drawBackground(midwestBackgroundTexture, gameplayController.getRoad().getSpeedRatio());
				break;
			case COLORADO:
				canvas.drawBackground(mountainBackgroundTexture, gameplayController.getRoad().getSpeedRatio());
				break;
            default:
				canvas.drawBackground(suburbBackgroundTexture, gameplayController.getRoad().getSpeedRatio());
				break;

		}

		canvas.endHUDDrawing();
		gameplayController.getRoad().draw(canvas);

		//Gnomez
		for (Enemy e : gameplayController.getEnemiez()) {
			e.draw(canvas);
		}
//		for (RoadImage i : gameplayController.getRoadsideObjs()) {
//			i.draw(canvas);
//		}
//		System.out.println(gameplayController.getRoadsideObjs() == null);

		canvas.drawWorld();

		// ** Draw HUD stuff **
		canvas.beginHUDDrawing();
		gameplayController.draw(canvas);

		// Draw fade out to rest stop
		if (gameplayController.getRoad().reachedEndOfLevel() && !exitToRestStop) {
			// TODO FINISH THE FADE make this not sus
			canvas.drawFade(fadeOutOpacity);
			if (!(Math.abs(fadeOutOpacity - 1.0f) <= 0.001f)) {
				fadeOutOpacity += 0.05f;
			}
			else {
				if (delay < 50) {
					delay++;
				}
				else {
					// ready to exit gamemode when the fade out is complete
					if (currLevel != 12) {
						exitToRestStop = true;
					} else {
						gameState = GameState.CUTSCENE;
					}
				}
			}
		}

		// Draw messages
		switch (gameState) {
			case CUTSCENE:
			case INTRO:
				break;
			case OVER:
				if(exitFromPause){
					break;
				}
				if (!gameplayController.getCar().isDestroyed()) {
				} else {
					canvas.draw(deathModule, Color.WHITE, deathModule.getWidth()*0.5f, deathModule.getHeight()*0.5f,
							canvas.getWidth()*0.5f, canvas.getHeight()*0.5f, 0,
							((float)1.0f*canvas.getHeight())/deathModule.getHeight(), ((float)1.0f*canvas.getHeight())/deathModule.getHeight());
				}
				break;
			case PLAY:
				break;
			case PAUSED:
				if(!exitModule && (indexLevel == 1 || indexLevel == 2 || indexLevel == 3 || indexLevel == 4)) {
					tutorialModule = new Image(0.5f, 0.5f, 0.7f, tipFlamingoTex, GameCanvas.TextureOrigin.MIDDLE);
					switch(indexLevel) {
						case 1:
							tutorialModule.setTexture(tipFlamingoTex);
							tutorialModule.drawNoShake(canvas);
							break;
						case 2:
							tutorialModule.setTexture(tipVisorTex);
							tutorialModule.drawNoShake(canvas);
							break;
						case 3:
							tutorialModule.setTexture(tipSatTex);
							tutorialModule.drawNoShake(canvas);
							break;
						case 4:
							tutorialModule.setTexture(tipGrillTex);
							tutorialModule.drawNoShake(canvas);
							break;
						default:
							break;
					}
				} else {
					// Weird place to do this, maybe find a way to make better
					if(pauseMenu == null){
						pauseMenu = new Image(0.5f,0.5f, 0.79f, pauseMenuTexture, GameCanvas.TextureOrigin.MIDDLE);
						pauseResumeButton = new Image(0.5f,0.77f,0.09f, pauseResumeButtonTexture, GameCanvas.TextureOrigin.MIDDLE);
						pauseRestartButton = new Image(0.5f,0.63f,0.09f, pauseRestartButtonTexture, GameCanvas.TextureOrigin.MIDDLE);
						pauseSettingsButton = new Image(0.5f,0.49f,0.11f, pauseSettingsButtonTexture, GameCanvas.TextureOrigin.MIDDLE);
						pauseMainMenuButton = new Image(0.5f,0.37f,0.08f, pauseMainMenuButtonTexture, GameCanvas.TextureOrigin.MIDDLE);
						pauseExitButton = new Image(0.5f,0.24f,0.09f, pauseExitButtonTexture, GameCanvas.TextureOrigin.MIDDLE);
						pauseEgg = new Image(0.7f,0.5f,0.5f, pauseEggTexture, GameCanvas.TextureOrigin.MIDDLE);
					}
					pauseMenu.drawNoShake(canvas);
					if(numTimesPaused == 8 || numTimesPaused == 14 || numTimesPaused == 95 ){
						pauseEgg.draw(canvas);
					}
					pauseResumeButton.drawNoShake(canvas);
					pauseRestartButton.drawNoShake(canvas);
					pauseSettingsButton.drawNoShake(canvas);
					pauseMainMenuButton.drawNoShake(canvas);
					pauseExitButton.drawNoShake(canvas);
					if(settings.isShowing()){
						settings.draw(canvas);
					}
				}
				break;
			default:
				break;
		}

		// Flush information to the graphic buffer.
		canvas.endHUDDrawing();
	}

	/**
	 * Called when the Screen is resized.
	 *
	 * This can happen at any point during a non-paused state but will never happen
	 * before a call to show().
	 *
	 * width/newsize = oldwidth/oldsize
	 * newsize*oldwidth = width*oldsize
	 * newsize/oldsize = width/oldwidth
	 *
	 * @param width  The new width in pixels
	 * @param height The new height in pixels
	 */
	public void resize(int width, int height) {
		int newSize = 0;
		FreetypeFontLoader.FreeTypeFontLoaderParameter size2Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		size2Params.fontFileName = FONT_FILE;
		size2Params.fontParameters.size = newSize;
		displayFont = new BitmapFont();
//		displayFont.getData().setScale(width / (SCREEN_DIMENSIONS.x/displayFont.getScaleX()),
//				height / (SCREEN_DIMENSIONS.y/displayFont.getScaleY()));

//		displayFont.getData().setScale((width/SCREEN_DIMENSIONS.x),
//			(height/SCREEN_DIMENSIONS.y));
		SCREEN_DIMENSIONS = new Vector2(width,height);

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
			switch (gameState) {
				case CUTSCENE:
					canvas.beginHUDDrawing();
					if (currLevel == 0) {
                        switch (cutSceneIndex) {
                            case 0:
                                canvas.draw(cutscene0_0Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 1:
                                canvas.draw(cutscene0_1Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 2:
                                canvas.draw(cutscene0_2Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 3:
                                canvas.draw(cutscene0_3Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 4:
                                canvas.draw(cutscene0_4Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 5:
                                canvas.draw(cutscene0_5Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 6:
                                canvas.draw(cutscene0_6Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                        }
					} else if (currLevel == 5) {
                        switch (cutSceneIndex) {
							case 0:
								canvas.draw(cutscene2_0Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
							case 1:
								canvas.draw(cutscene2_1Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
							case 2:
								canvas.draw(cutscene2_2Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
							case 3:
								canvas.draw(cutscene2_3Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
						}
					} else if (currLevel == 8) {
						switch (cutSceneIndex) {
                            case 0:
                                canvas.draw(cutscene1_0Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 1:
                                canvas.draw(cutscene1_1Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 2:
                                canvas.draw(cutscene1_2Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 3:
                                canvas.draw(cutscene1_3Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 4:
                                canvas.draw(cutscene1_4Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 5:
                                canvas.draw(cutscene1_5Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 6:
                                canvas.draw(cutscene1_6Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
							case 7:
								canvas.draw(cutscene1_7Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
							case 8:
								canvas.draw(cutscene1_8Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
							case 9:
								canvas.draw(cutscene1_9Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
						}
					} else if (currLevel == 12) {
						switch (cutSceneIndex) {
                            case 0:
                                canvas.draw(cutscene3_0Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 1:
                                canvas.draw(cutscene3_1Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 2:
                                canvas.draw(cutscene3_2Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 3:
                                canvas.draw(cutscene3_3Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 4:
                                canvas.draw(cutscene3_4Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
                            case 5:
                                canvas.draw(cutscene3_5Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
                                break;
						}

					} else if (currLevel == 10) {
						switch (cutSceneIndex) {
							case 0:
								canvas.draw(cutscene4_0Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
							case 1:
								canvas.draw(cutscene4_1Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
							case 2:
								canvas.draw(cutscene4_2Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
							case 3:
								canvas.draw(cutscene4_3Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
							case 4:
								canvas.draw(cutscene4_4Texture, 0, 0, canvas.getWidth(), canvas.getHeight());
								break;
						}
					}

					canvas.endHUDDrawing();
					break;
				case INTRO:
					break;
                default:
					draw(delta);
					break;
			}
			// Check if end of level and ready to exit - if so transition to rest stop mode
			if ((exitToRestStop||exitFromPause || exitToMainMenu) && listener != null ) {
				if(exitFromPause){
					update(delta);
				}
				soundController.reset();
				listener.exitScreen(this, 0);
				active = false;
			}
		}

	}


	public boolean beatGame() {
		return exitToMainMenu;
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