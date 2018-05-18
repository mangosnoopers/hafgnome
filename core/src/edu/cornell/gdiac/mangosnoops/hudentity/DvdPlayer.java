package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.GameplayController;

public class DvdPlayer {
    /** Current dvdPlaying -- is null no music is playing */
    private String dvdPlayingTitle;
    private int timeLeft;
    Texture dvdPlayingTexture;

    public DvdPlayer (){
        dvdPlayingTitle = null;
        timeLeft = 0;
    }

    /**
     *
     * @param name
     * @param duration
     * @return true if starts playing, false if unsuccessful (if a movie is already playing)
     */
    public boolean playDvd(String name, int duration, GameplayController gc) {
        // TODO: add texture and draw that
        if(dvdPlayingTitle == null) {
            dvdPlayingTitle = name;
            timeLeft = duration;
            gc.getRearviewDVD().showDVD();
            return true;
        }
        gc.getRearviewDVD().hideDVD();
        return false;
    }

    /** Returns true if a dvd is playing right now */
    public boolean isPlayingDvd() {
        return dvdPlayingTitle != null;
    }

    public void update() {
        if(timeLeft == 0) {
            dvdPlayingTitle = null;
        } else {
            timeLeft--;
        }
    }

    public void draw(GameCanvas canvas, BitmapFont displayFont) {
        displayFont.setColor(Color.WHITE);
        if(dvdPlayingTitle == null) {
            canvas.drawTextCenterOrigin("No DVD Inserted.\n", displayFont, 0.85f, 0.24f);
            canvas.drawTextCenterOrigin("\nPlease insert the DVD.", displayFont, 0.85f, 0.24f);
        } else {
            canvas.drawTextCenterOrigin(dvdPlayingTitle, displayFont, 0.85f, 0.24f);
            canvas.drawTextCenterOrigin("\n\nTime Remaining: " + timeLeft, displayFont, 0.85f, 0.24f);
        }
    }
}
