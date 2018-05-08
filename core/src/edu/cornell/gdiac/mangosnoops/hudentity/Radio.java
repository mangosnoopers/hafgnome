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

public class Radio {
    /** Rotation speed */
    private static float SLIDER_LEFTMOSTPOS = 0.7561f;
    private static float SLIDER_RIGHTMOSTPOS = 0.9439f;

    private float slider_left;
    private float slider_right;

    private Image knob;
    private Image slider;
    private Image pointer;
    private Image sound_on;
    private Image sound_off;
    private Image ned_like;
    private Image ned_dislike;
    private Image nosh_like;
    private Image nosh_dislike;

    boolean soundOn;
    /** Current angle of the radioknob */
    private float knobAng;
    /** List of available stations **/
    private static Array<Station> stations;
    /** The last played station **/
    Station lastStation;
    /** The current playing station **/
    Station currentStation;
    /** Whether or not the radio should play static (when they are holding down the slider)*/
    private boolean playStatic;

    /** Enum for song genres **/
    public enum Genre{
        CREEPY, DANCE, ACTION, JAZZ,
        POP, THUG, CLASSICAL, NONE
    }

    public Radio(Texture tex,
                 Texture s,
                 Texture p,
                 Texture son,
                 Texture soff,
                 Texture nel,
                 Texture ned,
                 Texture nol,
                 Texture nod,
                 ObjectMap<String,Genre> songs) {
        knob = new Image(0.75f, 0.225f, 0.07f, 0, tex, GameCanvas.TextureOrigin.MIDDLE);
        slider = new Image(0.85f, 0.15f, 0.02f, 50, s, GameCanvas.TextureOrigin.MIDDLE);
        pointer = new Image(0.85f, 0.17f, 0.03f, 0, p, GameCanvas.TextureOrigin.MIDDLE);
        sound_on = new Image(0.91f, 0.3f, 0.045f, 0, son, GameCanvas.TextureOrigin.MIDDLE);
        sound_off = new Image(0.91f, 0.3f, 0.045f, 0, soff, GameCanvas.TextureOrigin.MIDDLE);
        ned_like = new Image(0.5f, 0.5f, 0.07f, 0, nel, GameCanvas.TextureOrigin.MIDDLE);
        ned_dislike = new Image(0.5f, 0.5f, 0.07f, 0, ned, GameCanvas.TextureOrigin.MIDDLE);
        nosh_like = new Image(0.5f, 0.5f, 0.07f, 0, nol, GameCanvas.TextureOrigin.MIDDLE);
        nosh_dislike = new Image(0.5f, 0.5f, 0.07f, 0, nod, GameCanvas.TextureOrigin.MIDDLE);

        soundOn = true;
        playStatic = false;
        // Create Station list
        stations = new Array<Station>();
        for (String songname : songs.keys()) {
            stations.add(new Station(songs.get(songname), songname));
        }
    }

    public Station getLastStation(){ return lastStation; }

    public Station getCurrentStation(){ return currentStation; }

    public int getNumStations() { return stations.size; }

    public boolean isSoundOn() { return soundOn; }

    /**
     * Return the current angle of the radio knob.
     * @return knobAng
     */
    public float getknobAng() { return knobAng; }

    /**
     * @return current station name
     */
    public String getCurrentStationName() {
        if(currentStation != null) {
            return currentStation.getStationName();
        }
        return "";
    }

    /**
     * @return current station genre
     */
    public Genre getCurrentStationGenre(){
        if(currentStation != null) {
            return currentStation.getGenre();
        }
        return Genre.NONE;
    }

    /**
     * @return current station name
     */
    public String getCurrentStationSong() {
        if(currentStation != null) {
            return currentStation.getAudioFile().substring(11,currentStation.getAudioFile().length()-4);
        }
        return "";
    }

    /**
     * Sets current list of songs for this radio, mp3 link format
     * (name in assets)
     * @param genreMP3Map
     */
    public void updateStations(ObjectMap<String,String> genreMP3Map){
        if(stations != null){
            stations.clear();
        }
        for(String g : genreMP3Map.keys()){
            Station s = new Station(Genre.valueOf(g), genreMP3Map.get(g));
            stations.add(s);
        }
    }

    /**
     * Sets the current station based on the setting of the knob
     * Should the station change, the new audio is played and the old
     * is shut off
     */
    public void setStation() {
        int numStations = stations.size;
        if(numStations != 0) {
            lastStation = currentStation;
            int stationNumber;
            float oneUnit = (SLIDER_RIGHTMOSTPOS - SLIDER_LEFTMOSTPOS)/numStations;
            stationNumber = (int)((pointer.getPosition().x-SLIDER_LEFTMOSTPOS)/oneUnit);
            if(stationNumber >= numStations) stationNumber = numStations-1;
            currentStation = stations.get(stationNumber);
        }
    }

    public boolean shouldPlayStatic() { return playStatic; }

    boolean prevClicked = false;
    /**
     * Updates the radio based on the user's input.
     *
     * @param in where the mouse clicked (null if no click)
     * @param dx the change in x in the user's input
     */
    public void update(Vector2 in, float dx) {
        setStation();
        slider_left = 0.85f - 0.5f*slider.getTexture().getWidth()*0.02f*slider.getScreenHeight()/(slider.getTexture().getHeight()*slider.getScreenWidth());
        slider_right = 0.85f + 0.5f*slider.getTexture().getWidth()*0.02f*slider.getScreenHeight()/(slider.getTexture().getHeight()*slider.getScreenWidth());
        if(in != null && slider.inArea(in)) {
            soundOn = true;
            playStatic = true;
            float newPos = in.x/pointer.getScreenWidth();
            if(newPos < slider_left) newPos = slider_left;
            else if(newPos > slider_right) newPos = slider_right;
            pointer.updateX(newPos);
        } else {
            playStatic = false;
        }
        if(prevClicked == true && in == null && soundOn) {
            soundOn = false;
            currentStation.getAudio().setVolume(0);
        } else if(prevClicked == true && in == null && !soundOn) {
            soundOn = true;
            currentStation.getAudio().setVolume(1);
        }
        if(currentStation != null) {
            if(soundOn) {
                currentStation.getAudio().setVolume(0);
            } else {
                currentStation.getAudio().setVolume(1);
            }
        }
        prevClicked = in != null && sound_on.inArea(in);
    }

    /**
     * Draws the radio and its knob on the given canvas
     * @param canvas
     */
    public void draw(GameCanvas canvas, BitmapFont displayFont) {
//        knob.draw(canvas, knobAng);
        slider.draw(canvas);
        pointer.draw(canvas);
        if(soundOn) sound_on.draw(canvas);
        else sound_off.draw(canvas);
        switch(currentStation.getGenre()) {
            case DANCE:
                break;
            case ACTION:
                break;
            case JAZZ:
                break;
            case THUG:
                break;
            default:
                break;
        }
//        ned_like.draw(canvas);
//        nosh_like.draw(canvas);
        displayFont.setColor(Color.FIREBRICK);
        canvas.drawTextCenterOrigin(getCurrentStationName(), displayFont, 0.83f, 0.3f);
        canvas.drawTextCenterOrigin("\n" + getCurrentStationSong(), displayFont, 0.85f, 0.28f);
    }

    /**
     * Inner Class for the individual stations of the radio =========================================================
     *
     */
    public class Station{
        /** The name of this station **/
        private String name;
        /** The name of the file for the audio **/
        private String audioFileName;
        /**Station genre **/
        private Genre genre;
        private Music audio;

        /**
         * Class constructor. Sets name of song to name of file, minus its
         * filetype extension
         * @param filename the string of the path to the file
         * @param g the genre of the radio station
         */
        public Station(Genre g, String filename) {
            switch(g) {
                case CREEPY:
                    name = "Creepy";
                    break;
                case DANCE:
                    name = "Dance";
                    break;
                case ACTION:
                    name = "Action";
                    break;
                case JAZZ:
                    name = "Jazz";
                    break;
                case POP:
                    name = "Pop";
                    break;
                case THUG:
                    name = "Thug";
                    break;
                case CLASSICAL:
                    name = "Classical";
                    break;
                default:
                    name = "NONE";
                    break;
            }
            audioFileName = filename;
            audio = Gdx.audio.newMusic(Gdx.files.internal(filename));
            audio.setLooping(true);
            genre = g;
        }

        public String getStationName() { return name; }

        /**
         * Returns genre of the station
         * @return genre of the station
         */
        public Genre getGenre() { return genre; }

        /**
         * Returns a reference to the audio file string
         * @return reference to station's audiofile string
         */
        public String getAudioFile(){ return audioFileName; }

        public Music getAudio() { return audio; }
    }
}
