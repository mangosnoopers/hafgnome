package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.items.Item;
import edu.cornell.gdiac.util.ScreenListener;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class RestStopMode implements Screen, InputProcessor {
    // ASSETS - TODO maintain in an array (assets)
    private Array<String> assets;
    private static final String BACKGROUND_FILE = "images/restStopAssets/background.png";
    private static final String SHELF_FILE = "images/restStopAssets/shelf.png";
    private static final String GNOMECOUNTRY_DVD_FILE = "images/Items/Gnome Country for Old Men.png";
    private static final String SILENCE_DVD_FILE = "images/Items/Silence of the Gnomes.png";
    private static final String CHIPS_FILE = "images/Items/chips.png";
    private static final String MANGO_FILE = "images/Items/mango.png";
    private static final String READY_BUTTON_FILE = "images/restStopAssets/readybutton.png";
    private static String FONT_FILE = "fonts/Roboto-Regular.ttf";
    private Texture backgroundTex;
    private Texture shelfTex;
    private Texture readyButtonTex;
    private BitmapFont displayFont;
    private ObjectMap<String,Texture> itemTextures;

    // BUTTONS
    /** 0: Unclicked, 1: Button Down, 2: Button Up */
    private static final int UNCLICKED = 0;
    private static final int BUTTON_DOWN = 1;
    private static final int BUTTON_UP = 2;
    /** Ready button is clicked when the player is ready to return to the road */
    private int readyStatus;

    // ITEMS
    /** An array of the items at the rest stop */
    private Array<RestStopItem> items;
    /** The Image corresponding to the shelf */
    private Image shelf;
    /** Number of each item that will appear at this rest stop */
    private int numMango;
    private int numChips;
    private int numGnomeCountry;
    private int numSilenceOfGnomes;
    /** The player's inventory */
    private Inventory playerInv;
    /** Constants for whether an item is selected or unselected */
    private static final int UNSELECTED = 0;
    private static final int SELECTED = 1;
    /** Number of items that can be taken from the rest stop */
    private int numCanTake;
    /** Number of items selected so far */
    private int numSelected;
    /** Number of each specific item selected so far - used for the indicator */
    private int numSnacksSelected;
    private int numBooksSelected;
    private int numMoviesSelected;
    /** Constants for item names */
    private static final String MANGO = "mango";
    private static final String CHIPS = "chips";
    private static final String GNOME_COUNTRY = "Gnome Country for Old Men";
    private static final String SILENCE_GNOMES = "Silence of the Gnomes";

    // OTHER DOODADS
    /** AssetManager to be loading in the background */
    private AssetManager manager;
    /** Reference to GameCanvas created by the root */
    private GameCanvas canvas;
    /** Listener that will update the player mode when we are done */
    private ScreenListener listener;
    /** Whether or not this player mode is still active */
    private boolean active;

    // FADE IN/OUT
    /** Opacity used to fade in and out */
    private float fadeOpacity;
    /** True if screen should fade in, false otherwise */
    private boolean fadeIn;
    /** Fade delay */
    private int delay;

    // DRAWING
    /** Font sizes */
    private static final int TITLE_FONT_SIZE = 48;
    /** Shelf text location */
    private static final Vector2 SHELF_TEXT_LOC = new Vector2(0.39175f,0.87f);
    /** Dimensions of the screen **/
    private static Vector2 SCREEN_DIMENSIONS;
    /** Scaling of inventory items */
    private static final float ITEM_SIZE_SCALE = 0.12f;
    /** Relative coordinates and scaling of the ready button */
    private static final float READY_BUTTON_SCALING = 0.13f;
    private static final Vector2 READY_BUTTON_REL = new Vector2(0.92f,0.02f);
    /** Scaling for textures used to indicate # of items in inventory */
    private static final float IND_SCALING = 0.12f;
    /** Relative padding between each item in the indicator */
    private static final float IND_PADDING = 0.02f;

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
        //        manager.load(SHELF_FILE,Texture.class);
        //        assets.add(SHELF_FILE);
        //        manager.load(READY_BUTTON_FILE,Texture.class);
        //        assets.add(READY_BUTTON_FILE);
        //        manager.load(DVD_FILE,Texture.class);
        //        assets.add(DVD_FILE);
        //        manager.load(SNACK_FILE,Texture.class);
        //        assets.add(SNACK_FILE);
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
        //            backgroundTex = manager.get(BACKGROUND_FILE, Texture.class);
        //            backgroundTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //        }
        //
        //        if (manager.isLoaded(SHELF_FILE)) {
        //            shelfTex = manager.get(SHELF_FILE, Texture.class);
        //            shelfTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //        }
        //
        //        if (manager.isLoaded(READY_BUTTON_FILE)) {
        //            readyButtonTex = manager.get(READY_BUTTON_FILE, Texture.class);
        //            readyButtonTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //        }
        //
        //        if (manager.isLoaded(DVD_FILE)) {
        //            dvdTex = manager.get(DVD_FILE, Texture.class);
        //            dvdTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //        }
        //
        //        if (manager.isLoaded(SNACK_FILE)) {
        //            snackTex = manager.get(SNACK_FILE, Texture.class);
        //            snackTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //        }
    }

    /** Load the font. */
    public void loadFont() {
        FreetypeFontLoader.FreeTypeFontLoaderParameter size2Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size2Params.fontFileName = FONT_FILE;
        size2Params.fontParameters.size = TITLE_FONT_SIZE;
        manager.load(FONT_FILE, BitmapFont.class, size2Params);

        // Allocate the font
        if (manager.isLoaded(FONT_FILE)) {
            displayFont = manager.get(FONT_FILE,BitmapFont.class);
        } else {
            displayFont = null;
        }
    }

    /** Load the item textures and put them in their object mapping. */
    private void loadItemTextures() {
        itemTextures = new ObjectMap<String,Texture>();
        itemTextures.put(SILENCE_GNOMES, new Texture(SILENCE_DVD_FILE));
        itemTextures.put(GNOME_COUNTRY, new Texture(GNOMECOUNTRY_DVD_FILE));
        itemTextures.put(CHIPS, new Texture(CHIPS_FILE));
        itemTextures.put(MANGO, new Texture(MANGO_FILE));
    }

    /** Set the player's inventory to inv. */
    public void setPlayerInv(Inventory inv) { playerInv = inv; }

    /** Get the player's inventory. */
    public Inventory getPlayerInv() { return playerInv; }

    /**
     * Create a rest stop from the given JSON file.
     * @param canvas
     * @param manager
     * @param filename
     */
    public RestStopMode(GameCanvas canvas, AssetManager manager, String filename) {
        this.canvas = canvas;
        this.manager = manager;
        assets = new Array<String>();
        SCREEN_DIMENSIONS = new Vector2(canvas.getWidth(),canvas.getHeight());
        active = true;
        fadeIn = true;
        fadeOpacity = 1.0f;
        delay = 0;

        // Textures
        backgroundTex = new Texture(BACKGROUND_FILE);
        shelfTex = new Texture(SHELF_FILE);
        readyButtonTex = new Texture(READY_BUTTON_FILE);
        loadItemTextures();

        // Load font
        loadFont();

        // Create shelf image in middle of screen
        shelf = new Image(0.15f,0.0f, 0.9f, shelfTex);

        // Parse JSON to get item quantities/max number of items player can take
        parseJSON("levels/" + filename);

        // Generate the items, initialize some more variables
        items = new Array<RestStopItem>();
        generateItems();
        numSelected = 0;
        numSnacksSelected = 0; numBooksSelected = 0; numMoviesSelected = 0;
    }

    /**
     * Parse the JSON file that gives information about this rest stop.
     * @param f Path to the JSON file, relative to assets folder
     */
    private void parseJSON(String f) {
        try {
            Scanner scanner = new Scanner(new File(f));
            JSONObject json = new JSONObject(scanner.useDelimiter("\\A").next());
            scanner.close();
            numCanTake = json.getInt("numCanTake");
            numMango = json.getInt("numMango");
            numChips = json.getInt("numChips");
            // TODO: books
            numGnomeCountry = json.getInt("numGnomeCountry");
            numSilenceOfGnomes = json.getInt("numSilenceOfGnomes");
        } catch (FileNotFoundException e) {
            // TODO better error handling
            System.out.println("Rest stop file not found");
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

        // random generator for which item texture to use
        Random rand = new Random();

        // TOP SHELF: SNACKS
        // mangos
        for (int i = 0; i < numMango; i++) {
            items.add(new RestStopItem(UNCLICKED, Item.ItemType.SNACK,
                    slotX, slotY, ITEM_SIZE_SCALE, itemTextures.get(MANGO), MANGO));
            slotX += 0.09f;
        }
        // chips
        for (int i = 0; i < numChips; i++) {
            items.add(new RestStopItem(UNCLICKED, Item.ItemType.SNACK,
                    slotX, slotY, ITEM_SIZE_SCALE, itemTextures.get(CHIPS), CHIPS));
            slotX += 0.09f;
        }


        // MIDDLE SHELF - BOOKS
//        slotX = 0.3f;
//        slotY = (shelfTex.getHeight() * 0.36f) / canvas.getHeight();
//        for (int i = 0; i < numBooks; i++) {
//            // TODO change to books
//            Texture t = snackTexs.get(rand.nextInt(snackTexs.size));
//            items.add(new RestStopItem(UNCLICKED, Item.ItemType.SNACK,
//                    slotX, slotY, ITEM_SIZE_SCALE, t));
//            slotX += 0.09f;
//        }


        // BOTTOM SHELF - MOVIES
        slotX = 0.3f;
        slotY = (shelfTex.getHeight() * 0.16f) / canvas.getHeight();
        // gnome country for old men
        for (int i = 0; i < numGnomeCountry; i++) {
            items.add(new RestStopItem(UNCLICKED, Item.ItemType.DVD,
                    slotX, slotY, ITEM_SIZE_SCALE, itemTextures.get(GNOME_COUNTRY), GNOME_COUNTRY));
            slotX += 0.09f;
        }
        // silence of the gnomes
        for (int i = 0; i < numSilenceOfGnomes; i++) {
            items.add(new RestStopItem(UNCLICKED, Item.ItemType.DVD,
                    slotX, slotY, ITEM_SIZE_SCALE, itemTextures.get(SILENCE_GNOMES), SILENCE_GNOMES));
            slotX += 0.09f;
        }
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
     * Draw the status of this player mode.
     *
     * We prefer to separate update and draw from one another as separate methods, instead
     * of using the single render() method that LibGDX does.  We will talk about why we
     * prefer this in lecture.
     */
    private void draw() {
        canvas.beginHUDDrawing();
        // TODO FIX FADE-IN, add fade-out
        if (fadeIn) {
            drawFadeIn();
        } else {
            // Draw background
            canvas.draw(backgroundTex, 0, 0);

            // Draw the shelf and text on top
            // TODO - minimum of numCanTake and remaining inventory space
            shelf.draw(canvas);
            String grammar = numCanTake == 1 ? " item" : " items";
            canvas.drawText("Take up to " + numCanTake + grammar, displayFont,
                             SCREEN_DIMENSIONS.x * SHELF_TEXT_LOC.x,
                             SCREEN_DIMENSIONS.y * SHELF_TEXT_LOC.y, Color.BLACK);

            // Draw the items with a specific overlay
            for (RestStopItem i : items) {
                i.draw(canvas, i.overlay);
            }

            // Draw the ready button - colored if pressed down
            Color readyColor = Color.WHITE;
            if (readyStatus == BUTTON_DOWN) {
                readyColor = Color.CHARTREUSE;
            }
            canvas.draw(readyButtonTex, SCREEN_DIMENSIONS.x * READY_BUTTON_REL.x,
                    SCREEN_DIMENSIONS.y * READY_BUTTON_REL.y,
                    readyButtonTex.getWidth() * READY_BUTTON_SCALING,
                    readyButtonTex.getHeight() * READY_BUTTON_SCALING,
                    readyColor);

            // Draw current inventory quantities
            drawPlayerInv();

        }
        canvas.endHUDDrawing();
    }

    /** FIXME: Draw the fade-in transition */
    private void drawFadeIn() {
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

    /** Draw the current inventory quantities */
    private void drawPlayerInv() {
        float indRelX = 0.01f; // relative x location for item sprite
        float textRelX = 0.06f; // relative x location for text
        float indRelY = 0.97f; // relative y location for item sprite
        float oyOff = 0.7f;

        // Snacks
        Texture mango = itemTextures.get(MANGO);
        canvas.draw(mango,Color.WHITE, 0.0f, mango.getHeight() * oyOff,
                SCREEN_DIMENSIONS.x*indRelX,SCREEN_DIMENSIONS.y*indRelY, 0.0f,
                IND_SCALING, IND_SCALING);
        // Snack text
        int totalNumSnacks = playerInv.getNumSnacks() + numSnacksSelected;
        canvas.drawText("x " + totalNumSnacks, displayFont,
                SCREEN_DIMENSIONS.x*textRelX,SCREEN_DIMENSIONS.y*indRelY, Color.BLACK);

        // Books: TODO
        Texture book = mango;
//        Texture book = snackTexs.get(0);
//        indRelY -= (mango.getHeight()*IND_SCALING)/SCREEN_DIMENSIONS.y + IND_PADDING;
//        canvas.draw(book, Color.WHITE, 0.0f, book.getHeight() * oyOff,
//                SCREEN_DIMENSIONS.x*indRelX,SCREEN_DIMENSIONS.y*indRelY, 0.0f,
//                IND_SCALING, IND_SCALING);
//        // Book text
        //int totalNumBooks = playerInv.getNumBooks() + numBooksSelected;
//        canvas.drawText("x " + totalNumBooks, displayFont,SCREEN_DIMENSIONS.x*textRelX,
//                SCREEN_DIMENSIONS.y*indRelY, Color.BLACK);

        // Movies
        Texture dvd = itemTextures.get(GNOME_COUNTRY);
        indRelY -= (book.getHeight()*IND_SCALING)/SCREEN_DIMENSIONS.y + IND_PADDING;
        canvas.draw(dvd, Color.WHITE, 0.0f, dvd.getHeight() * oyOff,
                SCREEN_DIMENSIONS.x*indRelX,SCREEN_DIMENSIONS.y*indRelY, 0.0f,
                IND_SCALING, IND_SCALING);
        // Movie text
        int totalNumMovies = playerInv.getNumMovies() + numMoviesSelected;
        canvas.drawText("x " + totalNumMovies, displayFont,SCREEN_DIMENSIONS.x*textRelX,
                SCREEN_DIMENSIONS.y*indRelY, Color.BLACK);
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
            draw();

            // Check if player wants to exit rest stop
            if (readyStatus == BUTTON_UP && listener != null) {
                // Add selected items to their inventory
                for (RestStopItem i : items) {
                    if (i.toggleState == SELECTED) {
                        switch (i.getItemType()) {
                            case SNACK:
                                playerInv.getSnackSlot().incAmount(1);
                                break;
                            case DVD:
                                playerInv.getMovieSlot().incAmount(1);
                                break;
                            // TODO - book case (rn books are snacks)
                            default:
                                break;
                        }
                    }
                }

                // Save the game
                // TODO

                // Exit the screen
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

        if (readyStatus == BUTTON_UP) {
            return true;
        }

        // CHECK IF THINGS ARE PRESSED
        // Flip screenY to match graphics coordinates
        screenY = (int)SCREEN_DIMENSIONS.y-screenY;

        // Check if ready button was pressed
        // coordinates of ready button bottom center
        float halfReadyWidth = readyButtonTex.getWidth()*READY_BUTTON_SCALING*0.5f;
        float halfReadyHeight = readyButtonTex.getHeight()*READY_BUTTON_SCALING*0.5f;
        float readyMX = SCREEN_DIMENSIONS.x*READY_BUTTON_REL.x + halfReadyWidth;
        float readyMY = SCREEN_DIMENSIONS.y*READY_BUTTON_REL.y + halfReadyHeight;

        // If clicking on ready button, set to BUTTON_DOWN
        if (Math.abs(screenX - readyMX) < halfReadyWidth && Math.abs(screenY - readyMY) < halfReadyHeight) {
            readyStatus = BUTTON_DOWN;
        }

        // If clicking on an item, change click status to button down
        for (RestStopItem i : items) {
            // coordinates of center of item
            float halfItemWidth = i.texture.getWidth()*ITEM_SIZE_SCALE*0.5f;
            float halfItemHeight = i.texture.getHeight()*ITEM_SIZE_SCALE*0.5f;
            float mx = i.position.x*SCREEN_DIMENSIONS.x + halfItemWidth;
            float my = i.position.y*SCREEN_DIMENSIONS.y + halfItemHeight;

            if (Math.abs(screenX - mx) < halfItemHeight&& Math.abs(screenY - my) < halfItemHeight) {
                i.clickStatus = BUTTON_DOWN;
            }

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
                int oldToggleState = i.toggleState;

                // only allow the item to be selected if the user can take more
                // or allow the item to be unselected at any time
                if (oldToggleState == UNSELECTED && numSelected < numCanTake
                        || oldToggleState == SELECTED) {
                    i.switchState();
                    i.switchOverlay();

                    // update item quantity
                    // case 1: item was selected and is now unselected - decrease items taken
                    // case 2: item was unselected and is now selected - increase items taken
                    if (oldToggleState == SELECTED && i.toggleState == UNSELECTED) {
                        numSelected -= 1;

                        // also update the amount for the specific item
                        switch (i.getItemType()) {
                            case SNACK:
                                numSnacksSelected -= 1;
                                break;
                            case DVD:
                                numMoviesSelected -= 1;
                                break;
                            // TODO - book case (rn books are snacks)
                            default:
                                break;
                        }
                    } else if (oldToggleState == UNSELECTED & i.toggleState == SELECTED) {
                        numSelected += 1;

                        // also update the amount for the specific item
                        switch (i.getItemType()) {
                            case SNACK:
                                numSnacksSelected += 1;
                                break;
                            case DVD:
                                numMoviesSelected += 1;
                                break;
                            // TODO - book case (rn books are snacks)
                            default:
                                break;
                        }
                    }
                }


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

    private class RestStopItem extends Item {
        /** The click status of a rest stop item - unclicked, touch down, or touch up */
        private int clickStatus;
        /** The state of the item - selected or unselected */
        private int toggleState;
        /** The item's current overlay color - white when unselected, colored when selected */
        private Color overlay;

        /** Constructor for a rest stop item.
         *
         * @param clickStatus The click status of the item - unclicked, button down, button up
         * @param type The type of item
         * @param x The relative x-position of the item
         * @param y The relative y-position of the item
         * @param relSca The relative scale of the item
         * @param t The item's texture
         * @param n The item's name
         */
        private RestStopItem(int clickStatus, Item.ItemType type, float x, float y, float relSca, Texture t, String n) {
            super(x,y,relSca,t,type,n);
            this.clickStatus = clickStatus;
            toggleState = UNSELECTED;
            overlay = Color.WHITE;
        }

        /** Switch the toggle state of the item */
        private void switchState() {
            toggleState = toggleState == UNSELECTED ? SELECTED : UNSELECTED;
        }

        /** Switch the overlay of the item */
        private void switchOverlay() {
            overlay = overlay.equals(Color.WHITE) ? Color.CHARTREUSE : Color.WHITE;
        }
    }

}