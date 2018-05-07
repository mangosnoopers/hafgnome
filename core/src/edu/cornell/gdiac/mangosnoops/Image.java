package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Image {

    /** Texture center point relative to screen size
     *  i.e. (0.5f, 0.5f) would place asset in the middle of the screen */
    protected Vector2 position;
    /** The object's texture asset **/
    protected Texture texture;
    /** Relative width and height divided by texture -- i.e. 0.5f will make the
     * height of the texture half of the canvas screen height (width would scale the same amount)*/
    protected float relativeScale;
    /* Scale set at beginning of Image instantiation. Used so things may scale dynamically.*/
    protected float ORIGINAL_SCALE;
    /** An optional buffer given to the object in order to 'pad' its area of effectiveness**/
    protected float controlBuffer;
    /** Dimensions of the screen **/
    protected static Vector2 SCREEN_DIMENSIONS;
    protected static GameCanvas c;

    /** The maximimum amount of offset that is applied to
     *  the drawing coordinates, for the "shake" effect */
    protected float MAX_SHAKE_AMOUNT = 5;

    /** The current offset that is applied to the dash drawing
     *  coordinates, for the "shake" effect */
    protected static float currentShakeAmount = 0;

    /** The current shake magnitude */
    private static float currentShakeMagnitude = 0;

    /** Whether or not the object is shaking from a collision */
    protected static boolean isShaking = false;

    /** How quickly the shake ends, in range (0, 1)
     *  smaller value => depletes more quickly */
    protected final float SHAKE_DEPLETION = 0.95f;

    /** The sum of the deltas passed to every update call once a
     *  shake begins */
    protected static float shakeDeltaSum = 0;

    GameCanvas.TextureOrigin origin;
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

    public Image() {
        //used for master shaker lol
    }


    public Image(float x, float y, float relSca, Texture tex) {
        position = new Vector2(x,y);
        if(tex == null){
            ORIGINAL_SCALE = 0;
            relativeScale = 0;
            texture = null;
        }else{
            ORIGINAL_SCALE = relSca;
            relativeScale = relSca;
            texture = tex;
        }
        controlBuffer = 0;
        origin = GameCanvas.TextureOrigin.BOTTOM_LEFT;
    }

    public Image(float x, float y, float relSca, float cb, Texture tex) {
        position = new Vector2(x,y);
        if(tex == null){
            ORIGINAL_SCALE = 0;
            relativeScale = 0;
            texture = null;
        }else{
            ORIGINAL_SCALE = relSca;
            relativeScale = relSca;
            texture = tex;
        }
        controlBuffer = cb;
        origin = GameCanvas.TextureOrigin.BOTTOM_LEFT;
    }

    public Image(float x, float y, float relSca, Texture tex, GameCanvas.TextureOrigin o) {
        this(x, y, relSca, tex);
        origin = o;
    }

    public Image(float x, float y, float relSca, float cb, Texture tex, GameCanvas.TextureOrigin o) {
        this(x, y, relSca, cb, tex);
        origin = o;
    }

    public Image(Image i){
        this.position = new Vector2(i.position);
        this.relativeScale = i.relativeScale;
        this.controlBuffer = i.controlBuffer;
        this.texture = i.texture;
        this.ORIGINAL_SCALE = i.ORIGINAL_SCALE;
        this.origin = i.origin;
    }

    public Texture getTexture() {return texture; }

    public static float getScreenWidth() {
        return c.getWidth();
    }

    public static float getScreenHeight() {
        return c.getHeight();
    }

    public Vector2 getPosition() {
        return position;
    }

    public static void updateScreenDimensions(GameCanvas canvas){
        c = canvas;
        SCREEN_DIMENSIONS = new Vector2(canvas.getWidth(), canvas.getHeight());
    }

    public void updateX(float x) {
        position.x = x;
    }

    public void updateY(float y) {
        position.y = y;
    }
    /**
     * Returns true if the mouse is positioned inside the area of the object
     *
     *  @param p the vector giving the mouse's (x,y) screen coordinates
     */
    public boolean inArea(Vector2 p) {
        return c.inArea(p, texture, origin, position.x, position.y,
                relativeScale, false, controlBuffer);
    }

    /**
     * Same as inArea, however it detects only if the object is in the area
     * determined by its ORIGINAL_SCALE. Useful if inArea changes the relative
     * scale of the original texture.
     * @param p
     * @return
     */
    public boolean inAreaWOriginScale(Vector2 p) {
        return c.inArea(p, texture, origin, position.x, position.y,
                ORIGINAL_SCALE, false, controlBuffer);
    }

    public void drawNoShake(GameCanvas canvas) {
        canvas.draw(texture, origin, position.x, position.y,
                relativeScale, false, 0, Color.WHITE);
    }

    public void draw(GameCanvas canvas) {
        canvas.drawShake(texture, origin, position.x, position.y,
                relativeScale, false, 0, Color.WHITE, currentShakeAmount);
    }

    public void draw(GameCanvas canvas, Color tint) {
        canvas.drawShake(texture, origin, position.x, position.y,
                relativeScale, false, 0, tint, currentShakeAmount);
    }

    public void draw(GameCanvas canvas, float ang) {
        canvas.drawShake(texture, origin, position.x, position.y,
                relativeScale, false, ang, Color.WHITE, currentShakeAmount);
    }
}
