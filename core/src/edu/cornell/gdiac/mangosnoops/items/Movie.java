package edu.cornell.gdiac.mangosnoops.items;

import com.badlogic.gdx.graphics.Texture;

public class Movie extends Item {
    /** Duration of the movie */
    private int duration;

    /** Get the duration */
    public int getDuration() { return duration; }

    public Movie(float x, float y, float relSca, Texture t, ItemType type, String n, int duration) {
        super(x, y, relSca, t, type, n);
        this.duration = duration;
    }
}