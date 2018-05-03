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
    private Image buttonGps;
    private Image buttonRadio;
    private Image buttonDvd;
    private GPS gps;
    private Radio radio;
    private DvdPlayer dvdPlayer;
    private GameCanvas c;

    private enum OpenScreen {
        GPS,
        RADIO,
        DVD
    }
    private OpenScreen currScreen;


    public TouchScreen(GPS g, Radio r, DvdPlayer d, Texture ons, Texture ofs, Texture dvds, Texture bG, Texture bR, Texture bD) {
        onScreen = new Image(0.85f, 0.24f, 0.26f, ons, GameCanvas.TextureOrigin.MIDDLE);
        offScreen = new Image(0.85f, 0.24f, 0.26f, ofs, GameCanvas.TextureOrigin.MIDDLE);
        dvdSlot = new Image(0.85f, 0.07f, 0.01f, 100, dvds, GameCanvas.TextureOrigin.MIDDLE);
        gps = g;
        radio = r;
        dvdPlayer = d;
        buttonGps = new Image(0.7f, 0.34f, 0.04f, bG, GameCanvas.TextureOrigin.MIDDLE);
        buttonRadio = new Image(0.7f, 0.24f, 0.04f, bR, GameCanvas.TextureOrigin.MIDDLE);
        buttonDvd = new Image(0.7f, 0.14f, 0.04f, bD, GameCanvas.TextureOrigin.MIDDLE);
        currScreen = OpenScreen.RADIO;
    }

    public boolean inDvdSlot(Vector2 p) {
        return dvdSlot.inArea(p);
    }

    public void update(Vector2 p, float dx) {
        gps.update();
        radio.update(p , dx);
        dvdPlayer.update();
        if(p == null) return;
        if(buttonGps.inArea(p)) {
            currScreen = OpenScreen.GPS;
        } else if(buttonRadio.inArea(p)) {
            currScreen = OpenScreen.RADIO;
        } else if(buttonDvd.inArea(p)) {
            currScreen = OpenScreen.DVD;
        }
    }

    public void draw(GameCanvas canvas, BitmapFont displayFont) {
        c = canvas;
        onScreen.draw(canvas);
        dvdSlot.draw(canvas);
        buttonGps.draw(canvas);
        buttonRadio.draw(canvas);
        buttonDvd.draw(canvas);
        //Draw screens
        switch(currScreen) {
            case GPS:
                gps.draw(canvas, displayFont);
                break;
            case RADIO:
                if(radio.getNumStations() == 0) offScreen.draw(canvas);
                else radio.draw(canvas, displayFont);
                break;
            case DVD:
                dvdPlayer.draw(canvas, displayFont);
                break;
            default:
                break;
        }
    }
}
