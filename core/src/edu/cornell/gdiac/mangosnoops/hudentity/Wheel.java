package edu.cornell.gdiac.mangosnoops.hudentity;

import edu.cornell.gdiac.mangosnoops.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import edu.cornell.gdiac.mangosnoops.roadentity.Car;

public class Wheel extends Image {

    /** Current angle of the wheel */
    private float ang = 0;

    /** Factor to translate an angle to left/right movement */
    private static final float ANGLE_TO_LR = 7.0f;


    public Wheel(float x, float y, float relSize, float cb, Texture tex){
        super(x, y ,relSize, cb, tex);
    }

    /** Translates an angle to left/right movement */
    public float getHorizontalMovement() {
        return ang / ANGLE_TO_LR;
    }

    /**
     * Updates the wheel based on the user's input.
     * @param in where the mouse clicked (null if no click)
     * @param dx the change in x in the user's input
     */
    public void update(Vector2 in, float dx) {
        // change wheel angle and lateral screen movement
        if ((in != null && inArea(in)) || dx != 0) {
            if (ang >= -90 && ang <= 90) {
                ang -= dx;
            }

            if (ang < -90) {
                ang = -90;
            }

            if (ang > 90) {
                ang = 90;
            }

        } else {
            // Move back to original angle
            ang = ang + 0.2f * -ang;
        }
    }

    @Override
    public void draw(GameCanvas canvas){
        if(texture == null) {
            return;
        }

        float ox = 0.5f * texture.getWidth();
        float oy = 0.5f * texture.getHeight();

        canvas.draw(texture, Color.WHITE, ox, oy, position.x*canvas.getWidth(), currentShakeAmount+position.y*canvas.getHeight(), ang, relativeScale*canvas.getHeight(), relativeScale*canvas.getHeight());
    }

    public String toString(){
        return "Wheel";
    }

}
