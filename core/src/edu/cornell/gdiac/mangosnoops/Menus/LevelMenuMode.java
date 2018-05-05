package edu.cornell.gdiac.mangosnoops.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;
import edu.cornell.gdiac.mangosnoops.LevelObject;
import edu.cornell.gdiac.mangosnoops.RestStopMode;
import edu.cornell.gdiac.util.ScreenListener;

public class LevelMenuMode implements Screen, InputProcessor {
    // ASSETS
    private static final String GO_BUTTON_FILE = "images/levelSelectAssets/goButton.png";
    private static final String FINAL_LEVEL_MARKER_FILE = "images/levelSelectAssets/finalLevelMarker.png";
    private static final String LEVEL_MARKER_FILE = "images/levelSelectAssets/levelMarker.png";
    private static final String SELECTED_LEVEL_MARKER_FILE = "images/levelSelectAssets/selectedLevelMarker.png";
    private static final String MAP_FILE = "images/levelSelectAssets/usMap.png";
    private static final String PATH_FILE = "images/levelSelectAssets/levelsPath.png";
    private Texture goButtonTex;
    private Texture finalLevelMarkerTex;
    private Texture levelMarkerTex;
    private Texture selectedLevelMarkerTex;
    private Texture mapTex;
    private Texture pathTex;
    private Array<Image> images;

    // BUTTONS
    /** 0: Unclicked, 1: Button Down, 2: Button Up */
    private static final int UNCLICKED = 0;
    private static final int BUTTON_DOWN = 1;
    private static final int BUTTON_UP = 2;
    /** Go button is clicked when the player wants to load a level */
    private int goStatus;
//    private Image goButton;

    // DOODADS FOR EXITING THIS MODE
    /** True if the next screen to load should be playing mode, false otherwise */
    private boolean loadPlaying;
    /** The level to load */
    private int nextLevelNum;

    // OTHER DOODADS
    /** AssetManager to be loading in the background */
    private AssetManager manager;
    /** Reference to GameCanvas created by the root */
    private GameCanvas canvas;
    /** An array of all the level files for the game */
    private String[] levels;
    /** Listener that will update the player mode when we are done */
    private ScreenListener listener;
    /** Whether or not this player mode is still active */
    private boolean active;

    // DRAWING
    /** Dimensions of the screen **/
    private static Vector2 SCREEN_DIMENSIONS;
    /** Size of the level markers */
    private static final float LEVEL_MARKER_SIZE = 0.03f;

    /**
     * Returns true if the next screen to load should be playing mode, and false
     * if it should be the start screen.
     */
    public boolean getLoadPlaying() { return loadPlaying; }

    /**
     * Returns the index of the next level to load, or -1 if returning to the
     * start screen.
     */
    public int getNextLevelNum() {
        if (loadPlaying)
            return nextLevelNum;
        return -1;
    }

    public LevelMenuMode(GameCanvas canvas, AssetManager manager, String[] levels) {
        this.canvas = canvas;
        this.manager = manager;
        this.levels = levels;
        SCREEN_DIMENSIONS = new Vector2(canvas.getWidth(),canvas.getHeight());
        active = true;

        // Create textures
        initTextures();

        // Create images
        images = new Array<Image>();
        initImages();

    }

    /** Initialize all textures */
    private void initTextures() {
        goButtonTex = new Texture(GO_BUTTON_FILE);
        finalLevelMarkerTex = new Texture(FINAL_LEVEL_MARKER_FILE);
        levelMarkerTex = new Texture(LEVEL_MARKER_FILE);
        selectedLevelMarkerTex = new Texture(SELECTED_LEVEL_MARKER_FILE);
        mapTex = new Texture(MAP_FILE);
        pathTex = new Texture(PATH_FILE);
    }

    /** Initialize all images */
    private void initImages() {
        // background
        Image bg = new Image(0.0f,0.0f,1.0f, mapTex, GameCanvas.TextureOrigin.BOTTOM_LEFT);
        images.add(bg);

        // path
        Image path = new Image(0.25f,0.75f,0.215f, pathTex, GameCanvas.TextureOrigin.TOP_LEFT);
        images.add(path);

        // place the final level marker at the end of the path
        Image finalLevelMarker = new Image(0.25f,0.75f,LEVEL_MARKER_SIZE, finalLevelMarkerTex, GameCanvas.TextureOrigin.MIDDLE);
        images.add(finalLevelMarker);
    }

    /**
     * Sets the ScreenListener for this mode
     *
     * The ScreenListener will respond to requests to quit.
     */
    public void setScreenListener(ScreenListener listener) {
        this.listener = listener;
    }

    /**
     * Update the status of this player mode.
     *
     * We prefer to separate update and draw from one another as separate methods, instead
     * of using the single render() method that LibGDX does.  We will talk about why we
     * prefer this in lecture.
     *
     * @param delta Number of seconds since last animation frame
     */
    private void update(float delta) {
    }

    /**
     * Draw the status of this player mode.
     *
     * We prefer to separate update and draw from one another as separate methods, instead
     * of using the single render() method that LibGDX does.  We will talk about why we
     * prefer this in lecture.
     */
    private void draw() {
        canvas.beginHUDDrawing();

        // Draw images in the images array
        for (Image i : images) {
            i.draw(canvas);
        }

        canvas.endHUDDrawing();
    }

    // SCREEN METHODS

    /** Called when this screen becomes the current screen for a game. */
    public void show () {
        active = true;
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta) {
        if (active) {
            update(delta);
            draw();

            if (goStatus == BUTTON_UP && listener != null) {
                listener.exitScreen(this,0);
            }
        }
    }

    /** Called when this screen should release all resources. */
    public void dispose () {
    }

    /**
     * Called when the Screen is resized.
     *
     * This can happen at any point during a non-paused state but will never happen
     * before a call to show().
     *
     * @param width  The new width in pixels
     * @param height The new height in pixels
     */
    public void resize (int width, int height) {
        SCREEN_DIMENSIONS = new Vector2(width,height);
    }

    /**
     * Called when the Screen is paused.
     *
     * This is usually when it's not active or visible on screen. An Application is
     * also paused before it is destroyed.
     */
    public void pause () {
        // TODO Auto-generated method stub
    }

    /**
     * Called when the Screen is resumed from a paused state.
     *
     * This is usually when it regains focus.
     */
    public void resume () {
        // TODO Auto-generated method stub
    }

    /** Called when this screen is no longer the current screen for a game. */
    public void hide () {
        active = false;
    }

    // PROCESSING PLAYER INPUT

    /** Called when the screen was touched or a mouse button was pressed.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed (whether to hand the event to other listeners)
     */
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /** Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        return false;
    }

    // UNSUPPORTED

    /** Called when a finger or the mouse was dragged -- UNSUPPORTED
     * @param pointer the pointer for the event.
     * @return whether the input was processed */
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        return true;
    }

    /** Called when a key was pressed -- UNSUPPORTED
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    public boolean keyDown (int keycode) {
        if(keycode == Input.Keys.F) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else if(keycode == Input.Keys.ESCAPE) {
            Gdx.graphics.setWindowedMode(1600,900);
        }
        return true;
    }

    /** Called when a key was released -- UNSUPPORTED
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    public boolean keyUp (int keycode) {
        return true;
    }

    /** Called when a key was typed -- UNSUPPORTED
     *
     * @param character The character
     * @return whether the input was processed */
    public boolean keyTyped (char character) {
        return true;
    }

    /** Called when the mouse was moved without any buttons being pressed. -- UNSUPPORTED
     * Will not be called on iOS.
     * @return whether the input was processed */
    public boolean mouseMoved (int screenX, int screenY) {
        return true;
    }

    /** Called when the mouse wheel was scrolled. Will not  be called on iOS. -- UNSUPPORTED
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed. */
    public boolean scrolled (int amount) {
        return true;
    }
}
