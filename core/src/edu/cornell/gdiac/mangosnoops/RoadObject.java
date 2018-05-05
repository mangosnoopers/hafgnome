/*
 *  GameObject.java
 *
 * In this application, we have a lot more model classes than we did in
 * previous labs.  Because these classes have a lot in common, we have
 * put their features into a base class, just as you learned in OO programming.
 *
 * With that said, you have to be very careful when subclassing your models.
 * Your hierarchy can get deep and complicated very fast if you are not
 * careful.  In fact, we have a later lecture about how subclassing is
 * not always a good idea. But it is okay in this instance because we are
 * only subclass one-level deep.
 *
 * This class continues our policy of using "passive" models. It does not
 * access the methods or fields of any other Model class.  It also
 * does not store any other model object as a field. This allows us
 * to prevent the models from being tightly coupled.  All of the coupled
 * behavior has been moved to GameplayController.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import edu.cornell.gdiac.util.*;


/**
 * Base class for all Model objects in the game.
 */
public abstract class RoadObject {

    /**
     * Enum specifying the type of this game object.
     *
     * This Enum is not strictly necessary.  We could use runtime-time
     * typing instead.  However, enums can be used in switch statements
     * (which are very fast), which types cannot. That is the motivation
     * for this Enum.
     * If you add new subclasses of GameObject, you will need to add
     * to this Enum as well.
     */
    public enum ObjectType {
        /** A gnome, currently immortal */
        GNOME,
        FLAMINGO,
        GRILL,
        IMAGE, // Billboards, corn, etc
        /** The player car, which lives until it collides with a gnome */
        CAR,
        FLAME
    }

    // Attributes for all game objects
    /** Object position (centered on the texture middle) */
    protected Vector2 position;
    /** Object velocity vector */
    protected Vector2 velocity;
    /** Reference to texture origin */
    protected Vector2 origin;
    /** Radius of the object (used for collisions) */
    protected float radius;
    /** Whether or not the object should be removed at next timestep. */
    protected boolean destroyed;
    /** CURRENT image for this object. May change over time. */
    protected FilmStrip animator;

    // ACCESSORS
    public void setTexture(Texture texture) {
        animator = new FilmStrip(texture,1,1,1);
        radius = animator.getRegionHeight() / 2.0f;
        origin = new Vector2(animator.getRegionWidth()/2.0f, animator.getRegionHeight()/2.0f);
    }

    public Texture getTexture() {
        return animator == null ? null : animator.getTexture();
    }

    /**
     * Returns the position of this object (e.g. location of the center pixel)
     *
     * The value returned is a reference to the position vector, which may be
     * modified freely.
     *
     * @return the position of this object
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Returns the x-coordinate of the object position (center).
     *
     * @return the x-coordinate of the object position
     */
    public float getX() {
        return position.x;
    }

    /**
     * Sets the x-coordinate of the object position (center).
     *
     * @param value the x-coordinate of the object position
     */
    public void setX(float value) {
        position.x = value;
    }

    /**
     * Returns the y-coordinate of the object position (center).
     *
     * @return the y-coordinate of the object position
     */
    public float getY() {
        return position.y;
    }

    /**
     * Sets the y-coordinate of the object position (center).
     *
     * @param value the y-coordinate of the object position
     */
    public void setY(float value) {
        position.y = value;
    }

    /**
     * Returns the velocity of this object in pixels per animation frame.
     *
     * The value returned is a reference to the velocity vector, which may be
     * modified freely.
     *
     * @return the velocity of this object
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * Returns the x-coordinate of the object velocity.
     *
     * @return the x-coordinate of the object velocity.
     */
    public float getVX() {
        return velocity.x;
    }

    /**
     * Sets the x-coordinate of the object velocity.
     *
     * @param value the x-coordinate of the object velocity.
     */
    public void setVX(float value) {
        velocity.x = value;
    }

    /**
     * Gets the y-coordinate of the object velocity.
     */
    public float getVY() {
        return velocity.y;
    }

    /**
     * Sets the y-coordinate of the object velocity.
     *
     * @param value the y-coordinate of the object velocity.
     */
    public void setVY(float value) {
        velocity.y = value;
    }


    /**
     * Returns true if this object is destroyed.
     *
     * Objects are not removed immediately when destroyed.  They are garbage collected
     * at the end of the frame.  This tells us whether the object should be garbage
     * collected at the frame end.
     *
     * @return true if this object is destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Sets whether this object is destroyed.
     *
     * Objects are not removed immediately when destroyed.  They are garbage collected
     * at the end of the frame.  This tells us whether the object should be garbage
     * collected at the frame end.
     *
     * @param value whether this object is destroyed
     */
    public void setDestroyed(boolean value) {
        destroyed = value;
    }

    /**
     * Returns the radius of this object.
     *
     * All of our objects are circles, to make collision detection easy.
     *
     * @return the radius of this object.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Returns the type of this object.
     *
     * We use this instead of runtime-typing for performance reasons.
     *
     * @return the type of this object.
     */
    public abstract ObjectType getType();

    /**
     * Constructs a trivial game object
     *
     * The created object has no position or size.  These should be set by the subclasses.
     */
    public RoadObject() {
        position = new Vector2(0.0f, 0.0f);
        velocity = new Vector2(0.0f, 0.0f);
        radius = 0.0f;
        destroyed = false;
    }

    /**
     * Updates the state of this object.
     *
     * This method only is only intended to update values that change local state in
     * well-defined ways, like position or a cooldown value.  It does not handle
     * collisions (which are determined by the CollisionController).  It is
     * not intended to interact with other objects in any way at all.
     *
     * @param delta Number of seconds since last animation frame
     */
    public void update(float delta) {
        if (getType() == ObjectType.FLAME) {
            System.out.println(position);
        }
        position.add(velocity);
    }

    /**
     * Draws this object to the canvas
     *
     * There is only one drawing pass in this application, so you can draw the objects
     * in any order.
     *
     * @param canvas The drawing context
     */
    public void draw(GameCanvas canvas) {
        canvas.draw(animator, Color.WHITE, origin.x, origin.y,
                position.x, position.y, 0.0f, 1.0f, 1.f);
    }

}