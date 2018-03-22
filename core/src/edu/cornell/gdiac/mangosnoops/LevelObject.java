package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.utils.*;
import edu.cornell.gdiac.mangosnoops.roadentity.*;

public class LevelObject {
    /** All the objects at the beginning of the level. */
    private Array<Gnome> gnomez;
    /** Number of Lanes in the level. */
    private int numLanes;
    private Car yonda;

    public int getLanes() { return numLanes; }

    public Array<Gnome> getGnomez() { return gnomez; }

    public Car getCar() { return yonda; }

    /** A mapping between the level's song genres and its
     * song mp3 files
     */
    private ObjectMap<String,String> stationGenreMP3Mapping;

    /**
     * Loads in JSON File to create a Level Object.
     *
     * @param fileName name of JSON File with level information. */
    public LevelObject(String fileName) {
        yonda = new Car();
    }

    /**
     * Creates a default three length road.
     */
    public LevelObject() {
        throw new java.lang.UnsupportedOperationException();
    }

}
