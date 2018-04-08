package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public abstract class Image {

    /** Texture center point relative to screen size
     *  i.e. (0.5f, 0.5f) would place asset in the middle of the screen */
    protected Vector2 position;
    protected Texture texture;
    /** Relative width and height divided by texture -- i.e. 0.5f will make the
     * height of the texture half of the canvas screen height (width would scale the same amount)*/
    protected float relativeScale;
    protected float controlBuffer;

    public Image(float x, float y, float r, Texture t) {
        position = new Vector2(x,y);
        relativeScale = r/(float)texture.getHeight();
        texture = t;
        controlBuffer = 0;
    }

    public Image(float x, float y, float r, float cb, Texture t) {
        position = new Vector2(x,y);
        relativeScale = r/(float)texture.getHeight();
        texture = t;
        controlBuffer = cb;
    }

    /**
     *  Returns whether or not position is on this object.
     *
     *  @param p the vector giving (x,y) screen coordinates
     */
    public boolean inArea(Vector2 p, GameCanvas canvas) {
        return (p.x > position.x - 0.5f*relativeScale*canvas.getHeight()
                                    *((float)texture.getWidth()/(float)texture.getHeight()))
                && (p.x < position.x + 0.5f*relativeScale*canvas.getHeight()
                                        *(float)texture.getWidth()/(float)texture.getHeight())
                && (p.y > position.y - 0.5f*relativeScale*canvas.getHeight())
                && (p.y < position.y + 0.5f*relativeScale*canvas.getHeight());
    }

    public void draw(GameCanvas canvas) {
        canvas.draw(texture, Color.WHITE, 0, 0, position.x, position.y, 0,
                relativeScale*canvas.getHeight(),
                relativeScale*canvas.getHeight());
    }

}
