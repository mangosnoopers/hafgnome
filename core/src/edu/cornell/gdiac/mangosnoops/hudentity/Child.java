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
    private mood currentMood;
    private ObjectMap<mood,Texture> childTextures;
    private static final float CHILD_SCALE = 1f;

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
    public enum mood {
        happy, neutral, sad, critical
    }

    /*
     * Constructor
     */
    public Child(ChildType type, int x, int y) {
        ctype = type;
        isAwake = true;
        happiness = 1.0f;
        pos = new Vector2(x,y);
        currentMood = mood.happy;
    }

    /*
     * Returns the mood of the child
     */
    public mood getCurrentMood(){
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
        childTextures = new ObjectMap<mood, Texture>();
        childTextures.put(mood.happy, happy);
        childTextures.put(mood.neutral, neutral);
        childTextures.put(mood.sad,sad);
        childTextures.put(mood.critical,critical);
    }


    /**
     * Returns true if the mouse is positioned inside the area of the wheel.
     * The wheel must not be null.
     *
     * @param p the vector giving the mouse's (x,y) screen coordinates
     */
    private boolean inChildArea(Vector2 p) {
        return (p!=null) &&
                ((ctype == ChildType.NOSH) //add isNoshArea
                || (ctype == ChildType.NED)); //add isNedArea
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
    public void draw(GameCanvas canvas){
        if(currentMood == null) {
            return;
        }
        else if(childTextures == null){
            return;
        }

        Texture currentTex = childTextures.get(currentMood);
        float ox = 0.5f * currentTex.getWidth();
        float oy = 0.5f * currentTex.getHeight();

        canvas.draw(currentTex, Color.WHITE, ox, oy, pos.x, pos.y, 0, CHILD_SCALE, CHILD_SCALE);
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
