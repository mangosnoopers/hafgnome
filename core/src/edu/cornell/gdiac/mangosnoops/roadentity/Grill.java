package edu.cornell.gdiac.mangosnoops.roadentity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.util.FilmStrip;

public class Grill extends Enemy {

    /** How far away the flames extend in the y direction.
     *  e.g.:
     *
     *  y=50     y=40    y=30   y=20=flameEndY    y=carY
     *  Grill - flame - flame - flame - - - - - - carY
     */
    private float flameEndY;

    /** The instances of the Flame enemies belonging to this grill. */
    private Array<Flame> flames;

    private class Flame extends Enemy {
        public Flame(float x, float y) {
            super(x, y, ObjectType.FLAME);
        }
    }

    public Grill(float x, float y) {
        super(x, y, ObjectType.GRILL);
    }

    /**
     * Set the y value of the end of this grill's frame.
     * @param y said y value
     */
    public void setEndOfFlame(float y) {
        flameEndY = y;
    }

    public void update(float delta) {
        super.update(delta);
        /*
        for (Flame f : flames) {
            f.update(delta);
        }
        */
    }

    public void draw(GameCanvas canvas) {
        super.draw(canvas);
        /*
        for (Flame f : flames) {
            f.draw(canvas);
        }
        */
    }
}
