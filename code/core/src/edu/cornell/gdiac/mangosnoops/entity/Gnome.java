package edu.cornell.gdiac.mangosnoops.entity;

import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

public class Gnome extends GameObject{
    //CONSTANTS
    /** How fast we change frames (one frame per 4 calls to update) */
    private static final float ANIMATION_SPEED = 0.25f;
    /** The number of animation frames in our filmstrip */
    private static final int   NUM_ANIM_FRAMES = 2;

    //ATTRIBUTES
    /** Current animation frame for this ship */
    private float animeframe;
    /** The type of Gnome this is */
    private GnomeType gtype;

    /**
     * Enum specifying the type of gnome this is.
     *
     * (For after Gameplay Prototype)
     */
    public enum GnomeType {
        /** Default Type */
        BASIC,
    }

    /**
     * Returns the type of this object.
     *
     * We use this instead of runtime-typing for performance reasons.
     *
     * @return the type of this object.
     */
    public ObjectType getType() {
        return ObjectType.GNOME;
    }

    /**
     * Returns the Gnome type of this gnome.
     *
     * @return the Gnome type of this object.
     */
    public GnomeType getGnomeType() {
        return gtype;
    }

    /**
     * Sets the Gnome type of this gnome.
     */
    public void setGnomeType(GnomeType type) {
        gtype = type;
    }

    public Gnome() {
        animeframe = 0.0f;
        gtype = GnomeType.BASIC;
    }

    public void setTexture(Texture texture) {
        animator = new FilmStrip(texture,1,2,2);
        radius = animator.getRegionHeight() / 2.0f;
        origin = new Vector2(animator.getRegionWidth()/2.0f, animator.getRegionHeight()/2.0f);
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
    public void update(float delta) {
        // Call superclass's update
        super.update(delta);

        animeframe += ANIMATION_SPEED;
        if (animeframe >= NUM_ANIM_FRAMES) {
            animeframe -= NUM_ANIM_FRAMES;
        }
    }

    /**
     * Draws this shell to the canvas
     *
     * There is only one drawing pass in this application, so you can draw the objects
     * in any order.
     *
     * @param canvas The drawing context
     */
    public void draw(GameCanvas canvas) {
        float x = animator.getRegionWidth()/2.0f;
        float y = animator.getRegionHeight()/2.0f;
        animator.setFrame((int)animeframe);
        canvas.draw(animator, Color.WHITE, x, y, position.x, position.y, 0.0f, 1.0f, 1.f);
    }
}
