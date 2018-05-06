/*
 * GDXRoot.java
 *
 * This is the primary class file for running the game.  It is the "static main" of
 * LibGDX.  In the first lab, we extended ApplicationAdapter.  In previous lab
 * we extended Game.  This is because of a weird graphical artifact that we do not
 * understand.  Transparencies (in 3D only) is failing when we use ApplicationAdapter. 
 * There must be some undocumented OpenGL code in setScreen.
 *
 * This time we shown how to use Game with player modes.  The player modes are 
 * implemented by screens.  Player modes handle their own rendering (instead of the
 * root class calling render for them).  When a player mode is ready to quit, it
 * notifies the root class through the ScreenListener interface.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package edu.cornell.gdiac.mangosnoops;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.mangosnoops.Menus.LevelMenuMode;
import edu.cornell.gdiac.mangosnoops.Menus.SettingsMenu;
import edu.cornell.gdiac.mangosnoops.Menus.StartMenuMode;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.assets.loaders.resolvers.*;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Root class for a LibGDX.  
 * 
 * This class is technically not the ROOT CLASS. Each platform has another class above
 * this (e.g. PC games use DesktopLauncher) which serves as the true root.  However, 
 * those classes are unique to each platform, while this class is the same across all 
 * plaforms. In addition, this functions as the root class all intents and purposes, 
 * and you would draw it as a root class in an architecture specification.  
 */
public class GDXRoot extends Game implements ScreenListener {
	/** AssetManager to load game assets (textures, sounds, etc.) */
	private AssetManager manager;
	/** Drawing context to display graphics (VIEW CLASS) */
	private GameCanvas canvas; 
	/** Player mode for the asset loading screen (CONTROLLER CLASS) */
	private LoadingMode loading;
	/** Player mode for the level select screen */
	private LevelMenuMode levelSelect;
	/** Player mode for the the game proper (CONTROLLER CLASS) */
	private GameMode    playing;
	private RestStopMode reststop;
	private StartMenuMode start;
	private SettingsMenu settings;
	private SoundController soundController;

	/** Level files - currLevel is the level that will be played */
	private static final String[] LEVELS = new String[]{
			"level0.xlsx", "level1.xlsx"};
	private static int currLevel;
	private static final int NUM_TUTORIALS = 1;

	/** Rest stop files - REST_STOPS[currLevel] is the rest stop after LEVELS[currLevel] */
	// TODO - tutorials need their own rest stops
	private static final String[] REST_STOPS = new String[]{
			"rest_stop0.json", "rest_stop1.json"};

	/** Saved level files */
	private Array<String> SAVED_LEVELS = new Array<String>();

	/**
	 * Creates a new game from the configuration settings.
	 *
	 * This method configures the asset manager, but does not load any assets
	 * or assign any screen.
	 */
	public GDXRoot() {
		// Start loading with the asset manager
		manager = new AssetManager();
		
		// Add font support to the asset manager
		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		currLevel = 0;
	}


	/**
	 * Load the file names that are contained in the saved levels directory
	 * and places them in the SAVED_LEVELS array.
	 *
	 * Do this before creating the first LevelMenuMode to ensure that the
	 * correct number of nodes are displayed.
	 */
	private void loadSavedFilenames() {
		File[] files = new File("levels/savedlevels/").listFiles();
		for (File f : files) {
			String fn = f.getName();
			if (fn.contains("saved_level"))
				SAVED_LEVELS.add(fn);
		}
	}

	/** 
	 * Called when the Application is first created.
	 * 
	 * This is method immediately loads assets for the loading screen, and prepares
	 * the asynchronous loader for all other assets.
	 */
	public void create() {
		currLevel = 0;
		loadSavedFilenames();
		Gdx.graphics.setTitle("Home Away From Gnome");
		setCursor("images/mouse.png");

		canvas  = new GameCanvas();
		loading = new LoadingMode(canvas,manager,1);
		reststop = new RestStopMode(canvas, manager, REST_STOPS[currLevel]);
		levelSelect = new LevelMenuMode(canvas, manager, NUM_TUTORIALS, SAVED_LEVELS.size);
		settings = new SettingsMenu();
		soundController = new SoundController(settings);
		playing = new GameMode(canvas,settings,soundController,LEVELS[currLevel]);
		start = new StartMenuMode(canvas, manager,settings,soundController);

		loading.setScreenListener(this);
		playing.preLoadContent(manager); // Load game assets statically.
		settings.preLoadContent(manager);
		setScreen(loading);
	}

	/** 
	 * Called when the Application is destroyed. 
	 *
	 * This is preceded by a call to pause().
	 */
	public void dispose() {
		// Call dispose on our children
		Screen screen = getScreen();
		setScreen(null);
		screen.dispose();
		canvas.dispose();
		canvas = null;
	
		// Unload all of the resources
		manager.clear();
		manager.dispose();
		super.dispose();
	}
	
	/**
	 * Called when the Application is resized. 
	 *
	 * This can happen at any point during a non-paused state but will never happen 
	 * before a call to create().
	 *
	 * @param width  The new width in pixels
	 * @param height The new height in pixels
	 */
	public void resize(int width, int height) {
		canvas.resize();
		super.resize(width,height);
	}

	/**
	 * Save the game file as a JSON.
	 * The JSON includes the inventory, current level, and a UNIX time stamp of
	 * when it was saved.
	 *
	 * Levels are saved when exiting the rest stop.
	 * Loading the level will bring you to the next level.
	 *
	 * @param inv the player's inventory at time of saving
	 */
	private void saveGame(Inventory inv) {
		// the level number (which doesn't include tutorials), not the index in
		// the level array (which includes tutorials)
		int idxWithoutTutorials = currLevel - NUM_TUTORIALS;

		// create the JSON object
		JSONObject json = new JSONObject();
		json.put("numSnacks", inv.getNumSnacks());
		json.put("numBooks", 0); // TODO - Fix
		json.put("numMovies", inv.getNumMovies());
		json.put("currentLevelNum", idxWithoutTutorials);
		// UNIX timestamp - seconds since 1/1/1970
		json.put("timestamp", System.currentTimeMillis() / 1000L);

		// write to the file
		String filename = "levels/savedlevels/saved_level" + idxWithoutTutorials + ".json";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
			bw.write(json.toString());
			bw.close();
		} catch (IOException e) {
			System.out.println("IO exception when saving");
		}
	}

	/**
	 * The given screen has made a request to exit its player mode.
	 *
	 * The value exitCode can be used to implement menu options.
	 *
	 * @param screen   The screen requesting to exit
	 * @param exitCode The state of the screen upon exit
	 */
	public void exitScreen(Screen screen, int exitCode) {
		if (exitCode != 0) {
			Gdx.app.error("GDXRoot", "Exit with error code "+exitCode, new RuntimeException());
			Gdx.app.exit();
		} else if (screen == loading) {
			playing.loadContent(manager);
			settings.loadContent(manager);
			start.setScreenListener(this);
			Gdx.input.setInputProcessor(start);
			setScreen(start);
			loading.dispose();
			loading = null;
		} else if (screen == start) {
			if(start.levelSelectButtonClicked()) {
				// create a new level select mode if current one is null
				if (levelSelect == null) {
					levelSelect = new LevelMenuMode(canvas, manager, NUM_TUTORIALS, SAVED_LEVELS.size);
				}
				levelSelect.setScreenListener(this);
				Gdx.input.setInputProcessor(levelSelect);
				setScreen(levelSelect);
				start.dispose();
				start = null;
			} else if(start.exitButtonClicked()) {
				Gdx.app.exit();
			} else if(start.settingsButtonClicked()) {
			} else {
				playing.setScreenListener(this);
				//Gdx.input.setInputProcessor(playing);
				setScreen(playing);
				start.dispose();
				start = null;
			}
		} else if (screen == levelSelect) {
			if (levelSelect.loadPlaying()) {
				// load next level index either from the levels array or saved_levels array
				int nextIdx = levelSelect.getNextLevelIndex();
				String next = levelSelect.loadSavedLevel() ? SAVED_LEVELS.get(nextIdx) : LEVELS[nextIdx];
				System.out.println("NOW PLAYING LEVEL: " + next);
				playing = new GameMode(canvas,settings,soundController,LEVELS[currLevel]);
				playing.preLoadContent(manager);
				playing.loadContent(manager);
				playing.setScreenListener(this);
				setScreen(playing);
			}
			else {
				start = new StartMenuMode(canvas, manager,settings,soundController);
				Gdx.input.setInputProcessor(start);
				start.setScreenListener(this);
				setScreen(start);
			}

			levelSelect.dispose();
			levelSelect = null;

		} else if (screen == playing) {
			if(playing.exitFromPause){
				playing.exitFromPause = false;
				start = new StartMenuMode(canvas,manager,settings,soundController);
				start.setScreenListener(this);
				Gdx.input.setInputProcessor(start);
				setScreen(start);
				//playing.dispose();
				//playing = null;
			} else {
				reststop = new RestStopMode(canvas,manager,REST_STOPS[currLevel]);
				reststop.setPlayerInv(playing.getInventory());

				// increment current level index for saving purposes
				// and for loading when exiting rest stop mode
				currLevel = (currLevel + 1) % LEVELS.length;

				// save the game when entering - to ensure game is saved even if user quits at rest stop
				// only save if next level is not a tutorial
				if (!(LEVELS[currLevel].contains("tut")))
					saveGame(playing.getInventory());

				reststop.setScreenListener(this);
				Gdx.input.setInputProcessor(reststop);
				setScreen(reststop);
				playing.dispose();
				playing = null;
			}

		} else if (screen == reststop) {
			// save the game when exiting the rest stop - loading will bring you to the next level
			saveGame(reststop.getPlayerInv());

			playing = new GameMode(canvas,settings,soundController,LEVELS[currLevel]);
			playing.preLoadContent(manager);
			playing.loadContent(manager);
			playing.setInventory(reststop.getPlayerInv()); // manually set inventory bc new GameMode
			playing.setScreenListener(this);
			setScreen(playing);

			reststop.dispose();
			reststop = null;
		} else {
			// We quit the main application
			Gdx.app.exit();
		}
	}

	/** Set the cursor to the image specified by
	 * the path string
	 * @param path
	 */
	public void setCursor(String path){
		Pixmap pm = new Pixmap(Gdx.files.internal(path));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm,0 ,0 ));
		pm.dispose();
	}

}
