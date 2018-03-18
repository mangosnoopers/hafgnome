package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.math.Vector2;
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

    public Child(ChildType type) {
        ctype = type;
        isAwake = true;
        happiness = 1.0f;
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
