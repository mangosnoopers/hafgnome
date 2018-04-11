package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;
import edu.cornell.gdiac.mangosnoops.hudentity.Wheel;

public abstract class Image {

    /** Texture center point relative to screen size
     *  i.e. (0.5f, 0.5f) would place asset in the middle of the screen */
    protected Vector2 position;
    /** The object's texture asset **/
    protected Texture texture;
    /** Relative width and height divided by texture -- i.e. 0.5f will make the
     * height of the texture half of the canvas screen height (width would scale the same amount)*/
    protected float relativeScale;
    /** An optional buffer given to the object in order to 'pad' its area of effectiveness**/
    protected float controlBuffer;
    /** Dimensions of the screen **/
    protected static Vector2 SCREEN_DIMENSIONS;


    public Image(float x, float y, float relSca, Texture tex) {
        position = new Vector2(x,y);
        relativeScale = relSca/(float)tex.getHeight();
        texture = tex;
        controlBuffer = 0;
    }

    public Image(float x, float y, float relSca, float cb, Texture tex) {
        position = new Vector2(x,y);
        relativeScale = relSca/(float)tex.getHeight();
        texture = tex;
        controlBuffer = cb;
    }

    public static void updateScreenDimensions(GameCanvas canvas){
        SCREEN_DIMENSIONS = new Vector2(canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Returns true if the mouse is positioned inside the area of the object
     *
     *  @param p the vector giving the mouse's (x,y) screen coordinates
     */
    protected boolean inArea(Vector2 p) {

        return ((p.x > position.x*SCREEN_DIMENSIONS.x - (0.5*(float)texture.getWidth()*relativeScale*SCREEN_DIMENSIONS.y + controlBuffer))
                && (p.x < position.x*SCREEN_DIMENSIONS.x + (0.5*(float)texture.getWidth()*relativeScale*SCREEN_DIMENSIONS.y + controlBuffer))
                && (SCREEN_DIMENSIONS.y - p.y > position.y*SCREEN_DIMENSIONS.y - 0.5f*texture.getHeight()*relativeScale*SCREEN_DIMENSIONS.y)
                && (SCREEN_DIMENSIONS.y - p.y < position.y*SCREEN_DIMENSIONS.y + 0.5f*texture.getHeight()*relativeScale*SCREEN_DIMENSIONS.y));
    }

    public void draw(GameCanvas canvas) {
        canvas.draw(texture, Color.WHITE, 0, 0, position.x*canvas.getWidth(), position.y*canvas.getHeight(), 0,
                relativeScale*canvas.getHeight(),
                relativeScale*canvas.getHeight());
    }

    public void draw(GameCanvas canvas, Color tint) {
        canvas.draw(texture, tint, 0, 0, position.x*canvas.getWidth(), position.y*canvas.getHeight(), 0,
                relativeScale*canvas.getHeight(),
                relativeScale*canvas.getHeight());
    }

}
