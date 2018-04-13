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
 * Contains road logic, in particular, the "conveyor belt" effect stuff
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

    float LEFT_GRASS_X = -1.5f;
    float RIGHT_GRASS_X = 1.5f;
    float ROAD_X = 0;

    float ROAD_WIDTH = 1;
    float ROAD_HEIGHT = 1;

    float GRASS_WIDTH = 2;
    float GRASS_HEIGHT = 1;

    /** max # frames to vroom */
    private float MAX_VROOM_TIME = 40;

    /** frames left to vroom */
    private float vroomTimeLeft = MAX_VROOM_TIME;

    /** How quickly vroom time depreciates */
    private float VROOM_TIME_DEPRECIATION = 18f;

    private float NORMAL_SPEED = 1.8f;
    private float VROOM_SPEED = 4f;

    private float SPEED_DAMPING = 0.8f;

    private final float ROAD_HOVER_DISTANCE = 4.25f;

    private boolean vrooming = false;
    private float currentSpeed = NORMAL_SPEED;

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

    public Road() {

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


        if (vroomTimeLeft < 0) {
            vrooming = false;
            vroomTimeLeft = MAX_VROOM_TIME;
        }

        if (vrooming) {
            currentSpeed = VROOM_SPEED;
            vroomTimeLeft -= delta * VROOM_TIME_DEPRECIATION;
        } else {
            currentSpeed = currentSpeed + SPEED_DAMPING * delta * (NORMAL_SPEED - currentSpeed);
        }

        for (int i = 0; i < yPositions.size(); i++) {
            /* FIXME: optimize this */
            yPositions.set(i, yPositions.get(i) - currentSpeed * delta);
        }

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
            canvas.drawRoadObject(roadTexture, ROAD_X, y, ROAD_HOVER_DISTANCE, ROAD_WIDTH, ROAD_HEIGHT, 0, 0 );

            // Draw grass on the left
            canvas.drawRoadObject(grassTexture, LEFT_GRASS_X, y, ROAD_HOVER_DISTANCE, GRASS_WIDTH, GRASS_HEIGHT, 0, 0 );

            // Draw grass on the right
            canvas.drawRoadObject(grassTexture, RIGHT_GRASS_X, y, ROAD_HOVER_DISTANCE, GRASS_WIDTH, GRASS_HEIGHT, 0, 0 );
        }
    }

    public void setVrooming() {
        vrooming = true;
    }

    public float getSpeed() {
        return currentSpeed;
    }
}
