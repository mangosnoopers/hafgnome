package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public abstract class HUDObject extends Image{

    /** Update the object based on the mouse input
     *
     * @param in
     * @param dx
     */
    public abstract void update(Vector2 in, float dx);

    /**
     *
     * @param x The object's center coordinates
     * @param y
     * @param r The object's relative size to the screen
     * @param cb Controlbuffer
     * @param t Object's Texture
     */
    public HUDObject(float x, float y, float r, float cb, Texture t) {
        super(x, y, r, cb, t);
    }

}
