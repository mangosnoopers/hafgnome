package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.*;


public class Child extends HUDObject {

    /** The type of Gnome this is */
    private ChildType ctype;
    /** If the child is asleep */
    private boolean isAwake;
    /** Number of pokes this child has had. (Zero if awake) */
    private int numPokes;

    private Vector2 pos;
    private Mood currentMood;
    private ObjectMap<Mood,Texture> childTextures;
    private static final int SLACK = 30;

    /** How many clicks needed to wake up */
    private static final int NOSH_NUMCLICKS = 5;
    private static final int NED_NUMCLICKS = 5;

    /**
     * Enum specifying the type of child this is.
     */
    public enum ChildType {
        /** Default Type */
        NOSH,
        NED,
    }
    /*
     * Enum specifying the possible moods
     * the child can have
     */
    public enum Mood {
        HAPPY, NEUTRAL, SAD, CRITICAL
    }

    /*
     * Constructor
     */
    public Child(ChildType type) {
        ctype = type;
        isAwake = true;
        currentMood = Mood.HAPPY;
    }

    /*
     * Returns the mood of the child
     */
    public Mood getCurrentMood(){
        return currentMood;
    }

    /**
     * set the Textures for each of this child's moods
     * @param happy
     * @param neutral
     * @param sad
     * @param critical
     */
    public void setChildTextures(Texture happy, Texture neutral, Texture sad, Texture critical){
        childTextures = new ObjectMap<Mood, Texture>();
        childTextures.put(Mood.HAPPY, happy);
        childTextures.put(Mood.NEUTRAL, neutral);
        childTextures.put(Mood.SAD,sad);
        childTextures.put(Mood.CRITICAL,critical);
    }

    /**
     * Returns true if the mouse is positioned inside the area of the wheel.
     * The wheel must not be null.
     *
     * @param p the vector giving the mouse's (x,y) screen coordinates
     */
    private boolean inChildArea(Vector2 p) {
        //TODO: make the y calculation better
        return (p!=null) && (Math.abs(pos.x-p.x) < SLACK) && (Math.abs(100-p.y) < SLACK);
    }

    /**
     * Returns true if the child is the awake.
     */
    public boolean isAwake() { return isAwake; }

    /**
     * Returns the type of the child.
     */
    public ChildType getType() { return ctype; }

    /**
     * draws the child on the given canvas
     * @param canvas
     */
    public void draw(GameCanvas canvas, Texture rearviewMirror){
        if(currentMood == null || childTextures == null) { return; }

        float rearWidth = rearviewMirror.getWidth() * canvas.getHeight()/(rearviewMirror.getHeight()*3.5f);
        if(ctype == ChildType.NOSH) {
            pos = new Vector2(canvas.getWidth() - rearWidth/3.5f,canvas.getHeight()*0.95f);
        } else {
            pos = new Vector2(canvas.getWidth() - rearWidth/1.5f,canvas.getHeight()*0.95f);
        }

        Texture currentTex = childTextures.get(currentMood);
        float ox = 0.5f* currentTex.getWidth();
        float oy = currentTex.getHeight();

        canvas.draw(currentTex, Color.WHITE, ox, oy, pos.x, pos.y, 0,
                0.5f*(canvas.getHeight()/2.5f)/currentTex.getHeight(),
                0.5f*(canvas.getHeight()/2.5f)/currentTex.getHeight());
    }

    /**
     * Pokes the child once, if the player has clicked the child.
     *
     * @param clickPos
     * @return True if the child is the awake.
     */
    public boolean update(Vector2 clickPos) {
        if(inChildArea(clickPos)) {
            System.out.println("REACHED");
            numPokes++;

            if(isAwake) {
                //FSM to make child less happy
                switch(currentMood) {
                    case HAPPY:
                        currentMood = Mood.NEUTRAL;
                        break;
                    case NEUTRAL:
                        currentMood = Mood.SAD;
                        break;
                    case SAD:
                        currentMood = Mood.CRITICAL;
                        break;
                    default: //don't do anything for critical
                        break;
                }
            }

            if(ctype == ChildType.NOSH) isAwake = (numPokes == NOSH_NUMCLICKS);
            else isAwake = (numPokes == NED_NUMCLICKS);

            if(isAwake) numPokes = 0;
        }

        return isAwake; //TODO: decide if this is useful or should just be void
    }

}
