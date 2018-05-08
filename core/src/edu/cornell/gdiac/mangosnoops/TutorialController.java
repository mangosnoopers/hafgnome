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
    private static final String TUT_WHEEL = "images/Tutorial/tut_wheel.png";
    private static final String TUT_GAUGE_FILE = "images/Tutorial/tut_gauge.png";
    private static final String TUT_MIRROR_FILE = "images/Tutorial/tut_mirror.png";
    private static final String TUT_VROOM_FILE = "images/Tutorial/tut_vroom.png";
    private static final String TUT_HORN_FILE = "images/Tutorial/tut_horn.png";
    private static final String TUT_VISOR_FILE = "images/Tutorial/tut_visor.png";
    private static final String TUT_INVENTORY_FILE = "images/Tutorial/tut_inventory.png";
    private static final String TUT_ARROW = "images/Tutorial/arrow.png";
    private static final String TUT_SPEECH = "images/Tutorial/speechbubble_small.png";
    private static final String MODULE_BG = "images/PauseMenuAssets/pauseMenuBackground.png";

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
    private Texture moduleTexture;
    private Texture tutWheel;

    private Image module;
    private Image speechNosh;
    private Image speechNed;
    private String noshDialogue = null; //null if saying nothing, otherwise draw bubble with this text
    private String nedDialogue = null; //null if saying nothing, otherwise draw bubble with this text

    /** Images that will be flashed on the screen */
    private FlashingImage tutKeys;
    private FlashingImage tutGauge;
    private FlashingImage tutMirrorNedSnack;
    private FlashingImage tutMirrorNoshSnack;
    private FlashingImage tutHorn;
    private FlashingImage tutVisor;
    private FlashingImage tutInventory;
    private FlashingImage arrowNedSnack;
    private FlashingImage arrowNoshSnack;

    /** Flags to indicate that one-time events have occurred */
    private int madeNoshMad;
    private int madeNedMad;
    private int state;
    private boolean finishedTutorial;

    /** Forever appearing gnome row if you don't do the instruction */
    private Gnome gnome;
    private Gnome gnomeL;
    private Gnome gnomeR;

    /** Which tutorial it is to identify what to display */
    private int tutIndex;

    public TutorialController(GameCanvas canvas, LevelObject level, int tutNum, SoundController sc) {
        super(canvas, level.getLevelEndY(), level.getEnemiez(), level.getEvents(), level.getSongs(), sc, level.getRoadsideObjs());
        tutIndex = tutNum;
        madeNoshMad = 0;
        madeNedMad = 0;
        state = 0;
        finishedTutorial = false;
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
        tutHorn = new FlashingImage(0.12f, 0.12f, 0.17f, tutHornTexture);
        tutVisor = new FlashingImage(0.1f, 0.85f, 0.16f, tutVisorTexture);
        tutInventory = new FlashingImage(0.45f, 0.075f, 0.4f, tutInventoryTexture);
        arrowNedSnack = new FlashingImage(0.6f, 0.52f, 0.24f, arrowTexture);
        arrowNoshSnack = new FlashingImage(0.7f, 0.52f, 0.24f, arrowTexture);
        speechNed = new Image(NED_BUBBLE_X , NED_BUBBLE_Y, 0.1f, speechTexture);
        speechNosh = new Image(NOSH_BUBBLE_X, NOSH_BUBBLE_Y, 0.1f, speechTexture);
        module = new Image(0.5f,0.5f, 0.79f, moduleTexture, GameCanvas.TextureOrigin.MIDDLE);

        // For the tutorial, override some HUD elements that we want to flash
        vroomStick.setSpecialTexture(tutVroomTexture);
        getWheel().setSpecialTexture(tutWheel);

        if(tutIndex == 0) {
        } else if(tutIndex == 1) {
            vroomStick.setFlashing(true);
        } else if(tutIndex == 2) {
            gnome = new Gnome(0, 10);
            gnome.setFilmStrip(gnomeTexture, GNOME_FILMSTRIP_ROWS, GNOME_FILMSTRIP_COLS);
            getEnemiez().add(gnome);
            gnome.setDestroyed(true);
            gnomeL = new Gnome(-0.2f, 10);
            gnomeL.setFilmStrip(gnomeTexture, GNOME_FILMSTRIP_ROWS, GNOME_FILMSTRIP_COLS);
            getEnemiez().add(gnomeL);
            gnomeL.setDestroyed(true);
            gnomeR = new Gnome(0.2f, 10);
            gnomeR.setFilmStrip(gnomeTexture, GNOME_FILMSTRIP_ROWS, GNOME_FILMSTRIP_COLS);
            getEnemiez().add(gnomeR);
            gnomeR.setDestroyed(true);
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
        manager.load(MODULE_BG, Texture.class);
        assets.add(MODULE_BG);
        manager.load(TUT_WHEEL, Texture.class);
        assets.add(TUT_WHEEL);
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
        moduleTexture = createTexture(manager, MODULE_BG);
        tutWheel = createTexture(manager, TUT_WHEEL);
    }

    private float stamp = 0;
    private float stamp2 = 0;

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
                getWheel().setFlashing(true);
            } else if(stamp < 20) {
                noshDialogue = null;
                nedDialogue = null;
            } else {
                if(stamp < 30) {
                    tutKeys.setVisible(false);
                    getWheel().setFlashing(false);
                }
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
                    nedDialogue = null;
                    vroomStick.setFlashing(false);
                }
            } else if(Math.abs(events.get(0).getY() - ypos) < 0.4f) {
                nedDialogue = "Mom I think something\nis on our tail!";
                rearviewSeats.setFlashing(true);
                vroomStick.setFlashing(true);
            } else if(getVroomStick().isEngaged()) {
                nedDialogue = null;
                rearviewSeats.setFlashing(false);
                vroomStick.setFlashing(false);
            }
        } else if(tutIndex == 2) {
            if(!finishedTutorial) getRoad().setRoadExitY(500);

            if(stamp < 3) {
                noshDialogue = "I'm so sleepy Mom...";
            } else if(stamp < 4) {
                noshDialogue = null;
                nedDialogue = "Can you switch to\nClassical?";
                stamp2 = stamp;
            } else {
                switch(state) {
                    case 0: // can you switch to classical
                        if(stamp-stamp2 > 1 && stamp-stamp2 < 2) {
                            nedDialogue = "Can you switch to\nClassical?";
                        }
                        else if(stamp-stamp2 > 3 && stamp-stamp2 < 5) {
                            nedDialogue = null;
                            stamp2 = stamp;
                        }
                        if(getRadio().getCurrentStation().getGenre() == Radio.Genre.CLASSICAL) {
                            state++;
                            stamp2 = stamp;
                        }
                        break;
                    case 1:
                        if(!yonda.getNed().isAwake()) nedDialogue = "Zzzzz...";
                        else nedDialogue = null;
                        if(!yonda.getNosh().isAwake()) noshDialogue = "ZZzzzZZZzzZZ...";
                        else noshDialogue = null;
                        if (gnome.isDestroyed() || gnomeL.isDestroyed() || gnomeR.isDestroyed()) {
                            //create gnomes until hit
                            gnome.setDestroyed(true);
                            gnomeL.setDestroyed(true);
                            gnomeR.setDestroyed(true);

                            gnome.setDestroyed(false);
                            getEnemiez().add(gnome);
                            gnome.setY(14);
                            gnomeL.setDestroyed(false);
                            getEnemiez().add(gnomeL);
                            gnomeL.setY(14);
                            gnomeR.setDestroyed(false);
                            getEnemiez().add(gnomeR);
                            gnomeR.setY(14);
                        }
                        if(yonda.getNed().getCurrentMood() == Child.Mood.SAD) { //hit a gnome
                            if(madeNedMad == 0) {
                                madeNedMad ++;
                                stamp2 = stamp;
                            }
                            gnome.setDestroyed(true);
                            gnomeL.setDestroyed(true);
                            gnomeR.setDestroyed(true);
                            nedDialogue = "I can't sleep like this!";
                            if(stamp-stamp2 > 2 && stamp-stamp2 < 3) {
                                madeNedMad = 0;
                                nedDialogue = null;
                                state++;
                                stamp2 = stamp;
                            }
                        }
                        break;
                    case 2: //can you switch to comedy
                        if(getRadio().getCurrentStation().getGenre() == Radio.Genre.COMEDY) {
                            if(madeNoshMad == 0) {
                                madeNoshMad++;
                                stamp2 = stamp;
                            }
                            noshDialogue = "Yay! I like Comedy!";
                            if(stamp-stamp2 > 2 && yonda.getNosh().getCurrentMood() == Child.Mood.HAPPY) {
                                madeNoshMad = 0;
                                state++;
                                noshDialogue = null;
                                stamp2 = stamp;
                            }
                        } else {
                            if(stamp-stamp2 > 1 && stamp-stamp2 < 2) {
                                noshDialogue = "Can you switch to\nComedy?";
                            }
                            else if(stamp-stamp2 > 3 && stamp-stamp2 < 5) {
                                noshDialogue = null;
                                stamp2 = stamp;
                            }
                        }
                        break;
                    case 3: //can you switch to pop
                        if(getRadio().getCurrentStation().getGenre() == Radio.Genre.POP) {
                            if(madeNedMad == 0) {
                                madeNedMad++;
                                stamp2 = stamp;
                            }
                            nedDialogue = "Yay! I like Pop!";
                            if(stamp-stamp2 > 2 && yonda.getNed().getCurrentMood() == Child.Mood.HAPPY) {
                                state++;
                            }
                        } else {
                            if(stamp-stamp2 > 1 && stamp-stamp2 < 2) {
                                nedDialogue = "Can you switch to\nPop?";
                            }
                            else if(stamp-stamp2 > 3 && stamp-stamp2 < 5) {
                                nedDialogue = null;
                                stamp2 = stamp;
                            }
                        }
                        break;
                    case 4: //end level
                        if(!finishedTutorial) {
                            getRoad().setRoadExitY(10);
                            finishedTutorial = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }


        vroomStick.updateFlash(delta);
        getWheel().updateFlash(delta);

        tutKeys.update(delta);
        tutInventory.update(delta);
        tutMirrorNoshSnack.update(delta);
        tutMirrorNedSnack.update(delta);
        tutHorn.update(delta);
        arrowNoshSnack.update(delta);
        arrowNedSnack.update(delta);
    }

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
        stamp2 = 0;
        state = 0;
        noshDialogue = null;
        nedDialogue = null;
    }

}
