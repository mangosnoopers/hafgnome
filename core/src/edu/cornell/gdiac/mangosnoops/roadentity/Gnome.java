package edu.cornell.gdiac.mangosnoops.roadentity;

import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

public class Gnome extends RoadObject{
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
    /** The actual screen coordinate the gnome is drawn on */
    private Vector2 drawCoords;

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
     * Return the y screen coordinate that this gnome is drawn at.
     */
    public Vector2 getDrawCoords() { return drawCoords; }

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

    public Gnome(float x, float y) {
        this.setX(x);
        this.setY(y);
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

        setY(getY()-10*delta);
        if (getY() < -12) {
            setY(14);
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
    public void draw(GameCanvas canvas, Vector3 camera, Vector2 scale, float angle, float screenWidth, float screenHeight) {
        float x = animator.getRegionWidth()/2.0f;
        float y = animator.getRegionHeight()/2.0f;
        animator.setFrame((int)animeframe);

        // Project to 3d perspective here

        // 1.) Get position relative to camera
        double posX = getX() - camera.x;
        double posY = getY() - camera.y;

        // 2.) Rotate based on angle
        double rotatedX = Math.cos(angle) * posX + Math.sin(angle) * posY;
        double rotatedY = Math.cos(angle) * posY - Math.sin(angle) * posX;

        // 3.) Scale width/height based on position, scaling
        int scaledWidth = (int) (animator.getRegionWidth() * scale.x / rotatedX);
        int scaledHeight = (int) (animator.getRegionHeight() * scale.y / rotatedX);

        // 4.) Scale x/y based on position, scaling
        int drawX = (int) (scale.x / rotatedX * rotatedY + screenWidth / 2) - scaledWidth / 2;
        int drawY = (int) ((camera.z * scale.y) / rotatedX) + (int) screenHeight - 200 - scaledHeight;
        drawCoords = new Vector2(drawX,drawY);

        if (drawX > 0) {
            // 5.) draw sprite with x,y,width, height
            canvas.draw(animator, drawX, drawY, scaledWidth, scaledHeight);
        } else {
            this.setDestroyed(true);
        }
    }
}
