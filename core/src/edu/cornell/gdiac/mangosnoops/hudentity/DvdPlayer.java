package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.GameplayController;
import edu.cornell.gdiac.mangosnoops.SoundController;

public class DvdPlayer {
    /** Current dvdPlaying -- is null no music is playing */
    private String dvdPlayingTitle;
    private int timeLeft;
    Texture dvdPlayingTexture;
    SoundController soundController;
    private boolean wasPlayingDvd;
    public DvdPlayer (SoundController sc){
        dvdPlayingTitle = null;
        timeLeft = 0;
        soundController = sc;
    }

    /**
     *
     * @param name
     * @param duration
     * @return true if starts playing, false if unsuccessful (if a movie is already playing)
     */
    public boolean playDvd(String name, int duration) {
        // TODO: add texture and draw that
        if(dvdPlayingTitle == null) {
            dvdPlayingTitle = name;
            timeLeft = duration;
            soundController.playMovieMusic(true);
            wasPlayingDvd = true;
            return true;
        }
        return false;
    }

    /** Returns true if a dvd is playing right now */
    public boolean isPlayingDvd() {
        return dvdPlayingTitle != null;
    }

    public void update() {
        if(timeLeft == 0) {
            dvdPlayingTitle = null;
            soundController.playMovieMusic(false);
        } else {
            timeLeft--;
        }
    }

    public void draw(GameCanvas canvas, BitmapFont displayFont) {
        displayFont.setColor(Color.WHITE);
        if(dvdPlayingTitle == null) {
            canvas.drawTextCenterOriginShake("No DVD Inserted.\n", displayFont, 0.85f, 0.24f);
            canvas.drawTextCenterOriginShake("\nPlease insert the DVD.", displayFont, 0.85f, 0.24f);
        } else {
            canvas.drawTextCenterOriginShake(dvdPlayingTitle, displayFont, 0.85f, 0.24f);
            canvas.drawTextCenterOriginShake("\n\nTime Remaining: " + timeLeft, displayFont, 0.85f, 0.24f);
        }
    }
}
