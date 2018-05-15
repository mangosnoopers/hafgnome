package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.util.FilmStrip;

import java.util.Random;


public class Child extends Image {

    /**
     * The type of Gnome this is
     */
    private ChildType ctype;

    Texture speechBubble;

    private boolean gettingSad; //is gradually getting happier
    private boolean gettingHappy; //is gradually getting sadder
    private static final int MOOD_DELTA = 1; //how much happiness changes each step

    /**
     * These indices indicate which section of the filmstrip corresponds
     * to which mood
     */
    private final static int NED_HAPPY_INDEX = 0;
    private final static int NED_NEUTRAL_INDEX = 1;
    private final static int NED_SLEEP_INDEX = 2;
    private final static int NED_CRITICAL_INDEX = 3;
    private final static int NED_SAD_START_INDEX = 4;
    private final static int NED_SAD_END_INDEX = 12;
    private final static int NED_SAT_INDEX = 13;

    private final static int NOSH_HAPPY_INDEX = 0;
    private final static int NOSH_NEUTRAL_INDEX = 1;
    private final static int NOSH_SLEEP_INDEX = 2;
    private final static int NOSH_CRITICAL_INDEX = 3;
    private final static int NOSH_SAD_START_INDEX = 4;
    private final static int NOSH_SAD_END_INDEX = 12;

    /** The smallest number that the animation frame can be
     *  (which depends on the mood) */
    private ObjectMap<Mood, Integer> lowerFrameBound;
    /** The largest number that the animation frame can be */
    private ObjectMap<Mood, Integer> upperFrameBound;

    /**
     * Enum specifying the type of child this is.
     */
    public enum ChildType {
        /**
         * Default Type
         */
        NOSH,
        NED,
    }

    /**
     * Speech bubble offset for shaky effect
     */
    private float speechBubbleOffsetX = 3;
    private float speechBubbleOffsetY = -2;

    /**
     * Speed for child animation
     */
    private float ANIMATION_SPEED;

    /**
     * Current animation frame for animations
     */
    private float animationFrame;

    /**
     * The current FilmStrip
     */
    private FilmStrip currentFilmStrip;

    public float getShakeX() {
        return speechBubbleOffsetX;
    }

    public float getShakeY() {
        return speechBubbleOffsetY;
    }

    /**
     * Coordinates of speech bubbles for Ned and Nosh
     */
    public static Vector2 NED_SPEECH_BUBBLE_COORDS;
    public static Vector2 NOSH_SPEECH_BUBBLE_COORDS;

    /**
     * Speech bubble offset for scaling effect
     */
    private float deltaSum = 0;
    private float scale = 0;

    /**
     * Represents the current mood. 0-100 means they are awake, -100 they are asleep.
     */
    private int happiness;
    /**
     * Lower bounds of happiness states before they change (exclusive)
     */
    private static final int HAPPY_UBOUND = 1000;
    private static final int HAPPY_LBOUND = 750;
    private static final int NEUTRAL_LBOUND = 500;
    private static final int SAD_LBOUND = 250;

    /**
     * Enum specifying the possible moods
     * the child can have
     */
    public enum Mood {
        HAPPY, NEUTRAL, SAD, CRITICAL, SLEEP //TODO: this versus bool
    }

    /**
     * Constructor
     */
    public Child(ChildType type, Texture sb) {

        super(0, 0, 0, null);
        if (type == ChildType.NED) {
            NED_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f, 0.65f), MathUtils.random(0.45f, 0.75f));
            position = new Vector2(0.713f, 0.85f);
        } else {
            NOSH_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f, 0.65f), MathUtils.random(0.45f, 0.75f));
            position = new Vector2(0.855f, 0.85f);
        }
        Random rand = new Random();
        int animSpeedInt = rand.nextInt(5) + 20;
        ANIMATION_SPEED = (float) animSpeedInt / 100f;

        ctype = type;
        happiness = HAPPY_UBOUND;
        speechBubble = sb;

        lowerFrameBound = new ObjectMap<Mood, Integer>();
        upperFrameBound = new ObjectMap<Mood, Integer>();

        if (type == ChildType.NED) {
            lowerFrameBound.put(Mood.HAPPY, NED_HAPPY_INDEX);
            lowerFrameBound.put(Mood.NEUTRAL, NED_NEUTRAL_INDEX);
            lowerFrameBound.put(Mood.SAD, NED_SAD_START_INDEX);
            lowerFrameBound.put(Mood.CRITICAL, NED_CRITICAL_INDEX);
            lowerFrameBound.put(Mood.SLEEP, NED_SLEEP_INDEX);

            upperFrameBound.put(Mood.HAPPY, NED_HAPPY_INDEX);
            upperFrameBound.put(Mood.NEUTRAL, NED_NEUTRAL_INDEX);
            upperFrameBound.put(Mood.SAD, NED_SAD_END_INDEX);
            upperFrameBound.put(Mood.CRITICAL, NED_CRITICAL_INDEX);
            upperFrameBound.put(Mood.SLEEP, NED_SLEEP_INDEX);
        }

        if (type == ChildType.NOSH) {
            lowerFrameBound.put(Mood.HAPPY, NOSH_HAPPY_INDEX);
            lowerFrameBound.put(Mood.NEUTRAL, NOSH_NEUTRAL_INDEX);
            lowerFrameBound.put(Mood.SAD, NOSH_SAD_START_INDEX);
            lowerFrameBound.put(Mood.CRITICAL, NOSH_CRITICAL_INDEX);
            lowerFrameBound.put(Mood.SLEEP, NOSH_SLEEP_INDEX);

            upperFrameBound.put(Mood.HAPPY, NOSH_HAPPY_INDEX);
            upperFrameBound.put(Mood.NEUTRAL, NOSH_NEUTRAL_INDEX);
            upperFrameBound.put(Mood.SAD, NOSH_SAD_END_INDEX);
            upperFrameBound.put(Mood.CRITICAL, NOSH_CRITICAL_INDEX);
            upperFrameBound.put(Mood.SLEEP, NOSH_SLEEP_INDEX);
        }

    }

    /**
     * Decreases mood one segment
     */
    public void decreaseMood() {
        if (getCurrentMood() != Mood.SLEEP) {
            happiness -= 250;
            if (happiness < 0) happiness = 0;
        }
    }

    /**
     * Returns the mood of the child
     */
    public Mood getCurrentMood() {
        if (happiness > HAPPY_LBOUND) return Mood.HAPPY;
        else if (happiness > NEUTRAL_LBOUND) return Mood.NEUTRAL;
        else if (happiness > SAD_LBOUND) return Mood.SAD;
        else if (happiness >= 0) return Mood.CRITICAL;
        else return Mood.SLEEP;
    }

    /**
     * Returns true if the child is the awake.
     */
    public boolean isAwake() {
        return happiness >= 0;
    }

    /**
     * Sets the mood of the child, which also entails setting awake/not awake;
     *
     * @param m what Mood they'll be in when awoken
     *          TODO: should this be upper/lower bound of mood? rn upper
     */
    public void setMood(Mood m) {
        switch (m) {
            case HAPPY:
                happiness = HAPPY_UBOUND;
                break;
            case NEUTRAL:
                happiness = HAPPY_LBOUND;
                break;
            case SAD:
                happiness = NEUTRAL_LBOUND;
                break;
            case CRITICAL:
                happiness = SAD_LBOUND;
                break;
            default: //asleep
                setAsleep();
                break;
        }


    }

    /**
     * Changes whether or not the mood is shifting, and if so if it's getting
     * happier/sadder.
     *
     * @param set is true if you want to have dynamic happiness change, false if static
     *            happier is true if they are getting happier, false getting angrier
     */
    public void setMoodShifting(boolean set, boolean happier) {
        if (set && isAwake()) {
            if (happier) {
                gettingHappy = true;
                gettingSad = false;
            } else {
                gettingSad = true;
                gettingHappy = false;
            }
        } else {
            gettingSad = false;
            gettingHappy = false;
        }
    }

    /**
     * Sets the child to catch some zzzz's.
     */
    public void setAsleep() {
        happiness = -HAPPY_UBOUND;
        gettingSad = false;
        gettingHappy = false;
    }

    /**
     * Returns the type of the child.
     * TODO: might remove cause the ned/nosh instantiations are already distinguished
     */
    public ChildType getType() {
        return ctype;
    }

    /**
     * set the Texture for this Child.
     *
     * @param t    the texture file
     * @param rows the number of filmstrip rows
     * @param cols the number of filmstrip cols
     */
    public void setChildFilmStrip(Texture t, int rows, int cols) {
        currentFilmStrip = new FilmStrip(t, rows, cols, rows * cols);
        updateAnimation(getCurrentMood());

    }

    private void updateAnimation(Mood currentMood) {

        if (prevMood != getCurrentMood()) {
            animationFrame = lowerFrameBound.get(getCurrentMood());
        }

        animationFrame += ANIMATION_SPEED;
        if (animationFrame >= upperFrameBound.get(getCurrentMood())) {
            animationFrame = lowerFrameBound.get(getCurrentMood());
        }

        currentFilmStrip.setFrame((int) animationFrame);
    }

    /**
     * @param in
     * @return True if the child is the awake.
     */
    private Mood prevMood;

    private float del;

    public void update(float delta, Vector2 in) {
        del += delta;
        if (isAwake() && del > 0.005) { //TODO: may not need to check isAwake, this is a security blanket lol
            del = 0;
            if (gettingHappy) {
                happiness += MOOD_DELTA;
                if (happiness > HAPPY_UBOUND) {
                    happiness = HAPPY_UBOUND;
                    gettingHappy = false;
                }
            } else if (gettingSad) {
                happiness -= MOOD_DELTA;
                if (happiness < 0) {
                    happiness = 0;
                    gettingSad = false;
                }
            }
        } else {
            gettingHappy = false;
            gettingSad = false;
        }

        if (getCurrentMood() == Mood.CRITICAL) {
            speechBubbleOffsetX *= -1;
            speechBubbleOffsetY *= -1;
            if (prevMood != getCurrentMood()) {
                if (this.ctype == ChildType.NED) {
                    if (NOSH_SPEECH_BUBBLE_COORDS != null) {
                        while (Math.abs(NED_SPEECH_BUBBLE_COORDS.x - NOSH_SPEECH_BUBBLE_COORDS.x) < 0.1f || Math.abs(NED_SPEECH_BUBBLE_COORDS.y - NOSH_SPEECH_BUBBLE_COORDS.y) < 0.1f) {
                            NED_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f, 0.65f), MathUtils.random(0.45f, 0.75f));
                        }
                    }
//                   System.out.println(NED_SPEECH_BUBBLE_COORDS);
                }
                if (this.ctype == ChildType.NOSH) {
                    if (NED_SPEECH_BUBBLE_COORDS != null) {
                        while (Math.abs(NED_SPEECH_BUBBLE_COORDS.x - NOSH_SPEECH_BUBBLE_COORDS.x) < 0.1f || Math.abs(NED_SPEECH_BUBBLE_COORDS.y - NOSH_SPEECH_BUBBLE_COORDS.y) < 0.1f) {
                            NOSH_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f, 0.65f), MathUtils.random(0.45f, 0.75f));
                        }
                    }
//                   System.out.println(NOSH_SPEECH_BUBBLE_COORDS);

                }
            }


        }

        updateAnimation(getCurrentMood());
        prevMood = getCurrentMood();

        deltaSum += delta;

        scale = 1f + (float) (0.2 * Math.sin(8 * deltaSum));

    }

    /**
     * draws the child on the given canvas
     *
     * @param canvas
     */
    @Override
    public void draw(GameCanvas canvas) {
        if (currentFilmStrip == null) {
            return;
        }

        /* FIXME: rearWidth used to be based on rearview mirror but it's different now */
        float rearWidth = 50 * canvas.getHeight() / (50 * 3.5f);

        float ox = 0.5f * currentFilmStrip.getRegionWidth();
        float oy = 0.5f * currentFilmStrip.getRegionHeight();
        float drawY = position.y * canvas.getHeight() + currentShakeAmount;

        canvas.draw(currentFilmStrip, Color.WHITE, ox, oy, position.x*canvas.getWidth(), drawY, 0,
                0.55f*(canvas.getHeight()/2.5f)/currentFilmStrip.getRegionHeight(),
                0.55f*(canvas.getHeight()/2.5f)/currentFilmStrip.getRegionHeight());
        }



    public void drawSpeechBubble(GameCanvas canvas) {
        if (getCurrentMood() == Mood.CRITICAL) {
            if (getType() == ChildType.NED) {
                canvas.draw(speechBubble, Color.WHITE, speechBubble.getWidth() * 0.5f, speechBubble.getHeight() * 0.5f,
                        Child.NED_SPEECH_BUBBLE_COORDS.x * canvas.getWidth(), Child.NED_SPEECH_BUBBLE_COORDS.y * canvas.getHeight(),
                        0, 0.9f * scale * (float) canvas.getHeight() / (float) speechBubble.getWidth(), 0.9f * scale * (float) canvas.getHeight() / (float) speechBubble.getHeight());
            } else {
                canvas.draw(speechBubble, Color.WHITE, speechBubble.getWidth() * 0.5f, speechBubble.getHeight() * 0.5f,
                        Child.NOSH_SPEECH_BUBBLE_COORDS.x * canvas.getWidth(), Child.NOSH_SPEECH_BUBBLE_COORDS.y * canvas.getHeight(),
                        0, 0.9f * scale * (float) canvas.getHeight() / (float) speechBubble.getWidth(), 0.9f * scale * (float) canvas.getHeight() / (float) speechBubble.getHeight());
            }

        }
    }


    /**
     * Returns true if the mouse is positioned inside the area of the wheel.
     * The wheel must not be null.
     *
     * @param in the vector giving the mouse click's (x,y) screen coordinates
     */
    public boolean inChildArea(Vector2 in) {
//        System.out.println("input: " + in.x + " " + in.y);
//        System.out.println("X: " + (position.x*SCREEN_DIMENSIONS.x + currentFilmStrip.getRegionWidth()*0.5f) + " " + (position.x*SCREEN_DIMENSIONS.x - currentFilmStrip.getRegionWidth()*0.5f));
//        System.out.println("Y: " + (position.y*SCREEN_DIMENSIONS.y + currentFilmStrip.getRegionHeight()*0.5f) + " " + (position.y*SCREEN_DIMENSIONS.y - currentFilmStrip.getRegionHeight()*0.5f));
        return (in.x <= position.x * c.getWidth() + currentFilmStrip.getRegionWidth() * 0.5f)
                && (in.x >= position.x * c.getWidth() - currentFilmStrip.getRegionWidth() * 0.5f)
                && (c.getHeight() - in.y <= position.y * c.getHeight() + currentFilmStrip.getRegionHeight() * 0.5f)
                && (c.getHeight() - in.y >= position.y * c.getHeight() - currentFilmStrip.getRegionHeight() * 0.5f);
    }

}
