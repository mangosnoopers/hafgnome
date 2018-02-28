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

/**
 * Controller to handle gameplay interactions.
 * </summary>
 * <remarks>
 * This controller also acts as the root class for all the models.
 */
public class GameplayController {
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

	/** The minimum x-velocity of a newly generated shell */
	private static final float MIN_SHELL_VX = 3;
	/** The maximum y-velocity of a newly generated shell */
	private static final float MAX_SHELL_VX = 10;
	/** The y-position offset of a newly generated bullet */
	private static final float BULLET_OFFSET = 5.0f;
	/** The vertical speed of a newly generated bullet */
	private static final float BULLET_SPEED  = 10.0f;
	/** The minimum velocity factor (x shell velocity) of a newly created star */
	private static final float MIN_STAR_FACTOR = 0.1f;
	/** The maximum velocity factor (x shell velocity) of a newly created star */
	private static final float MAX_STAR_FACTOR = 0.2f;
	/** The minimum velocity offset (+ shell velocity) of a newly created star */
	private static final float MIN_STAR_OFFSET = -3.0f;
	/** The maximum velocity offset (+ shell velocity) of a newly created star */
	private static final float MAX_STAR_OFFSET = 3.0f;

	/** Reference to player (need to change to allow multiple players) */
	private Ship player;
	/** Shell count for the display in window corner */
	private int shellCount;

	// List of objects with the garbage collection set.
	/** The currently active object */
	private Array<GameObject> objects;
	/** The backing set for garbage collection */
	private Array<GameObject> backing;

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
		player = null;
		shellCount = 0;
		objects = new Array<GameObject>();
		backing = new Array<GameObject>();
	}

	/**
	 * Returns the list of the currently active (not destroyed) game objects
	 *
 	 * As this method returns a reference and Lists are mutable, other classes can 
 	 * technical modify this list.  That is a very bad idea.  Other classes should
	 * only mark objects as destroyed and leave list management to this class.
	 *
	 * @return the list of the currently active (not destroyed) game objects
	 */
	public Array<GameObject> getObjects() {
		return objects;
	}

	/**
	 * Returns a reference to the currently active player.
	 *
	 * This property needs to be modified if you want multiple players.
	 *
	 * @return a reference to the currently active player.
	 */
	public Ship getPlayer() {
		return player;
	}

	/**
	 * Returns true if the currently active player is alive.
	 *
	 * This property needs to be modified if you want multiple players.
	 *
	 * @return true if the currently active player is alive.
	 */
	public boolean isAlive() {
		return player != null;
	}

	/**
	 * Returns the number of shells currently active on the screen.
	 *
	 * @return the number of shells currently active on the screen.
	 */
	public int getShellCount() {
		return shellCount;
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
		player = new Ship();
		player.setTexture(beetleTexture);
		player.getPosition().set(x,y);

		// Player must be in object list.
		objects.add(player);
	}

	/**
	 * Resets the game, deleting all objects.
	 */
	public void reset() {
		player = null;
		shellCount = 0;
		objects.clear();
	}

	/**
	 * Adds a new shell to the game.
	 *
	 * A shell is generated at the top with a random horizontal position. Notice that
	 * this allocates memory to the heap.  If we were REALLY worried about performance,
	 * we would use a memory pool here.
	 *
	 * @param width  Current game width
	 * @param height Current game height
	 */
	public void addShell(float width, float height) {
		// Add a new shell
		Shell b = new Shell();
		if (RandomController.rollInt(0, 2) == 0) {
			// Needs two shots to kill
			b.setTexture(redTexture);
			b.setDamagedTexture(greenTexture);
		} else {
			//  Needs one shot to kill
			b.setTexture(greenTexture);
			b.setDamagedTexture(null);
		}

		// Only define vx. Gravity takes care of vy.
		float vx = RandomController.rollFloat(MIN_SHELL_VX,MAX_SHELL_VX);
		// Coin flip positive or negative
		vx = vx*((float)RandomController.rollInt(0, 1) * 2 - 1);

		// Position the shell
		b.setX(RandomController.rollFloat(0, width));
		b.setY(height);
		b.setVX(vx);
		objects.add(b);
		shellCount++;
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
		for (GameObject o : objects) {
			if (o.isDestroyed()) {
				destroy(o);
			} else {
				backing.add(o);
			}
		}

		// Swap the backing store and the objects.
		// This is essentially stop-and-copy garbage collection
		Array<GameObject> tmp = backing;
		backing = objects;
		objects = tmp;
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
		switch(o.getType()) {
		case SHIP:
			player = null;
			break;
		case SHELL:
			// Create some stars
			for (int j = 0; j < 6; j++) {
				Star s = new Star();
				s.setTexture(starTexture);
				s.getPosition().set(o.getPosition());
				float vx = o.getVX() * RandomController.rollFloat(MIN_STAR_FACTOR, MAX_STAR_FACTOR) 
							+ RandomController.rollFloat(MIN_STAR_OFFSET, MAX_STAR_OFFSET);
				float vy = o.getVY() * RandomController.rollFloat(MIN_STAR_FACTOR, MAX_STAR_FACTOR) 
							+ RandomController.rollFloat(MIN_STAR_OFFSET, MAX_STAR_OFFSET);
				s.getVelocity().set(vx,vy);
				backing.add(s);
			}
			shellCount--;
			break;
		default:
			break;
		}
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
		// Process the player
		if (player != null) {
			resolvePlayer(input,delta);
		}

		// Process the other (non-ship) objects.
		for (GameObject o : objects) {
			o.update(delta);
		}
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
	public void resolvePlayer(InputController input, float delta) {
		player.setMovement(input.getMovement());
		player.setFiring(input.didFire());
		player.update(delta);
		if (!player.isFiring()) {
			return;
		}

		// Create a new bullet
		Bullet b = new Bullet();
		b.setTexture(bulletTexture);
		b.setX(player.getX());
		b.setY(player.getY()+player.getRadius()+BULLET_OFFSET);
		b.setVY(BULLET_SPEED);
		backing.add(b); // Bullet added NEXT frame.

		// Prevent player from firing immediately afterwards.
		player.resetCooldown();
	}
}