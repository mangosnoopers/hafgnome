
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

import edu.cornell.gdiac.mangosnoops.hudentity.*;
import edu.cornell.gdiac.mangosnoops.roadentity.*;

import java.util.Random;

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

	/** Car instance, containing information about the wheel and children */
	private Car yonda;

	/** Location and animation information for the wheel **/
	private Wheel wheel;

	/** Location and animation information for the wheel **/
	private Radio radio;

	/** Data structure with level format */
	private LevelObject level;


	// Graphics assets for the entities
    /** The texture file for the wheel **/
    private static final String WHEEL_FILE = "images/Wheel.png";
    /** The texture file for the gnome */
	private static final String GNOME_FILE = "images/gnome.png";
	/** The texture file for the gnome */
	private static final String RADIO_FILE = "images/radio.png";
	/** The texture file for the gnome */
	private static final String RADIO_KNOB_FILE = "images/radioDial.png";
	/** The texture files for Nosh's moods */
	private static final String NOSH_HAPPY_FILE = "images/NoshTextures/nosh_happy.png";
	private static final String NOSH_NEUTRAL_FILE = "images/NoshTextures/nosh_neutral.png";
	private static final String NOSH_SAD_FILE = "images/NoshTextures/nosh_sad.png";
	private static final String NOSH_CRITICAL_FILE = "images/NoshTextures/nosh_critical.png";
	private static final String NOSH_SLEEP_FILE = "images/NoshTextures/nosh_sleep.png";
	/** The texture files for Ned's moods */
	private static final String NED_HAPPY_FILE = "images/NedTextures/ned_happy.png";
	private static final String NED_NEUTRAL_FILE = "images/NedTextures/ned_neutral.png";
	private static final String NED_SAD_FILE = "images/NedTextures/ned_sad.png";
	private static final String NED_CRITICAL_FILE = "images/NedTextures/ned_critical.png";
	private static final String NED_SLEEP_FILE = "images/NedTextures/ned_sleep.png";

	/** Texture for the wheel**/
	private Texture wheelTexture;
	/** Texture for the wheel**/
	private Texture gnomeTexture;
	/** Texture for the radio**/
	private Texture radioTexture;
	/** Texture for the radio knob**/
	private Texture radioknobTexture;
	/** Textures for nosh **/
	private Texture nosh_happy;
	private Texture nosh_neutral;
	private Texture nosh_sad;
	private Texture nosh_critical;
	private Texture nosh_sleep;
	/** Textures for ned **/
	private Texture ned_happy;
	private Texture ned_neutral;
	private Texture ned_sad;
	private Texture ned_critical;
	private Texture ned_sleep;

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
		manager.load(WHEEL_FILE,Texture.class);
		assets.add(WHEEL_FILE);
		manager.load(GNOME_FILE, Texture.class);
		assets.add(GNOME_FILE);
		manager.load(RADIO_FILE, Texture.class);
		assets.add(RADIO_FILE);
		manager.load(RADIO_KNOB_FILE, Texture.class);
		assets.add(RADIO_KNOB_FILE);
		manager.load(NOSH_HAPPY_FILE, Texture.class);
		assets.add(NOSH_HAPPY_FILE);
		manager.load(NOSH_NEUTRAL_FILE, Texture.class);
		assets.add(NOSH_NEUTRAL_FILE);
		manager.load(NOSH_SAD_FILE, Texture.class);
		assets.add(NOSH_SAD_FILE);
		manager.load(NOSH_CRITICAL_FILE, Texture.class);
		assets.add(NOSH_CRITICAL_FILE);
		manager.load(NOSH_SLEEP_FILE, Texture.class);
		assets.add(NOSH_SLEEP_FILE);
		manager.load(NED_HAPPY_FILE, Texture.class);
		assets.add(NED_HAPPY_FILE);
		manager.load(NED_NEUTRAL_FILE, Texture.class);
		assets.add(NED_NEUTRAL_FILE);
		manager.load(NED_SAD_FILE, Texture.class);
		assets.add(NED_SAD_FILE);
		manager.load(NED_CRITICAL_FILE, Texture.class);
		assets.add(NED_CRITICAL_FILE);
		manager.load(NED_SLEEP_FILE, Texture.class);
		assets.add(NED_SLEEP_FILE);
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
		wheelTexture = createTexture(manager,WHEEL_FILE);
		gnomeTexture = createTexture(manager, GNOME_FILE);
		radioTexture = createTexture(manager, RADIO_FILE);
		radioknobTexture = createTexture(manager,RADIO_KNOB_FILE);
		nosh_happy = createTexture(manager,NOSH_HAPPY_FILE);
		nosh_neutral = createTexture(manager,NOSH_NEUTRAL_FILE);
		nosh_sad = createTexture(manager, NOSH_SAD_FILE);
		nosh_critical = createTexture(manager, NOSH_CRITICAL_FILE);
		nosh_sleep= createTexture(manager, NOSH_SLEEP_FILE);
		ned_happy = createTexture(manager,NED_HAPPY_FILE);
		ned_neutral = createTexture(manager,NED_NEUTRAL_FILE);
		ned_sad = createTexture(manager, NED_SAD_FILE);
		ned_critical = createTexture(manager, NED_CRITICAL_FILE);
		ned_sleep = createTexture(manager, NED_SLEEP_FILE);
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
	 *
	 * @param level is the Level information (which was saved in a JSON file)
	 */
	public GameplayController(LevelObject level) {
		this.level = level;
		yonda = null;
		gnomez = new Array<Gnome>();
		backing = new Array<Gnome>();
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
	 * Returns a reference to the radio
	 *
	 * @return reference to the radio
	 **/
    public Radio getRadio(){ return radio; }


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
		gnomez = level.getGnomez();
		*/
		wheel = new Wheel(275,70);
		wheel.setWheelSprite(wheelTexture);
		radio = new Radio(545, 50);
		radio.setRadioSprite(radioTexture);
		radio.setKnobSprite(radioknobTexture);

		yonda.getNosh().setChildTextures(nosh_happy,nosh_neutral,nosh_sad,nosh_critical,nosh_sleep);
		yonda.getNed().setChildTextures(ned_happy,ned_neutral,ned_sad,ned_critical,ned_sleep);

		Gnome newGnome = new Gnome(-0.1f, 50);
		Gnome newGnome2 = new Gnome(0.1f, 100);
		Gnome newGnome3 = new Gnome(0, 120);
		Gnome newGnome4 = new Gnome(0, 150);
		Gnome newGnome5 = new Gnome(0.1f,170);
		Gnome newGnome6 = new Gnome(-0.1f, 10);
		Gnome newGnome7 = new Gnome(0f, 15);
		Gnome newGnome8 = new Gnome(0.1f, 30);
		Gnome newGnome9 = new Gnome(0, 40);
		Gnome newGnome10 = new Gnome(-0.1f,300);
		Gnome newGnome11 = new Gnome(0, 5);
		Gnome newGnome12 = new Gnome(-0.1f, 15);
		Gnome newGnome13 = new Gnome(0.1f, 80);
		Gnome newGnome14 = new Gnome(-0.1f, 90);
		Gnome newGnome15 = new Gnome(0.1f, 100);
		newGnome.setTexture(gnomeTexture);
		newGnome2.setTexture(gnomeTexture);
		newGnome3.setTexture(gnomeTexture);
		newGnome4.setTexture(gnomeTexture);
		newGnome5.setTexture(gnomeTexture);
		newGnome6.setTexture(gnomeTexture);
		newGnome7.setTexture(gnomeTexture);
		newGnome8.setTexture(gnomeTexture);
		newGnome9.setTexture(gnomeTexture);
		newGnome10.setTexture(gnomeTexture);
		newGnome11.setTexture(gnomeTexture);
		newGnome12.setTexture(gnomeTexture);
		newGnome13.setTexture(gnomeTexture);
		newGnome14.setTexture(gnomeTexture);
		newGnome15.setTexture(gnomeTexture);
		gnomez.add(newGnome);
		gnomez.add(newGnome2);
		gnomez.add(newGnome3);
		gnomez.add(newGnome4);
		gnomez.add(newGnome5);
		gnomez.add(newGnome6);
		gnomez.add(newGnome7);
		gnomez.add(newGnome8);
		gnomez.add(newGnome9);
		gnomez.add(newGnome10);
		gnomez.add(newGnome11);
		gnomez.add(newGnome12);
		gnomez.add(newGnome13);
		gnomez.add(newGnome14);
		gnomez.add(newGnome15);
	}

	/**
	 * Resets the game, deleting all objects.
	 */
	public void reset() {
		rotationMagnitude = 0;
//		yonda = null; TODO: prob make this less sus
		yonda.reset();
		wheel = null;
		radio = null;
		gnomez.clear();
		backing.clear();
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
	protected void destroy(RoadObject o) {
	    // TODO: carry out actions that occur on death of object o
		switch(o.getType()) {
			case CAR:
				break;
			case GNOME:
				break;
			default:
				break;
		}
	}
	
	/**
	 * Resolve the actions of all game objects
	 *
	 * @param input  Reference to the input controller
	 * @param delta  Number of seconds since last animation frame
	 */
	public void resolveActions(InputController input, float delta) {
		for (Gnome g : gnomez) { g.update(delta); }

		// Update the wheel angle
		wheel.update(input.getClickPos(), input.getDX());

		// Update the radio
		radio.update(input.getClickPos(), input.getDX());

		yonda.update(input.getClickPos(), delta);
	}

	/** TODO: MAKE THIS NOT JANK IM JUST TRYING TO MAKE THE KIDS SLEEP
	 *
	 * @param counter
	 * @param ned
	 * @param nosh
	 */
	public void resolveChildren(int counter, Child ned, Child nosh) {
		Random generator = new Random();
		float ned_prob = 0.1f;
		float nosh_prob = 0.1f;

		// check every 10 frames
		if (counter % 10 == 0) {
			// make ned sleepy with given probability
			if (generator.nextFloat() <= ned_prob) {
				ned.setAwake(false);
			}
			// make nosh sleepy with given probability
			if (generator.nextFloat() <= nosh_prob) {
				nosh.setAwake(false);
			}

		}

	}
}