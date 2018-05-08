package edu.cornell.gdiac.mangosnoops.roadentity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.util.FilmStrip;

public class Grill extends Enemy {

    /** How far away the flames extend in the y direction.
     *  e.g.:
     *
     *  y=50=startY    y=40    y=30   y=20=end         y=carY
     *  Grill - - - - flame - flame - flame - - - - - - Car
     */
    private float startY;
    private float grillEndY; // Confusingass name but oh well

    private static final float FLAME_PADDING = 0.5f;

    /** The instances of the Flame enemies belonging to this grill. */
    private Array<Flame> flames;

    private class Flame extends Enemy {
        public Flame(float x, float y) {
            super(x, y, ObjectType.FLAME);
            hoverDistance = 4.3f;
            enemyWidth = 0.07f;
            enemyHeight = 0.04f;
        }
    }

    public Grill(float x, float y) {
        super(x, y, ObjectType.GRILL);
        startY = y;
        flames = new Array<Flame>();
        enemyWidth = 0.16f;
        enemyHeight = 0.1f;
    }

    public Grill(Enemy g) {
        super(g.getX(), g.getY(), ObjectType.GRILL);
        startY = g.getY();
        flames = new Array<Flame>();
        enemyWidth = 0.16f;
        enemyHeight = 0.1f;
    }

    public float getGrillEndY() {
        return grillEndY;
    }

    /**
     * Set the y value of the end of this grill's frame.
     * @param y said y value
     */
    public void setStartOfGrill(float y) {
        grillEndY = y;
        setY(y);
        float currentY = getY() - FLAME_PADDING;
        while (currentY - FLAME_PADDING >= startY) {
            flames.add(new Flame(getX(), currentY));
            currentY -= FLAME_PADDING;
        }
    }

    public void update(float delta) {
        super.update(delta);
        for (Flame f : flames) {
            f.update(delta);
        }
    }

    /**
     * @return a reference to the flame enemies
     */
    public Array<Flame> getFlames() {
        return flames;
    }

    public void setFireTexture(Texture t) {
        for (Flame f : flames) {
            f.setFilmStrip(t, 1, 1);
        }
    }

    public void draw(GameCanvas canvas) {
        super.draw(canvas);
    }
}
