package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;
import edu.cornell.gdiac.util.FilmStrip;

public class RearviewDVD extends Image {

    private FilmStrip DVDFilmStrip;

    private final float ANIM_SPEED = 0.2f;

    /** Number of animation frames */
    private final float NUM_ANIM_FRAMES = 6;

    /** Current animation frame (cast it to int for use) */
    private float currentAnimFrame;

    /** Represents current state of animation */
    private enum AnimationState {
        OPENING,
        CLOSING,
        OPEN,
        CLOSED
    }

    /** Current animation state */
    private AnimationState animState = AnimationState.CLOSED;

    public RearviewDVD(float x, float y, float relSca, Texture tex) {
        super(x, y, relSca, tex);
        DVDFilmStrip = new FilmStrip(tex, 1, 6, 6);

    }

    public void showDVD() {
        if (animState == AnimationState.CLOSED) {
            animState = AnimationState.OPENING;
        }
    }

    public void hideDVD() {
        if (animState == AnimationState.OPEN) {
            animState = AnimationState.CLOSING;
        }
    }

    public void update(float delta) {

        switch (animState) {
            case OPENING:
                currentAnimFrame += ANIM_SPEED;
                if (currentAnimFrame >= NUM_ANIM_FRAMES) {
                    currentAnimFrame = NUM_ANIM_FRAMES - 1;
                    animState = AnimationState.OPEN;
                }
                break;
            case CLOSING:
                currentAnimFrame -= ANIM_SPEED;
                if (currentAnimFrame <= 0) {
                    currentAnimFrame = 0;
                    animState = AnimationState.CLOSED;
                }
                break;
            case OPEN:
            case CLOSED:
            default:
                break;
        }

        DVDFilmStrip.setFrame((int) currentAnimFrame);

    }

    public void draw(GameCanvas canvas) {

        float ox = 0.5f * DVDFilmStrip.getRegionWidth();
        float oy = 0.5f * DVDFilmStrip.getRegionHeight();
        float drawY = position.y * canvas.getHeight() + currentShakeAmount;

        canvas.draw(DVDFilmStrip, Color.WHITE, ox, oy, position.x * canvas.getWidth(), drawY, 0,
                0.55f * (canvas.getHeight() / 2.5f) / DVDFilmStrip.getRegionHeight(),
                0.55f * (canvas.getHeight() / 2.5f) / DVDFilmStrip.getRegionHeight());

    }
}
