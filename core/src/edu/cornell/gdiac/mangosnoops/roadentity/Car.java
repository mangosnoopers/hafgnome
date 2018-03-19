package edu.cornell.gdiac.mangosnoops.roadentity;

import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import edu.cornell.gdiac.mangosnoops.hudentity.Child;

public class Car extends RoadObject {
    //CONSTANTS
    /** Factor to translate an angle to left/right movement */
    private static final float ANGLE_TO_LR = 7.0f;
    /** Horizontal speed in X direction -- multiply by movement **/
    private static final float CAR_XSPEED = 4.0f;
    /** How fast we change frames (one frame per 4 calls to update) */
    private static final float ANIMATION_SPEED = 0.25f;
    /** The number of animation frames in our filmstrip */
    private static final int   NUM_ANIM_FRAMES = 2;

    //ATTRIBUTES
    /** The left/right movement of the player this turn -- left is negative */
    private float movement = 0.0f;
    /** Current animation frame for this ship */
    private float animeframe;
    /** Angle of car */
    private float angle;
    /** True if the car is active */
    private boolean active;
    /** Health of the car, max health is 100 */
    private int health;
    /** Angle of the health pointer */
    private float healthPointerAng;

    //PARTY MEMBERS
    /** Noshy boi */
    private Child nosh;
    /** Neddy boi */
    private Child ned;

    public Car() {
        super.setY(-10f);
        angle = 0.0f;
        active = true;
        health = 100;
        nosh = new Child(Child.ChildType.NOSH, 300,300);
        ned = new Child(Child.ChildType.NED, 0, 0);
        healthPointerAng = 0.0f;
    }

    /**
     * Returns the type of this object.
     *
     * We use this instead of runtime-typing for performance reasons.
     *
     * @return the type of this object.
     */
    public ObjectType getType() {
        return ObjectType.CAR;
    }

    /**
     * Returns the current player (left/right) movement input.
     *
     * @return the current player movement input.
     */
    public float getMovement() { return movement; }

    /**
     * Returns the angle of the car.
     */
    public float getAngle() { return angle; }

    /**
     * Returns one Nosh
     * @return reference to Nosh
     */
    public Child getNosh() {
        return nosh;
    }

    /**
     * Returns a reference to Ned
     * @return Ned
     */
    public Child getNed() {
        return ned;
    }

    public boolean noshAwake() { return nosh.isAwake(); }

    public boolean nedAwake() { return ned.isAwake(); }

    public void setHealth(int newHealth) { health = newHealth; }

    /**
     * Returns the angle of the health gauge pointer.
     */
    public float getHealthPointerAng() { return healthPointerAng; }

    /**
     * Returns the car's current health.
     */
    public int getHealth() { return health; }

    /**
     * Updates the animation frame and position of this ship.
     *
     * Notice how little this method does.  It does not actively fire the weapon.  It
     * only manages the cooldown and indicates whether the weapon is currently firing.
     * The result of weapon fire is managed by the GameplayController.
     *
     * @param delta Number of seconds since last animation frame
     */
    public void update(Vector2 clickPos, float delta) {
        // Call superclass's update
        super.update(delta);

        if (movement != 0.0f) {
            position.x += movement * CAR_XSPEED;
        }

        nosh.update(clickPos);
        ned.update(clickPos);

        // Update health angle
        healthPointerAng = Math.max((float) (health - 100), -90.0f);

    }

}
