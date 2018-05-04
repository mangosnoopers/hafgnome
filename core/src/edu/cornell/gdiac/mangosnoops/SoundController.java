package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.hudentity.DvdPlayer;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;
import edu.cornell.gdiac.mangosnoops.hudentity.TouchScreen;

public class SoundController {
    /** All played music */
    private Array<Music> music;
    private static Music carAmbience = Gdx.audio.newMusic(Gdx.files.internal("sounds/carAmbience.mp3"));
    private static Music radioStatic = Gdx.audio.newMusic(Gdx.files.internal("sounds/radioStatic.mp3"));
    private static Music carBeep = Gdx.audio.newMusic(Gdx.files.internal("sounds/beepbeep.mp3"));

    /**
     * Object Constructor
     */
    public SoundController(){
        music = new Array<Music>();
        carAmbience.setLooping(true);
        radioStatic.setLooping(true);
    }

    public void startAmbience() {
        carAmbience.play();
    }

    boolean wasStatic = false;
    /**
     * Given a radio r, the SoundController creates Music files for its most current station
     * and plays them. Additionally, it will stop audio for and dispose of the Music files
     * for the last played station.
     * @param r
     */
    public void playRadio(Radio r) {
        Radio.Station currentStation = r.getCurrentStation();
        if(r.shouldPlayStatic()) {
            if(currentStation.getAudio().isPlaying()) currentStation.getAudio().setVolume(0);
            radioStatic.play();
            System.out.println("STATIC " + currentStation.getAudio().getVolume());
        } else {
            radioStatic.stop();
            Radio.Station lastStation = r.getLastStation();

            if (lastStation != currentStation && lastStation != null) {
                lastStation.getAudio().setVolume(0);
            }
            if ((wasStatic || lastStation != currentStation) && currentStation != null) {
                if(currentStation.getAudio().isPlaying()) currentStation.getAudio().setVolume(1f);
                else currentStation.getAudio().play();
                music.add(currentStation.getAudio());
            }
        }
        wasStatic = r.shouldPlayStatic();
    }

    public void muteRadio(Radio r) {
        r.getCurrentStation().getAudio().stop();
    }

    public void playDvd(DvdPlayer m) {
        // TODO : add audio functionality
    }

    /** Called in resolveActions when horn is honked */
    public void beepSound() {
        carBeep.play();
    }

    /** Stops audio and disposes the file **/
    private void stopAudio(Music m){
        if(m == null){
            return;
        }
        m.stop();
        m.dispose();
    }

    /** Universal play method, plays all audio based on game updates **/
    public void play(TouchScreen t){
        if(t.getDvdPlayer().isPlayingDvd()) {
            muteRadio(t.getRadio());
            playDvd(t.getDvdPlayer());
        } else {
            playRadio(t.getRadio());
        }
    }

    /** Rest method **/
    public void reset() {
        carAmbience.stop();
        for(Music m : music) {
            stopAudio(m);
        }
    }

}