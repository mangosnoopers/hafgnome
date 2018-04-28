package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;

public class DvdPlayer {
    /** Current dvdPlaying -- is null no music is playing */
    private String dvdPlaying;
    private int timeLeft;

    public DvdPlayer (){
        dvdPlaying = "hey";
        timeLeft = 100;
    }

    /**
     *
     * @param name
     * @param duration
     * @return true if starts playing, false if unsuccessful (if a movie is already playing)
     */
    public boolean playDvd(String name, int duration) {
        if(dvdPlaying == null) {
            dvdPlaying = name;
            timeLeft = duration;
            return true;
        }
        return false;
    }

    /** Returns true if a dvd is playing right now */
    public boolean isPlayingDvd() {
        return dvdPlaying != null;
    }

    public void update() {
        if(timeLeft == 0) {
            dvdPlaying = null;
        } else {
            timeLeft--;
        }
    }

    public void draw(GameCanvas canvas, BitmapFont displayFont) {
        displayFont.setColor(Color.WHITE);
        if(dvdPlaying == null) {
            canvas.drawTextCenterOrigin("No DVD Inserted.", displayFont, 0.85f, 0.24f);
            canvas.drawTextCenterOrigin("\n\nPlease insert the DVD.", displayFont, 0.85f, 0.24f);
        } else {
            canvas.drawTextCenterOrigin(dvdPlaying, displayFont, 0.85f, 0.24f);
            canvas.drawTextCenterOrigin("\n\nTime Remaining: " + timeLeft, displayFont, 0.85f, 0.24f);
        }
    }
}
