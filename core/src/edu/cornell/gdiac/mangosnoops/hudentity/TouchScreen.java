package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;

public class TouchScreen {

    Image offScreen;
    Image dvdSlot;
    Texture buttonRadio;
    Texture buttonGps;
    Texture buttonDvd;
    Radio radio;
    GameCanvas c;

    public TouchScreen(Radio r, Texture os, Texture dvds) {
        offScreen = new Image(0.83f, 0.24f, 0.26f, os, GameCanvas.TextureOrigin.MIDDLE);
        dvdSlot = new Image(0.83f, 0.07f, 0.01f, dvds, GameCanvas.TextureOrigin.MIDDLE);
        radio = r;
    }

    public boolean inDvdSlot(Vector2 p) {
        return dvdSlot.inArea(p);
    }

    public void draw(GameCanvas canvas) {
        c = canvas;
        offScreen.draw(canvas);
        dvdSlot.draw(canvas);
    }
}
