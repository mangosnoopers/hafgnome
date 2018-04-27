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
public class RearviewEnemy extends Image {

    private float currentHeight;
    private float currentSpeed;
    private float ORIGINAL_SCALE;

    /** Whether or not the enemy is alive (which would entail that it's
     *  crawling up the back of the car) */
    private boolean isAlive;
    private boolean isDying;


    /** Speed constants */
    private final float NORMAL_SPEED = 30f;

    /** The speed at which the enemy starts to damage the car */
    private final float DAMAGE_HEIGHT = 0.05f;

    /**
     * Creates a new RearviewEnemy.
     */
    public RearviewEnemy(float x, float y, float relScale, float cb, Texture tex) {
        super(x,y,relScale,cb,tex);
        currentHeight = 0;
        isAlive = false;
        currentSpeed = NORMAL_SPEED;
        ORIGINAL_SCALE = relativeScale;
    }

    @Override
    public void draw(GameCanvas canvas) {
        if(texture == null){
            return;
        }

        if (isDying || isAlive) {
            canvas.drawShake(texture, GameCanvas.TextureOrigin.MIDDLE, position.x, position.y,
                    relativeScale, false, 0, Color.WHITE, currentShakeAmount + currentHeight);
        }
    }

    public void update(float deltaSpeed) {
        if (isAlive && currentHeight < DAMAGE_HEIGHT*c.getHeight()) {
            currentHeight += (currentSpeed * deltaSpeed * c.getHeight());
        }
        if(currentHeight > DAMAGE_HEIGHT*c.getHeight()){
            currentHeight = DAMAGE_HEIGHT*c.getHeight();
        }

        //Make gnome fly off
        if(isDying) {
            relativeScale -= ((ORIGINAL_SCALE - 0.6f*relativeScale)*0.04f);
            if ((relativeScale - ORIGINAL_SCALE*0.01) <= 0) {
                isDying = false;
                isAlive = false;
                relativeScale = ORIGINAL_SCALE;
            }
        }
    }

    public boolean exists() {
        return isAlive;
    }

    public boolean isAttackingCar() {
        return isAlive && (currentHeight >= DAMAGE_HEIGHT*c.getHeight());
    }

    /** Creates a "new" rearview enemy, if there isn't already one. */
    public void create() {
        if (!isAlive && !isDying) {
            isAlive = true;
            currentHeight = 0;
        }
    }

    public void destroyIfAlive() {
        if(isAlive) {
            isDying = true;
        }
    }

}
