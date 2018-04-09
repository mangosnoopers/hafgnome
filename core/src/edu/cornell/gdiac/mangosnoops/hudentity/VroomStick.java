package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;

public class VroomStick {

    private static Vector2 position;
    private static Texture vroomStickTexture;
    private static Vector2 SCALING = new Vector2(0.4f, 0.4f); /* FIXME: should this be in GameCanvas? */

    private float WINDOW_WIDTH = Gdx.graphics.getWidth();
    private float WINDOW_HEIGHT = Gdx.graphics.getHeight();

    /**
     * engaged indicates that the stick has been fully pulled, which
     * triggers the increase of speed for a time that is specified in Road.
     *
     * "speed" is currently implemented like this: make the road "conveyor belt"
     * move more quickly. We could move some of this logic to Car I think.
     */
    private boolean engaged;

    private float ang = 0;
    private float ENGAGE_ANGLE = -35;

    public VroomStick(float x, float y) {
        position = new Vector2(x, y);
        engaged = false;
    }

    public void setVroomStickSprite(Texture v) { vroomStickTexture = v; }

    public void draw(GameCanvas canvas) {
        float width = vroomStickTexture.getWidth() * SCALING.x;
        float height = vroomStickTexture.getHeight() * SCALING.y;

        float ox = position.x - vroomStickTexture.getWidth() / 2;
        float oy = position.y - vroomStickTexture.getHeight() / 2;

        canvas.draw(vroomStickTexture, Color.WHITE, ox, oy, position.x-30, position.y+5, ang, SCALING.x, SCALING.y);
    }

    public void update(Vector2 in, float dy) {
        if (in != null && inVroomStickArea(in)) {
            ang -= dy;
            if (ang < ENGAGE_ANGLE) {
                ang = ENGAGE_ANGLE;
                engaged = true;
            }
        } else {
            ang += 0.2f * -ang;
            engaged = false;
        }

    }

    public boolean inVroomStickArea(Vector2 p) {

        /* FIXME: magic numbers */
        return WINDOW_WIDTH - p.x < position.x + 40
                && WINDOW_WIDTH - p.x > position.x - 40
                && WINDOW_HEIGHT - p.y < position.y + 150
                && WINDOW_HEIGHT - p.y > position.y - 100;
    }

    public boolean isEngaged() { return engaged; }

}
