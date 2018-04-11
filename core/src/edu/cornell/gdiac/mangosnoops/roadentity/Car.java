package edu.cornell.gdiac.mangosnoops.roadentity;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.mangosnoops.hudentity.Wheel;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import edu.cornell.gdiac.mangosnoops.hudentity.Child;

public class Car extends RoadObject {
    //CONSTANTS
    /** Factor to translate an angle to left/right movement */
    private static final float ANGLE_TO_LR = 7.0f;
    /** Horizontal speed in X direction -- multiply by movement **/
    private static final float CAR_XSPEED = 0.1f;
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

    /** Start position of car. */
    private Vector2 CAR_START_POS = new Vector2(0, -10f);

    /** Bounds for the car. */
    private float LEFT_X_BOUND = -0.25f;
    private float RIGHT_X_BOUND = 0.25f;

    /** Indicates whether or not to color screen red, visualizing
     *  that the car has taken damage */
    private boolean isDamaged;

    private float timeToDisplayDamageIndicator;

    /** How much to decrement timeToDisplayDamageIndicator each frame
     *  (higher value => less time for damage indicator) */
    private final float DISPLAY_DEPLETION = 26;

    private float displayAlpha = 1.0f;

    //PARTY MEMBERS
    /** Noshy boi */
    private Child nosh;
    /** Neddy boi */
    private Child ned;

    public Car() {
        angle = 0.0f;
        active = true;
        health = 100;
        nosh = new Child(Child.ChildType.NOSH);
        ned = new Child(Child.ChildType.NED);
        healthPointerAng = 50.0f;

        timeToDisplayDamageIndicator = 10;

        position = CAR_START_POS;
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
     * Damage the car. Only has an effect if the car isn't already
     * being damaged.
     *
     * The car must display the entire damage animation before it can
     * be damaged again (the "damage animation" refers to the red flash
     * thing).
     */
    public void damage() {
        if (!isDamaged) {
            isDamaged = true;
            timeToDisplayDamageIndicator = 10;
            setHealth(getHealth() - 10);
        }
    }

    public boolean getIsDamaged() { return isDamaged; }

    public float getDamageDisplayAlpha() { return displayAlpha; }

    /**
     * Returns the angle of the health gauge pointer.
     */
    public float getHealthPointerAng() { return healthPointerAng; }

    /**
     * Returns the car's current health.
     */
    public int getHealth() { return health; }

    /**
     * Reset the car to restart the game.
     */
    public void reset() {
        angle = 0.0f;
        active = true;
        health = 100;
        nosh = new Child(Child.ChildType.NOSH);
        ned = new Child(Child.ChildType.NED);
        healthPointerAng = 50.0f;

        timeToDisplayDamageIndicator = 10;
    }

    /**
     * Updates the animation frame and position of this ship.
     *
     * Notice how little this method does.  It does not actively fire the weapon.  It
     * only manages the cooldown and indicates whether the weapon is currently firing.
     * The result of weapon fire is managed by the GameplayController.
     *
     * @param delta Number of seconds since last animation frame
     */
    public void update(Vector2 clickPos, Wheel wheel, float delta) {
        // Call superclass's update
        super.update(delta);
//        System.out.println(position);

        position.x += wheel.getHorizontalMovement() * delta * -1 * CAR_XSPEED;

        if (position.x < LEFT_X_BOUND) {
            position.x = LEFT_X_BOUND;
        }
        if (position.x > RIGHT_X_BOUND) {
            position.x = RIGHT_X_BOUND;
        }

        nosh.update(clickPos);
        ned.update(clickPos);

        // Update health angle
        healthPointerAng = Math.max((float) (health - 50), -50.0f);

        if (isDamaged && timeToDisplayDamageIndicator > 0) {
            timeToDisplayDamageIndicator -= delta * DISPLAY_DEPLETION;
            displayAlpha -= delta * 2;
        } else {
            isDamaged = false;
            timeToDisplayDamageIndicator = 10;
            displayAlpha = 1.0f;
        }

    }

}
