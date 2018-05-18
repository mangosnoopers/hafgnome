package edu.cornell.gdiac.mangosnoops.roadentity;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

import java.util.Random;

public class Enemy extends RoadObject{
    // CONSTANTS
    /** How fast we change frames (one frame per 4 calls to update) */
    private float animationSpeed = 0.25f;
    /** The number of animation frames in our filmstrip */
    private int   numAnimationFrames;

    // ATTRIBUTES
    /** Current animation frame for this ship */
    private float animeframe;

    /** How high the enemy hovers in the world */
    protected float hoverDistance = 4.309f;

    /** speed of road */
    private float currSpeed;

    /** enemy width */
    protected float enemyWidth = 0.2f;

    /** enemy height */
    protected float enemyHeight = 0.09f;

    /** the type of this enemy */
    private ObjectType enemyType;

    /** The minimum animation frame */
    protected int minAnimFrame;

    /** The maximum animation frame */
    protected int maxAnimFrame;

    /** Reference to Enemy that is directly left-adjacent */
    private Enemy rightEnemy;

    public void setRightEnemy(Enemy e) {
        rightEnemy = e;
    }

    public Enemy getRightEnemy() {
        return rightEnemy;
    }

    public float getEnemyWidth() {
        return enemyWidth;
    }

    private Vector2 startPos;

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

    public Vector2 getStartPos() { return startPos; }

    public Enemy(float x, float y, ObjectType type) {
        startPos = new Vector2(x, y);
        setX(x);
        setY(y);
        Random rand = new Random();
        minAnimFrame = 0;
        maxAnimFrame = 0;
        animeframe = 0;
        enemyType = type;
    }


    public Enemy(Enemy e) {
        startPos = new Vector2(e.getStartPos().x, e.getStartPos().y);
        setX(e.getX());
        setY(e.getY());
        Random rand = new Random();
        minAnimFrame = 0;
        maxAnimFrame = 0;
        animeframe = 0;
        enemyType = e.getType();
        rightEnemy = e.getRightEnemy();
    }

    /**
     * Set a new hover distance.
     * @param newHoverDistance the new hover distance
     */
    public void setHoverDistance(float newHoverDistance) {
        hoverDistance = newHoverDistance;
    }

    /**
     * Get the current hover distance.
     * @return new hover distance
     */
    public float getHoverDistance() {
        return hoverDistance;
    }

    /**
     * Set new enemy width
     * @param newWidth
     */
    public void setEnemyWidth(float newWidth) {
        enemyWidth = newWidth;
    }

    /**
     * Set new enemy height
     * @param newHeight
     */
    public void setEnemyHeight(float newHeight) {
        enemyHeight = newHeight;
    }

    /**
     * Set the FilmStrip of this Enemy.
     *
     * @param texture the FilmStrip texture
     * @param rows the number of rows in the texture
     * @param cols the number of columns in the texture
     */
    public void setFilmStrip(Texture texture, int rows, int cols) {
        numAnimationFrames = rows * cols - 1;
        minAnimFrame = 0;
        maxAnimFrame = numAnimationFrames;
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
        setSpeed(newSpeed);
        animeframe += animationSpeed;
        setY(getY()-currSpeed * delta);

    }

    public void draw(GameCanvas canvas) {
        if (animeframe >= maxAnimFrame) {
            animeframe = minAnimFrame;
        }
        animator.setFrame((int) animeframe);
        canvas.drawRoadObject(animator, getX(), getY(), hoverDistance, enemyWidth, enemyHeight, 90, 0);
    }

    public void setSpeed (float s) {
        currSpeed = s;
    }
}
