package edu.cornell.gdiac.mangosnoops.roadentity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.RoadObject;

import javax.xml.soap.Text;
import java.util.LinkedList;

/**
 * Contains logic relating to the road and exit.
 */
public class Road extends RoadObject {

    /** The road decals. */
    private LinkedList<Float> yPositions;
    /** The number of road decals to draw. */
    int NUM_ROAD_DECALS = 30;
    /** The distance from the camera of the farthest road. */
    float DISTANCE_TO_DRAW = 5;
    /** The threshold after which a road should be moved to the front of the
     * "conveyor belt". */
    float END_OF_CONVEYOR_BELT = -3;
    /** The road texture. */
    Texture roadTexture;
    /** The grass texture. */
    Texture grassTexture;
    /** The exit texture */
    Texture exitTexture;

    /** Road y rotation amount */
    float ROAD_X_ROTATION = 0f;

    /** World x coordinates of grass */
    float LEFT_GRASS_X = -1.5f;
    float RIGHT_GRASS_X = 1.5f;

    /** World x coordinates of road */
    float ROAD_X = 0;

    /** World x,y coordinates of exit */
    float EXIT_X = 0.8f;
    float exitY;

    /** Texture dimensions of road */
    float ROAD_WIDTH = 1;
    float ROAD_HEIGHT = 1;

    /** Texture dimensions of grass */
    float GRASS_WIDTH = 2;
    float GRASS_HEIGHT = 1;

    /** Texture dimensions of exit */
    float EXIT_WIDTH = 1;
    float EXIT_HEIGHT = 6;

    /** */
    float EXIT_GRASS_OFFSET = 0.6f;

    /** max # frames to vroom */
    private float MAX_VROOM_TIME = 7;

    /** frames left to vroom */
    private float vroomTimeLeft = MAX_VROOM_TIME;

    /** How quickly vroom time depreciates */
    private float VROOM_TIME_DEPRECIATION = 25f;

    /** Speed constants */
    private float NORMAL_SPEED = 1.4f;
    private float VROOM_SPEED = 3f;
    private float DECELERATION = 3f;
    private float VROOM_ACCELERATION = 6f;

    /** How high the road/ground objects "hover" */
    private final float ROAD_HOVER_DISTANCE = 4.25f;

    /** The current speed of the car */
    private float currentSpeed = NORMAL_SPEED;
    /** How far away the exit starts */
    private float initialExitY;

    private enum RoadState {
        NORMAL, // Road is normal speed
        ACCELERATING, // engaged vroom stick -> this state, speed up until VROOM_SPEED
        DECELERATING, // not engaged vroom stick, speed is > normal, -> this state, slow down til NORMAL_SPEED
        MAX_SPEED
    }

    /** Current state of the road */
    private RoadState state;

    /** Return the current speed */
    public float getCurrentSpeed() { return currentSpeed; }

    @Override
    public ObjectType getType() {
        return null;
    }

    public void setRoadTexture(Texture t) {
        roadTexture = t;
    }

    public void setGrassTexture(Texture t) {
        grassTexture = t;
    }

    public void setExitTexture(Texture t) {
        exitTexture = t;
    }

    public void setRoadExitY(float newEndY) {
        exitY = newEndY;
    }

    public float getRoadExitY() { return exitY; }

    public Road(float endY) {

        state = RoadState.NORMAL;

        // The exit will appear at the end of the level
        initialExitY = endY;
        exitY = endY;

        // Invariant: roadPositions[i+1] is closer to the camera than roadPositions[i]
        yPositions = new LinkedList<Float>();
        for (int i = 0; i < NUM_ROAD_DECALS; i++) {
            if (i == 0) {
                yPositions.add(new Float(DISTANCE_TO_DRAW));
            } else {
                yPositions.add(new Float(yPositions.get(i-1)-1));
            }
        }

    }

    public void update(float delta) {

//        if (state != RoadState.NORMAL) {
//            System.out.println(state);
//        }

        // Update currentSpeed based on state
        switch (state) {
            case NORMAL:
                // Do nothing
                break;
            case ACCELERATING:
                /* Accelerate, and if reached max speed, transition to new state
                   and cap the speed */
                currentSpeed += VROOM_ACCELERATION * delta;
                if (currentSpeed > VROOM_SPEED) {
                    currentSpeed = VROOM_SPEED;
                    state = RoadState.MAX_SPEED;
                }
                break;
            case MAX_SPEED:
                vroomTimeLeft -= delta * VROOM_TIME_DEPRECIATION;
                if (vroomTimeLeft < 0) {
                    vroomTimeLeft = MAX_VROOM_TIME;
                    state = RoadState.DECELERATING;
                }
                break;
            case DECELERATING:
                currentSpeed -= DECELERATION * delta;
                if (currentSpeed < NORMAL_SPEED) {
                    currentSpeed = NORMAL_SPEED;
                    state = RoadState.NORMAL;
                }
                break;
        }


        // Move farthest road texture towards the camera
        yPositions.set(0, yPositions.get(0) - currentSpeed * delta);

        // Move the rest of the road textures
        for (int i = 1; i < yPositions.size(); i++) {
            /* FIXME: optimize this */
            yPositions.set(i, yPositions.get(i-1) - ROAD_HEIGHT);
        }

        // Move the exit towards the camera
        exitY -= currentSpeed * delta;

        Float lastY = yPositions.getLast();
        Float firstY = yPositions.getFirst();
        if (lastY < END_OF_CONVEYOR_BELT) {
            yPositions.addFirst(firstY+1);
            yPositions.removeLast();
        }

    }

    public void draw(GameCanvas canvas) {

        for (Float y : yPositions) {

            // Draw road
            canvas.drawRoadObject(roadTexture, ROAD_X, y, ROAD_HOVER_DISTANCE, ROAD_WIDTH, ROAD_HEIGHT, ROAD_X_ROTATION, 0);

            // Draw grass on the left
            canvas.drawRoadObject(grassTexture, LEFT_GRASS_X, y, ROAD_HOVER_DISTANCE, GRASS_WIDTH, GRASS_HEIGHT, ROAD_X_ROTATION, 0);

            // Draw grass on the right
            canvas.drawRoadObject(grassTexture, RIGHT_GRASS_X, y, ROAD_HOVER_DISTANCE, GRASS_WIDTH, GRASS_HEIGHT, ROAD_X_ROTATION, 0);

            // Draw exit road
            if (y > exitY) {
                canvas.drawRoadObject(roadTexture, EXIT_X, y, ROAD_HOVER_DISTANCE, ROAD_WIDTH, ROAD_HEIGHT, ROAD_X_ROTATION, 0);
            }
        }
    }

    /**
     * Set "vrooming" to true, which will increase the road speed,
     * which will subsequently decay over time, back to the normal speed.
     */
    public void setVrooming() {
        state = RoadState.ACCELERATING;
    }

    /**
     * @return the current speed of the road
     */
    public float getSpeed() {
        return currentSpeed;
    }

    public void setNormalSpeed(float newSpeed) {
        NORMAL_SPEED = newSpeed;
    }

    public void setAccelerationSpeed(float newAcc) {
        VROOM_ACCELERATION = newAcc;
    }

    public void setVroomSpeed(float newVroom) {
        VROOM_SPEED = newVroom;
    }

    public void setDECELERATION(float newDec) {
        DECELERATION = newDec;
    }

    /**
     * @return current speed to normal speed ratio, capped at 2x
     */
    public float getSpeedRatio() {
        return Math.min(currentSpeed / NORMAL_SPEED, 1.9f);
    }

    /**
     * @return whether or not the Car has reached the end of the level.
     */
    public boolean reachedEndOfLevel() {
        return exitY < 1;
    }

    /** Reset the road. */
    public void reset () {
        exitY = initialExitY;
        currentSpeed = NORMAL_SPEED;
    }
}
