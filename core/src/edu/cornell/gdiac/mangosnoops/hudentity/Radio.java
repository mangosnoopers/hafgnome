package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

import java.io.File;

public class Radio extends HUDObject {
    /** Rotation speed */
    private static final float ROTATION_SPEED = 0.05f;
    /** Window height */
    private static final float WINDOW_HEIGHT = 600;
    /** Coordinates of radio center */
    private Vector2 pos;
    /** Coordinates of radioknob center */
    private Vector2 knobPos;
    /** Current angle of the radioknob */
    private float knobAng;
    /** Size of the list of stations **/
    private int stationListSize;
    /** List of available stations **/
    private ObjectMap<Integer,Station> Stations;
    /** The last played station **/
    Station lastStation;
    /** The current playing station **/
    Station currentStation;
    /** The number of the currently playing station **/
    int stationNumber;
    /** The Texture for this radio **/
    private Texture radioTexture;
    /** The Texture for this radio's knob **/
    private Texture knobTexture;
    /** Scale for drawing of the radio **/
    private final float KNOB_SCALE = 0.2f;
    /** TODO: delete */
    private int clicks;

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

    /**
     * Returns image texture for this
     * radio's knob
     * @return knobTexture
     */
    public Texture getKnobTexture() {
        return knobTexture;
    }

    /**
     * Set Texture for this radio's knob
     * @param tex
     */
    public void setKnobSprite(Texture tex) { knobTexture = tex; }

    /**
     * Return texture for this radio
     * @return radioTexture
     */
    public Texture getRadioTexture() {
        return radioTexture;
    }

    /**
     * return name of the current playing station
     * @return current station name
     */
    public String getCurrentStationName() {
        if(currentStation != null) {
            return currentStation.toString();
        }
        return "";
    }

    // TODO: maybe delete?
    public Station getCurrentStation() { return currentStation; }
    public boolean getCurrentStationNed() { return currentStation.nedHappy; }
    public boolean getCurrentStationNosh() { return currentStation.noshHappy; }

    /**
     * Sets the current station based on the setting of the knob
     * Should the station change, the new audio is played and the old
     * is shut off
     */
    public void setStation(){
        stationNumber = -(int)knobAng;
        if(stationNumber <= 0){
            stationNumber = 0;
        }
        lastStation = currentStation;
        if(stationNumber>10 && stationNumber<100){
            currentStation = Stations.get(4);
        }
        else if(stationNumber>120 && stationNumber<190){
            currentStation = Stations.get(5);
        }
        else if(stationNumber>220 && stationNumber<300){
            currentStation = Stations.get(6);
        }
        else{
            currentStation = null;
        }

        if(lastStation != currentStation){
            playRadio();
        }
    }

    /**
     * Plays the audio of the current station given a change
     * in station. Also responsible for stopping the audio
     * of the previous station.
     *
     * Should the current station be null, nothing is played.
     */
    public void playRadio(){
        if(lastStation != null){
            lastStation.stopAudio();
        }
        if(currentStation == null){
            return;
        }

        currentStation.playAudio();
    }
    /** Creates a Radio with a center at screen coordinates (x,y).
     *
     * Additionally initializes the list of radio songs based on
     * those in asset folder
     *
     * @param x The screen x-coordinate of the center
     * @param y The screen y-coordinate of the center
     */
    public Radio(float x, float y) {
        pos = new Vector2(x+50,y+50);
        knobPos = new Vector2(x,y);

        // Create Station list
        Stations = new ObjectMap<Integer, Station>();

        // TODO: make this not trash and work with custom moods
        // BENSOUND CREEPY
        File creepy = new File("RadioSongs/bensound-creepy.mp3");
        Stations.put(stationListSize, new Station(creepy.getName(), true, false));
        stationListSize++;

        // BENSOUND DANCE
        File dance = new File("RadioSongs/bensound-dance.mp3");
        Stations.put(stationListSize, new Station(dance.getName(), true, true));
        stationListSize++;

        // BENSOUND EXTREME ACTION
        File action = new File("RadioSongs/bensound-extremeaction.mp3");
        Stations.put(stationListSize, new Station(action.getName(), false, true));
        stationListSize++;

        // BENSOUND JAZZ COMEDY
        File jazz = new File("RadioSongs/bensound-jazzcomedy.mp3");
        Stations.put(stationListSize, new Station(jazz.getName(), true, true));
        stationListSize++;

        // TEST SONG 1
        File dadada = new File("RadioSongs/testsong.wav");
        Stations.put(stationListSize, new Station(dadada.getName(), false, true));
        stationListSize++;

        // TEST SONG 2
        File no = new File("RadioSongs/testsong2.wav");
        Stations.put(stationListSize, new Station(no.getName(), true, false));
        stationListSize++;

        // TEST SONG 3
        File weirdAl = new File("RadioSongs/testsong3.wav");
        Stations.put(stationListSize, new Station(weirdAl.getName(), true, true));
        stationListSize++;

//        File[] radiosongs = new File("RadioSongs").listFiles();
//        for(File f : radiosongs){
//            if(f.isFile()){
//                Stations.put(stationListSize,new Station(f.getName()));
//                stationListSize++;
//            }
//        }

    }

    /**
     * Draws the radio and its knob on the given canvas
     * @param canvas
     */
    public void draw(GameCanvas canvas){
        if(radioTexture == null || knobTexture == null) {
            return;
        }

        //Radio chasis sprite draw origin
        float oxr = 0.5f * radioTexture.getWidth();
        float oyr = 0.5f * radioTexture.getHeight();

        //knob draw positions
        float oxk = 0.5f * knobTexture.getWidth();
        float oyk = 0.5f * knobTexture.getHeight();

        //canvas.draw(radioTexture, Color.WHITE, oxr, oyr, pos.x, pos.y, 0, 0.5f, 0.5f);
        canvas.draw(knobTexture, Color.WHITE, oxk, oyk, knobPos.x, knobPos.y, knobAng, KNOB_SCALE, KNOB_SCALE);

    }

    /** Returns true if the mouse is positioned inside the radio dial.
     *
     * @param p the vector giving where the mouse's (x,y) screen coordinates.
     */
    private boolean inRadioArea(Vector2 p) {
        return p.x > knobPos.x - knobTexture.getWidth()*KNOB_SCALE*0.5f
                && p.x < knobPos.x + knobTexture.getWidth()*KNOB_SCALE*0.5f
                && WINDOW_HEIGHT - p.y > knobPos.y - knobTexture.getHeight()*KNOB_SCALE*0.5f
                && WINDOW_HEIGHT - p.y < knobPos.y + knobTexture.getHeight()*KNOB_SCALE*0.5f;
    }

    /**
     * Updates the radio based on the user's input.
     *
     * @param in where the mouse clicked (null if no click)
     * @param dx the change in x in the user's input
     */
    public void update(Vector2 in, float dx) {
        Vector2 src = new Vector2(0.0f,5.0f);
        if (in != null && inRadioArea(in)) {
//            knobAng = knobAng - dx;
            // TODO: make this not weird
            knobAng -= (in.angle(src) * ROTATION_SPEED);

            if (knobAng <= -360.0f) {
                knobAng = 0.0f;
            }
            setStation();
        }
    }

    /**
     * Inner Class for the individual stations of the
     * radio
     */
    private class Station{
        /** The name of this station **/
        private String name;
        /** The name of the file for the audio **/
        private String audioFile;
        /** indicates whether the current station is playing or not **/
        private boolean playing;
        /** Music class for the audio **/
        private Music audio;
        /** volume at which to play the audio **/
        private float volume;
        /** True if this station makes Ned happy */
        private boolean nedHappy;
        /** True if this station makes Nosh happy */
        private boolean noshHappy;

        /**
         * Class constructor. Sets name of song to name of file, minus its
         * filetype extension
         * @param filename the string of the path to the file
         * @param ned true if this station will make Ned happy
         * @param nosh true if this station will make Nosh happy
         */
        public Station(String filename, boolean ned, boolean nosh) {
            name = filename.substring(0,filename.length()-4);
            audioFile = "RadioSongs/" + filename;
            nedHappy = ned;
            noshHappy = nosh;
        }

        /**
         * Returns true if this station makes Ned happy
         */
        public boolean nedHappy() { return nedHappy; }

        /**
         * Returns true if this station makes Nosh happy
         */
        public boolean noshHappy() { return noshHappy; }

        /**
         * Creates the music file for the song and plays it
         **/
        public void playAudio(){
            playing = true;
            audio = Gdx.audio.newMusic(Gdx.files.internal(audioFile));
            audio.play();
        }

        /**
         * Stops the music file from playing
         * as the music is a managed resource, once we are no longer playing it
         * it is disposed of
         **/
        public void stopAudio(){
            playing = false;
            audio.stop();
            audio.dispose();
        }

        /**
         * Returns current playing volume
         * @return volume
         **/
        public float getVolume() {
            return volume;
        }

        /**
         *Changes the volume of the currently playing audio to
         * the specified value
         * @param volume
         **/
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
