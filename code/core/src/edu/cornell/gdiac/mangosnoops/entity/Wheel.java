package edu.cornell.gdiac.mangosnoops.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

public class Wheel {

    /** Coordinates of wheel center */
    private static Vector2 center;
    /** Dimensions of wheelzone */
    private static Vector2 wheelZone;
    /** Vector from center of wheel to its outer radius */
    private static Vector2 outerRadius;
    /** Vector from center of wheel to its inner radius */
    private static Vector2 innerRadius;
    /** Current angle of the wheel */
    private float ang = 0;
    /** The Car that this wheel belongs to */
    private Car car;
    /** True if the wheel is active TODO: DELETE AFTER GAMEPLAY PROTOTYPE*/
    private boolean active;
    /** The texture sprite of this wheel */
    private Texture wheelSprite;

    /**
     * Return the current angle of the wheel.
     */
    public float getAng() { return ang; }

    /**
     * Set the angle of the wheel.
     * @param a The angle to set the wheel to
     */
    public void setAng(float a) { ang = a; }

    /**
     * Return the wheel sprite texture.
     */
    public Texture getWheelSprite() { return wheelSprite; }

    /**
     * Sets the image texture for this wheel
     */
    public void setWheelSprite(Texture tex) { wheelSprite = tex; }

    /**
     * Returns the center coordinates of the wheel sprite.
     */
    public Vector2 getCenter() { return center; }

    /**
     * Returns true if the wheel is currently active.
     */
    public boolean isActive() { return active; }

    /**
     * Sets the active status of the wheel.
     */
    public void setActive(boolean b) { active = b; }

    /** Creates a Wheel with a center at screen coordinates (x,y).
     *
     * @param x The screen x-coordinate of the center
     * @param y The screen y-coordinate of the center
     */
    public Wheel(float x, float y) {
        // TODO: use these to detect wheel bounds in inputcontroller instead?
        wheelZone = new Vector2();
        outerRadius = new Vector2();
        innerRadius = new Vector2();
        center = new Vector2(x,y);
        active = true;
    }

    /**
     * Rotate wheel by an angle theta
     * */
    public void rotate(float theta){ return; }

    public void snapBack(){
        ang = 0;
        return;
    }

    public void drawWheel(GameCanvas canvas){
        if(wheelSprite == null) {
            return;
        }
        float ox = 0.5f * wheelSprite.getWidth();
        float oy = 0.5f * wheelSprite.getHeight();

        canvas.draw(wheelSprite, Color.WHITE, ox, oy, center.x, center.y, ang, 1, 1);
    }


}
