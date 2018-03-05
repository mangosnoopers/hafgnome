/*
 * GameplayController.java
 *
 * For many of you, this class will seem like the most unusual one in the entire project.  
 * It implements a lot of functionality that looks like it should go into the various 
 * GameObject subclasses. However, a lot of this functionality involves the creation or
 * destruction of objects.  We cannot do this without a lot of cyclic dependencies, 
 * which are bad.
 *
 * You will notice that gameplay-wise, most of the features in this class are 
 * interactions, not actions. This demonstrates why a software developer needs to 
 * understand the difference between these two.  
 *
 * You will definitely need to modify this file in Part 2 of the lab. However, you are
 * free to modify any file you want.  You are also free to add new classes and assets.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import edu.cornell.gdiac.mangosnoops.entity.*;
import edu.cornell.gdiac.mangosnoops.GameObject.ObjectType;

import java.util.HashSet;

/**
 * Controller to handle gameplay interactions.
 * </summary>
 * <remarks>
 * This controller also acts as the root class for all the models.
 */
public class GameplayController {


	/** The change in x, computed based on the wheel angle */
	private float rotationMagnitude;

	/** Data structure containing gnome data */
	private Array<Gnome> gnomez;

	/** Car instance, containing information about the wheel, */
	private Car yonda;

	/** Location and animation information for the wheel **/
	private Wheel wheel;

	/** Data structure with level format */
	private LevelObject level;

	// Graphics assets for the entities
	/** The texture file for a ship object*/
	private static final String BEETLE_FILE = "images/beetle.png";
	/** The texture file for a bullet object*/
	private static final String BULLET_FILE = "images/bullet.png";
	/** The texture file for a green shell */
	private static final String GSHELL_FILE = "images/green.png";
	/** The texture file for a red shell */
	private static final String RSHELL_FILE = "images/red.png";
	/** The texture file for a star */
	private static final String STAR_FILE = "images/star.png";
    /** The texture file for the wheel **/
    private static final String WHEEL_FILE = "images/wheelard_straight.png";

    /** Texture for all ships, as they look the same */
	private Texture beetleTexture;
	/** Texture for all stars, as they look the same */
	private Texture starTexture;
	/** Texture for all bullets, as they look the same */
	private Texture bulletTexture;
	/** Texture for green shells, as they look the same */
	private Texture greenTexture;
	/** Texture for red shells, as they look the same */
	private Texture redTexture;
	/** Texture for the wheel**/
	private Texture wheelTexture;

	// List of objects with the garbage collection set.
	/** The backing set for garbage collection */
	private Array<Gnome> backing;

	/** 
	 * Preloads the assets for this game.
	 * 
	 * The asset manager for LibGDX is asynchronous.  That means that you
	 * tell it what to load and then wait while it loads them.  This is 
	 * the first step: telling it what to load.
	 * 
	 * @param manager Reference to global asset manager.
	 * @param assets  Asset list to track which assets where loaded
	 */
	public void preLoadContent(AssetManager manager, Array<String> assets) {
		manager.load(BEETLE_FILE,Texture.class);
		assets.add(BEETLE_FILE);		
		manager.load(BULLET_FILE,Texture.class);
		assets.add(BULLET_FILE);		
		manager.load(STAR_FILE,Texture.class);
		assets.add(STAR_FILE);		
		manager.load(GSHELL_FILE,Texture.class);
		assets.add(GSHELL_FILE);			
		manager.load(RSHELL_FILE,Texture.class);
		assets.add(RSHELL_FILE);
		manager.load(WHEEL_FILE,Texture.class);
		assets.add(WHEEL_FILE);
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
		beetleTexture = createTexture(manager,BEETLE_FILE);
		bulletTexture = createTexture(manager,BULLET_FILE);
		starTexture = createTexture(manager,STAR_FILE);
		redTexture = createTexture(manager,RSHELL_FILE);
		greenTexture = createTexture(manager,GSHELL_FILE);
		wheelTexture = createTexture(manager,WHEEL_FILE);
	}
	
	
	private Texture createTexture(AssetManager manager, String file) {
		if (manager.isLoaded(file)) {
			Texture texture = manager.get(file, Texture.class);
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			return texture;
		}
		return null;
	}



	/**
	 * Creates a new GameplayController with no active elements.
	 */
	public GameplayController() {
		yonda = null;
		gnomez = new Array<Gnome>();
		backing = new Array<Gnome>();
		wheel = new Wheel(400,350);\
	}

	/**
	 * Creates a new GameplayController with no active elements.
	 *
	 * @param level is the Level information (which was saved in a JSON file)
	 */
	public GameplayController(LevelObject level) {
		this.level = level;
		yonda = null;
		gnomez = new Array<Gnome>();
		backing = new Array<Gnome>();
		wheel = new Wheel(400, 350);
	}

	/**
	 * Returns the list of the currently active (not destroyed) game objects
	 *
	 * As this method returns a reference and Lists are mutable, other classes can
	 * technical modify this list.  That is a very bad idea.  Other classes should
	 * only mark objects as destroyed and leave list management to this class.
	 *
	 * @return a reference to all the gnomes
	 */
	public Array<Gnome> getGnomez() { return gnomez; }

	/**
	 * Returns a reference to the currently active car
	 *
	 * @return a reference to the currently active car.
	 */
	public Car getCar() {
		return yonda;
	}

    /**
     * Returns a reference to the wheel
     *
     * @return reference to the wheel
     **/
    public Wheel getWheel(){ return wheel; }

	/**
	 * Returns true if the currently active player is alive.
	 *
	 * This property needs to be modified if you want multiple players.
	 *
	 * @return true if the currently active player is alive.
	 */
	public boolean isAlive() {
		return yonda != null;
	}

	/**
	 * Starts a new game.
	 *
	 * This method creates a single player, but does nothing else.
	 *
	 * @param x Starting x-position for the player
	 * @param y Starting y-position for the player
	 */
	public void start(float x, float y) {
		// Create the player's ship
        yonda = level.getCar();
        /* TODO: commented this out to get game to run, car is null rn
		yonda.setTexture(beetleTexture);
		yonda.getPosition().set(x,y);
		gnomez = level.getGnomez();
		*/
	}

	/**
	 * Resets the game, deleting all objects.
	 */
	public void reset() {
		yonda = null;
		gnomez.clear();
	}

	/**
	 * Garbage collects all deleted objects.
	 *
	 * This method works on the principle that it is always cheaper to copy live objects
	 * than to delete dead ones.  Deletion restructures the list and is O(n^2) if the 
	 * number of deletions is high.  Since Add() is O(1), copying is O(n).
	 */
	public void garbageCollect() {
		// INVARIANT: backing and objects are disjoint
		for (Gnome g : gnomez) {
			if (g.isDestroyed()) {
				destroy(g);
			} else {
				backing.add(g);
			}
		}

		// Swap the backing store and the objects.
		// This is essentially stop-and-copy garbage collection
		Array<Gnome> tmp = backing;
		backing = gnomez;
		gnomez = tmp;
		backing.clear();
	}
	
	/**
	 * Process specialized destruction functionality
	 *
	 * Some objects do something special (e.g. explode) on destruction. That is handled 
	 * in this method.
	 *
	 * Notice that this allocates memory to the heap.  If we were REALLY worried about 
	 * performance, we would use a memory pool here.
	 *
	 * @param o Object to destroy
	 */
	protected void destroy(GameObject o) {
	    // TODO: carry out actions that occur on death of object o
	}
	
	/**
	 * Resolve the actions of all game objects (player and shells)
	 *
	 * You will probably want to modify this heavily in Part 2.
	 *
	 * @param input  Reference to the input controller
	 * @param delta  Number of seconds since last animation frame
	 */
	public void resolveActions(InputController input, float delta) {
	    // TODO: update object states based on input
	}

	/**
	 * Process the player's actions.
	 *
	 * Notice that firing bullets allocates memory to the heap.  If we were REALLY 
	 * worried about performance, we would use a memory pool here.
	 *
	 * @param input  Reference to the input controller
	 * @param delta  Number of seconds since last animation frame
	 */
	public void resolveCar(InputController input, float delta) {
		// TODO: update car state based on input
	}
}