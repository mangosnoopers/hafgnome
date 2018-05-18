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
        super(x, y ,relSize, cb, tex, GameCanvas.TextureOrigin.MIDDLE);
    }

    /** Translates an angle to left/right movement */
    public float getHorizontalMovement() {
        return ang / ANGLE_TO_LR;
    }

    public float getAng() { return ang; }

    /**
     * Updates the wheel based on the user's input.
     * @param in where the mouse clicked (null if no click)
     * @param dx the change in x in the user's input
     */
    public boolean update(Vector2 in, float dx, boolean keyboardTurn) {
        // change wheel angle and lateral screen movement
        if ((in != null && inArea(in)) || (keyboardTurn && dx != 0)) {
            if (ang >= -90 && ang <= 90) {
                ang -= dx/2.0f;
            }

            if (ang < -90) {
                ang = -90;
            }

            if (ang > 90) {
                ang = 90;
            }
            return true;

        } else {
            // Move back to original angle
            ang = ang + 0.1f * -ang;
            return false;
        }
    }

    @Override
    public void draw(GameCanvas canvas){
        if(texture == null) {
            return;
        }

        super.draw(canvas, ang);
    }

    public String toString(){
        return "Wheel";
    }

}
