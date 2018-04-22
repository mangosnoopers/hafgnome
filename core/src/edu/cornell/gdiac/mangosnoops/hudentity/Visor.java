package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;

public class Visor extends Image {

    private Texture openVisor;
    private Texture closedVisor;
    private boolean open = false; //true if the visor is open
    Vector2 save;

    /** Texture of sun effect */
    private Texture sun;
    private Texture sun2;
    private Texture sun3;
    private Texture white;

    public Visor(Texture open, Texture closed, Texture sun, Texture sun2, Texture sun3, Texture white) {
        super();
        openVisor = open;
        closedVisor = closed;
        this.sun = sun;
        this.sun2 = sun2;
        this.sun3 = sun3;
        this.white = white;
    }

    public boolean isOpen() {
        return open;
    }

    /** Flips ONLY IF in input is in area. */
    public void update(Vector2 p, boolean prevMousePressed) {
        if(p != null) save = p;
        if(!prevMousePressed) {
            if(open && inArea(save)) {
                open = false;
            } else if (!open && inArea(save)) {
                open = true;
            }
        }
    }

    public boolean inArea(Vector2 p) {
        if(open) {
            return (p.x > 0.06f*SCREEN_DIMENSIONS.x && p.x < (0.06f+0.35f)*SCREEN_DIMENSIONS.x
                    && p.y > 0 && p.y < openVisor.getHeight()*SCREEN_DIMENSIONS.x*0.35f/openVisor.getWidth());
        } else {
            return (p.x > 0.06f*SCREEN_DIMENSIONS.x && p.x < (0.06f+0.35f)*SCREEN_DIMENSIONS.x
                    && p.y > 0 && p.y < closedVisor.getHeight()*SCREEN_DIMENSIONS.x*0.35f/openVisor.getWidth());
        }
    }

    public void draw(GameCanvas canvas) {
        if(open) {
            canvas.draw(openVisor, Color.WHITE, 0, openVisor.getHeight(), 0.06f*canvas.getWidth(), canvas.getHeight(), 0,
                    canvas.getWidth()*0.35f/openVisor.getWidth(),canvas.getWidth()*0.35f/openVisor.getWidth());
        } else {
            canvas.draw(closedVisor, Color.WHITE, 0, closedVisor.getHeight(), 0.06f*canvas.getWidth(), canvas.getHeight(), 0,
                    canvas.getWidth()*0.35f/openVisor.getWidth(),canvas.getWidth()*0.35f/openVisor.getWidth());
        }
    }

    /** Draw this part before the HUD elements. (tints the screen yellow) */
    public void drawSunA(GameCanvas canvas, boolean sunShine) {
        if(sunShine && !open) {
            canvas.setBlendState(GameCanvas.BlendState.ADDITIVE);
            canvas.draw(white, new Color(1, 1, 0, 0.7f), 0, 0, 0, 0, 0,
                    canvas.getWidth(), canvas.getHeight());
            canvas.setBlendState(GameCanvas.BlendState.NO_PREMULT);
            canvas.draw(white, new Color(1, 0.7f, 0, 0.7f), 0, 0, 0, 0, 0,
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
    private int del = 0;

    /** Draw this part after the HUD elements (does the sun flare effect)*/
    public void drawSunB(GameCanvas canvas, boolean sunShine) {
        del++;
        if(sunShine) {
            if(del > 10) {
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
