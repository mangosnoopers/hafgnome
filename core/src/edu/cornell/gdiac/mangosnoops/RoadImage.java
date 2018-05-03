package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.math.Vector2;

public class RoadImage extends RoadObject {

    /**
     * Return the type of RoadObject
     */
    public ObjectType getType() {
        return ObjectType.IMAGE;
    }

    /**
     * Construct a roadside image at the given position.
     * @param x the x-position of the image
     * @param y the y-position of the image
     */
    public RoadImage(float x, float y) {
        super();
        position = new Vector2(x,y);
        // TODO velocity, moving stuff
    }
}
