package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;
import edu.cornell.gdiac.mangosnoops.hudentity.Wheel;

public class Image {

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

    /** The maximimum amount of offset that is applied to
     *  the drawing coordinates, for the "shake" effect */
    protected float MAX_SHAKE_AMOUNT = 5;

    /** The current offset that is applied to the dash drawing
     *  coordinates, for the "shake" effect */
    protected float currentShakeAmount = 0;

    /** The current shake magnitude */
    private float currentShakeMagnitude = 0;

    /** Whether or not the object is shaking from a collision */
    protected boolean isShaking = false;

    /** How quickly the shake ends, in range (0, 1)
     *  smaller value => depletes more quickly */
    protected float SHAKE_DEPLETION = 0.95f;

    /** The sum of the deltas passed to every update call once a
     *  shake begins */
    protected float shakeDeltaSum = 0;

    /** Update the shake amount. */
    protected void updateShake(float delta) {

        if (isShaking) {
            shakeDeltaSum += delta;
            currentShakeMagnitude *= SHAKE_DEPLETION;

            currentShakeAmount = currentShakeMagnitude  * ((float) Math.cos(30*shakeDeltaSum) - MAX_SHAKE_AMOUNT);
            if (Math.abs(currentShakeMagnitude) < 0.001) {
                isShaking = false;
                currentShakeMagnitude = 0;
                shakeDeltaSum = 0;
            }

        } else {
            currentShakeAmount = 0;
        }

    }

    /** Apply the shake to the particular image.  Inventory must override this,
     *  because items should not shake if they are being dragged by the mouse. */
    public void applyShake() {
        currentShakeMagnitude = MAX_SHAKE_AMOUNT;
        isShaking = true;
        shakeDeltaSum = 0;
    }

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
    public boolean inArea(Vector2 p) {
        return ((p.x > position.x*SCREEN_DIMENSIONS.x - (0.5f*texture.getWidth()*relativeScale*SCREEN_DIMENSIONS.y + controlBuffer))
                && (p.x < position.x*SCREEN_DIMENSIONS.x + (0.5f*texture.getWidth()*relativeScale*SCREEN_DIMENSIONS.y + controlBuffer))
                && (SCREEN_DIMENSIONS.y - p.y > position.y*SCREEN_DIMENSIONS.y - (0.5f*texture.getHeight()*relativeScale*SCREEN_DIMENSIONS.y + controlBuffer))
                && (SCREEN_DIMENSIONS.y - p.y < position.y*SCREEN_DIMENSIONS.y + (0.5f*texture.getHeight()*relativeScale*SCREEN_DIMENSIONS.y + controlBuffer)));
    }

    public void draw(GameCanvas canvas) {

        float yWithOffset = position.y * canvas.getHeight() + currentShakeAmount;
        canvas.draw(texture, Color.WHITE, 0, 0, position.x*canvas.getWidth(), yWithOffset, 0,
                relativeScale*canvas.getHeight(),
                relativeScale*canvas.getHeight());
    }

    public void draw(GameCanvas canvas, Color tint) {

        float yWithOffset = position.y * canvas.getHeight() + currentShakeAmount;
        canvas.draw(texture, tint, 0, 0, position.x*canvas.getWidth(), yWithOffset, 0,
                relativeScale*canvas.getHeight(),
                relativeScale*canvas.getHeight());
    }

    public void draw(GameCanvas canvas, float ang) {
        System.out.println("The angle is" + ang);
        float yWithOffset = position.y * canvas.getHeight() + currentShakeAmount;
        canvas.draw(texture, Color.WHITE, 0, 0, position.x*canvas.getWidth(), yWithOffset, ang,
                relativeScale*canvas.getHeight(),
                relativeScale*canvas.getHeight());
    }
}
