package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.*;

/**
 * A RearviewEnemy appears in the rearview mirror. It slowly rises until
 * it's fully visible, and then it starts to damage the car. When the
 * player engages the vroom-stick thing, it disappears.
 *
 * Only one rearview enemy can occur at once (maybe change?).
 *
 * Usage: create a single instance of this class. When you want to create a
 *        new enemy, call the create() method. Destroy it with destroy().
 *
 * E.g.:
 *
 * RearviewEnemy r = new RearviewEnemy();
 *
 * // Create new enemy
 * r.create()
 *
 * if (timeToDestroyEnemy()) { r.destroy(); }
 *
 * ...
 *
 * if (timeToCreateNewEnemy()) { r.create(); }
 *
 * TODO:
 *  - garbage collection stuff
 *  - do we want there to be >1 rearview enemy at once?
 */
public class RearviewEnemy extends HUDObject {

    private float currentHeight;
    private float currentSpeed;

    /** Whether or not the enemy is alive (which would entail that it's
     *  crawling up the back of the car) */
    private boolean isAlive;

    /** FIXME: do this relative to screen width, height **/
    private final float ENEMY_X = 600f;
    private final float ENEMY_Y = 350f;

    /** Speed constants */
    private final float NORMAL_SPEED = 30f;

    /** The speed at which the enemy starts to damage the car */
    private final float DAMAGE_HEIGHT = 100f;

    private Texture texture;

    /**
     * Creates a new RearviewEnemy.
     */
    public RearviewEnemy() {
        currentHeight = 0;
        isAlive = false;
        currentSpeed = NORMAL_SPEED;

    }

    public void setTexture(Texture t) {
        texture = t;
    }

    public void draw(GameCanvas canvas) {
        if (isAlive) {
            float w = 0.5f * texture.getWidth();
            float h = 0.5f * texture.getHeight();
            canvas.draw(texture, ENEMY_X, ENEMY_Y + currentHeight, w, h);
        }
    }

    public void update(float delta) {
        if (isAlive && currentHeight < DAMAGE_HEIGHT) {
            currentHeight += currentSpeed * delta;
        }
    }

    public boolean exists() {
        return isAlive;
    }

    public boolean isAttackingCar() {
        return isAlive && currentHeight >= DAMAGE_HEIGHT;
    }

    /** Creates a "new" rearview enemy, if there isn't already one. */
    public void create() {
        if (!isAlive) {
            isAlive = true;
            currentHeight = 0;

        }
    }

    public void destroyIfAlive() {
        isAlive = false;
    }

}
