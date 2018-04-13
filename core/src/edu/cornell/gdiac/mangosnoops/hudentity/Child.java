package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.*;


public class Child {

    /** The type of Gnome this is */
    private ChildType ctype;

    private Vector2 pos;
    private ObjectMap<Mood,Texture> childTextures;
    private static final int SLACK = 30; //pixel leeway in inChildArea TODO maybe remove

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

    public float getShakeX() { return speechBubbleOffsetX; }
    public float getShakeY() { return speechBubbleOffsetY; }

    /** Coordinates of speech bubbles for Ned and Nosh */
    public static Vector2 NED_SPEECH_BUBBLE_COORDS;
    public static Vector2 NOSH_SPEECH_BUBBLE_COORDS;

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
    public void setChildTextures(Texture happy, Texture neutral, Texture sad, Texture critical, Texture sleep){
        childTextures = new ObjectMap<Mood, Texture>();
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
    public void update(Vector2 in) {
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
                      while(Math.abs(NED_SPEECH_BUBBLE_COORDS.x-NOSH_SPEECH_BUBBLE_COORDS.x)<0.05f || Math.abs(NED_SPEECH_BUBBLE_COORDS.y-NOSH_SPEECH_BUBBLE_COORDS.y)<0.05f ){
                          NED_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f,0.65f),MathUtils.random(0.45f,0.75f));
                      }
                  }
                   System.out.println(NED_SPEECH_BUBBLE_COORDS);
               }
               if(this.ctype == ChildType.NOSH){
                   NOSH_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f,0.65f),MathUtils.random(0.45f,0.75f));
                   if(NED_SPEECH_BUBBLE_COORDS != null) {
                       while (Math.abs(NED_SPEECH_BUBBLE_COORDS.x - NOSH_SPEECH_BUBBLE_COORDS.x) < 0.05f || Math.abs(NED_SPEECH_BUBBLE_COORDS.y - NOSH_SPEECH_BUBBLE_COORDS.y) < 0.05f) {
                           NOSH_SPEECH_BUBBLE_COORDS = new Vector2(MathUtils.random(0.3f, 0.65f), MathUtils.random(0.45f, 0.75f));
                       }
                   }
                   System.out.println(NOSH_SPEECH_BUBBLE_COORDS);

               }
           }
        }
        prevMood = getCurrentMood();
    }

    /**
     * draws the child on the given canvas
     * @param canvas
     */
    public void draw(GameCanvas canvas, Texture rearviewMirror){
        if(childTextures == null) { return; }

        float rearWidth = rearviewMirror.getWidth() * canvas.getHeight()/(rearviewMirror.getHeight()*3.5f);
        if(ctype == ChildType.NOSH) {
            pos = new Vector2(canvas.getWidth() - rearWidth/3.5f,canvas.getHeight()*0.95f);
        } else {
            pos = new Vector2(canvas.getWidth() - rearWidth/1.5f,canvas.getHeight()*0.95f);
        }

        Texture currentTex = childTextures.get(getCurrentMood());
        float ox = 0.5f* currentTex.getWidth();
        float oy = currentTex.getHeight();

        canvas.draw(currentTex, Color.WHITE, ox, oy, pos.x, pos.y, 0,
                0.5f*(canvas.getHeight()/2.5f)/currentTex.getHeight(),
                0.5f*(canvas.getHeight()/2.5f)/currentTex.getHeight());
    }

    /**
     * Returns true if the mouse is positioned inside the area of the wheel.
     * The wheel must not be null.
     *
     * @param p the vector giving the mouse click's (x,y) screen coordinates
     */
    public boolean inChildArea(Vector2 p) {
        //TODO: do we need this? Currently cannot click on children
        return (p!=null) && (Math.abs(pos.x-p.x) < SLACK) && (Math.abs(100-p.y) < SLACK);
    }

}
