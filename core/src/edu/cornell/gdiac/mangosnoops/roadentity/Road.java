package edu.cornell.gdiac.mangosnoops.roadentity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.RoadObject;

/**
 * Contains road logic, in particular, the "conveyor belt" effect stuff
 */
public class Road extends RoadObject {

    private Array<Decal> roadDecals;
    int NUM_ROAD_DECALS = 30;
    TextureRegion roadTextureRegion;

    @Override
    public ObjectType getType() {
        return null;
    }

    public Road() {
        /* FIXME: Get texture from asset manager */
        roadTextureRegion = new TextureRegion(new Texture(Gdx.files.internal("images/road.png")));

        roadDecals = new Array<Decal>(7);
        for (int i = 0; i < NUM_ROAD_DECALS; i++) {
            Decal newDecal = Decal.newDecal(1, 1, roadTextureRegion);
            newDecal.setPosition(0, -0.8f*i, 1);
            roadDecals.add(newDecal);
        }
    }

    public void update(float delta) {

        for (Decal d : roadDecals) {
            float newY = d.getY() - 2 * delta;
            if (newY < -13) {
                newY = 0;
            }
            d.setPosition(0, newY, 4.25f);

        }
    }

    public void draw(GameCanvas canvas, float xOff) {
        for (Decal d: roadDecals) {
            canvas.drawRoad(xOff, d);
        }
    }
}
