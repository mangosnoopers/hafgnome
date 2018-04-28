package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.hudentity.Child;
import edu.cornell.gdiac.mangosnoops.hudentity.FlashingImage;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;
import edu.cornell.gdiac.mangosnoops.hudentity.RearviewEnemy;
import edu.cornell.gdiac.mangosnoops.roadentity.Enemy;
import edu.cornell.gdiac.mangosnoops.roadentity.Gnome;
import edu.cornell.gdiac.mangosnoops.roadentity.Road;

public class TutorialController extends GameplayController {

    /** Texture files */
    private static final String TUT_KEYS_FILE = "images/Tutorial/tut_keys.png";
    private static final String TUT_GAUGE_FILE = "images/Tutorial/tut_gauge.png";
    private static final String TUT_MIRROR_FILE = "images/Tutorial/tut_mirror.png";
    private static final String TUT_VROOM_FILE = "images/Tutorial/tut_vroom.png";
    private static final String TUT_HORN_FILE = "images/Tutorial/tut_horn.png";
    private static final String TUT_VISOR_FILE = "images/Tutorial/tut_visor.png";
    private static final String TUT_INVENTORY_FILE = "images/Tutorial/tut_inventory.png";

    /** Texture types */
    private Texture tutKeysTexture;
    private Texture tutGaugeTexture;
    private Texture tutMirrorTexture;
    private Texture tutVroomTexture;
    private Texture tutHornTexture;
    private Texture tutVisorTexture;
    private Texture tutInventoryTexture;

    /** Images that will be flashed on the screen */
    private FlashingImage tutKeys;
    private FlashingImage tutGauge;
    private FlashingImage tutMirror;
    private FlashingImage tutVroom;
    private FlashingImage tutHorn;
    private FlashingImage tutVisor;
    private FlashingImage tutInventory;

    private enum TutorialState {
        LEARN_STEERING,
        LEARN_VROOMING,
        LEARN_NED_SNACK,
        DONE
    }

    private TutorialState state;

    /** Flags to indicate that one-time events have occurred */
    private boolean createdRearviewGnome = false;
    private boolean madeNedMad = false;

    /** Gnome that appears until you dodge it */
    private Gnome gnome;

    public TutorialController(GameCanvas canvas) {
        super(canvas, 500, new Array<Enemy>(), new Array<Event>(), new ObjectMap<String, Radio.Genre>());

    }

    public void start(float x, float y) {
        super.start(x, y);
        tutKeys = new FlashingImage(0.05f, 0.5f, 0.12f, tutKeysTexture);
        tutGauge = new FlashingImage(0.34f, 0.05f, 0.175f, tutGaugeTexture);
        tutMirror = new FlashingImage(0.65f, 0.7f, 0.3f, tutMirrorTexture);
        tutVroom = new FlashingImage(0.193f, 0.2f,0.3f, tutVroomTexture);
        tutHorn = new FlashingImage(0.12f, 0.12f, 0.17f, tutHornTexture);
        tutVisor = new FlashingImage(0.1f, 0.85f, 0.16f, tutVisorTexture);
        tutInventory = new FlashingImage(0.45f, 0.075f, 0.4f, tutInventoryTexture);

        gnome = new Gnome(0, 10);
        gnome.setFilmStrip(gnomeTexture, GNOME_FILMSTRIP_ROWS, GNOME_FILMSTRIP_COLS);
        getEnemiez().add(gnome);

        state = TutorialState.LEARN_STEERING;


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
    }

    public void resolveActions(InputController input, float delta) {
        super.resolveActions(input, delta);

        tutKeys.update(delta);
        tutVroom.update(delta);
        tutInventory.update(delta);
        tutMirror.update(delta);

        switch (state) {
            case LEARN_STEERING:
                tutKeys.setVisible(true);
                if (gnome.isDestroyed()) {
                    gnome.setDestroyed(false);
                    getEnemiez().add(gnome);
                    gnome.setY(10);
                }

                if (gnome.getY() < -13 && !gnome.isDestroyed()) {
                    gnome.setDestroyed(true);
                    tutKeys.setVisible(false);
                    state = TutorialState.LEARN_VROOMING;
                }
                break;
            case LEARN_VROOMING:
                tutVroom.setVisible(true);
                if (!createdRearviewGnome) {
                    createdRearviewGnome = true;
                    rearviewEnemy.create();
                }

                if (!rearviewEnemy.exists()) {
                    tutVroom.setVisible(false);
                    state = TutorialState.LEARN_NED_SNACK;
                }
                break;
            case LEARN_NED_SNACK:
                tutMirror.setVisible(true);
                tutInventory.setVisible(true);
                if (!madeNedMad) {
                    getCar().getNed().setMood(Child.Mood.CRITICAL);
                    madeNedMad = true;
                }
                if (getCar().getNed().getCurrentMood() != Child.Mood.CRITICAL) {
                    tutMirror.setVisible(false);
                    tutInventory.setVisible(false);
                    state = TutorialState.DONE;
                }
                break;
        }

    }

    public void draw(GameCanvas canvas) {
        super.draw(canvas);
        tutKeys.draw(canvas);
        tutGauge.draw(canvas);
        tutMirror.draw(canvas);
        tutVroom.draw(canvas);
        tutHorn.draw(canvas);
        tutVisor.draw(canvas);
        tutInventory.draw(canvas);

    }

    public void reset() {
        super.reset();
        createdRearviewGnome = false;
        madeNedMad = false;
    }

}
