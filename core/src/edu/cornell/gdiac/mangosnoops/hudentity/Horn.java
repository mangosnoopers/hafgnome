package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Texture;
import edu.cornell.gdiac.mangosnoops.Image;

public class Horn extends Image {

    /** Whether or not the Horn is honking */
    private boolean isHonking;

    /** How long a honk lasts */
    private final static float HONK_LENGTH = 2f;

    /** How much time remains of the honk */
    private float honkTimeRemaining;

    /** How quickly the honk time decreases */
    private final static float HONK_DEPRECATION_RATE = 2f;

    public Horn(float x, float y, float relScal, float cb, Texture tex) {
        super(x, y, relScal, tex);
        isHonking = false;
    }

    /**
     * Honk the horn, if possible.
     */
    public void honk() {
        if (!isHonking) {
            honkTimeRemaining = HONK_LENGTH;
            isHonking = true;
        }
    }

    /**
     * Update the state of the honk. Must be called every frame
     */
    public void updateHonk(float delta) {
        if (isHonking) {
            honkTimeRemaining -= delta * HONK_DEPRECATION_RATE;
            if (honkTimeRemaining <= 0) {
                isHonking = false;
            }
        }


    }

}
