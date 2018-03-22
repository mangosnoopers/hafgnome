package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;

public class SoundController {

    private ObjectMap<Radio.Station, Music> stationToMusic;

    public SoundController(){
        stationToMusic = new ObjectMap<Radio.Station, Music>();
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
        if (lastStation != currentStation) {
            if (lastStation != null) {
                stopAudio(stationToMusic.get(lastStation));
                stationToMusic.remove(lastStation);
            }
            if (currentStation == null) {
                return;
            }
            Music audio = Gdx.audio.newMusic(Gdx.files.internal(currentStation.getAudioFile()));
            stationToMusic.put(currentStation, audio);
            stationToMusic.get(currentStation).play();
            stationToMusic.get(currentStation).setLooping(true);
            //r.getCurrentStation().playAudio();
        }
        return;
    }

    private void stopAudio(Music m){
        if(m == null){
            return;
        }
        m.stop();
        m.dispose();
    }

    public void reset() {
        if (stationToMusic != null && stationToMusic.size > 0) {
            for (Radio.Station s : stationToMusic.keys()) {
                if (stationToMusic.get(s).isPlaying()) {
                    stopAudio(stationToMusic.get(s));
                }
            }
        }
        stationToMusic = new ObjectMap<Radio.Station, Music>();
    }
}


