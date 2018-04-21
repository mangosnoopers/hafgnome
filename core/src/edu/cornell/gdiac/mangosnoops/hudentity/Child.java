package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.FilmStrip;

import java.util.Random;


public class Child extends Image{

    /** The type of Gnome this is */
    private ChildType ctype;

    private ObjectMap<Mood,FilmStrip> childTextures;

    private boolean gettingSad; //is gradually getting happier
    private boolean gettingHappy; //is gradually getting sadder
    private static final int MOOD_DELTA = 1; //how much happiness changes each step

    /**
     * Enum specifying the type of child this is.
     */
    public enum ChildType {
        /** Default Type */
        NOSH,
        NED,
    }


    /** Speech bubble offset for shaky effect */
    private float speechBubbleOffsetX = 3;
    private float speechBubbleOffsetY = -2;

    /** Spped for child animation */
    private static float ANIMATION_SPEED;

    /** Current animation frame for animations */
    private float animationFrame;

    /** How many animation frames there are */
    private static final int NUM_ANIMATION_FRAMES = 2;

    /** The current FilmStrip */
    private FilmStrip currentFilmStrip;

    public float getShakeX() { return speechBubbleOffsetX; }
    public float getShakeY() { return speechBubbleOffsetY; }

    /** Coordinates of speech bubbles for Ned and Nosh */
    public static Vector2 NED_SPEECH_BUBBLE_COORDS;
    public static Vector2 NOSH_SPEECH_BUBBLE_COORDS;

    /** Speech bubble offset for scaling effect */
    private float deltaSum = 0;
    private float scale = 0;

    /** Represents the current mood. 0-100 means they are awake, -100 they are asleep.*/
    private int happiness;
    /** Lower bounds of happiness states before they change (exclusive) */
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
    public Child(ChildType type) {
        super(0,0,0, null);
        if(type == ChildType.NED){
            position = new Vector2(0.76f, 0.83f);
        } else{
            position = new Vector2(0.915f, 0.81f);
        }
        Random rand = new Random();
        int animSpeedInt = rand.nextInt(5) + 20;
        ANIMATION_SPEED = animSpeedInt / 100;
        ctype = type;
        happiness = HAPPY_UBOUND;
    }

    /**
     * Returns the mood of the child
     */
    public Mood getCurrentMood() {
        if(happiness > HAPPY_LBOUND) return Mood.HAPPY;
        else if(happiness > NEUTRAL_LBOUND) return Mood.NEUTRAL;
        else if(happiness > SAD_LBOUND) return Mood.SAD;
        else if(happiness >= 0) return Mood.CRITICAL;
        else return Mood.SLEEP;
    }

    /**
     * Returns true if the child is the awake.
     */
    public boolean isAwake() { return happiness >= 0; }

    /**
     * Sets the mood of the child, which also entails setting awake/not awake;
     *
     * @param m what Mood they'll be in when awoken
     *          TODO: should this be upper/lower bound of mood? rn upper
     */
    public void setMood(Mood m) {
        switch(m){
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
     *        happier is true if they are getting happier, false getting angrier
     */
    public void setMoodShifting(boolean set, boolean happier) {
        if(set && isAwake()) {
            if(happier) {
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
     *  Sets the child to catch some zzzz's.
     */
    public void setAsleep() {
        happiness = -HAPPY_UBOUND;
    }

    /**
     * Returns the type of the child.
     * TODO: might remove cause the ned/nosh instantiations are already distinguished
     */
    public ChildType getType() { return ctype; }

    /**
     * set the Textures for each of this child's moods
     * @param happy
     * @param neutral
     * @param sad
     * @param critical
     */
    public void setChildFilmStrips(FilmStrip happy, FilmStrip neutral, FilmStrip sad, FilmStrip critical, FilmStrip sleep){
        childTextures = new ObjectMap<Mood, FilmStrip>();
        childTextures.put(Mood.HAPPY, happy);
        childTextures.put(Mood.NEUTRAL, neutral);
        childTextures.put(Mood.SAD,sad);
        childTextures.put(Mood.CRITICAL,critical);
        childTextures.put(Mood.SLEEP,sleep);
    }

    /**
     *
     *
     * @param in
     * @return True if the child is the awake.
     */
    private Mood prevMood;
    public void update(float delta, Vector2 in) {
        animationFrame += ANIMATION_SPEED;
        if (animationFrame >= NUM_ANIMATION_FRAMES) {
            animationFrame -= NUM_ANIMATION_FRAMES;
        }


        if(isAwake()) { //TODO: may not need to check isAwake, this is a security blanket lol
            if(gettingHappy) {
                happiness += MOOD_DELTA;
                if(happiness > HAPPY_UBOUND) {
                    happiness = HAPPY_UBOUND;
                    gettingHappy = false;
                }
            } else if(gettingSad) {
                happiness -= MOOD_DELTA;
                if(happiness < 0) {
                    happiness = 0;
                    gettingSad = false;
                }
            }
        }

        if(getCurrentMood()==Mood.CRITICAL){
            speechBubbleOffsetX *= -1;
            speechBubbleOffsetY *= -1;
           if(prevMood!=getCurrentMood()){
               if(this.ctype == ChildType.NED){
                  NED_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f,0.65f),MathUtils.random(0.45f,0.75f));
                  if(NOSH_SPEECH_BUBBLE_COORDS != null){
                      while(Math.abs(NED_SPEECH_BUBBLE_COORDS.x-NOSH_SPEECH_BUBBLE_COORDS.x)<0.1f || Math.abs(NED_SPEECH_BUBBLE_COORDS.y-NOSH_SPEECH_BUBBLE_COORDS.y)<0.1f ){
                          NED_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f,0.65f),MathUtils.random(0.45f,0.75f));
                      }
                  }
                   System.out.println(NED_SPEECH_BUBBLE_COORDS);
               }
               if(this.ctype == ChildType.NOSH){
                   NOSH_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f,0.65f),MathUtils.random(0.45f,0.75f));
                   if(NED_SPEECH_BUBBLE_COORDS != null) {
                       while (Math.abs(NED_SPEECH_BUBBLE_COORDS.x - NOSH_SPEECH_BUBBLE_COORDS.x) < 0.1f || Math.abs(NED_SPEECH_BUBBLE_COORDS.y - NOSH_SPEECH_BUBBLE_COORDS.y) < 0.1f) {
                           NOSH_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f, 0.65f), MathUtils.random(0.45f, 0.75f));
                       }
                   }
                   System.out.println(NOSH_SPEECH_BUBBLE_COORDS);

               }
           }
        }
        prevMood = getCurrentMood();

        deltaSum += delta;

        scale = 1f + (float) (0.2*Math.sin(8*deltaSum));

    }

    /**
     * draws the child on the given canvas
     * @param canvas
     */
    @Override
    public void draw(GameCanvas canvas) {
        if(childTextures == null) { return; }

        /* FIXME: rearWidth used to be based on rearview mirror but it's different now */
        float rearWidth = 50 * canvas.getHeight()/(50*3.5f);

        currentFilmStrip = childTextures.get(getCurrentMood());
        currentFilmStrip.setFrame((int) animationFrame);
        float ox = 0.5f* currentFilmStrip.getRegionWidth();
        float oy = 0.5f* currentFilmStrip.getRegionHeight();
        float drawY = position.y * canvas.getHeight() + currentShakeAmount;

        canvas.draw(currentFilmStrip, Color.WHITE, ox, oy, position.x*canvas.getWidth(), drawY, 0,
                0.5f*(canvas.getHeight()/2.5f)/currentFilmStrip.getRegionHeight(),
                0.5f*(canvas.getHeight()/2.5f)/currentFilmStrip.getRegionHeight());
        }


    public void drawSpeechBubble(GameCanvas canvas, Texture speechBubble) {
        if (getCurrentMood() == Mood.CRITICAL) {
            float speechX;
            float speechY;
            ;
            if (getType() == ChildType.NED) {

                canvas.draw(speechBubble, Color.WHITE, speechBubble.getWidth()*0.5f, speechBubble.getHeight()*0.5f,
                        Child.NED_SPEECH_BUBBLE_COORDS.x*canvas.getWidth(), Child.NED_SPEECH_BUBBLE_COORDS.y*canvas.getHeight(),
                        0, 0.9f*scale*(float)canvas.getHeight()/(float)speechBubble.getWidth(), 0.9f*scale*(float)canvas.getHeight()/(float)speechBubble.getHeight() );
            }
            else {
                canvas.draw(speechBubble, Color.WHITE, speechBubble.getWidth()*0.5f, speechBubble.getHeight()*0.5f,
                        Child.NOSH_SPEECH_BUBBLE_COORDS.x*canvas.getWidth(), Child.NOSH_SPEECH_BUBBLE_COORDS.y*canvas.getHeight(),
                        0, 0.9f*scale*(float)canvas.getHeight()/(float)speechBubble.getWidth(), 0.9f*scale*(float)canvas.getHeight()/(float)speechBubble.getHeight() );
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
        in.y = SCREEN_DIMENSIONS.y - in.y;
        return (in.x <= position.x*SCREEN_DIMENSIONS.x + currentFilmStrip.getRegionWidth()*0.5f)
                && (in.x <= position.x*SCREEN_DIMENSIONS.x + currentFilmStrip.getRegionWidth()*0.5f)
                && (in.x >= position.x*SCREEN_DIMENSIONS.x - currentFilmStrip.getRegionWidth()*0.5f)
                && (in.y <= position.y*SCREEN_DIMENSIONS.y + currentFilmStrip.getRegionHeight()*0.5f)
                && (in.y >= position.x*SCREEN_DIMENSIONS.x - currentFilmStrip.getRegionHeight()*0.5f);
    }

}
