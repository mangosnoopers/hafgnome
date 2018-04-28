package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;

public class SoundController {
    /** All played music */
    private Array<Music> music;

    /**
     * Object Constructor
     */
    public SoundController(){
        music = new Array<Music>();
    }

    /**
     * Given a radio r, the SoundController creates Music files for its most current station
     * and plays them. Additionally, it will stop audio for and dispose of the Music files
     * for the last played station.
     * @param r
     */
    public void playRadio(Radio r) {
        Radio.Station lastStation = r.getLastStation();
        Radio.Station currentStation = r.getCurrentStation();

        if (lastStation != currentStation){
            if (lastStation != null) {
                lastStation.getAudio().pause();
            }
            if (currentStation != null) {
                currentStation.getAudio().play();
                music.add(currentStation.getAudio());
            }
        }
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
    public void play(Radio radio){
        playRadio(radio);
    }

    /** Rest method **/
    public void reset() {
        for(Music m : music) {
            stopAudio(m);
        }
    }

}



