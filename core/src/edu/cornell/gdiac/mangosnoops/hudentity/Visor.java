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

    public Visor(Texture open, Texture closed) {
        super();
        openVisor = open;
        closedVisor = closed;
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
}
