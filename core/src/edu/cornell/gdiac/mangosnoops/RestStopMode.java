package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.util.ScreenListener;
import edu.cornell.gdiac.mangosnoops.hudentity.Inventory;

import java.util.Random;

public class RestStopMode implements Screen, InputProcessor {
    // ASSETS
    private static final String BACKGROUND_FILE = "images/restStopAssets/background.png";
    private static final String SHELF_FILE = "images/restStopAssets/shelf.png";
    private static final String DVD_FILE = "images/Items/dvd.png";
    private static final String SNACK_FILE = "images/Items/snack.png";
    private static final String READY_BUTTON_FILE = "images/restStopAssets/readybutton.png";
    private Texture backgroundTex;
    private Texture shelfTex;
    private Texture dvdTex;
    private Texture snackTex;
    private Texture readyButtonTex;

    // BUTTONS
    /** 0: Unclicked, 1: Button Down, 2: Button Up */
    private static final int UNCLICKED = 0;
    private static final int BUTTON_DOWN = 1;
    private static final int BUTTON_UP = 2;
    /** Ready button is clicked when the player is ready to return to the road */
    private int readyStatus;

    // INVENTORY
    /** Inventory - on a shelf */
    private Inventory inv;
    /** Number of shelves */
    private static final int NUM_SHELVES = 3;
    /** Number of items per shelf */
    private static final int ITEMS_PER_SHELF = 5;

    // ITEMS
    private Array<RestStopItem> items;
    private Image shelf;
    private int numMovies;
    private int numSnacks;
    private int numBooks;
    private Inventory playerInv;

    // OTHER DOODADS
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
    /** Opacity used to fade in and out */
    private float fadeOpacity;
    /** True if screen should fade in, false otherwise */
    private boolean fadeIn;

    // DRAWING
    /** Dimensions of the screen **/
    private static Vector2 SCREEN_DIMENSIONS;
    /** Offset between items drawn on inventory */
    private static final float INV_ITEM_OFFSET = 0.1f;
    /** Offset between shelves in the inventory */
    private static final float INV_SHELF_OFFSET = 105.0f;
    /** Size of inventory items */
    private static final float ITEM_SIZE_SCALE = 0.12f;
    /** Factor to scale the button by */
    private static final float READY_BUTTON_SCALING = 0.13f;
    /** Standard window size (for scaling) */
    private static int STANDARD_WIDTH  = 1600;
    /** Standard window height (for scaling) */
    private static int STANDARD_HEIGHT = 900;
    /** Ration of the bar height to the screen */
    private static float BAR_HEIGHT_RATIO = 0.25f;
    /** The y-coordinate of the center of the ready button */
    private float centerY;
    /** The x-coordinate of the center of the ready button */
    private float centerX;
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
        manager.load(READY_BUTTON_FILE,Texture.class);
        assets.add(READY_BUTTON_FILE);
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
            backgroundTex = manager.get(BACKGROUND_FILE, Texture.class);
            backgroundTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        if (manager.isLoaded(SHELF_FILE)) {
            shelfTex = manager.get(SHELF_FILE, Texture.class);
            shelfTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        if (manager.isLoaded(READY_BUTTON_FILE)) {
            readyButtonTex = manager.get(READY_BUTTON_FILE, Texture.class);
            readyButtonTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        if (manager.isLoaded(DVD_FILE)) {
            dvdTex = manager.get(DVD_FILE, Texture.class);
            dvdTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        if (manager.isLoaded(SNACK_FILE)) {
            snackTex = manager.get(SNACK_FILE, Texture.class);
            snackTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
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
        // starting position - top left slot of the shelf
        float slotX = 0.3f;
        float slotY = (shelfTex.getHeight() * 0.56f) / canvas.getHeight();

        // top shelf - snacks
        for (int i = 0; i < numSnacks; i++) {
            items.add(new RestStopItem(UNCLICKED, Inventory.Item.ItemType.SNACK,
                    slotX, slotY, ITEM_SIZE_SCALE, snackTex));
            slotX += 0.09f;
        }

        slotX = 0.3f;
        slotY = (shelfTex.getHeight() * 0.36f) / canvas.getHeight();

        // middle shelf - books
        for (int i = 0; i < numBooks; i++) {
            // TODO change to books
            items.add(new RestStopItem(UNCLICKED, Inventory.Item.ItemType.SNACK,
                    slotX, slotY, ITEM_SIZE_SCALE, snackTex));
            slotX += 0.09f;
        }

        slotX = 0.3f;
        slotY = (shelfTex.getHeight() * 0.16f) / canvas.getHeight();

        // bottom shelf - movies
        for (int i = 0; i < numMovies; i++) {
            items.add(new RestStopItem(UNCLICKED, Inventory.Item.ItemType.DVD,
                    slotX, slotY, ITEM_SIZE_SCALE, dvdTex));
            slotX += 0.09f;
        }
    }

    /**
     * Creates an inventory of items for this rest stop.
     * @return the inventory
     */
    private Inventory createInventory() {
        float shelf_ox = 0.69f;
        float shelf_oy = 0.1f;
        float shelf_relsca = 1.0f;
        float shelf_cb = 0.0f;
        float slot_width = (shelfTex.getWidth() / ITEMS_PER_SHELF) / canvas.getWidth();
        float slot_height = (shelfTex.getHeight() / NUM_SHELVES - INV_SHELF_OFFSET) / canvas.getHeight();
        return new Inventory(shelf_ox, shelf_oy, shelf_relsca, shelf_cb, shelfTex,
                slot_width, slot_height, NUM_SHELVES*ITEMS_PER_SHELF);
    }

    public void setPlayerInv(Inventory inv) { playerInv = inv; }

    public Inventory getPlayerInv() { return playerInv; }

    public RestStopMode(GameCanvas canvas, AssetManager manager) {
        this.canvas = canvas;
        this.manager = manager;
        assets = new Array<String>();
        SCREEN_DIMENSIONS = new Vector2(canvas.getWidth(),canvas.getHeight());
        active = true;
        fadeIn = true;
        fadeOpacity = 1.0f;

        // Textures
        backgroundTex = new Texture(BACKGROUND_FILE);
        shelfTex = new Texture(SHELF_FILE);
        dvdTex = new Texture(DVD_FILE);
        snackTex = new Texture(SNACK_FILE);
        readyButtonTex = new Texture(READY_BUTTON_FILE);

        // The shelf image in middle of screen
        shelf = new Image(0.15f,0.0f, 0.9f, shelfTex);

        // Get item quantities for this rest stop
        Random rand = new Random();
        numSnacks = rand.nextInt(2) + 3; // nextInt(high-low) + low
        numBooks = rand.nextInt(2) + 1;
        numMovies = rand.nextInt(1) + 1;

        // Generate the items
        items = new Array<RestStopItem>();
        generateItems();

        // TODO CHANGe this also in draw
        centerX = SCREEN_DIMENSIONS.x * 0.92f + readyButtonTex.getWidth()/2.0f;
        centerY = SCREEN_DIMENSIONS.y * 0.02f + readyButtonTex.getWidth()/2.0f;

        // Create inventory and add items to it
        //        Image.updateScreenDimensions(canvas);
        //        Inventory.Item.setTexturesAndScales(dvd,ITEM_SIZE_SCALE,snack,ITEM_SIZE_SCALE);
        //        inv = createInventory();
        //        inv.setItemOffset(INV_ITEM_OFFSET);
        //        generateItems();
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
    private int delay = 0;
    private void draw() {
        canvas.beginHUDDrawing();
        System.out.println(fadeOpacity);
        // TODO FIX FADE-IN
        if (fadeIn) {
            canvas.drawFade(fadeOpacity);
            if (fadeOpacity > 0.1f){ //) && !(Math.abs(fadeOpacity - 0.01f) <= 0.0001f)) {
                fadeOpacity -= 0.05f;
            }
            else {
                if (delay < 50) {
                    delay++;
                }
                else {
                    fadeIn = false;
                }
            }
        }

        if (!fadeIn) {
            canvas.draw(backgroundTex, 0, 0);

            // Draw the shelf and items
            // Blue overlay if selected
            shelf.draw(canvas);
            for (RestStopItem i : items) {
                Color overlay = Color.WHITE;
                if (i.clickStatus == BUTTON_UP) {
                    overlay = Color.BLUE;
                }
                i.draw(canvas, overlay);
            }

            // draw the ready button
            canvas.draw(readyButtonTex, SCREEN_DIMENSIONS.x * 0.92f, SCREEN_DIMENSIONS.y * 0.02f, readyButtonTex.getWidth() * READY_BUTTON_SCALING, readyButtonTex.getHeight() * READY_BUTTON_SCALING);
        }
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
        backgroundTex.dispose();
        backgroundTex = null;
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
        System.out.println("TOUCHDOWNN");

        if (readyStatus == BUTTON_UP) {
            return true;
        }

        for (RestStopItem i : items) {
            if (i.clickStatus == BUTTON_UP) {
                return true;
            }
        }

        // Check if ready button was pressed
        // Flip to match graphics coordinates
        screenY = heightY-screenY;
        //        float distX = (screenX-(centerX))*(screenX-(centerX));
        float distX = (screenX-(centerX));
        float distY = (screenY-(centerY));

        //        float distY = (screenY-(centerY))*(screenY-(centerY));

        if (distX < readyButtonTex.getWidth() && distY < readyButtonTex.getHeight()) {
            readyStatus = BUTTON_DOWN;
        }

        // TODO: also check for items on shelf
        // if in click area, change click status to button down
        for (RestStopItem i : items) {

        }

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

        for (RestStopItem i : items) {
            if (i.clickStatus == BUTTON_DOWN) {
                i.clickStatus = BUTTON_UP;
                return false;
            }
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

    /** Called when the mouse wheel was scrolled. Will not  be called on iOS. -- UNSUPPORTED
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed. */
    public boolean scrolled (int amount) {
        return true;
    }

    private class RestStopItem extends Image {
        private int clickStatus;
        private Inventory.Item.ItemType type;

        private RestStopItem(int clickStatus, Inventory.Item.ItemType t, float x, float y, float relSca, Texture tex) {
            super(x,y,relSca,tex);
            this.clickStatus = clickStatus;
            type = t;
        }
    }

}