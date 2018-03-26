package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.HUDObject;

public class VroomStick extends HUDObject {

    private static Vector2 position;
    private static Texture vroomStickTexture;
    private static Vector2 SCALING = new Vector2(0.4f, 0.4f); /* FIXME: should this be in GameCanvas? */


    public VroomStick(float x, float y) {
        position = new Vector2(x, y);
    }

    public void setVroomStickSprite(Texture v) { vroomStickTexture = v; }

    public void draw(GameCanvas canvas) {
        float width = vroomStickTexture.getWidth() * SCALING.x;
        float height = vroomStickTexture.getHeight() * SCALING.y;
        canvas.draw(vroomStickTexture, position.x, position.y, width, height);
    }

}
