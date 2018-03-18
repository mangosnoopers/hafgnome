package edu.cornell.gdiac.mangosnoops.hudentity;

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
    /** The texture sprite of this wheel */
    private Texture wheelSprite;

    /**
     * Return the current angle of the wheel.
     */
    public float getAng() { return ang; }

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
    }

    /**
     * Updates the wheel based on the user's input.
     *
     * @param in where the mouse clicked (null if no click)
     * @param dx the change in x in the user's input
     */
    public void update(Vector2 in, float dx) {
        // change wheel angle and lateral screen movement
        if (in != null && inWheelArea(in)) {
            if (ang >= -90 && ang <= 90) {
                ang -= dx;
            }

            if (ang < -90) {
                ang = -90;
            }

            if (ang > 90) {
                ang = 90;
            }
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

    public void snapBack() {
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
