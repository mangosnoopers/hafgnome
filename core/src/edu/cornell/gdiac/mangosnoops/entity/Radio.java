package edu.cornell.gdiac.mangosnoops.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

import java.io.File;

public class Radio {

    private Vector2 pos;

    private Vector2 knobPos;

    private float knobAng;

    private Car car;

    private int stationListSize;

    private ObjectMap<Integer,Station> Stations;

    Station lastStation;

    Station currentStation;

    private Texture radioTexture;

    private Texture knobTexture;

    /**
     * Return the current position of the radio.
     * @return pos
     */
    public Vector2 getPos() {
        return pos;
    }

    /**
     * Return the current position of the radio knob.
     * @return knobPos
     */
    public Vector2 getKnobPos() {
        return knobPos;
    }

    /**
     * Set the angle of the radio knob.
     * @param a The angle to set the wheel to
     */
    public void setknobAng(float a) { knobAng = a; }

    /**
     * Return the current angle of the radio knob.
     * @return knobAng
     */
    public float getknobAng() { return knobAng; }

    /**
     * Sets the image texture for this radio
     */
    public void setRadioSprite(Texture tex) { radioTexture = tex; }

    public Texture getKnobTexture() {
        return knobTexture;
    }

    public void setKnobSprite(Texture tex) { knobTexture = tex; }

    public Texture getRadioTexture() {
        return radioTexture;
    }

    public String getCurrentStationName() {
        if(currentStation != null) {
            return currentStation.toString();
        }
        return "";
    }

    public void setStation(){
        int stationNumber = -(int)knobAng;
        if(stationNumber <= 0){
            stationNumber = 0;
        }
        lastStation = currentStation;
        if(stationNumber>10 && stationNumber<100){
            currentStation = Stations.get(0);
        }
        else if(stationNumber>120 && stationNumber<190){
            currentStation = Stations.get(1);
        }
        else if(stationNumber>220 && stationNumber<300){
            currentStation = Stations.get(2);
        }
        else{
            currentStation = null;
        }
        System.out.println(stationNumber);
        System.out.println("last :" + lastStation);
        System.out.println("current:" + currentStation);

        if(lastStation != currentStation){
            System.out.println("entered da if1");
            playRadio();
        }
    }

    public void playRadio(){
        if(lastStation != null){
            System.out.println("entered da if2");
            lastStation.stopAudio();
        }
        if(currentStation == null){
            return;
        }

        currentStation.playAudio();
    }
    /** Creates a Radio with a center at screen coordinates (x,y).
     *
     * Additionally initiallizes list of radio songs based on
     * those in asset folder
     *
     * @param x The screen x-coordinate of the center
     * @param y The screen y-coordinate of the center
     */
    public Radio(float x, float y) {
        this.pos = new Vector2(x,y);
        this.knobPos = new Vector2(x-75,y);
        Stations = new ObjectMap<Integer, Station>();
        File[] radiosongs = new File("RadioSongs").listFiles();
        for(File f : radiosongs){
            if(f.isFile()){
                Stations.put(stationListSize,new Station(f.getName()));
                stationListSize++;
            }
        }
    }

    public void drawRadio(GameCanvas canvas){
        if(radioTexture == null || knobTexture == null) {
            return;
        }

        //Radio chasis sprite draw origin
        float oxr = 0.5f * radioTexture.getWidth();
        float oyr = 0.5f * radioTexture.getHeight();

        //knob draw positions
        float oxk = 0.5f * knobTexture.getWidth();
        float oyk = 0.5f * knobTexture.getHeight();

        canvas.draw(radioTexture, Color.WHITE, oxr, oyr, pos.x, pos.y, 0, 0.5f, 0.5f);
        canvas.draw(knobTexture, Color.WHITE, oxk, oyk, knobPos.x, knobPos.y, knobAng, 0.33f, 0.33f);

    }

    private class Station{
        private String name;

        private String audioFile;

        private boolean playing;

        private Music audio;

        private float volume;

        public Station(String filename) {
            this.name = filename.substring(0,filename.length()-4);
            this.audioFile = "RadioSongs/" + filename;


        }
        public void setName(String name){
            this.name = name;
        }

        public String getName(String name){
            return name;
        }

        public void setAudioFile(String audioFile) {
            this.audioFile = audioFile;
        }

        public String getAudioFile() {
            return audioFile;
        }

        public void setPlaying(boolean playing){
            this.playing = playing;
        }

        public boolean isPlaying() {
            return playing;
        }

        public void playAudio(){
            playing = true;
            audio = Gdx.audio.newMusic(Gdx.files.internal(audioFile));
            audio.play();
        }

        public void stopAudio(){
            System.out.println("stopping");
            playing = false;
            audio.stop();
            audio.dispose();
        }

        public void changeVolume(float volume){
            this.volume = volume;
            if(playing){
                audio.setVolume(volume);
            }
        }

        @Override
        public String toString(){
            return name;
        }
    }
}
