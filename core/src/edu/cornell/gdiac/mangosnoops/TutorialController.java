package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private static final String TUT_SPEECH = "images/Tutorial/speechbubble_small.png";

    /** Texture types */
    private Texture tutKeysTexture;
    private Texture tutGaugeTexture;
    private Texture tutMirrorTexture;
    private Texture tutVroomTexture;
    private Texture tutHornTexture;
    private Texture tutVisorTexture;
    private Texture tutInventoryTexture;
    private Texture arrowTexture;
    private Texture speechTexture;

    private Image speechNosh;
    private Image speechNed;
    private String noshDialogue = null; //null if saying nothing, otherwise draw bubble with this text
    private String nedDialogue = null; //null if saying nothing, otherwise draw bubble with this text

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

    /** Flags to indicate that one-time events have occurred */
    private int madeNoshMad;
    private int madeNedMad;

    /** Which tutorial it is to identify what to display */
    private int tutIndex;

    public TutorialController(GameCanvas canvas, LevelObject level, int tutNum, SoundController sc) {
        super(canvas, level.getLevelEndY(), level.getEnemiez(), level.getEvents(), level.getSongs(), sc);
        tutIndex = tutNum;
        if(tutIndex == 0) {
            madeNoshMad = 0;
            madeNedMad = 0;
        }
    }

    private static final float NED_BUBBLE_X = 0.45f;
    private static final float NED_BUBBLE_Y = 0.85f;
    private static final float NOSH_BUBBLE_X = 0.64f;
    private static final float NOSH_BUBBLE_Y = 0.9f;

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
        speechNed = new Image(NED_BUBBLE_X , NED_BUBBLE_Y, 0.1f, speechTexture);
        speechNosh = new Image(NOSH_BUBBLE_X, NOSH_BUBBLE_Y, 0.1f, speechTexture);

        if(tutIndex == 0) {
        } else if(tutIndex == 1) {
            tutVroom.setVisible(true);
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
        manager.load(TUT_SPEECH, Texture.class);
        assets.add(TUT_SPEECH);
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
        speechTexture = createTexture(manager, TUT_SPEECH);
    }

    private float stamp = 0;

    public void resolveActions(InputController input, float delta) {
        super.resolveActions(input, delta);
        stamp += road.getSpeed() * delta;
        if(tutIndex == 0) {
            if(stamp < 3) {
                noshDialogue = "Moooooom why are\nwe leaving home?";
            } else if(stamp < 7) {
                noshDialogue = null;
                nedDialogue = "Will we be able to make\nit to soccer practice?";
            } else if (stamp < 9) {
                nedDialogue = null;
            } else if(stamp < 15) {
                nedDialogue = null;
                noshDialogue = "What are those in the\nhorizon?";
            } else if(stamp < 18) {
                noshDialogue = null;
                nedDialogue = "Are those sentient\ngarden gnomes?";
                tutKeys.setVisible(true);
            } else if(stamp < 19) {
                noshDialogue = null;
                nedDialogue = null;
            } else {
                // Show to put snacks
                if (madeNoshMad == 0 && yonda.getNosh().getCurrentMood() != Child.Mood.HAPPY) {
                    tutMirrorNoshSnack.setVisible(true);
                    tutInventory.setVisible(true);
                    arrowNoshSnack.setVisible(true);
                    madeNoshMad ++;
                } else if(madeNoshMad == 1 && yonda.getNosh().getCurrentMood() == Child.Mood.HAPPY) {
                    madeNoshMad ++;
                }else if (madeNoshMad == 2 || yonda.getNosh().getCurrentMood() == Child.Mood.HAPPY) {
                    noshDialogue = null;
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
                if(madeNoshMad != 2 && yonda.getNosh().getCurrentMood() == Child.Mood.CRITICAL) {
                    noshDialogue = "GIVE ME SNACK!";
                } else {
                    noshDialogue = null;
                }
                if(madeNedMad != 2 && yonda.getNed().getCurrentMood() == Child.Mood.CRITICAL) {
                    nedDialogue = "GIVE ME SNACK!";
                } else {
                    nedDialogue = null;
                }
            }
        } else if(tutIndex == 1) {
            if(Math.abs(events.get(0).getY() - ypos) < 0.1f) {
                if(!rearviewEnemy.exists()) {
                    tutVroom.setVisible(false);
                    arrowNedSnack.setVisible(false);
                }
            } else if(Math.abs(events.get(0).getY() - ypos) < 0.3f) {
                tutVroom.setVisible(true);
                arrowNedSnack.setVisible(true);
            } else if(getVroomStick().isEngaged()) {
                tutVroom.setVisible(false);
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
    }

    private static final float IND_TEXT_X = 0.07f;
    private static final float IND_TEXT_Y = 0.85f;

    public void speechBubble(GameCanvas canvas, BitmapFont displayFont) {
        if(nedDialogue != null) {
            speechNed.drawNoShake(canvas);
            displayFont.setColor(Color.BLACK);
            canvas.drawText(nedDialogue, displayFont,
                    canvas.getWidth()*(NED_BUBBLE_X+0.01f),canvas.getHeight()*(NED_BUBBLE_Y+0.08f),
                    Color.BLACK);
        }
        if(noshDialogue != null){
            speechNosh.drawNoShake(canvas);
            displayFont.setColor(Color.BLACK);
            canvas.drawText(noshDialogue, displayFont,
                    canvas.getWidth()*(NOSH_BUBBLE_X+0.01f),canvas.getHeight()*(NOSH_BUBBLE_Y+0.08f),
                    Color.BLACK);
        }
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
        speechBubble(canvas, displayFont);
    }

    public void reset() {
        super.reset();
        madeNedMad = 0;
        madeNoshMad = 0;
        stamp = 0;
        noshDialogue = null;
        nedDialogue = null;
    }

}
