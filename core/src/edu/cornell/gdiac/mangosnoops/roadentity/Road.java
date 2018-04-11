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

/**
 * Contains road logic, in particular, the "conveyor belt" effect stuff
 */
public class Road extends RoadObject {

    private Array<Vector3> roadPositions;
    int NUM_ROAD_DECALS = 30;
    Texture texture;

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

    @Override
    public ObjectType getType() {
        return null;
    }

    public Road() {
        /* FIXME: Get texture from asset manager */
        texture = new Texture(Gdx.files.internal("images/road.png"));

        roadPositions = new Array<Vector3>(7);
        for (int i = 0; i < NUM_ROAD_DECALS; i++) {
            roadPositions.add(new Vector3(0, -0.8f*i, 1));
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
        for (Vector3 p : roadPositions) {
            float newY = p.y - currentSpeed * delta;
            if (newY < -13) { /* FIXME: magic # */
                newY = 0;
            }
            p.set(0, newY, ROAD_HOVER_DISTANCE);
        }
    }

    public void draw(GameCanvas canvas) {

        for (Vector3 p : roadPositions) {
            canvas.drawRoadObject(texture, p.x, p.y, ROAD_HOVER_DISTANCE, 1, 1, 0, 0 );
        }
    }

    public void setVrooming() {
        vrooming = true;
    }

    public float getSpeed() {
        return currentSpeed;
    }
}
