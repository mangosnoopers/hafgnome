package edu.cornell.gdiac.mangosnoops.roadentity;

public class Flamingo extends Enemy {

    /** Flying speed of the flamingo */
    private final static float FLYING_XSPEED = 1f;
    private final static float FLYING_HOVERSPEED = 0.45f;

    /** How far away the flamingo can be without being
     *  affected by the horn. */
    private final static float FLYAWAY_DISTANCE = -8f;

    /** Whether or not the Flamingo is flying away. */
    private boolean isFlyingAway;

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
        isFlyingAway = false;
    }

    /**
     * Makes the Flamingo fly away. This should mean that:
     *  - Colliding with the Flamingo won't affect the car
     *  - The Flamingo will move up and to the right, out of view
     *  - The Flamingo uses its flying animation
     */
    public void setFlyingAway() {
        isFlyingAway = true;
        setAnimationBounds(4, 5);
    }

    /**
     * Returns whether or not the Flamingo is in the process
     * of flying away. Use this to ensure that flying flamingos
     * won't damage the car.
     * @return whether or not the Flamingo is flying
     */
    public boolean isFlyingAway() {
        return isFlyingAway;
    }

    public void update(float delta, float roadSpeed) {
        super.update(delta, roadSpeed);

        if (isFlyingAway) {
            float newX = getX() - FLYING_XSPEED * delta;
            float newHoverDistance = getHoverDistance() + FLYING_HOVERSPEED * delta;
            setX(newX);
            setHoverDistance(newHoverDistance);
        }
    }

    public float getFlyAwayDistance() {
        return FLYAWAY_DISTANCE;
    }

    public void setAnimationBounds(int minFrame, int maxFrame) {
        minAnimFrame = minFrame;
        maxAnimFrame = maxFrame;
        System.out.println("Set max anim frame to " + maxAnimFrame);
    }
}
