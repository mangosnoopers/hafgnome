package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.mangosnoops.hudentity.Inventory;
import edu.cornell.gdiac.util.ScreenListener;

import java.util.Random;

public class RestStopMode implements Screen, InputProcessor {
    /** Assets TODO */
    private static final String BACKGROUND_FILE = "images/restStopAssets/background.png";
    private static final String SHELF_FILE = "images/restStopAssets/shelf.png";
    private static final String DVD_FILE = "images/Items/dvd.png";
    private static final String SNACK_FILE = "images/Items/snack.png";
//    private static final String READY_BUTTON_FILE = "";

    private Texture background;
    private Texture shelf;
    private Texture dvd;
    private Texture snack;
//    private Texture readyButton;

    /** 0: Unclicked, 1: Button Down, 2: Button Up */
    private static final int UNCLICKED = 0;
    private static final int BUTTON_DOWN = 1;
    private static final int BUTTON_UP = 2;

    /** Ready button is clicked when the player is ready to return to the road */
    private int readyStatus;
    // TODO: item statuses (array)

    /** Inventory - on a shelf */
    private Inventory inv;
    /** Number of items per shelf */
    private static final int ITEMS_PER_SHELF = 10;
    /** Number of shelves */
    private static final int NUM_SHELVES = 3;

    /** AssetManager to be loading in the background */
    private AssetManager manager;
    /** Reference to GameCanvas created by the root */
    private GameCanvas canvas;
    /** Listener that will update the player mode when we are done */
    private ScreenListener listener;
    /** Track all loaded assets (for unloading purposes) */
    private Array<String> assets;
    /** Whether or not this player mode is still active */
    private boolean active;

    /** Dimensions of the screen **/
    private static Vector2 SCREEN_DIMENSIONS;
    /** Factor to scale the shelf by */
    private static final float SHELF_SCALING = 0.9f;
    /** Factor to move the shelf downward */
    private static final float SHELF_Y_OFFSET = 30.0f;
    /** Standard window size (for scaling) */
    private static int STANDARD_WIDTH  = 1600;
    /** Standard window height (for scaling) */
    private static int STANDARD_HEIGHT = 900;
    /** Ration of the bar height to the screen */
    private static float BAR_HEIGHT_RATIO = 0.25f;
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
        manager.load(BACKGROUND_FILE,Texture.class);
        assets.add(BACKGROUND_FILE);
        manager.load(SHELF_FILE,Texture.class);
        assets.add(SHELF_FILE);
//        manager.load(READY_BUTTON_FILE,Texture.class);
//        assets.add(READY_BUTTON_FILE);
        manager.load(DVD_FILE,Texture.class);
        assets.add(DVD_FILE);
        manager.load(SNACK_FILE,Texture.class);
        assets.add(SNACK_FILE);
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
        if (manager.isLoaded(BACKGROUND_FILE)) {
            background = manager.get(BACKGROUND_FILE, Texture.class);
            background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        if (manager.isLoaded(SHELF_FILE)) {
            shelf = manager.get(SHELF_FILE, Texture.class);
            shelf.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

//        if (manager.isLoaded(READY_BUTTON_FILE)) {
//            readyButton = manager.get(READY_BUTTON_FILE, Texture.class);
//            readyButton.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        }

        if (manager.isLoaded(DVD_FILE)) {
            dvd = manager.get(DVD_FILE, Texture.class);
            dvd.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        if (manager.isLoaded(SNACK_FILE)) {
            snack = manager.get(SNACK_FILE, Texture.class);
            snack.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
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

    /**
     * Generates items that can be obtained at this rest stop.
     * 3-5 snacks, 1-3 books, 0-1 movies
     *
     * @return a new Inventory with the generated items
     */
    private void generateItems() {
        // Randomly generate item quantities
        Random rand = new Random();
        int numSnacks = rand.nextInt(2) + 3;
        int numBooks = rand.nextInt(2) + 1;
        int numMovies = rand.nextInt(1);

        Array<Inventory.Slot> i = new Array<Inventory.Slot>();
        i.add(new Inventory.Slot(i,inv, Inventory.Item.ItemType.DVD,1));
        i.add(new Inventory.Slot(i,inv, Inventory.Item.ItemType.SNACK,1));
        // TODO: add books

        inv.load(i);
    }

    /**
     * Creates an inventory of items for this rest stop.
     * @return the inventory
     */
    private Inventory createInventory() {
        float shelf_ox = 0.5f;
        float shelf_oy = 0.5f;
        float shelf_relsca = 0.3f;
        float shelf_cb = 0.0f;
        float slot_width = 0.33f;
        float slot_height = 0.33f;
        return new Inventory(shelf_ox, shelf_oy, shelf_relsca, shelf_cb, shelf,
                             slot_width, slot_height, ITEMS_PER_SHELF * NUM_SHELVES);
    }

    public RestStopMode(GameCanvas canvas, AssetManager manager) {
        this.canvas = canvas;
        this.manager = manager;
        assets = new Array<String>();
        SCREEN_DIMENSIONS = new Vector2(canvas.getWidth(),canvas.getHeight());
        active = true;

        // Assets
        background = new Texture(BACKGROUND_FILE);
        shelf = new Texture(SHELF_FILE);
        dvd = new Texture(DVD_FILE);
        snack = new Texture(SNACK_FILE);

        // Create inventory and add items to it
        Image.updateScreenDimensions(canvas);
        Inventory.Item.setTexturesAndScales(dvd,1.0f,snack,1.0f);
        inv = createInventory();
        generateItems();
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
        canvas.draw(background, 0, 0);

        // draw the shelf and items
        canvas.draw(shelf,(SCREEN_DIMENSIONS.x - shelf.getWidth()*SHELF_SCALING)/2.0f,
                (SCREEN_DIMENSIONS.y - shelf.getHeight()*SHELF_SCALING)/2.0f - SHELF_Y_OFFSET,
                shelf.getWidth()*SHELF_SCALING, shelf.getHeight()*SHELF_SCALING);
        inv.draw(canvas);

        canvas.endHUDDrawing();
    }


    // SCREEN METHODS

    /** Called when this screen becomes the current screen for a {@link Game}. */
    public void show () {
        active = true;
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta) {
        if (active) {
            update(delta);
            draw();

            if (readyStatus == BUTTON_UP && listener != null) {
                listener.exitScreen(this,0);
            }
        }
    }

    /** Called when this screen should release all resources. */
    public void dispose () {
        background.dispose();
        background = null;
        // TODO add others
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
        // Compute the drawing scale
        float sx = ((float)width)/STANDARD_WIDTH;
        float sy = ((float)height)/STANDARD_HEIGHT;
        scale = (sx < sy ? sx : sy);

        centerY = (int)(BAR_HEIGHT_RATIO*height);
        centerX = width/2;
        heightY = height;
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
        if (readyStatus == BUTTON_UP) {
            return true;
        }

        // Check if ready button was pressed
        // Flip to match graphics coordinates
        screenY = heightY-screenY;

        int OFFSET_X = 120; // TODO make global
        int OFFSET_Y = 120;
        float radius = 0.0f; // TODO: float radius = readyButton.getWidth()/4.0f;
        float distX = (screenX-(centerX-OFFSET_X))*(screenX-(centerX-OFFSET_X));
        float distY = (screenY-(centerY+OFFSET_Y))*(screenY-(centerY+OFFSET_Y));
        if (distX < radius*radius && distY < radius*radius) {
            readyStatus = BUTTON_DOWN;
        }

        // TODO: also check for items on shelf

        return false;
    }

    /** Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (readyStatus == BUTTON_DOWN) {
            readyStatus = BUTTON_UP;
            return false;
        }
        return true;
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

    /** Called when the mouse wheel was scrolled. Will not be called on iOS. -- UNSUPPORTED
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed. */
    public boolean scrolled (int amount) {
        return true;
    }

}
