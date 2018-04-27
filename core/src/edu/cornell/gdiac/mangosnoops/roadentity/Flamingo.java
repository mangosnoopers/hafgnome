package edu.cornell.gdiac.mangosnoops.roadentity;

public class Flamingo extends Enemy {

    /**
     * Create a new Flamingo at position (x,y).
     * The Flamingo will appear stationary, and will
     * use its standing animation.
     *
     * @param x initial x-position of Flamingo
     * @param y initial y-position of Flamingo
     */
    public Flamingo(float x, float y) {
        super(x, y, ObjectType.FLAMINGO);
    }

    /**
     * Makes the Flamingo fly away. This should mean that:
     *  - Colliding with the Flamingo won't affect the car
     *  - The Flamingo will move up and to the right, out of view
     *  - The Flamingo uses its flying animation
     */
    public void setFlyingAway() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns whether or not the Flamingo is in the process
     * of flying away. Use this to ensure that flying flamingos
     * won't damage the car.
     * @return whether or not the Flamingo is flying
     */
    public boolean isFlyingAway() {
        throw new UnsupportedOperationException();
    }

}
