package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;
import edu.cornell.gdiac.util.FilmStrip;

public class Visor extends Image {

    /** Animation information */
    private FilmStrip visorFilmStrip;
    private final static int NUM_ANIMATION_FRAMES = 7;
    private final static int NUM_FILMSTRIP_ROWS = 7;
    private final static int NUM_FILMSTRIP_COLS = 1;
    private final static float ANIMATION_SPEED = 1.2f;
    private float animFrame = 0;

    /** current animation state */
    private AnimationState animationState;

    /** enum representing current animation state */
    private enum AnimationState {
        OPENING,
        CLOSING,
        OPEN,
        CLOSED
    }

    private boolean open = false; //true if the visor is open
    Vector2 save;

    /** Texture of sun effect */
    private Texture sun;
    private Texture sun2;
    private Texture sun3;
    private Texture white;

    public Visor(Texture v, Texture sun, Texture sun2, Texture sun3, Texture white) {
        super();
        this.sun = sun;
        this.sun2 = sun2;
        this.sun3 = sun3;
        this.white = white;
        visorFilmStrip = new FilmStrip(v, NUM_FILMSTRIP_ROWS, NUM_FILMSTRIP_COLS, NUM_FILMSTRIP_ROWS * NUM_FILMSTRIP_COLS);
        animationState = AnimationState.CLOSING;
    }

    public void close() {
        open = false;
        animationState = AnimationState.CLOSING;
    }

    public boolean isOpen() {
        return open;
    }

    public void update(float delta) {
        del += delta;
        switch (animationState) {
            case OPENING:
                animFrame += ANIMATION_SPEED;
                if (animFrame > NUM_ANIMATION_FRAMES) {
                    animFrame = NUM_ANIMATION_FRAMES - 1;
                    animationState = AnimationState.OPEN;
                }
                break;
            case CLOSING:
                animFrame -= ANIMATION_SPEED;
                if (animFrame < 0) {
                    animFrame = 0;
                    animationState = AnimationState.CLOSED;
                }
                break;
            case OPEN:
            case CLOSED:
                break;
        }

        visorFilmStrip.setFrame((int) animFrame);
    }

    /** Flips ONLY IF in input is in area. */
    public void resolveInput(Vector2 p, boolean prevMousePressed) {
        if(p != null) save = p;
            if(!prevMousePressed) {
                if(open && inArea(save)) {
                    open = false;
                    animationState = AnimationState.CLOSING;
                } else if (!open && inArea(save)) {
                    open = true;
                    animationState = AnimationState.OPENING;
                }
            }

    }

    public boolean inArea(Vector2 p) {
        if(open) {
            return c.inArea(p, visorFilmStrip, GameCanvas.TextureOrigin.TOP_LEFT, 0, 1, 0.33f, true);
        } else {
            return c.inArea(p, visorFilmStrip, GameCanvas.TextureOrigin.TOP_LEFT, 0, 1, 0.33f, true);
        }
    }

    public void draw(GameCanvas canvas) {
        if(open) {
            canvas.draw(visorFilmStrip, GameCanvas.TextureOrigin.TOP_LEFT, 0, 1, 0.33f, true);
        } else {
            canvas.draw(visorFilmStrip, GameCanvas.TextureOrigin.TOP_LEFT, 0, 1, 0.33f, true);
        }
    }

    /** Draw this part before the HUD elements. (tints the screen yellow) */
    public void drawSunA(GameCanvas canvas, boolean sunShine) {
        if(sunShine && !open) {
            canvas.setBlendState(GameCanvas.BlendState.ADDITIVE);
            canvas.draw(white, new Color(1, 1, 0, 0.9f), 0, 0, 0, 0, 0,
                    canvas.getWidth(), canvas.getHeight());
            canvas.setBlendState(GameCanvas.BlendState.NO_PREMULT);
            canvas.draw(white, new Color(1, 0.7f, 0, 0.9f), 0, 0, 0, 0, 0,
                    canvas.getWidth(), canvas.getHeight());
        } else if (sunShine) {
            canvas.setBlendState(GameCanvas.BlendState.ADDITIVE);
            canvas.draw(white, new Color(1, 1, 0, 0.3f), 0, 0, 0, 0, 0,
                    canvas.getWidth(), canvas.getHeight());
            canvas.setBlendState(GameCanvas.BlendState.NO_PREMULT);
            canvas.draw(white, new Color(1, 0.7f, 0, 0.3f), 0, 0, 0, 0, 0,
                    canvas.getWidth(), canvas.getHeight());
        }
    }

    private int rotate = 0;
    private boolean up = true;
    private float del = 0;

    /** Draw this part after the HUD elements (does the sun flare effect)*/
    public void drawSunB(GameCanvas canvas, boolean sunShine) {
        if(sunShine) {
            if(del > 0.2) {
                del = 0;
                if(up) {
                    rotate ++;
                    if(rotate > 5) up = false;
                } else {
                    rotate --;
                    if(rotate < 0) up = true;
                }
            }
            canvas.draw(sun3, Color.WHITE, 0.5f*sun3.getWidth(), 0.5f*sun3.getHeight(), 0.32f*canvas.getWidth(), 0.98f*canvas.getHeight(), 0,
                    ((float).3*canvas.getWidth()+rotate*100)/sun3.getWidth(), ((float).3*canvas.getWidth()+rotate*100)/sun3.getWidth());
            canvas.setBlendState(GameCanvas.BlendState.ADDITIVE);
            if(!open) {
                canvas.draw(sun2, new Color(1, 1, 1, (float)rotate/20.0f), 0.25f*sun.getWidth(), 0.75f*sun.getHeight(), 0.2f*canvas.getWidth(), canvas.getHeight(), 0,
                        ((float)1.25*canvas.getWidth())/sun.getWidth()+rotate, ((float)1.25*canvas.getWidth())/sun.getWidth()+rotate);
                canvas.draw(sun, new Color(1, 1, 1, 0.4f), 0.25f*sun.getWidth(), 0.75f*sun.getHeight(), 0.2f*canvas.getWidth(), canvas.getHeight(), 0,
                        ((float)1.25*canvas.getWidth())/sun.getWidth(), ((float)1.25*canvas.getWidth())/sun.getWidth());
            } else {
                canvas.draw(sun2, new Color(1, 1, 1, 0.2f), 0.25f*sun.getWidth(), 0.75f*sun.getHeight(), 0.2f*canvas.getWidth(), canvas.getHeight(), 0,
                        ((float)1.25*canvas.getWidth())/sun.getWidth(), ((float)1.25*canvas.getWidth())/sun.getWidth());
                canvas.draw(sun, new Color(1, 1, 1, 0.2f), 0.25f*sun.getWidth(), 0.75f*sun.getHeight(), 0.2f*canvas.getWidth(), canvas.getHeight(), 0,
                        ((float)1.25*canvas.getWidth())/sun.getWidth(), ((float)1.25*canvas.getWidth())/sun.getWidth());
            }
            canvas.setBlendState(GameCanvas.BlendState.NO_PREMULT);
        }
    }
}
