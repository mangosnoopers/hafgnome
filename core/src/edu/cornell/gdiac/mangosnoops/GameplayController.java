
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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import edu.cornell.gdiac.mangosnoops.hudentity.*;
import edu.cornell.gdiac.mangosnoops.roadentity.*;


import java.util.Random;

/**
 * Controller to handle gameplay interactions.
 * This controller also acts as the root class for all the models.
 */
public class GameplayController {
	/** The change in x, computed based on the wheel angle */
	private float rotationMagnitude;
	/** Road instance, contains road "conveyor belt" logic */
	private Road road;
	/** Car instance, containing information about the wheel and children */
	private Car yonda;
	/** Location and animation information for the wheel **/
	private Wheel wheel;
	/** Location, animation information for vroomstick */
	private VroomStick vroomStick;
	/** Location and animation information for the wheel */
	private Radio radio;
	/** Inventory */
	private Inventory inventory;
	/** Contains location for the previous click, used for debouncing */
	private Vector2 prevClick = null;
	/** An array of enemies for this level */
	private Array<Gnome> gnomez;
	/** An array of events for this level */
	private Array<Event> events;
	/** The next event to happen */
	private int nextEvent;
	/** Object containing all information about the current level. This includes
	 *  everything specific to a level: the songs, enemies, events, etc. */
	private LevelObject level;
	/** Rearview enemy instance. The way it's handled right now, there is only
	 *  one at a time. FIXME: could change that if necessary */
	private RearviewEnemy rearviewEnemy;
	/** The y-position player is driving over, used for checking for events */
	private float ypos;


	// Graphics assets for the entities
    /** The texture file for the wheel **/
    private static final String WHEEL_FILE = "images/DashHUD/Wheel.png";
    /** The texture file for the vroom stick*/
	private static final String VROOM_STICK_FILE = "images/DashHUD/vroomstick.png";
    /** The texture file for the gnomes */
	private static final String GNOME_FILE = "images/Enemies/gnome.png";
	private static final String REARVIEW_GNOME_FILE = "images/Enemies/gnome_rear.png";
	/** The texture file for the radio knob */
	private static final String RADIO_KNOB_FILE = "images/DashHUD/radioDial.png";
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
	/** The texture files for all Items **/
	private static final String DVD_FILE = "images/Items/dvd.png";
	private static final String SNACK_FILE = "images/Items/snack.png";
	/** The texture file for the road */
	private static final String ROAD_FILE = "images/road.png";
	/** The texture file for the grass */
	private static final String GRASS_FILE = "images/grass.png";
	/** The texture file for the exit */
	private static final String EXIT_FILE = "images/exit.png";

	/** Texture for road */
	private Texture roadTexture;
	/** Texture for grass */
	private Texture grassTexture;
	/** Texture for exit */
	private Texture exitTexture;

	/** Texture for the wheel */
	private Texture wheelTexture;
	/** Texture for the vroomstick */
	private Texture vroomStickTexture;
	/** Texture for the gnomes */
	private Texture gnomeTexture;
	private Texture rearviewGnomeTexture;
	/** Texture for the radio */
	private Texture radioTexture;
	/** Texture for the radio knob */
	private Texture radioknobTexture;
	/** Textures for nosh */
	private Texture nosh_happy;
	private Texture nosh_neutral;
	private Texture nosh_sad;
	private Texture nosh_critical;
	private Texture nosh_sleep;
	/** Textures for ned */
	private Texture ned_happy;
	private Texture ned_neutral;
	private Texture ned_sad;
	private Texture ned_critical;
	private Texture ned_sleep;
	/** Textures for items **/
	private Texture dvdTexture;
	private Texture snackTexture;

	// List of objects with the garbage collection set.
	/** The backing set for garbage collection */
	private Array<Gnome> backing;

	/** Enum specifying the region this level takes place in. */
	public enum Region {
		SUBURBS, HIGHWAY, MIDWEST, COLORADO;
	}

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
		manager.load(VROOM_STICK_FILE, Texture.class);
		assets.add(VROOM_STICK_FILE);
		manager.load(GNOME_FILE, Texture.class);
		assets.add(GNOME_FILE);
		manager.load(REARVIEW_GNOME_FILE, Texture.class);
		assets.add(REARVIEW_GNOME_FILE);
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
		manager.load(DVD_FILE,Texture.class);
		assets.add(DVD_FILE);
		manager.load(SNACK_FILE,Texture.class);
		assets.add(SNACK_FILE);
		manager.load(GRASS_FILE, Texture.class);
		assets.add(GRASS_FILE);
		manager.load(ROAD_FILE, Texture.class);
		assets.add(ROAD_FILE);
		manager.load(EXIT_FILE, Texture.class);
		assets.add(EXIT_FILE);
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
		vroomStickTexture = createTexture(manager, VROOM_STICK_FILE);
		gnomeTexture = createTexture(manager, GNOME_FILE);
		rearviewGnomeTexture = createTexture(manager, REARVIEW_GNOME_FILE);
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
		dvdTexture = createTexture(manager,DVD_FILE);
		snackTexture = createTexture(manager,SNACK_FILE);
		roadTexture = createTexture(manager, ROAD_FILE);
		grassTexture = createTexture(manager, GRASS_FILE);
		exitTexture = createTexture(manager, EXIT_FILE);
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
	 * @param level is the Level information
	 */
	public GameplayController(LevelObject level) {
		this.level = level;
		gnomez = new Array<Gnome>();
		events = new Array<Event>();
		yonda = new Car();
		backing = new Array<Gnome>();
		road = new Road(level.getLevelEndY());
		ypos = 0.0f;
		nextEvent = 0;
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
	 * Returns a reference to the car.
	 */
	public Car getCar() {
		return yonda;
	}

	/**
	 * Returns a reference to the road.
	 */
	public Road getRoad() { return road; }

  /**
   * Returns a reference to a rearview enemy.
   */
	public RearviewEnemy getRearviewEnemy() { return rearviewEnemy; }

    /**
     * Returns a reference to the wheel.
     */
    public Wheel getWheel(){ return wheel; }

  /**
   * Returns a reference to the vroom stick.
   */
	public VroomStick getVroomStick() { return vroomStick; }

	/**
	 * Returns a reference to the radio
	 */
    public Radio getRadio(){ return radio; }

	/**
	 * Starts a new game.
	 *
	 * This method creates a single player, but does nothing else.
	 *
	 * @param x Starting x-position for the player
	 * @param y Starting y-position for the player
	 */
	public void start(float x, float y) {
		for(Gnome g: level.getGnomez()){
			gnomez.add(new Gnome(g));
		}
		// TODO CHANGE THIS LOL
		for (Gnome g : gnomez) {
			g.setTexture(gnomeTexture);
		}
		events = level.getEvents();

		wheel = new Wheel(0.193f,0.22f, 0.5f, 60, wheelTexture);
		vroomStick = new VroomStick(0.193f, 0.2f,0.3f, 0, vroomStickTexture);
		radio = new Radio(0.66f, 0.06f, 0.07f, 0, radioknobTexture, level.getSongs());
		inventory = new Inventory(0.4756f,0.0366f, 0,0,wheelTexture, 0.146f, 0.128f, 2);

		yonda.getNosh().setChildTextures(nosh_happy,nosh_neutral,nosh_sad,nosh_critical,nosh_sleep);
		yonda.getNed().setChildTextures(ned_happy,ned_neutral,ned_sad,ned_critical,ned_sleep);
		Inventory.Item.setTexturesAndScales(dvdTexture,0.3f,snackTexture,0.3f);

		road.setRoadTexture(roadTexture);
		road.setGrassTexture(grassTexture);
		road.setExitTexture(exitTexture);

		// Rearview enemy
		rearviewEnemy = new RearviewEnemy(0.843f, 0.81f, 0.18f,0, rearviewGnomeTexture);

  }

	/**
	 * Resets the game, deleting all objects.
	 */
	public void reset() {
		rotationMagnitude = 0;
		yonda.reset();
		wheel = null;
		radio = null;
		gnomez = new Array<Gnome>(level.getGnomez().size);
		backing.clear();
		ypos = 0.0f;
		nextEvent = 0;
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
	 * Makes the first event in the event queue occur and removes it from the
	 * queue if it should occur at the current time. Does nothing if the first
	 * event in the queue should not occur at this time or if the queue is empty.
	 *
	 * @param delta Number of seconds since the last animation frame
	 * @param ned Ned
	 * @param nosh Nosh
	 */
	public void handleEvents(float delta, Child ned, Child nosh) {
		if (events.size != 0) {
			float dy = road.getSpeed() * delta; // change in y position
			ypos += dy; // add it to y position tracker
			Event first = events.get(nextEvent);

			// give some leeway in position for events to occur
			if (Math.abs(first.getY() - ypos) < 0.1f) {
				switch (first.getType()) {
					case REAR_ENEMY:
						rearviewEnemy.create();
						break;
					case SUN:
						// TODO
						break;
					case NED_WAKES_UP:
						if (!ned.isAwake()) {
							ned.setMood(Child.Mood.NEUTRAL);
							ned.setMoodShifting(true, false);
						}
						break;
					case NOSH_WAKES_UP:
						if (!nosh.isAwake()) {
							nosh.setMood(Child.Mood.NEUTRAL);
							nosh.setMoodShifting(true, false);
						}
						break;
					case SAT_QUESTION:
						// TODO
						break;
					default:
						break;
				}
			}
		}
	}

	/**
	 * Resolve the actions of all game objects
	 *
	 * @param input  Reference to the input controller
	 * @param delta  Number of seconds since last animation frame
	 */
	public void resolveActions(InputController input, float delta) {

//		for(int i=0; i<gnomez.size; i++){
//			System.out.println("gnome["+i+"]: " + gnomez.get(i).getY());
//		}
		// Update world objects (road and gnome positions)
        road.update(delta);
        for (Gnome g : gnomez) {
            g.update(delta, road.getSpeed());
        }

        // Update the HUD
        Vector2 in = input.getClickPos();
        Vector2 mouseCoords = null;
        Vector2 dr = new Vector2(input.getDX(), input.getDY());
        if (in != null){
            mouseCoords = new Vector2(in);
        }

        wheel.update(mouseCoords, dr.x);
		vroomStick.update(in, dr.y);
		radio.update(mouseCoords, dr.x);
		inventory.update(in,input.mousePressed());

		rearviewEnemy.update(delta*0.0004f);

		if (vroomStick.isEngaged()) {
			rearviewEnemy.destroyIfAlive();
			road.setVrooming();
		}

		if (rearviewEnemy.isAttackingCar()) {
			getCar().damage();
			if (getCar().getHealth() == 0)
				getCar().setDestroyed(true);
		}

		if(prevClick != null && input.getClickPos() == null) {
			yonda.update(prevClick, wheel, delta);
			prevClick = null;
		} else {
			prevClick = input.getClickPos();
			yonda.update(null, wheel, delta);
		}

		if (road.reachedEndOfLevel()) {
			getCar().takeExit();
		}

	}

	/** TODO: MAKE THIS NOT JANK IM JUST TRYING TO MAKE THE KIDS SLEEP also handles radio
	 *
	 * @param counter
	 * @param ned
	 * @param nosh
	 */
	public void resolveChildren(int counter, Child ned, Child nosh, Radio r) {
		// check radio station and update each child's happiness based on it,
		// checks this every 200 frames (may need to adjust)
		if (r.getCurrentStation() != null && r.getknobAng() <= 0 && counter%200 == 0) {

			// TODO : ADD CASES FOR OTHER GENRES
			switch (r.getCurrentStationGenre()){
				case DANCE: // ned likes, nosh dislikes
					if(ned.isAwake()){
                        ned.setMoodShifting(true, true);
					}
					if(nosh.isAwake()){
                        nosh.setMoodShifting(true, false);
					}
					break;
				case CREEPY: // ned likes, nosh dislikes
					if(ned.isAwake()){
                        ned.setMoodShifting(true, true);
					}
					if(nosh.isAwake()){
						nosh.setMoodShifting(true, false);
					}
					break;
				case JAZZ: // ned likes, nosh dislikes
					if(ned.isAwake()){
						ned.setMoodShifting(true, true);
					}
					if(nosh.isAwake()){
                        nosh.setMoodShifting(true, false);
					}
					break;
				case COMEDY: // ned dislikes, nosh likes
					if(ned.isAwake()){
						ned.setMoodShifting(true, false);
					}
					if(nosh.isAwake()){
						nosh.setMoodShifting(true, true);
					}
					break;
				default:
					break;
			}
//			if (ned.isAwake()) {
//				if (r.getCurrentStationNed()) {
//					ned.setHappy();
//				} else {
//					ned.decreaseHappiness();
//				}
//			}
//
//			// check radio for nosh
//			if (nosh.isAwake()) {
//				if (r.getCurrentStationNosh()) {
//					nosh.setHappy();
//				} else {
//					nosh.decreaseHappiness();
//				}
//			}
		} else if (r.getCurrentStation() == null && counter != 0 && counter % 240 == 0 && ned.isAwake()) {
            ned.setMoodShifting(true, false);
        } else if (r.getCurrentStation() == null && counter != 0 && counter % 115 == 0 && nosh.isAwake()){
            nosh.setMoodShifting(true, false);
        }


        // TODO: eventually remove this random stuff, commenting out for testing
//		Random generator = new Random();
//		float ned_prob = 0.3f;
//		float nosh_prob = 0.3f;
//
//		float rearviewProb = 0.3f;
//
//		// check every 100 frames
//		if (counter % 100 == 0) {
//			// make ned sleepy with given probability
//			if (generator.nextFloat() <= ned_prob) {
////				ned.setAsleep();
//				ned.setMoodShifting(true, false);
//			}
//			// make nosh sleepy with given probability
//			if (generator.nextFloat() <= nosh_prob) {
//				nosh.setAsleep();
//			}
//
//			// Create rearview enemy with given probability
//			if (generator.nextFloat() <= rearviewProb) {
//				rearviewEnemy.create();
//			}
//
//		}

	}
}