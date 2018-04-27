package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;

public class TouchScreen {

    Texture offScreen;
    Texture dvdSlot;
    Texture buttonRadio;
    Texture buttonGps;
    Texture buttonDvd;
    Radio radio;
    GameCanvas c;

    public TouchScreen(Radio r, Texture os, Texture dvds) {
        offScreen = os;
        dvdSlot = dvds;
        radio = r;
    }

    public boolean inDvdSlot(Vector2 p) {
        return c.inArea(p, dvdSlot, GameCanvas.TextureOrigin.MIDDLE, 0.83f, 0.07f, 0.2f, true);
    }

    public void draw(GameCanvas canvas) {
        c = canvas;
        canvas.draw(offScreen, GameCanvas.TextureOrigin.MIDDLE, 0.83f, 0.24f, 0.26f, true);
        canvas.draw(dvdSlot, GameCanvas.TextureOrigin.MIDDLE, 0.83f, 0.07f, 0.2f, true);
    }
}
