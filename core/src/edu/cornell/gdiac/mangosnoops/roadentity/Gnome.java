package edu.cornell.gdiac.mangosnoops.roadentity;

public class Gnome extends Enemy {

    /**
     * Create a new Gnome at position (x, y).
     * @param x initial x-position of the gnome
     * @param y initial y-position of the gnome
     */
    public Gnome(float x, float y) {
        super(x, y, EnemyType.GNOME);
    }

    /**
     * Create a new Gnome with the same position and
     * other attributes as g.
     *
     * @param g the gnome to copy
     */
    public Gnome(Gnome g) {
        super(g.getX(), g.getY(), EnemyType.GNOME);
    }
}
