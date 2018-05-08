
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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import edu.cornell.gdiac.mangosnoops.hudentity.*;
import edu.cornell.gdiac.mangosnoops.roadentity.*;
import edu.cornell.gdiac.util.FilmStrip;
import java.util.HashMap;
/**
 * Controller to handle gameplay interactions.
 * This controller also acts as the root class for all the models.
 */
public class GameplayController {
	private SoundController soundController;
	/** Road instance, contains road "conveyor belt" logic */
	protected Road road;
	/** Car instance, containing information about the wheel and children */
	protected Car yonda;
	/** Location and animation information for the wheel **/
	private Wheel wheel;
	/** Location, animation information for vroomstick */
	protected VroomStick vroomStick;
	/** Inventory */
	private Inventory inventory;
	/** Inventory at the beginning of the level */
	private Inventory initialInventory;
	/** Visor */
    private Visor visor;
	/** Contains location for the previous click, used for debouncing */
	private Vector2 prevClick = null;
	/** An array of enemies for this level */
	private Array<Enemy> enemiez;
    /** An array of enemies for this level -- is not altered, used for reset */
    private Array<Enemy> enemiezSave;
	/** The next event to happen */
	private int nextEvent;
	/** Rearview enemy instance. The way it's handled right now, there is only
	 *  one at a time. FIXME: could change that if necessary */
	protected RearviewEnemy rearviewEnemy;
	/** The y-position player is driving over, used for checking for events */
	protected float ypos;
	private SATQuestions satQuestions;
	private TouchScreen touchscreen;
	private Radio radio;
	private DvdPlayer dvdPlayer;


	/** An array of events for this level */
	protected Array<Event> events;
	/** The Horn! */
	private Horn horn;

	private ObjectMap<String,Radio.Genre> songs;

	private Image healthGauge;
	private Image rearviewDamageIndicator;
	private Image rearviewBackground;
	protected Image rearviewSeats;
	private Image rearviewCover;
	private Image healthGaugePointer;

	private Image masterShaker; //apply shake to this object and will shake all HUDObjects

	/** If there is sun shining right now */
	public boolean sunShine;

	// FilmStrip information
	/** The Gnome FilmStrip information */
	protected static final int GNOME_FILMSTRIP_ROWS = 1;
	protected static final int GNOME_FILMSTRIP_COLS = 12;

	/** The Flamingo FilmStrip information */
	protected static final int FLAMINGO_FILMSTRIP_ROWS = 1;
	protected static final int FLAMINGO_FILMSTRIP_COLS = 6;
	protected static final int FLAMINGO_STAND_START = 0;
	protected static final int FLAMINGO_STAND_END = 4;
	protected static final int FLAMINGO_FLY_START = 4;
	protected static final int FLAMINGO_FLY_END = 5;

	/** Child FilmStrip information */
	protected static final int NOSH_FILMSTRIP_ROWS = 1;
	protected static final int NOSH_FILMSTRIP_COLS = 13;
	protected static final int NED_FILMSTRIP_ROWS = 1;
	protected static final int NED_FILMSTRIP_COLS = 14;

	/** Grill FilmStrip information */
	protected static final int GRILL_FILMSTRIP_ROWS = 1;
	protected static final int GRILL_FILMSTRIP_COLS = 4;

	// Graphics assets for the entities
	/** The texture file for the flame **/
	private static final String FLAME_FILE = "images/fire.png";
	/** The texture file for the visor **/
	private static final String VISOR_FILE = "images/visor.png";
    /** The texture file for the wheel **/
    private static final String WHEEL_FILE = "images/DashHUD/Wheel.png";
    /** The texture file for the vroom stick*/
	private static final String VROOM_STICK_FILE = "images/DashHUD/vroomstick.png";
    /** The texture file for the gnomes */
	private static final String GNOME_FILE = "images/Enemies/gnome.png";
	private static final String REARVIEW_GNOME_FILE = "images/Enemies/gnome_rear.png";
	/** The texture file for the flamingo */
	private static final String FLAMINGO_FILE = "images/Enemies/flamingo.png";
	/** The texture file for the grill **/
	private static final String GRILL_FILE = "images/Enemies/grill.png";
	/** The texture file for the radio knob */
	private static final String RADIO_KNOB_FILE = "images/DashHUD/radioDial.png";
	private static final String RADIO_SLIDER_FILE = "images/RadioScreenAssets/frequencySlider.png";
	private static final String RADIO_POINTER_FILE = "images/RadioScreenAssets/frequencyMarker.png";
	private static final String RADIO_SOUNDON_FILE = "images/RadioScreenAssets/soundIcon.png";
	private static final String RADIO_SOUNDOFF_FILE = "images/RadioScreenAssets/muteSoundIcon.png";
	private static final String RADIO_NEDLIKE_FILE = "images/RadioScreenAssets/nedLike.png";
	private static final String RADIO_NEDDISLIKE_FILE = "images/RadioScreenAssets/nedDislike.png";
	private static final String RADIO_NOSHLIKE_FILE = "images/RadioScreenAssets/noshLike.png";
	private static final String RADIO_NOSHDISLIKE_FILE = "images/RadioScreenAssets/noshDislike.png";
	/** The texture files for Nosh's moods */
	private static final String NOSH_FILE = "images/nosh.png";
	/** Ned texture file */
	private static final String NED_FILE = "images/ned.png";
	/** The texture files for the visor states */
    private static final String VISOR_OPEN_FILE = "images/visor_open.png";
    private static final String VISOR_CLOSED_FILE = "images/visor_closed.png";
	/** The texture file for the road */
	private static final String ROAD_FILE = "images/road.png";
	/** The texture file for the grass */
	private static final String GRASS_FILE = "images/grass.png";
	/** The texture file for the exit */
	private static final String EXIT_FILE = "images/exit.png";
	/** The texture file for the dash */
	private static final String DASH_FILE = "images/DashHUD/Dash.png";
	/** The file for the health gauge */
	private static final String HEALTH_GAUGE_FILE = "images/DashHUD/gauge.png";
	/** The file for the health gauge pointer */
	private static final String HEALTH_POINTER_FILE = "images/DashHUD/pointer.png";
	/** Rearview mirror stuff */
	private static final String REARVIEW_BACKGROUND = "images/rearview_background.png";
	private static final String REARVIEW_COVER = "images/rearview_cover.png";
	private static final String REARVIEW_DAMAGE = "images/rearview_damage.png";
	private static final String REARVIEW_SEATS = "images/rearview_seats.png";
	/** The texture files for all Items **/
	private static final String DVD0_FILE = "images/Items/Gnome Country for Old Men.png";
	private static final String SNACK0_FILE = "images/Items/mango.png";
	/** Sun effect that will be overlayed */
	private static final String SUN_FILE = "images/sun_1.jpg";
	private static final String SUN2_FILE = "images/sun_2.jpg";
	private static final String SUN3_FILE = "images/sun_3.png";
	private static final String WHITE_FILE = "images/white.png";
	/** The file for the angry speech bubble */
	private static final String SPEECH_BUBBLE_FILE = "images/speechbubble.png";
	/** SAT Questions */
	private static final String SAT_BUBBLE_FILE = "SatQuestions/satbubble.png";
	private static final String SAT_ARMADILLO_FILE = "SatQuestions/armadillo.png";
	private static final String SAT_WHALE_FILE = "SatQuestions/whale.png";
	private static final String SAT_LEMONMAN_FILE = "SatQuestions/lemonMan.png";
	private static final String SAT_AUSTRALIA_FILE = "SatQuestions/australia.png";
    private static final String SAT_HOTDOGS_FILE = "SatQuestions/hotdogs.jpg";
    private static final String SAT_HOTLEGS_FILE = "SatQuestions/hotlegs.jpg";
    private static final String SAT_JAPAN_FILE = "SatQuestions/japan.png";
    private static final String SAT_JUPITER_FILE = "SatQuestions/jupiter.png";
    private static final String SAT_NEPTUNE_FILE = "SatQuestions/neptune.png";
	/** Touchscreen */
	private static final String ON_TOUCHSCREEN_FILE = "images/DashHUD/ontouchscreen.png";
	private static final String OFF_TOUCHSCREEN_FILE = "images/DashHUD/offtouchscreen.png";
	private static final String DVD_SLOT_FILE = "images/DashHUD/dvdslot.png";
	/** Billboards */
	private static final String BILLBOARD_END_IS_NEAR_FILE = "images/billboards/the_end_is_near.png";
	private static final String BILLBOARD_GRILL_FILE = "images/billboards/aGrillYouCanTrust.png";
	private static final String BILLBOARD_FLAMINGO_FILE = "images/billboards/flamingoSale.png";
	private static final String BILLBOARD_WHERE_WILL_YOU_BE_FILE = "images/billboards/whereWillYouBe.png";
	private static final String EXIT_SIGN_FILE = "images/billboards/restStopSign.png";
	private static final String SUNFLOWER_FILE = "images/billboards/midwest_sunflower.png";
	private static final String TREE_FILE = "images/billboards/mountains_tree.png";
	private static final String TOPIARY_FILE = "images/billboards/suburb_topiary.png";
	/** Speed signs */
	private static final String SPEEDLIMIT_25_FILE = "images/billboards/speedLimit25.png";
	private static final String SPEEDLIMIT_55_FILE = "images/billboards/speedLimit55.png";
	private static final String SPEEDLIMIT_80_FILE = "images/billboards/speedLimit80.png";
	/** Billboard font */
	private static String BILLBOARD_FONT_FILE = "fonts/Roadgeek 2005 Series E.ttf";
	/** The font file to use for scores */
	private static String FONT_FILE = "fonts/ComicSans.ttf";
	/** The Horn! */
	private static final String HORN_FILE = "images/DashHUD/Horn.png";

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
	protected  Texture gnomeTexture;
	protected  Texture rearviewGnomeTexture;
	/** Texture for the flamingo */
	protected Texture flamingoTexture;
	/** Texture for the flamingo */
	protected Texture grillTexture;
	/** Texture for the radio knob */
	private Texture radioknobTexture;
	private Texture radioSlider;
	private Texture radioPointer;
	private Texture radioSoundOn;
	private Texture radioSoundOff;
	private Texture radioNedLike;
	private Texture radioNedDislike;
	private Texture radioNoshLike;
	private Texture radioNoshDislike;
	/** Texture for nosh (this is a film strip  */
	private Texture noshTexture;
	/** Texture for ned (this is a film strip) */
	private Texture nedTexture;
	/** Textures for items **/
	private Texture dvdTexture;
	private Texture snackTexture;

	/** Texture for visor */
	private Texture visorTexture;

	/** Texture of the dash **/
	private Texture dashTexture;
	/** Texture of the health gauge */
	private Texture healthGaugeTexture;
	/** Texture of the health gauge's pointer */
	private Texture healthPointerTexture;
	/** Texture of the rear view mirror */
	private Texture rearviewBackgroundTexture;
	private Texture rearviewSeatsTexture;
	private Texture rearviewCoverTexture;
	/** Texture of sun effect */
	private Texture sun;
	private Texture sun2;
	private Texture sun3;
	private Texture white;
	/** Texture of the angry speech bubble */
	private Texture speechBubble;
	/** SAT Questions */
	private Texture satBubble;
	private Texture satArmadillo;
	private Texture satWhale;
	private Texture satLemonMan;
    private Texture satAustralia;
    private Texture satHotDogs;
    private Texture satHotLegs;
    private Texture satJapan;
    private Texture satJupiter;
    private Texture satNeptune;
	private HashMap<String, Texture> satTextures;
	/** Touchscreen */
	private Texture onTouchscreen;
	private Texture offTouchscreen;
	private Texture dvdSlot;
	/** Horn texture */
	private Texture hornTexture;
	/** Flame texture */
	private Texture flameTexture;

	/** Rearview damage texture */
	private Texture rearviewDamageTexture;

	/** Cached color object for indicating rearview damage */
	private Color rearviewDamageColor = new Color(Color.RED);

	/** The font for giving messages to the player */
	protected BitmapFont displayFont;

	// List of objects with the garbage collection set.
	/** The backing set for garbage collection */
	private Array<Enemy> backing;

	/** Billboard textures */
	private Texture billboardEndIsNearTex;
	private Texture billboardGrillTex;
	private Texture billboardFlamingoTex;
	private Texture billboardWhereWillYouBeTex;
	private Texture exitSignTex;
	private BitmapFont billboardFont;
	/** Speed sign textures */
	private Texture speedLimit25Tex;
	private Texture speedLimit55Tex;
	private Texture speedLimit80Tex;
	/** Other roadside assets */
	private Texture sunflowerTex;
	private Texture topiaryTex;
	private Texture treeTex;

	// BILLBOARD NAMING CONSTANTS
	private static final String BILLBOARD_END_IS_NEAR = "the end is near";
	private static final String BILLBOARD_GRILL = "a grill you can trust";
	private static final String BILLBOARD_FLAMINGO = "flamingo sale";
	private static final String BILLBOARD_WHERE_WILL_YOU_BE = "where will you be";
	private static final String EXIT_SIGN = "exit sign";
	private static final String SUNFLOWER = "sunflower";
	private static final String TREE = "tree";
	private static final String TOPIARY = "topiary";

	/** An object map between roadside image names and their textures */
	private ObjectMap<String, Texture> roadsideTexs;
	/** Array of roadside objects that will appear in the game */
	private Array<RoadImage> roadsideObjs;
	/** Used for reset */
	private Array<RoadImage> roadsideObjsSave;

	// INVENTORY CONSTANTS
	private static final float INV_X_LEFT = 0.4756f;
	private static final float INV_Y_BOTTOM = 0.0366f;
	private static final float INV_SLOT_WIDTH = 0.146f;
	private static final float INV_SLOT_HEIGHT = 0.15f;
	private static final int INV_RELSCA = 0;
	private static final int INV_CB = 0;
	private static final int INV_NUM_SLOTS = 2;

	/** Enum specifying the region this level takes place in. */
	public enum Region {
		SUBURBS, HIGHWAY, MIDWEST, COLORADO;
	}

	public Image getMasterShaker() { return masterShaker; }

	public Array<RoadImage> getRoadsideObjs() { return roadsideObjs; }

	public ObjectMap<String, Texture> getRoadsideTexs() { return roadsideTexs; }

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
		manager.load(REARVIEW_DAMAGE,Texture.class);
		assets.add(REARVIEW_DAMAGE);
		manager.load(NED_FILE,Texture.class);
		assets.add(NED_FILE);
		manager.load(NOSH_FILE,Texture.class);
		assets.add(NOSH_FILE);
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
		manager.load(RADIO_SLIDER_FILE, Texture.class);
		assets.add(RADIO_SLIDER_FILE);
		manager.load(RADIO_POINTER_FILE, Texture.class);
		assets.add(RADIO_POINTER_FILE);
		manager.load(RADIO_SOUNDON_FILE, Texture.class);
		assets.add(RADIO_SOUNDON_FILE);
		manager.load(RADIO_SOUNDOFF_FILE, Texture.class);
		assets.add(RADIO_SOUNDOFF_FILE);
		manager.load(RADIO_NEDLIKE_FILE, Texture.class);
		assets.add(RADIO_NEDLIKE_FILE);
		manager.load(RADIO_NEDDISLIKE_FILE, Texture.class);
		assets.add(RADIO_NEDDISLIKE_FILE);
		manager.load(RADIO_NOSHLIKE_FILE, Texture.class);
		assets.add(RADIO_NOSHLIKE_FILE);
		manager.load(RADIO_NOSHDISLIKE_FILE, Texture.class);
		assets.add(RADIO_NEDDISLIKE_FILE);
		manager.load(DVD0_FILE,Texture.class);
		assets.add(DVD0_FILE);
		manager.load(SNACK0_FILE,Texture.class);
		assets.add(SNACK0_FILE);
		manager.load(GRASS_FILE, Texture.class);
		assets.add(GRASS_FILE);
		manager.load(ROAD_FILE, Texture.class);
		assets.add(ROAD_FILE);
		manager.load(EXIT_FILE, Texture.class);
		assets.add(EXIT_FILE);
		manager.load(DASH_FILE,Texture.class);
		assets.add(DASH_FILE);
		manager.load(HEALTH_GAUGE_FILE, Texture.class);
		assets.add(HEALTH_GAUGE_FILE);
		manager.load(HEALTH_POINTER_FILE, Texture.class);
		assets.add(HEALTH_POINTER_FILE);
		manager.load(REARVIEW_BACKGROUND, Texture.class);
		assets.add(REARVIEW_BACKGROUND);
		manager.load(REARVIEW_COVER, Texture.class);
		assets.add(REARVIEW_COVER);
		manager.load(REARVIEW_SEATS, Texture.class);
		assets.add(REARVIEW_SEATS);
		manager.load(VISOR_FILE, Texture.class);
		assets.add(VISOR_FILE);
        manager.load(VISOR_OPEN_FILE, Texture.class);
        assets.add(VISOR_OPEN_FILE);
        manager.load(VISOR_CLOSED_FILE, Texture.class);
		manager.load(SUN_FILE, Texture.class);
		assets.add(SUN_FILE);
		manager.load(SUN2_FILE, Texture.class);
		assets.add(SUN2_FILE);
		manager.load(SUN3_FILE, Texture.class);
		assets.add(SUN3_FILE);
		manager.load(WHITE_FILE, Texture.class);
		assets.add(WHITE_FILE);
		manager.load(SPEECH_BUBBLE_FILE, Texture.class);
		assets.add(SPEECH_BUBBLE_FILE);
		manager.load(SAT_BUBBLE_FILE, Texture.class);
		assets.add(SAT_BUBBLE_FILE);
		manager.load(SAT_ARMADILLO_FILE, Texture.class);
		assets.add(SAT_ARMADILLO_FILE);
		manager.load(SAT_WHALE_FILE, Texture.class);
		assets.add(SAT_WHALE_FILE);
		manager.load(SAT_LEMONMAN_FILE, Texture.class);
		assets.add(SAT_LEMONMAN_FILE);
        manager.load(SAT_AUSTRALIA_FILE, Texture.class);
        assets.add(SAT_AUSTRALIA_FILE);
        manager.load(SAT_HOTDOGS_FILE, Texture.class);
        assets.add(SAT_HOTDOGS_FILE);
        manager.load(SAT_HOTLEGS_FILE, Texture.class);
        assets.add(SAT_HOTLEGS_FILE);
        manager.load(SAT_JAPAN_FILE, Texture.class);
        assets.add(SAT_JAPAN_FILE);
        manager.load(SAT_JUPITER_FILE, Texture.class);
        assets.add(SAT_JUPITER_FILE);
        manager.load(SAT_NEPTUNE_FILE, Texture.class);
        assets.add(SAT_NEPTUNE_FILE);
		manager.load(FLAMINGO_FILE, Texture.class);
		assets.add(FLAMINGO_FILE);
		manager.load(ON_TOUCHSCREEN_FILE, Texture.class);
		assets.add(ON_TOUCHSCREEN_FILE);
		manager.load(OFF_TOUCHSCREEN_FILE, Texture.class);
		assets.add(OFF_TOUCHSCREEN_FILE);
		manager.load(DVD_SLOT_FILE, Texture.class);
		assets.add(DVD_SLOT_FILE);
		manager.load(HORN_FILE, Texture.class);
		assets.add(HORN_FILE);
		manager.load(GRILL_FILE, Texture.class);
		assets.add(GRILL_FILE);
		manager.load(FLAME_FILE, Texture.class);
		assets.add(FLAME_FILE);
		manager.load(BILLBOARD_END_IS_NEAR_FILE, Texture.class);
		assets.add(BILLBOARD_END_IS_NEAR_FILE);
		manager.load(BILLBOARD_GRILL_FILE, Texture.class);
		assets.add(BILLBOARD_GRILL_FILE);
		manager.load(BILLBOARD_FLAMINGO_FILE, Texture.class);
		assets.add(BILLBOARD_FLAMINGO_FILE);
		manager.load(BILLBOARD_WHERE_WILL_YOU_BE_FILE, Texture.class);
		assets.add(BILLBOARD_WHERE_WILL_YOU_BE_FILE);
		manager.load(SPEEDLIMIT_25_FILE, Texture.class);
		assets.add(SPEEDLIMIT_25_FILE);
		manager.load(SPEEDLIMIT_55_FILE, Texture.class);
		assets.add(SPEEDLIMIT_55_FILE);
		manager.load(SPEEDLIMIT_80_FILE, Texture.class);
		assets.add(SPEEDLIMIT_80_FILE);
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
		flamingoTexture = createTexture(manager, FLAMINGO_FILE);
		rearviewGnomeTexture = createTexture(manager, REARVIEW_GNOME_FILE);
		radioknobTexture = createTexture(manager,RADIO_KNOB_FILE);
		dvdSlot = createTexture(manager, DVD_SLOT_FILE);
		radioSlider = createTexture(manager, RADIO_SLIDER_FILE);
		radioPointer = createTexture(manager, RADIO_POINTER_FILE);
		radioSoundOn = createTexture(manager, RADIO_SOUNDON_FILE);
		radioSoundOff = createTexture(manager, RADIO_SOUNDOFF_FILE);
		radioNedLike = createTexture(manager, RADIO_NEDLIKE_FILE);
		radioNedDislike = createTexture(manager, RADIO_NEDDISLIKE_FILE);
		radioNoshLike = createTexture(manager, RADIO_NOSHLIKE_FILE);
		radioNoshDislike = createTexture(manager, RADIO_NOSHDISLIKE_FILE);
		nedTexture = createTexture(manager, NED_FILE);
		noshTexture = createTexture(manager, NOSH_FILE);
		roadTexture = createTexture(manager, ROAD_FILE);
		grassTexture = createTexture(manager, GRASS_FILE);
		exitTexture = createTexture(manager, EXIT_FILE);
		dashTexture = createTexture(manager, DASH_FILE);
		healthGaugeTexture = createTexture(manager, HEALTH_GAUGE_FILE);
		healthPointerTexture = createTexture(manager, HEALTH_POINTER_FILE);
		rearviewBackgroundTexture = createTexture(manager, REARVIEW_BACKGROUND);
		rearviewSeatsTexture = createTexture(manager, REARVIEW_SEATS);
		rearviewCoverTexture = createTexture(manager, REARVIEW_COVER);
        visorTexture = createTexture(manager, VISOR_FILE);
        sun = createTexture(manager, SUN_FILE);
		sun2 = createTexture(manager, SUN2_FILE);
		sun3 = createTexture(manager, SUN3_FILE);
		white = createTexture(manager, WHITE_FILE);
		speechBubble = createTexture(manager, SPEECH_BUBBLE_FILE);
		yonda.addSpeechBubbleTexture(speechBubble);
		if (manager.isLoaded(FONT_FILE)) {
			displayFont = manager.get(FONT_FILE,BitmapFont.class);
		} else {
			displayFont = null;
		}
		satBubble = createTexture(manager, SAT_BUBBLE_FILE);
		satArmadillo = createTexture(manager, SAT_ARMADILLO_FILE);
		satTextures.put(SAT_ARMADILLO_FILE, satArmadillo);
		satWhale = createTexture(manager, SAT_WHALE_FILE);
		satTextures.put(SAT_WHALE_FILE, satWhale);
		satLemonMan = createTexture(manager, SAT_LEMONMAN_FILE);
		satTextures.put(SAT_LEMONMAN_FILE, satLemonMan);
        satAustralia = createTexture(manager, SAT_AUSTRALIA_FILE);
        satTextures.put(SAT_AUSTRALIA_FILE, satAustralia);
        satHotDogs = createTexture(manager, SAT_HOTDOGS_FILE);
        satTextures.put(SAT_HOTDOGS_FILE, satHotDogs);
        satHotLegs = createTexture(manager, SAT_HOTLEGS_FILE);
        satTextures.put(SAT_HOTLEGS_FILE, satHotLegs);
        satJapan = createTexture(manager, SAT_JAPAN_FILE);
        satTextures.put(SAT_JAPAN_FILE, satJapan);
        satJupiter = createTexture(manager, SAT_JUPITER_FILE);
        satTextures.put(SAT_JUPITER_FILE, satJupiter);
        satNeptune = createTexture(manager, SAT_NEPTUNE_FILE);
        satTextures.put(SAT_NEPTUNE_FILE, satNeptune);
		satQuestions = new SATQuestions(satTextures, satBubble);
		onTouchscreen = createTexture(manager, ON_TOUCHSCREEN_FILE);
		offTouchscreen = createTexture(manager, OFF_TOUCHSCREEN_FILE);
		hornTexture = createTexture(manager, HORN_FILE);
		grillTexture = createTexture(manager, GRILL_FILE);
		flameTexture = createTexture(manager, FLAME_FILE);
		rearviewDamageTexture = createTexture(manager, REARVIEW_DAMAGE);

		billboardEndIsNearTex = createTexture(manager, BILLBOARD_END_IS_NEAR_FILE);
		billboardGrillTex = createTexture(manager, BILLBOARD_GRILL_FILE);
		billboardFlamingoTex = createTexture(manager, BILLBOARD_FLAMINGO_FILE);
		billboardWhereWillYouBeTex = createTexture(manager, BILLBOARD_WHERE_WILL_YOU_BE_FILE);
		exitSignTex = createTexture(manager, EXIT_SIGN_FILE);
		speedLimit25Tex = createTexture(manager, SPEEDLIMIT_25_FILE);
		speedLimit55Tex = createTexture(manager, SPEEDLIMIT_55_FILE);
		speedLimit80Tex = createTexture(manager, SPEEDLIMIT_80_FILE);
		if (manager.isLoaded(BILLBOARD_FONT_FILE)) {
			billboardFont = manager.get(BILLBOARD_FONT_FILE,BitmapFont.class);
		} else {
			billboardFont = null;
		}
	}

	protected Texture createTexture(AssetManager manager, String file) {
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
	 */
	public GameplayController(GameCanvas canvas, float endY,
							  Array<Enemy> enemies,
							  Array<Event> e,
							  ObjectMap<String,Radio.Genre> s,
							  SoundController sc,
							  Array<RoadImage> roadsideObjs) {
		soundController = sc;
		songs = s;
		enemiez = enemies;
        radio = new Radio(radioknobTexture, radioSlider, radioPointer, radioSoundOn,
							radioSoundOff, radioNedLike, radioNedDislike, radioNoshLike,
							radioNoshDislike, songs);
        enemiezSave = new Array<Enemy>();
		for(Enemy enemy : enemies) {
            enemiezSave.add(new Enemy(enemy));
        }
		yonda = new Car();
		backing = new Array<Enemy>();
		road = new Road(endY);
		ypos = 0.0f;
		nextEvent = 0;
		sunShine = false;
		events = e;

		// Initialize the inventory TODO REMOVE STARTING INV STUFF
		// Item textures
		dvdTexture = new Texture(DVD0_FILE);
		snackTexture = new Texture(SNACK0_FILE);

		Image.updateScreenDimensions(canvas);
		// create the default inventory
		Inventory.Item.setTexturesAndScales(dvdTexture,0.12f,snackTexture,0.135f);
		inventory = new Inventory(INV_X_LEFT,INV_Y_BOTTOM,INV_RELSCA,INV_CB,wheelTexture,
					 INV_SLOT_WIDTH, INV_SLOT_HEIGHT, INV_NUM_SLOTS);
		Array<Inventory.Slot> i = new Array<Inventory.Slot>();
		i.add(new Inventory.Slot(i,inventory, Inventory.Item.ItemType.DVD,1));
		i.add(new Inventory.Slot(i,inventory, Inventory.Item.ItemType.SNACK,3));
		inventory.load(i);

		// create a copy of the default inventory
		initialInventory = new Inventory(INV_X_LEFT,INV_Y_BOTTOM,INV_RELSCA,INV_CB,wheelTexture,
							INV_SLOT_WIDTH, INV_SLOT_HEIGHT, INV_NUM_SLOTS);
		Array<Inventory.Slot> iCopy = new Array<Inventory.Slot>();
		iCopy.add(new Inventory.Slot(iCopy,initialInventory, Inventory.Item.ItemType.DVD,1));
		iCopy.add(new Inventory.Slot(iCopy,initialInventory, Inventory.Item.ItemType.SNACK,3));
		initialInventory.load(iCopy);

		satTextures = new HashMap<String, Texture>();

		roadsideTexs = new ObjectMap<String, Texture>();
		loadRoadsideTexs();

		// load roadside objects and create a save
		this.roadsideObjs = roadsideObjs;
		roadsideObjsSave = new Array<RoadImage>();
		for(RoadImage img : roadsideObjs) {
			roadsideObjsSave.add(new RoadImage(img));
		}
	}

	/**
	 * Creates the object mapping between roadside image names and their
	 * corresponding textures.
	 */
	private void loadRoadsideTexs() {
		roadsideTexs.put(BILLBOARD_END_IS_NEAR, new Texture(BILLBOARD_END_IS_NEAR_FILE));
		roadsideTexs.put(BILLBOARD_GRILL, new Texture(BILLBOARD_GRILL_FILE));
		roadsideTexs.put(BILLBOARD_FLAMINGO, new Texture(BILLBOARD_FLAMINGO_FILE));
		roadsideTexs.put(BILLBOARD_WHERE_WILL_YOU_BE, new Texture(BILLBOARD_WHERE_WILL_YOU_BE_FILE));
		roadsideTexs.put(EXIT_SIGN, new Texture(EXIT_SIGN_FILE));
		roadsideTexs.put(SUNFLOWER, new Texture(SUNFLOWER_FILE));
		roadsideTexs.put(TREE, new Texture(TREE_FILE));
		roadsideTexs.put(TOPIARY, new Texture(TOPIARY_FILE));
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
	public Array<Enemy> getEnemiez() { return enemiez; }

	/**
	 * Returns a reference to the car.
	 */
	public Car getCar() { return yonda; }

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

	public Image getHealthGauge() { return healthGauge; }
	public Image getHealthGaugePointer() { return healthGaugePointer; }
	public Image getRearviewBackground() { return rearviewBackground; }
	public Image getRearviewSeats() { return rearviewSeats; }
	public Image getRearviewCover() { return rearviewCover; }

	/**
	 * Returns a reference to the radio
	 */
    public Radio getRadio(){ return radio; }

    public TouchScreen getTouchscreen() { return touchscreen; }

	/**
	 * Returns a reference to the inventory
	 */
	public Inventory getInventory(){ return inventory; }

	/** Set the inventory and creates a copy of it */
	public void setInventory(Inventory i) {
		inventory = i;

		// create a copy
		initialInventory = new Inventory(INV_X_LEFT,INV_Y_BOTTOM,INV_RELSCA,INV_CB,wheelTexture,
							INV_SLOT_WIDTH, INV_SLOT_HEIGHT, INV_NUM_SLOTS);
		Array<Inventory.Slot> iCopy = new Array<Inventory.Slot>();
		iCopy.add(new Inventory.Slot(iCopy,initialInventory, Inventory.Item.ItemType.DVD,inventory.getNumMovies()));
		iCopy.add(new Inventory.Slot(iCopy,initialInventory, Inventory.Item.ItemType.SNACK,inventory.getNumSnacks()));
		initialInventory.load(iCopy);

	}

    /**
     * Returns a reference to the visor
     */
    public Visor getVisor(){ return visor; }

	/**
	 * Starts a new game.
	 *
	 * This method creates a single player, but does nothing else.
	 *
	 * @param x Starting x-position for the player
	 * @param y Starting y-position for the player
	 */
	public void start(float x, float y) {
		soundController.startAmbience();
		radio = new Radio(radioknobTexture, radioSlider, radioPointer, radioSoundOn,
				radioSoundOff, radioNedLike, radioNedDislike, radioNoshLike,
				radioNoshDislike, songs);
		dvdPlayer = new DvdPlayer();
		touchscreen = new TouchScreen(radio, dvdPlayer, onTouchscreen, offTouchscreen, dvdSlot);
		masterShaker = new Image();
        sunShine = false;
		yonda.getNosh().setChildFilmStrip(noshTexture, NOSH_FILMSTRIP_ROWS, NOSH_FILMSTRIP_COLS);
		yonda.getNed().setChildFilmStrip(nedTexture, NED_FILMSTRIP_ROWS, NED_FILMSTRIP_COLS);
		yonda.setDashTexture(dashTexture);
		getCar().setGaugeTexture(healthGaugeTexture);
		getCar().setGaugePointerTexture(healthPointerTexture);

		horn = new Horn(0.17f, 0.1845f, 0.17f, 20, hornTexture);

		healthGauge = new Image(0.34f, 0.05f, 0.175f, healthGaugeTexture);
		healthGaugePointer = new Image(0.39f, 0.08f, 0.09f, healthPointerTexture);

		rearviewBackground = new Image(0.78f, 0.86f, 0.257f, rearviewBackgroundTexture, GameCanvas.TextureOrigin.MIDDLE);
		rearviewSeats = new Image(0.78f, 0.86f, 0.3f, rearviewSeatsTexture,GameCanvas.TextureOrigin.MIDDLE);
		rearviewCover = new Image(0.78f, 0.86f, 0.3f, rearviewCoverTexture,GameCanvas.TextureOrigin.MIDDLE);
        rearviewEnemy = new RearviewEnemy(0.78f, 0.8f, 0.18f,0, rearviewGnomeTexture);
		rearviewDamageIndicator = new Image(0.78f, 0.86f, 0.3f, rearviewDamageTexture,GameCanvas.TextureOrigin.MIDDLE);

		// TODO CHANGE THIS LOL
		for (Enemy e : enemiez) {
            if (e.getType() == RoadObject.ObjectType.GNOME) {
            	e.setFilmStrip(gnomeTexture, GNOME_FILMSTRIP_ROWS, GNOME_FILMSTRIP_COLS);
			}
			if (e.getType() == RoadObject.ObjectType.FLAMINGO) {
            	e.setFilmStrip(flamingoTexture, FLAMINGO_FILMSTRIP_ROWS, FLAMINGO_FILMSTRIP_COLS);
            	Flamingo f = (Flamingo) e;
				f.setAnimationBounds(FLAMINGO_STAND_START, FLAMINGO_STAND_END);
				f.setEnemyWidth(0.2f);
				f.setEnemyHeight(0.1f);
			}
			if (e.getType() == RoadObject.ObjectType.GRILL) {
            	e.setFilmStrip(grillTexture, GRILL_FILMSTRIP_ROWS, GRILL_FILMSTRIP_COLS);
				((Grill) e).setFireTexture(flameTexture);
				System.out.println(((Grill) e).getFlames().size);
				enemiez.addAll(((Grill) e).getFlames());
				enemiezSave.addAll(((Grill) e).getFlames());
			}
		}

		wheel = new Wheel(0.17f,0.19f, 0.5f, 60, wheelTexture);
		vroomStick = new VroomStick(0.19f, 0.19f,0.26f, 50, vroomStickTexture);
		visor = new Visor(visorTexture, sun, sun2, sun3, white);
		yonda.setVisor(visor);

		road.setRoadTexture(roadTexture);
		road.setGrassTexture(grassTexture);
		road.setExitTexture(exitTexture);
  }

	/**
	 * Resets the game, deleting all objects.
	 */
	public void reset() {
		road.reset();
		yonda.reset();
		wheel = null;
		radio = null;
		enemiez = new Array<Enemy>();
        for(Enemy enemy : enemiezSave) {

        	switch(enemy.getType()) {
				case GRILL:
					enemiez.add(new Grill(enemy));
					break;
				case GNOME:
					enemiez.add(new Gnome(enemy));
					break;
				case FLAMINGO:
					enemiez.add(new Flamingo(enemy));
					break;
				case FLAME:
					Flame newFlame = new Flame(enemy);
					newFlame.setFilmStrip(flameTexture, 1, 1);
					enemiez.add(newFlame);
				default:
					break;
			}
        }
        roadsideObjs = new Array<RoadImage>();
		for(RoadImage image : roadsideObjsSave) {
			roadsideObjs.add(new RoadImage(image));
		}
		backing.clear();
		ypos = 0.0f;
		nextEvent = 0;
		satQuestions.reset();

		// reset inventory
		inventory = initialInventory;
		// create a copy
		initialInventory = new Inventory(INV_X_LEFT,INV_Y_BOTTOM,INV_RELSCA,INV_CB,wheelTexture,
				INV_SLOT_WIDTH, INV_SLOT_HEIGHT, INV_NUM_SLOTS);
		Array<Inventory.Slot> iCopy = new Array<Inventory.Slot>();
		iCopy.add(new Inventory.Slot(iCopy,initialInventory, Inventory.Item.ItemType.DVD,inventory.getNumMovies()));
		iCopy.add(new Inventory.Slot(iCopy,initialInventory, Inventory.Item.ItemType.SNACK,inventory.getNumSnacks()));
		initialInventory.load(iCopy);
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
		for (Enemy e : enemiez) {
			if (e.isDestroyed()) {
				destroy(e);
			} else {
				backing.add(e);
			}
		}

		// Swap the backing store and the objects.
		// This is essentially stop-and-copy garbage collection
		Array<Enemy> tmp = backing;
		backing = enemiez;
		enemiez = tmp;
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
		if (nextEvent < events.size && events.size != 0) {
			float dy = road.getSpeed() * delta; // change in y position
			ypos += dy; // add it to y position tracker
			Event first = events.get(nextEvent);

			// give some leeway in position for events to occur
			if (Math.abs(first.getY() - ypos) < 0.1f) {
				switch (first.getType()) {
					case REAR_ENEMY:
						rearviewEnemy.create();
						break;
					case SUN_START:
						sunShine = true;
						break;
					case SUN_END:
						sunShine = false;
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
						satQuestions.askQuestion();
						break;
					default:
						break;
				}
				nextEvent += 1;
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

		// Update world objects (road and gnome positions)
        road.update(delta);
        for (Enemy e : enemiez) {
            e.update(delta, road.getSpeed());

            /* FIXME: idk if its best to have this here */
            if (horn.isHonking()) {
            	if (e.getType() == RoadObject.ObjectType.FLAMINGO) {
            		Flamingo f = (Flamingo) e;
            		if (e.getY() < f.getFlyAwayDistance()) {
						if(!f.isFlyingAway()) soundController.flamingoFlapSound();
            		    f.setAnimationBounds(FLAMINGO_FLY_START, FLAMINGO_FLY_END);
						f.setFlyingAway();
					}
				}
			}
        }

        for (RoadImage img : roadsideObjs) {
        	img.update(delta, road.getSpeed());
		}

        // Update the HUD
        Vector2 in = input.getClickPos();
        Vector2 dr = new Vector2(input.getDX(), input.getDY());
  		boolean mousePressed = input.isMousePressed();
  		satQuestions.update(in, input.getNumKeyPressed(), yonda.getNed());
  		horn.updateHonk(delta);
  		visor.update(delta);
        if(in != null) {
            visor.resolveInput(new Vector2(in), input.isPrevMousePressed());
			wheel.update(new Vector2(in), dr.x, input.isTurnPressed());
			vroomStick.update(new Vector2(in), dr.y);
			touchscreen.update(new Vector2(in), dr.x);
			if(horn.update(new Vector2(in), delta)) { soundController.beepSound(); }
			inventory.update(new Vector2(in), mousePressed);
		}
		else{
			wheel.update(null, dr.x, input.isTurnPressed());
			vroomStick.update(null, dr.y);
			touchscreen.update(null, dr.x);
			inventory.update(null, mousePressed);
		}
		resolveItemDrop(input);
		rearviewEnemy.update(delta*0.0004f);

		if (vroomStick.isEngaged()) {
			rearviewEnemy.destroyIfAlive();
			road.setVrooming();
		}

		if (rearviewEnemy.isAttackingCar()) {
			getCar().damageWithReargnome();
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

		masterShaker.updateShake(delta);

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

		if(dvdPlayer.isPlayingDvd()) {
			ned.setMood(Child.Mood.HAPPY);
			nosh.setMood(Child.Mood.HAPPY);
		}
		if (r.getCurrentStation() != null && r.isSoundOn() && r.getknobAng() <= 0 && counter%200 == 0) {

		    if(r.getCurrentStationGenre() != Radio.Genre.CLASSICAL) {
		        if(!ned.isAwake()) ned.setMood(Child.Mood.SAD);
                if(!nosh.isAwake()) nosh.setMood(Child.Mood.SAD);
            }

			// TODO : ADD CASES FOR OTHER GENRES
			switch (r.getCurrentStationGenre()){
                case CLASSICAL: // ned dislikes, nosh likes
                    if(ned.getCurrentMood() == Child.Mood.HAPPY){
                        ned.setAsleep();
                    }
                    if(nosh.getCurrentMood() == Child.Mood.HAPPY){
                        nosh.setAsleep();
                    }
                    break;
				case COMEDY: // nosh likes
					if(nosh.isAwake()){
                        nosh.setMoodShifting(true, true);
					}
					break;
				case ACTION: //nosh likes
					if(nosh.isAwake()){
                        nosh.setMoodShifting(true, true);
					}
					break;
				case POP: // ned likes
					if(ned.isAwake()){
						ned.setMoodShifting(true, true);
					}
					break;
                case THUG: //ned likes
                    if(ned.isAwake()) {
                        ned.setMoodShifting(true, true);
                    }
                    break;
				default: //jazz and creepy are neutral
					break;
			}
		}

	}

	Vector2 droppedPos;
	public void resolveItemDrop(InputController inputController) {
//		System.out.println("prevClick: "+inputController.isPrevMousePressed());
//		System.out.println("currClick: "+inputController.isMousePressed());

		if (inventory.getItemInHand() != null && inputController.isPrevMousePressed() && !inputController.isMousePressed()) {
			switch (inventory.getItemInHand().getItemType()) {
				case SNACK:
					if(yonda.getNosh().inChildArea(droppedPos) && yonda.getNosh().isAwake() && yonda.getNosh().getCurrentMood() != Child.Mood.HAPPY) {
						yonda.getNosh().setMood(Child.Mood.HAPPY);
					} else if(yonda.getNed().inChildArea(droppedPos) && yonda.getNed().isAwake() && yonda.getNed().getCurrentMood() != Child.Mood.HAPPY) {
						yonda.getNed().setMood(Child.Mood.HAPPY);
					} else {
						inventory.cancelTake();
						return;
					}
					break;
				case DVD:
					if(touchscreen.inDvdSlot(droppedPos)) {
						if(!dvdPlayer.playDvd("Gnome Country for Old Men", 1000)) {
							inventory.cancelTake();
						}
					} else {
						inventory.cancelTake();
						return;
					}
					break;
			}
			inventory.setItemInHand(null);
		}
		droppedPos = inputController.getClickPos();
	}

	public void draw(GameCanvas canvas) {

	    canvas.setCameraFOV(road.getSpeedRatio());

		//Gnomez
		for (Enemy e : enemiez) {
			e.draw(canvas);
		}

		for (RoadImage i : roadsideObjs) {
			i.draw(canvas, roadsideTexs.get(i.getName()));
		}

		//Draw sun effect part 1
		visor.drawSunA(canvas, sunShine);

		///**  Draw Dash and Interactive HUD Elements **///
		yonda.drawDash(canvas);

        // Health gauge and pointer
        Color healthGaugeColor = Color.WHITE;
        if (yonda.getIsDamaged()) {
            healthGaugeColor = Color.RED;
        }
        healthGauge.draw(canvas, healthGaugeColor);
        healthGaugePointer.draw(canvas, -yonda.getHealthPointerAng());

		// Vroom Stick
		vroomStick.draw(canvas);

		// Wheel
		wheel.draw(canvas);

		// Touchscreen
		touchscreen.draw(canvas, displayFont);

		// Horn
		horn.draw(canvas, wheel.getAng());

		rearviewBackground.draw(canvas);
		rearviewEnemy.draw(canvas);
		rearviewSeats.draw(canvas);

		// Decide on damage indicator information
		float damageAlpha = getCar().getDamageDisplayAlpha();
		rearviewDamageColor.set(Color.RED.r, Color.RED.g, Color.RED.b, damageAlpha);
		rearviewDamageIndicator.draw(canvas, rearviewDamageColor);


		// Draw Ned and Nosh
		yonda.getNosh().draw(canvas);
		yonda.getNed().draw(canvas);
		rearviewCover.draw(canvas);

		// Draw inventory
		inventory.draw(canvas);
		inventory.drawItemInHand(canvas);

		// Draw speech bubbles, if necessary
		if (!road.reachedEndOfLevel()) {
			yonda.getNed().drawSpeechBubble(canvas);
			yonda.getNosh().drawSpeechBubble(canvas);
		}

		//Draw sun effect part 2
		visor.drawSunB(canvas, sunShine);

		//Draw visor
		visor.draw(canvas);


		satQuestions.draw(canvas, displayFont);
	}
}