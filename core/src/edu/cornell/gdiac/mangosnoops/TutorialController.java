package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.hudentity.Child;
import edu.cornell.gdiac.mangosnoops.hudentity.FlashingImage;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;
import edu.cornell.gdiac.mangosnoops.hudentity.RearviewEnemy;
import edu.cornell.gdiac.mangosnoops.roadentity.Enemy;
import edu.cornell.gdiac.mangosnoops.roadentity.Flamingo;
import edu.cornell.gdiac.mangosnoops.roadentity.Gnome;
import edu.cornell.gdiac.mangosnoops.roadentity.Road;
import org.apache.poi.ss.formula.functions.T;

/**
 * This class controls the tutorial sequence. It makes the assumption
 * that Enemies are destroyed when they collide with the player.
 */
public class TutorialController extends GameplayController {

    /** Texture files */
    private static final String TUT_KEYS_FILE = "images/Tutorial/tut_keys.png";
    private static final String TUT_GAUGE_FILE = "images/Tutorial/tut_gauge.png";
    private static final String TUT_MIRROR_FILE = "images/Tutorial/tut_mirror.png";
    private static final String TUT_VROOM_FILE = "images/Tutorial/tut_vroom.png";
    private static final String TUT_HORN_FILE = "images/Tutorial/tut_horn.png";
    private static final String TUT_VISOR_FILE = "images/Tutorial/tut_visor.png";
    private static final String TUT_INVENTORY_FILE = "images/Tutorial/tut_inventory.png";
    private static final String TUT_ARROW = "images/Tutorial/arrow.png";

    /** Texture types */
    private Texture tutKeysTexture;
    private Texture tutGaugeTexture;
    private Texture tutMirrorTexture;
    private Texture tutVroomTexture;
    private Texture tutHornTexture;
    private Texture tutVisorTexture;
    private Texture tutInventoryTexture;
    private Texture arrowTexture;

    /** Images that will be flashed on the screen */
    private FlashingImage tutKeys;
    private FlashingImage tutGauge;
    private FlashingImage tutMirrorNedSnack;
    private FlashingImage tutMirrorNoshSnack;
    private FlashingImage tutVroom;
    private FlashingImage tutHorn;
    private FlashingImage tutVisor;
    private FlashingImage tutInventory;
    private FlashingImage arrowNedSnack;
    private FlashingImage arrowNoshSnack;

    private enum TutorialState {
        LEARN_STEERING,
        LEARN_VROOMING,
        LEARN_NED_SNACK,
        LEARN_HONK,
        DONE
    }

    private TutorialState state;

    /** Flags to indicate that one-time events have occurred */
    private boolean createdRearviewGnome;
    private boolean createdFlamingos;
    private int madeNoshMad;
    private int madeNedMad;
    private boolean finishedTutorial;

    /** Gnome that appears until you dodge it */
    private Gnome gnome;
    private Array<Flamingo> flamingos;

    /** Which tutorial it is to identify what to display */
    private int tutIndex;

    /** If an enemy makes it past this Y value without getting
     *  destroyed, then it must have not hit the player. */
    private float ENEMY_Y_THRESHOLD = -13f;

    public TutorialController(GameCanvas canvas, LevelObject level, int tutNum, SoundController sc) {
        super(canvas, level.getLevelEndY(), level.getEnemiez(), level.getEvents(), level.getSongs(), sc);
        tutIndex = tutNum;
        createdRearviewGnome = false;
        createdFlamingos = false;
        madeNoshMad = 0;
        madeNedMad = 0;
        finishedTutorial = false;
    }

    public void start(float x, float y) {
        super.start(x, y);
        tutKeys = new FlashingImage(0.06f, 0.45f, 0.12f, tutKeysTexture);
        tutGauge = new FlashingImage(0.34f, 0.05f, 0.175f, tutGaugeTexture);
        tutMirrorNedSnack = new FlashingImage(0.65f, 0.7f, 0.3f, tutMirrorTexture);
        tutMirrorNoshSnack = new FlashingImage(0.75f, 0.7f, 0.3f, tutMirrorTexture);
        tutVroom = new FlashingImage(0.193f, 0.2f,0.3f, tutVroomTexture);
        tutHorn = new FlashingImage(0.12f, 0.12f, 0.17f, tutHornTexture);

        tutVisor = new FlashingImage(0.1f, 0.85f, 0.16f, tutVisorTexture);
        tutInventory = new FlashingImage(0.45f, 0.075f, 0.4f, tutInventoryTexture);

        arrowNedSnack = new FlashingImage(0.6f, 0.52f, 0.24f, arrowTexture);
        arrowNoshSnack = new FlashingImage(0.7f, 0.52f, 0.24f, arrowTexture);

        gnome = new Gnome(0, 10);
        gnome.setFilmStrip(gnomeTexture, GNOME_FILMSTRIP_ROWS, GNOME_FILMSTRIP_COLS);
        getEnemiez().add(gnome);

        flamingos = new Array<Flamingo>();

        state = TutorialState.LEARN_STEERING;

        if(tutIndex == 0) {
            tutKeys.setVisible(true);
        }
    }

    public void preLoadContent(AssetManager manager, Array<String> assets) {
        super.preLoadContent(manager, assets);
        manager.load(TUT_KEYS_FILE, Texture.class);
        assets.add(TUT_KEYS_FILE);
        manager.load(TUT_GAUGE_FILE, Texture.class);
        assets.add(TUT_GAUGE_FILE);
        manager.load(TUT_MIRROR_FILE, Texture.class);
        assets.add(TUT_MIRROR_FILE);
        manager.load(TUT_VROOM_FILE, Texture.class);
        assets.add(TUT_VROOM_FILE);
        manager.load(TUT_HORN_FILE, Texture.class);
        assets.add(TUT_HORN_FILE);
        manager.load(TUT_VISOR_FILE, Texture.class);
        assets.add(TUT_VISOR_FILE);
        manager.load(TUT_INVENTORY_FILE, Texture.class);
        assets.add(TUT_INVENTORY_FILE);
        manager.load(TUT_ARROW, Texture.class);
        assets.add(TUT_ARROW);
    }

    public void loadContent(AssetManager manager) {
        super.loadContent(manager);
        tutKeysTexture = createTexture(manager, TUT_KEYS_FILE);
        tutGaugeTexture = createTexture(manager, TUT_GAUGE_FILE);
        tutMirrorTexture = createTexture(manager, TUT_MIRROR_FILE);
        tutVroomTexture = createTexture(manager, TUT_VROOM_FILE);
        tutHornTexture = createTexture(manager, TUT_HORN_FILE);
        tutVisorTexture = createTexture(manager, TUT_VISOR_FILE);
        tutInventoryTexture = createTexture(manager, TUT_INVENTORY_FILE);
        arrowTexture = createTexture(manager, TUT_ARROW);
    }

    /**
     * Create and initialize the flamingos. This should only happen once.
     */
    private void createFlamingos() {
        flamingos.add(new Flamingo(-0.2f, 10));
        flamingos.add(new Flamingo(0f, 10));
        flamingos.add(new Flamingo(0.2f, 10));
        for (Flamingo f : flamingos) {
            f.setFilmStrip(flamingoTexture, FLAMINGO_FILMSTRIP_ROWS, FLAMINGO_FILMSTRIP_COLS);
            getEnemiez().add(f);
        }
    }

    /**
     * Reset the Flamingos if and only if one of them
     * got destroyed, which implies that one of them
     * collided with the player.
     */
    private void resetFlamingos() {

        boolean atLeastOneDestroyed = false;
        for (Flamingo f : flamingos) {
            atLeastOneDestroyed |= f.isDestroyed();
        }

        if (atLeastOneDestroyed) {
            getEnemiez().clear();
            for (Flamingo f : flamingos) {
                f.setDestroyed(false);
                getEnemiez().add(f);
                f.setY(10);
            }
        }
    }

    /**
     * @return whether or not the user made it past the flamingos
     */
    private boolean madeItPastFlamingos() {
        boolean madeIt = true;
        for (Flamingo f : flamingos) {
            madeIt &= (f.getY() < ENEMY_Y_THRESHOLD && !f.isDestroyed());
        }
        return madeIt;
    }

    public void resolveActions(InputController input, float delta) {

        super.resolveActions(input, delta);

//        /** Make sure road is some arbitrarily far distance away
//         *  so the tutorial doesn't end */
//        if (!finishedTutorial) getRoad().setRoadExitY(500);
//
//        //System.out.println(getRoad().getRoadExitY());

        if(tutIndex == 0) {
            // Show to put snacks
            if (madeNoshMad == 0 && yonda.getNosh().getCurrentMood() != Child.Mood.HAPPY) {
                tutMirrorNoshSnack.setVisible(true);
                tutInventory.setVisible(true);
                arrowNoshSnack.setVisible(true);
                madeNoshMad ++;
            } else if(madeNoshMad == 1 && yonda.getNosh().getCurrentMood() == Child.Mood.HAPPY) {
                madeNoshMad ++;
            }else if (madeNoshMad == 2 || yonda.getNosh().getCurrentMood() == Child.Mood.HAPPY) {
                tutMirrorNoshSnack.setVisible(false);
                arrowNoshSnack.setVisible(false);
            }
            if (madeNedMad == 0 && yonda.getNed().getCurrentMood() != Child.Mood.HAPPY) {
                tutMirrorNedSnack.setVisible(true);
                tutInventory.setVisible(true);
                arrowNedSnack.setVisible(true);
                madeNedMad ++;
            } else if(madeNedMad == 1 && yonda.getNed().getCurrentMood() == Child.Mood.HAPPY) {
                madeNedMad ++;
            } else if (madeNedMad == 2 || yonda.getNed().getCurrentMood() == Child.Mood.HAPPY){
                tutMirrorNedSnack.setVisible(false);
                arrowNedSnack.setVisible(false);
                if(madeNoshMad == 2 || yonda.getNosh().getCurrentMood() == Child.Mood.HAPPY) {
                    tutInventory.setVisible(false);
                }
            }
        }


        tutKeys.update(delta);
        tutVroom.update(delta);
        tutInventory.update(delta);
        tutMirrorNoshSnack.update(delta);
        tutMirrorNedSnack.update(delta);
        tutHorn.update(delta);
        arrowNoshSnack.update(delta);
        arrowNedSnack.update(delta);
//
//        switch (state) {
//            case LEARN_STEERING:
//                tutKeys.setVisible(true);
//                if (gnome.isDestroyed()) {
//                    gnome.setDestroyed(false);
//                    getEnemiez().add(gnome);
//                    gnome.setY(10);
//                }
//
//                if (gnome.getY() < ENEMY_Y_THRESHOLD && !gnome.isDestroyed()) {
//                    gnome.setDestroyed(true);
//                    tutKeys.setVisible(false);
//                    state = TutorialState.LEARN_VROOMING;
//                }
//                break;
//            case LEARN_VROOMING:
//                tutVroom.setVisible(true);
//                if (!createdRearviewGnome) {
//                    createdRearviewGnome = true;
//                    rearviewEnemy.create();
//                }
//
//                if (!rearviewEnemy.exists()) {
//                    tutVroom.setVisible(false);
//                    state = TutorialState.LEARN_HONK;
//                }
//                break;
//            case LEARN_HONK:
//                tutHorn.setVisible(true);
//                if (!createdFlamingos) {
//                    createFlamingos();
//                    createdFlamingos = true;
//                }
//                if (madeItPastFlamingos()) {
//                    tutHorn.setVisible(false);
//                    state = TutorialState.LEARN_NED_SNACK;
//                } else {
//                    resetFlamingos();
//                }
//
//                break;
//            case LEARN_NED_SNACK:
//                tutMirror.setVisible(true);
//                tutInventory.setVisible(true);
//                arrow.setVisible(true);
//                if (!madeNedMad) {
//                    madeNedMad = true;
//                }
//                if (getCar().getNed().getCurrentMood() == Child.Mood.HAPPY) {
//                    tutMirror.setVisible(false);
//                    tutInventory.setVisible(false);
//                    arrow.setVisible(false);
//                    state = TutorialState.DONE;
//                }
//                break;
//            case DONE:
//                if (!finishedTutorial) {
//                    finishedTutorial = true;
//                    getRoad().setRoadExitY(1);
//                }
//        }

    }

    public void speechBubble(Child.ChildType childType, GameCanvas canvas) {

    }

    public void draw(GameCanvas canvas) {
        super.draw(canvas);
        tutKeys.draw(canvas);
        tutGauge.draw(canvas);
        tutMirrorNoshSnack.draw(canvas);
        tutMirrorNedSnack.draw(canvas);
        tutVroom.draw(canvas);
        tutHorn.draw(canvas);
        tutVisor.draw(canvas);
        tutInventory.draw(canvas);
        arrowNoshSnack.draw(canvas, -35f);
        arrowNedSnack.draw(canvas, -35f);
    }

    public void reset() {
        super.reset();
        createdRearviewGnome = false;
        madeNedMad = 0;
        madeNoshMad = 0;
        createdFlamingos = false;
    }

}
