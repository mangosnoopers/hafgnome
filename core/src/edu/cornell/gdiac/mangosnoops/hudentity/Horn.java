package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;
import edu.cornell.gdiac.mangosnoops.SoundController;

public class Horn extends Image {

    /** Whether or not the Horn is honking */
    private boolean isHonking;

    /** How long a honk lasts */
    private final static float HONK_LENGTH = 15f;

    /** How much time remains of the honk */
    private float honkTimeRemaining;

    /** How quickly the honk time decreases */
    private final static float HONK_DEPRECATION_RATE = 20;

    public Horn(float x, float y, float relScal, float cb, Texture tex) {
        super(x, y, relScal, tex);
        isHonking = false;
    }

    /**
     * Honk the horn, if possible.
     */
    private void honk() {
        if (!isHonking) {
            honkTimeRemaining = HONK_LENGTH;
            isHonking = true;
            System.out.println("Honked");
        }
    }

    public void update(Vector2 in, float delta) {
        if (in != null && inArea(in)) {
            honk();
        }
    }

    /**
     * @return whether or not the horn is honking.
     */
    public boolean isHonking() {
        return isHonking;
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

    public void draw(GameCanvas canvas) {
        super.draw(canvas);
        canvas.drawText(isHonking + " " + honkTimeRemaining, new BitmapFont(), 100, 100);
    }

}
