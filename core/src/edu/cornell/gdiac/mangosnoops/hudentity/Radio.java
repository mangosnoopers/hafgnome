package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import edu.cornell.gdiac.mangosnoops.Image;

import java.awt.*;
import java.io.File;
import java.util.List;

public class Radio extends Image {
    /** Rotation speed */
    private static final float ROTATION_SPEED = 0.05f;

    /** Current angle of the radioknob */
    private float knobAng;
    /** List of available stations **/
    private Array<Station> Stations;
    /** The last played station **/
    Station lastStation;
    /** The current playing station **/
    Station currentStation;
    /** The number of the currently playing station **/
    int stationNumber;

    /** Enum for song genres **/
    public enum Genre{
        CREEPY, DANCE, ACTION, JAZZ,
        POP, THUG, COMEDY, NONE
    }

    /** TODO: delete */
    private int clicks;

    //TODO delete, this is only here to test until JSON parser is completed
    private ObjectMap<String,String> testMap;
    private void genTestMap(){
        testMap = new ObjectMap<String, String>();
        testMap.put("CREEPY", "bensound-creepy.mp3");
        testMap.put("DANCE", "bensound-dance.mp3");
        testMap.put("JAZZ", "bensound-jazzcomedy.mp3");
    }


    // TODO: shouldn't need this constructor?
    public Radio(float x, float y, float relSize, float cb, Texture tex) {
        super(x, y, relSize, cb, tex);

        // Create Station list
        Stations = new Array<Station>();

        // TODO Take this out, it is only here for testing until JSON parser is complete
        genTestMap();
        updateStations(testMap);

    }

    public Radio(float x, float y, float relSize, float cb, Texture tex, ObjectMap<String,Genre> songs) {
        super(x, y, relSize, cb, tex);

        // Create Station list
        Stations = new Array<Station>();
        for (String songname : songs.keys()) {
            Stations.add(new Station(songs.get(songname), songname));
        }
    }

    public Station getLastStation(){ return lastStation; }

    public Station getCurrentStation(){ return currentStation; }

    /**
     * Return the current position of the radio.
     * @return pos
     */
    public Vector2 getPos() {
        return position;
    }

    /**
     * Return the current angle of the radio knob.
     * @return knobAng
     */
    public float getknobAng() { return knobAng; }

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

    /**
     * return genre of the current playing station
     * @return current station genre
     */
    public Genre getCurrentStationGenre(){
        if(currentStation != null) {
            return currentStation.getGenre();
        }
        return Genre.NONE;
    }

    /**
     * Sets current list of songs for this radio, mp3 link format
     * (name in assets)
     * @param genreMP3Map
     */
    public void updateStations(ObjectMap<String,String> genreMP3Map){
        if(Stations != null){
            Stations.clear();
        }
        for(String g : genreMP3Map.keys()){
            Station s = new Station(Genre.valueOf(g), genreMP3Map.get(g));
            Stations.add(s);
        }
    }

    /**
     * Sets the current station based on the setting of the knob
     * Should the station change, the new audio is played and the old
     * is shut off
     */
    public void setStation() {
        // TODO: make this work for < 3 songs
        stationNumber = -(int) knobAng;
        if (stationNumber <= 0) {
            stationNumber = 0;
        }
        lastStation = currentStation;
        if (stationNumber > 10 && stationNumber < 100) {
            currentStation = Stations.get(0);
        } else if (stationNumber > 120 && stationNumber < 190) {
            currentStation = Stations.get(1);
        } else if (stationNumber > 220 && stationNumber < 300) {
            currentStation = Stations.get(2);
        } else {
            currentStation = null;
        }
    }

    /**
     * Draws the radio and its knob on the given canvas
     * @param canvas
     */
    public void draw(GameCanvas canvas, BitmapFont displayFont) {
        if (texture == null) {
            return;
        }

        float oxk = 0.5f * texture.getWidth();
        float oyk = 0.5f * texture.getHeight();

        canvas.draw(texture, Color.WHITE, oxk, oyk, position.x * canvas.getWidth(), position.y * canvas.getHeight(), knobAng,
                relativeScale * canvas.getHeight(), relativeScale * canvas.getHeight());

        canvas.drawText(getCurrentStationName(), displayFont, 0.75f * canvas.getWidth(), 0.2f * canvas.getHeight());
    }


    /**
     * Updates the radio based on the user's input.
     *
     * @param in where the mouse clicked (null if no click)
     * @param dx the change in x in the user's input
     */
    public void update(Vector2 in, float dx) {
        Vector2 src = new Vector2(0.0f,5.0f);
        //System.out.println("From update Radio: " + in);
        if (in != null && inArea(in)) {
            knobAng -= (in.angle(src) * ROTATION_SPEED);

            if (knobAng <= -360.0f) {
                knobAng = 0.0f;
            }
        }
        setStation();
    }

    /**
     * Inner Class for the individual stations of the radio
     *
     */
    public class Station{
        /** The name of this station **/
        private String name;
        /** The name of the file for the audio **/
        private String audioFile;
        /**Station genre **/
        private Genre genre;


        /**
         * Class constructor. Sets name of song to name of file, minus its
         * filetype extension
         * @param filename the string of the path to the file
         * @param g the genre of the radio station
         */
        public Station(Genre g, String filename) {
            name = filename.substring(11,filename.length()-4);
            audioFile = filename;
            genre = g;
        }

        /**
         * Returns genre of the station
         * @return genre of the station
         */
        public Genre getGenre() { return genre; }

        /**
         * Returns a reference to the audio file string
         * @return reference to station's audiofile string
         */
        public String getAudioFile(){ return audioFile; }

        @Override
        public String toString(){
            return name;
        }

    }

    public String toString(){
        return "Radio";
    }
}
