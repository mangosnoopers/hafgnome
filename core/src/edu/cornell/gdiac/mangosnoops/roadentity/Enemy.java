package edu.cornell.gdiac.mangosnoops.roadentity;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

import java.util.Random;

public abstract class Enemy extends RoadObject{
    // CONSTANTS
    /** How fast we change frames (one frame per 4 calls to update) */
    private float animationSpeed = 0.25f;
    /** The number of animation frames in our filmstrip */
    private int   numAnimationFrames = 11;

    // ATTRIBUTES
    /** Current animation frame for this ship */
    private float animeframe;

    /** How high the enemy hovers in the world */
    private final float HOVER_DISTANCE = 4.309f;

    /** speed of road */
    private float currSpeed;

    /** enemy width */
    private static final float enemyWidth = 0.15f;

    /** enemy height */
    private static final float enemyHeight = 0.08f;

    /** speed of enemy relative to road, FIXME: change this prob */
    private float enemySpeed = 2f;

    /** the type of this enemy */
    private ObjectType enemyType;


    /**
     * Returns the type of this object.
     *
     * We use this instead of runtime-typing for performance reasons.
     *
     * @return the type of this object.
     */
    public ObjectType getType() {
        return enemyType;
    }

    public Enemy(float x, float y, ObjectType type) {
        setX(x);
        setY(y);
        Random rand = new Random();
        animeframe = (float) rand.nextInt(11);
        enemyType = type;
    }

    /**
     * Set the FilmStrip of this Enemy.
     *
     * @param texture the FilmStrip texture
     * @param rows the number of rows in the texture
     * @param cols the number of columns in the texture
     */
    public void setFilmStrip(Texture texture, int rows, int cols) {
        animator = new FilmStrip(texture,rows,cols,rows * cols);
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
    public void update(float delta, float newSpeed) {
        super.update(delta);

        animeframe += animationSpeed;
        if (animeframe >= numAnimationFrames) {
            animeframe -= numAnimationFrames;
        }

        setY(getY()-currSpeed * enemySpeed * delta);
        if (getY() < -12) {
            setY(14);
        }

        setSpeed(newSpeed);
    }

    public void draw(GameCanvas canvas) {
        animator.setFrame((int) animeframe);
        canvas.drawRoadObject(animator, getX(), getY(), HOVER_DISTANCE, enemyWidth, enemyHeight, 90, 0);
    }

    public void setSpeed (float s) {
        currSpeed = s;
    }
}
