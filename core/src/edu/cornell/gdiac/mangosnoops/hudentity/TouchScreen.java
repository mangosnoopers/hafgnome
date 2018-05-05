package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;

public class TouchScreen {

    private Image offScreen;
    private Image onScreen;
    private Image dvdSlot;
    private Radio radio;
    private DvdPlayer dvdPlayer;
    private GameCanvas c;

    public TouchScreen(Radio r, DvdPlayer d, Texture ons, Texture ofs, Texture dvds) {
        onScreen = new Image(0.85f, 0.24f, 0.26f, ons, GameCanvas.TextureOrigin.MIDDLE);
        offScreen = new Image(0.85f, 0.24f, 0.26f, ofs, GameCanvas.TextureOrigin.MIDDLE);
        dvdSlot = new Image(0.85f, 0.07f, 0.01f, 100, dvds, GameCanvas.TextureOrigin.MIDDLE);
        radio = r;
        dvdPlayer = d;
    }

    public Radio getRadio() { return radio; }

    public DvdPlayer getDvdPlayer() { return dvdPlayer; }

    public boolean inDvdSlot(Vector2 p) {
        return dvdSlot.inArea(p);
    }

    public void update(Vector2 p, float dx) {
        if(dvdPlayer.isPlayingDvd()) {
            dvdPlayer.update();
        } else {
            radio.update(p , dx);
        }
    }

    public void draw(GameCanvas canvas, BitmapFont displayFont) {
        c = canvas;
        onScreen.draw(canvas);
        dvdSlot.draw(canvas);
        //Draw screens
        if(dvdPlayer.isPlayingDvd()) {
            dvdPlayer.draw(canvas, displayFont);
        } else {
            if(radio.getNumStations() == 0) offScreen.draw(canvas);
            else radio.draw(canvas, displayFont);
        }
    }
}
