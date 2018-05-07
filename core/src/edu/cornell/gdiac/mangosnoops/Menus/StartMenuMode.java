/*
* MenuController.java
*
* This class creates all the menu screens. They are stored as
* separate variables that can be called by GameMode.
*
* */

package edu.cornell.gdiac.mangosnoops.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.SoundController;
import edu.cornell.gdiac.util.ScreenListener;

public class StartMenuMode implements Screen, InputProcessor {
    private static final String BACKGROUND_FILE = "images/startMenuAssets/background.png";
    private static final String EXITBUTTON_FILE = "images/startMenuAssets/justexitButton.png";
    private static final String LEVELSBUTTON_FILE = "images/startMenuAssets/justlevelsButton.png";
    private static final String SETTINGSBUTTON_FILE = "images/startMenuAssets/justSettingsButton.png";
    private static final String STARTBUTTON_FILE = "images/startMenuAssets/juststartButton.png";
    private static final String LOGO_FILE = "images/startMenuAssets/logo.png";

    private Texture background;
    private Texture exitbuttonTexture;
    private Texture levelsbuttonTexture;
    private Texture settingbuttonTexture;
    private Texture startbuttonTexture;
    private Texture logo;

    Music menuSong;
    private SoundController soundController;

    /** 0: Unclicked, 1: Button Down, 2: Button Up */
    private int   exitButton;
    private int   levelsButton;
    private int   settingButton;
    private int   startButton;

    /** AssetManager to be loading in the background */
    private AssetManager manager;
    /** Reference to GameCanvas created by the root */
    private GameCanvas canvas;
    /** Listener that will update the player mode when we are done */
    private ScreenListener listener;
    /**Settings Menu**/
    private SettingsMenu settings;
    /** Track all loaded assets (for unloading purposes) */
    private Array<String> assets;
    /** Whether or not this player mode is still active */
    private boolean active;

    /** Default budget for asset loader (do nothing but load 60 fps) */
    private static int DEFAULT_BUDGET = 15;
    /** Standard window size (for scaling) */
    private static int STANDARD_WIDTH  = 1600;
    /** Standard window height (for scaling) */
    private static int STANDARD_HEIGHT = 900;
    /** Ratio of the bar width to the screen */
    private static float BAR_WIDTH_RATIO  = 0.66f;
    /** Ration of the bar height to the screen */
    private static float BAR_HEIGHT_RATIO = 0.25f;
    /** Height of the progress bar */
    private static int PROGRESS_HEIGHT = 30;
    /** Width of the rounded cap on left or right */
    private static int PROGRESS_CAP    = 15;
    /** Width of the middle portion in texture atlas */
    private static int PROGRESS_MIDDLE = 200;
    private static float BUTTON_SCALE  = 0.75f;

    private int offsetX= 120;
    private int offsetY = 120;

    /** The y-coordinate of the center of the progress bar */
    private int centerY;
    /** The x-coordinate of the center of the progress bar */
    private int centerX;
    /** The height of the canvas window (necessary since sprite origin != screen origin) */
    private int heightY;
    /** Scaling factor for when the student changes the resolution. */
    private float scale;

    /**
     * Preloads the assets for this game.
     *
     * The asset manager for LibGDX is asynchronous.  That means that you
     * tell it what to load and then wait while it loads them.  This is
     * the first step: telling it what to load.
     *
     * @param manager Reference to global asset manager.
     */
    public void preLoadContent(AssetManager manager) {
//        manager.load(BACKGROUND_FILE,Texture.class);
//        assets.add(BACKGROUND_FILE);
//        manager.load(EXITBUTTON_FILE,Texture.class);
//        assets.add(EXITBUTTON_FILE);
//        manager.load(LEVELSBUTTON_FILE,Texture.class);
//        assets.add(LEVELSBUTTON_FILE);
//        manager.load(SETTINGSBUTTON_FILE,Texture.class);
//        assets.add(SETTINGSBUTTON_FILE);
//        manager.load(STARTBUTTON_FILE,Texture.class);
//        assets.add(STARTBUTTON_FILE);
//        manager.load(LOGO_FILE,Texture.class);
//        assets.add(LOGO_FILE);
    }

    public boolean startButtonClicked() {
        return startButton == 2;
    }
    public boolean settingsButtonClicked() {
        return settingButton == 2;
    }
    public boolean exitButtonClicked() {
        return exitButton == 2;
    }
    public boolean levelSelectButtonClicked() {
        return levelsButton == 2;
    }

    /**
     * Loads the assets for this game.
     *
     * The asset manager for LibGDX is asynchronous.  That means that you
     * tell it what to load and then wait while it loads them.  This is
     * the second step: extracting assets from the manager after it has
     * finished loading them.
     *
     * @param manager Reference to global asset manager.
     */
    public void loadContent(AssetManager manager) {
//        if (manager.isLoaded(BACKGROUND_FILE)) {
//            background = manager.get(BACKGROUND_FILE, Texture.class);
//            background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        }
//        if (manager.isLoaded(EXITBUTTON_FILE)) {
//            exitbutton = manager.get(EXITBUTTON_FILE, Texture.class);
//        }
//        if (manager.isLoaded(LEVELSBUTTON_FILE)) {
//            levelsbutton = manager.get(LEVELSBUTTON_FILE, Texture.class);
//        }
//        if (manager.isLoaded(SETTINGSBUTTON_FILE) {
//            settingbutton = manager.get(SETTINGSBUTTON_FILE, Texture.class);
//        }
//        if (manager.isLoaded(STARTBUTTON_FILE)) {
//            startbutton = manager.get(STARTBUTTON_FILE, Texture.class);
//        }
//        if (manager.isLoaded(LOGO_FILE)) {
//            logo = manager.get(LOGO_FILE, Texture.class);
//        }
    }

    /**
     * Unloads the assets for this game.
     *
     * This method erases the static variables.  It also deletes the associated textures
     * from the asset manager.
     *
     * @param manager Reference to global asset manager.
     */
    public void unloadContent(AssetManager manager) {
        for(String s : assets) {
            if (manager.isLoaded(s)) {
                manager.unload(s);
            }
        }
    }

    public StartMenuMode(GameCanvas canvas, AssetManager manager, SettingsMenu settings, SoundController soundController) {
        //construct each of the screens lol
        this.canvas = canvas;
        this.manager = manager;
        this.settings = settings;
        this.soundController = soundController;

        background = new Texture(BACKGROUND_FILE);
        exitbuttonTexture = new Texture(EXITBUTTON_FILE);
        levelsbuttonTexture = new Texture(LEVELSBUTTON_FILE);
        settingbuttonTexture = new Texture(SETTINGSBUTTON_FILE);
        startbuttonTexture = new Texture(STARTBUTTON_FILE);
        logo = new Texture(LOGO_FILE);

        soundController.playMenuSong(true);

        active = true;
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
        if(settings.isShowing()) {
            settings.update(new Vector2(Gdx.input.getX(), Gdx.input.getY()), soundController);
        }
        soundController.play(null);
    }

    /**
     * Draw the status of this player mode.
     *
     * We prefer to separate update and draw from one another as separate methods, instead
     * of using the single render() method that LibGDX does.  We will talk about why we
     * prefer this in lecture.
     */
    private void draw() {
        canvas.clearScreen();
        canvas.beginHUDDrawing();
        canvas.draw(background, 0, 0);
        offsetX = (int)(BUTTON_SCALE*scale*startbuttonTexture.getHeight()/1.75f);
        offsetY = offsetX;
        canvas.draw(logo, Color.WHITE, logo.getWidth()/2, logo.getHeight()/2,
                centerX, centerY*3, 0, BUTTON_SCALE*scale, BUTTON_SCALE*scale);
        canvas.draw(startbuttonTexture, Color.WHITE, startbuttonTexture.getWidth()/2, startbuttonTexture.getHeight()/2,
                centerX-offsetX, centerY+ offsetY, 0, BUTTON_SCALE*scale, BUTTON_SCALE*scale);
        canvas.draw(levelsbuttonTexture, Color.WHITE, startbuttonTexture.getWidth()/2, startbuttonTexture.getHeight()/2,
                centerX+offsetX, centerY+ offsetY, 0, BUTTON_SCALE*scale, BUTTON_SCALE*scale);
        canvas.draw(settingbuttonTexture, Color.WHITE, startbuttonTexture.getWidth()/2, startbuttonTexture.getHeight()/2,
                centerX-offsetX, centerY- offsetY, 0, BUTTON_SCALE*scale, BUTTON_SCALE*scale);
        canvas.draw(exitbuttonTexture, Color.WHITE, startbuttonTexture.getWidth()/2, startbuttonTexture.getHeight()/2,
                centerX+offsetX, centerY- offsetY, 0, BUTTON_SCALE*scale, BUTTON_SCALE*scale);
        if(settings.isShowing()){
            settings.draw(canvas);
        }
        canvas.endHUDDrawing();
    }

    /**
     * Called when this screen should release all resources.
     */
    public void dispose() {
        background.dispose();
        exitbuttonTexture.dispose();
        levelsbuttonTexture.dispose();
        settingbuttonTexture.dispose();
        startbuttonTexture.dispose();
        logo.dispose();
        soundController.playMenuSong(false);
        background = null;
        exitbuttonTexture  = null;
        levelsbuttonTexture = null;
        settingbuttonTexture = null;
        startbuttonTexture = null;
        logo = null;
    }

    // ADDITIONAL SCREEN METHODS
    /**
     * Called when the Screen should render itself.
     *
     * We defer to the other methods update() and draw().  However, it is VERY important
     * that we only quit AFTER a draw.
     *
     * @param delta Number of seconds since last animation frame
     */
    public void render(float delta) {
        if(active) {
            update(delta);
            draw();
            // We are are ready, notify our listener
            if ((exitButton == 2 || levelsButton == 2 || settingButton == 2 || startButton == 2) && listener != null) {
                listener.exitScreen(this, 0);
            }
        }
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
    public void resize(int width, int height) {
        // Compute the drawing scale
        float sx = ((float)width)/STANDARD_WIDTH;
        float sy = ((float)height)/STANDARD_HEIGHT;
        scale = (sx < sy ? sx : sy);

        centerY = (int)(BAR_HEIGHT_RATIO*height);
        centerX = width/2;
        heightY = height;
    }

     /* Called when the Screen is paused.
     /**
     *
     * This is usually when it's not active or visible on screen. An Application is
     * also paused before it is destroyed.
     */
    public void pause() {
        // TODO Auto-generated method stub

    }

    /**
     * Called when the Screen is resumed from a paused state.
     *
     * This is usually when it regains focus.
     */
    public void resume() {
        // TODO Auto-generated method stub

    }

    /**
     * Called when this screen becomes the current screen for a Game.
     */
    public void show() {
        // Useless if called in outside animation loop
        active = true;
    }

    /**
     * Called when this screen is no longer the current screen for a Game.
     */
    public void hide() {
        // Useless if called in outside animation loop
        active = false;
    }

    /**
     * Sets the ScreenListener for this mode
     *
     * The ScreenListener will respond to requests to quit.
     */
    public void setScreenListener(ScreenListener listener) {
        this.listener = listener;
    }

    // PROCESSING PLAYER INPUT
    /**
     * Called when the screen was touched or a mouse button was pressed.
     *
     * This method checks to see if the play button is available and if the click
     * is in the bounds of the play button.  If so, it signals the that the button
     * has been pressed and is currently down. Any mouse button is accepted.
     *
     * @param screenX the x-coordinate of the mouse on the screen
     * @param screenY the y-coordinate of the mouse on the screen
     * @param pointer the button or touch finger number
     * @return whether to hand the event to other listeners.
     */
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //if a button is already selected do nothing
        if (startButton == 2 || exitButton == 2 || levelsButton == 2 || settingButton == 2) {
            return true;
        }

        if(!settings.isShowing()) {
            // Flip to match graphics coordinates
            screenY = heightY - screenY;
            float radius = startbuttonTexture.getWidth() * BUTTON_SCALE * scale * 0.5f;
            float distX = Math.abs(screenX - (centerX - offsetX));
            float distY = Math.abs(screenY - (centerY + offsetY));
            if (distX < radius && distY < radius) {
                startButton = 1;
                return false;
            }
            distX = Math.abs(screenX - (centerX + offsetX));
            if (distX < radius && distY < radius) {
                levelsButton = 1;
                return false;
            }
            distY = Math.abs(screenY - (centerY - offsetY));
            if (distX < radius && distY < radius) {
                exitButton = 1;
                return false;
            }
            distX = Math.abs(screenX - (centerX - offsetX));
            if (distX < radius && distY < radius) {
                settingButton = 1;
                return false;
            }
        }

        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released.
     *
     * This method checks to see if the play button is currently pressed down. If so,
     * it signals the that the player is ready to go.
     *
     * @param screenX the x-coordinate of the mouse on the screen
     * @param screenY the y-coordinate of the mouse on the screen
     * @param pointer the button or touch finger number
     * @return whether to hand the event to other listeners.
     */
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!settings.isShowing()) {
            if (startButton == 1) {
                startButton = 2;
                return false;
            } else if (exitButton == 1) {
                exitButton = 2;
                return false;
            } else if (levelsButton == 1) {
                levelsButton = 2;
                return false;
            } else if (settingButton == 1) {
                settingButton = 2;
                settings.setShowing(true);
                settingButton = 0;
                return false;
            }
        }
        return true;
    }

    /**
     * Called when a button on the Controller was pressed.
     *
     * The buttonCode is controller specific. This listener only supports the start
     * button on an X-Box controller.  This outcome of this method is identical to
     * pressing (but not releasing) the play button.
     *
     * @param controller The game controller
     * @param buttonCode The button pressed
     * @return whether to hand the event to other listeners.
     */
    public boolean buttonDown (Controller controller, int buttonCode) {
        return true;
    }

    /**
     * Called when a button on the Controller was released.
     *
     * The buttonCode is controller specific. This listener only supports the start
     * button on an X-Box controller.  This outcome of this method is identical to
     * releasing the the play button after pressing it.
     *
     * @param controller The game controller
     * @param buttonCode The button pressed
     * @return whether to hand the event to other listeners.
     */
    public boolean buttonUp (Controller controller, int buttonCode) {
        return true;
    }

    // UNSUPPORTED METHODS FROM InputProcessor

    /**
     * Called when a key is pressed (UNSUPPORTED)
     *
     * @param keycode the key pressed
     * @return whether to hand the event to other listeners.
     */
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.F) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else if(keycode == Input.Keys.ESCAPE) {
            Gdx.graphics.setWindowedMode(1600,900);
        }
        return true;
    }

    /**
     * Called when a key is typed (UNSUPPORTED)
     *
     * @param character the key typed
     * @return whether to hand the event to other listeners.
     */
    public boolean keyTyped(char character) {
        return true;
    }

    /**
     * Called when a key is released (UNSUPPORTED)
     *
     * @param keycode the key released
     * @return whether to hand the event to other listeners.
     */
    public boolean keyUp(int keycode) {
        return true;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. (UNSUPPORTED)
     *
     * @param screenX the x-coordinate of the mouse on the screen
     * @param screenY the y-coordinate of the mouse on the screen
     * @return whether to hand the event to other listeners.
     */
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    /**
     * Called when the mouse wheel was scrolled. (UNSUPPORTED)
     *
     * @param amount the amount of scroll from the wheel
     * @return whether to hand the event to other listeners.
     */
    public boolean scrolled(int amount) {
        return true;
    }

    /**
     * Called when the mouse or finger was dragged. (UNSUPPORTED)
     *
     * @param screenX the x-coordinate of the mouse on the screen
     * @param screenY the y-coordinate of the mouse on the screen
     * @param pointer the button or touch finger number
     * @return whether to hand the event to other listeners.
     */
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }

    // UNSUPPORTED METHODS FROM ControllerListener

    /**
     * Called when a controller is connected. (UNSUPPORTED)
     *
     * @param controller The game controller
     */
    public void connected (Controller controller) {}

    /**
     * Called when a controller is disconnected. (UNSUPPORTED)
     *
     * @param controller The game controller
     */
    public void disconnected (Controller controller) {}

    /**
     * Called when an axis on the Controller moved. (UNSUPPORTED)
     *
     * The axisCode is controller specific. The axis value is in the range [-1, 1].
     *
     * @param controller The game controller
     * @param axisCode 	The axis moved
     * @param value 	The axis value, -1 to 1
     * @return whether to hand the event to other listeners.
     */
    public boolean axisMoved (Controller controller, int axisCode, float value) {
        return true;
    }

    /**
     * Called when a POV on the Controller moved. (UNSUPPORTED)
     *
     * The povCode is controller specific. The value is a cardinal direction.
     *
     * @param controller The game controller
     * @param povCode 	The POV controller moved
     * @param value 	The direction of the POV
     * @return whether to hand the event to other listeners.
     */
    public boolean povMoved (Controller controller, int povCode, PovDirection value) {
        return true;
    }

    /**
     * Called when an x-slider on the Controller moved. (UNSUPPORTED)
     *
     * The x-slider is controller specific.
     *
     * @param controller The game controller
     * @param sliderCode The slider controller moved
     * @param value 	 The direction of the slider
     * @return whether to hand the event to other listeners.
     */
    public boolean xSliderMoved (Controller controller, int sliderCode, boolean value) {
        return true;
    }

    /**
     * Called when a y-slider on the Controller moved. (UNSUPPORTED)
     *
     * The y-slider is controller specific.
     *
     * @param controller The game controller
     * @param sliderCode The slider controller moved
     * @param value 	 The direction of the slider
     * @return whether to hand the event to other listeners.
     */
    public boolean ySliderMoved (Controller controller, int sliderCode, boolean value) {
        return true;
    }

    /**
     * Called when an accelerometer value on the Controller changed. (UNSUPPORTED)
     *
     * The accelerometerCode is controller specific. The value is a Vector3 representing
     * the acceleration on a 3-axis accelerometer in m/s^2.
     *
     * @param controller The game controller
     * @param accelerometerCode The accelerometer adjusted
     * @param value A vector with the 3-axis acceleration
     * @return whether to hand the event to other listeners.
     */
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return true;
    }


}