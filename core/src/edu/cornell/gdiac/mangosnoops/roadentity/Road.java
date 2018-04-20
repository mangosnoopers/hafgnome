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
    float END_OF_CONVEYOR_BELT = -13;
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
    private float MAX_VROOM_TIME = 40;

    /** frames left to vroom */
    private float vroomTimeLeft = MAX_VROOM_TIME;

    /** How quickly vroom time depreciates */
    private float VROOM_TIME_DEPRECIATION = 18f;

    /** Speed constants */
    private float NORMAL_SPEED = 1.8f;
    private float VROOM_SPEED = 4f;
    private float SPEED_DAMPING = 0.8f;

    /** How high the road/ground objects "hover" */
    private final float ROAD_HOVER_DISTANCE = 4.25f;

    /** Whether or not the car is vrooming */
    private boolean vrooming = false;
    /** The current speed of the car*/
    private float currentSpeed = NORMAL_SPEED;

    private float initialExitY;

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

    public Road(float endY) {

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

        // End vrooming if vroom time is over
        if (vroomTimeLeft < 0) {
            vrooming = false;
            vroomTimeLeft = MAX_VROOM_TIME;
        }

        // Compute which speed to use
        if (vrooming) {
            currentSpeed = VROOM_SPEED;
            vroomTimeLeft -= delta * VROOM_TIME_DEPRECIATION;
        } else if (reachedEndOfLevel()) {
            currentSpeed = NORMAL_SPEED;
        } else {
            currentSpeed = currentSpeed + SPEED_DAMPING * delta * (NORMAL_SPEED - currentSpeed);
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
            if (y > exitY) {
                /* FIXME: The grass and the exit overlap a bit and look weird */
                canvas.drawRoadObject(grassTexture, RIGHT_GRASS_X+EXIT_GRASS_OFFSET, y, ROAD_HOVER_DISTANCE, GRASS_WIDTH, GRASS_HEIGHT, ROAD_X_ROTATION, 0);
            } else {
                canvas.drawRoadObject(grassTexture, RIGHT_GRASS_X, y, ROAD_HOVER_DISTANCE, GRASS_WIDTH, GRASS_HEIGHT, ROAD_X_ROTATION, 0);
            }

            // Draw exit on the right
            canvas.drawRoadObject(exitTexture, EXIT_X, exitY,  ROAD_HOVER_DISTANCE, EXIT_WIDTH, EXIT_HEIGHT, ROAD_X_ROTATION, 0);

            // Draw exit road
            if (y > exitY) {
                canvas.drawRoadObject(roadTexture, EXIT_X, y, ROAD_HOVER_DISTANCE, ROAD_WIDTH, ROAD_HEIGHT, ROAD_X_ROTATION, 0);
            }
        }
    }

    public void setVrooming() {
        vrooming = true;
    }

    public float getSpeed() {
        return currentSpeed;
    }

    public boolean reachedEndOfLevel() {
        return exitY < -7;
    }

    /** Reset the road. */
    public void reset () {
        exitY = initialExitY;
    }
}
