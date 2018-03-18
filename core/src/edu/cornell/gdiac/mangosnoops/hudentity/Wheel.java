package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import edu.cornell.gdiac.mangosnoops.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import edu.cornell.gdiac.mangosnoops.roadentity.Car;

public class Wheel extends HUDObject {
    /** Window height */
    private static final float WINDOW_HEIGHT = 600;
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
    /** Scale for drawing of the wheel **/
    private final float WHEEL_SCALE = 0.5f;

    /** True if the wheel is active TODO: DELETE AFTER GAMEPLAY PROTOTYPE*/
    private boolean active;
    /** Health of the wheel TODO: DELETE AFTER GAMEPLAY PROTOTYPE */
    private int health;
    /** The texture sprite of this wheel */
    private Texture wheelSprite;

    public float getWHEEL_SCALE(){ return WHEEL_SCALE; }
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
     * Returns true if the wheel is currently active. TODO: DELETE LATER
     */
    public boolean isActive() { return active; }

    /**
     * Sets the active status of the wheel. TODO: DELETE LATER
     */
    public void setActive(boolean b) { active = b; }

    /** TODO: delete all these later */
    public void setHealth(int i) { health = i; }
    public int getHealth() { return health; }


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

        // TODO: delete these later
        active = true;
        health = 2;
    }

    /**
     * Rotates the wheel based on a given delta x.
     */
    public void rotate(float dx) {
        // change wheel angle and lateral screen movement
        if(ang >= -90 && ang <= 90) {
            ang -= dx;
        }

        if(ang < -90){
            ang = -90;
        }

        if(ang > 90){
            ang = 90;
        }
    }

    /**
     * Returns true if the mouse is positioned inside the area of the wheel.
     * The wheel must not be null.
     *
     * @param p the vector giving the mouse's (x,y) screen coordinates
     */
    private boolean inWheelArea(Vector2 p) {
        float controlBuffer = 60.0f;
        return p.x > center.x - (wheelSprite.getWidth()*WHEEL_SCALE-controlBuffer)
                && p.x < center.x + (wheelSprite.getWidth()*WHEEL_SCALE*0.5f+controlBuffer)
                && WINDOW_HEIGHT - p.y > center.y - wheelSprite.getHeight()*WHEEL_SCALE*0.5f
                && WINDOW_HEIGHT - p.y < center.y + wheelSprite.getHeight()*WHEEL_SCALE*0.5f;
    }

    public void snapBack(){
        ang = 0;
        return;
    }

    public void draw(GameCanvas canvas){
        if(wheelSprite == null) {
            return;
        }
        float ox = 0.5f * wheelSprite.getWidth();
        float oy = 0.5f * wheelSprite.getHeight();

        canvas.draw(wheelSprite, Color.WHITE, ox, oy, center.x, center.y, ang, WHEEL_SCALE, WHEEL_SCALE);
    }


}
