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
    /** How happy the child is right now */
    private float happiness;
    /** If the child is asleep */
    private boolean isAwake;
    /** Number of pokes this child has had. (Zero if awake) */
    private int numPokes;

    private Vector2 pos;
    private Mood currentMood;
    private ObjectMap<Mood,Texture> childTextures;
    private static final float CHILD_SCALE = 0.5f;

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
        happiness = 1.0f;
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
        return (p!=null) && false;
//                ((ctype == ChildType.NOSH) //add isNoshArea
//                || (ctype == ChildType.NED)); //add isNedArea
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

        Texture currentTex = childTextures.get(currentMood);
        float ox = 0.5f* currentTex.getWidth();
        float oy = currentTex.getHeight();

        if(ctype == ChildType.NOSH) {
            canvas.draw(currentTex, Color.WHITE, ox, oy, canvas.getWidth() - rearWidth/3.5f,canvas.getHeight()*0.95f, 0,
                    0.5f*(canvas.getHeight()/2.5f)/currentTex.getHeight(),
                    0.5f*(canvas.getHeight()/2.5f)/currentTex.getHeight());
        } else {
            canvas.draw(currentTex, Color.WHITE, ox, oy, canvas.getWidth() - rearWidth/1.5f,canvas.getHeight()*0.95f, 0,
                    0.5f*(canvas.getHeight()/2.5f)/currentTex.getHeight(),
                    0.5f*(canvas.getHeight()/2.5f)/currentTex.getHeight());
        }
    }

    /**
     * Pokes the child once, if the player has clicked the child.
     *
     * @param clickPos
     * @return True if the child is the awake.
     */
    public boolean update(Vector2 clickPos) {
        if(inChildArea(clickPos)) {
            numPokes++;

            if(ctype == ChildType.NOSH) isAwake = (numPokes == NOSH_NUMCLICKS);
            else isAwake = (numPokes == NED_NUMCLICKS);

            if(isAwake) numPokes = 0;
        }

        return isAwake;
    }

}
