package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;

public class Visor {

    private Image openVisor;
    private Image closedVisor;
    private boolean open; //true if the visor is open
    Vector2 save;

    public Visor(Texture open, Texture closed) {
        openVisor = new Image(0.1f,0.8f,0.3f,0,open);
        closedVisor = new Image(0.1f,0.95f,0.3f*closed.getHeight()/open.getHeight(),0,closed);
        this.open = false;
    }

    public boolean isOpen() {
        return open;
    }

    /** Flips ONLY IF in input is in area. */
    public void update(Vector2 p, boolean prevMousePressed) {
        save = p;
        if(!prevMousePressed) {
            if(open && openVisor.inArea(save)) {
                open = false;
            } else if (!open && closedVisor.inArea(save)) {
                open = true;
            }
        }
    }

    public void draw(GameCanvas canvas) {
        if(open) {
            openVisor.draw(canvas);
        } else {
            closedVisor.draw(canvas);
        }
    }
}
