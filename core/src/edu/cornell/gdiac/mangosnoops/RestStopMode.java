package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.util.ScreenListener;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class RestStopMode implements Screen, InputProcessor {
    // ASSETS
    private static final String BACKGROUND_FILE = "images/restStopAssets/background.png";
    private static final String SHELF_FILE = "images/restStopAssets/shelf.png";
    private static final String GNOMECOUNTRY_DVD_FILE = "images/Items/Gnome Country for Old Men.png";
    private static final String MANGO_FILE = "images/Items/mango.png";
    private static final String READY_BUTTON_FILE = "images/levelSelectAssets/goButton.png";
    private static String FONT_FILE = "fonts/Roadgeek 2005 Series E.ttf";
    private Texture backgroundTex;
    private Texture shelfTex;
    private Texture dvdTex;
    private Texture snackTex;
    private Texture readyButtonTex;
    private BitmapFont displayFont;
    private ObjectMap<String,Texture> itemTextures;
    private static Music bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/bensound-jazzcomedy.mp3"));

    // BUTTONS
    /** 0: Unclicked, 1: Button Down, 2: Button Up */
    private static final int UNCLICKED = 0;
    private static final int BUTTON_DOWN = 1;
    private static final int BUTTON_UP = 2;
    /** Ready button is clicked when the player is ready to return to the road */
    private int readyStatus;
    private Image readyButton;

    // ITEMS
    /** An array of the items at the rest stop */
    private Array<RestStopItem> items;
    /** The Image corresponding to the shelf */
    private Image shelf;
    /** Number of each item that will appear at this rest stop */
    private int numMango;
    private int numGnomeCountry;
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
    private static final String GNOME_COUNTRY = "Gnome Country for Old Men";
    private static final int SLOT_MAX_QUANTITY = 5;

    // OTHER DOODADS
    /**Plays music and sounds **/
    private SoundController soundController;
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
    private static final Vector2 SHELF_TEXT_LOC = new Vector2(0.37175f,0.87f);
    /** Dimensions of the screen **/
    private static Vector2 SCREEN_DIMENSIONS;
    /** Scaling of inventory items */
    private static final float ITEM_SIZE_SCALE = 0.12f;
    /** Relative coordinates and scaling of the ready button */
    private static final Vector2 READY_BUTTON_SCALING = new Vector2(0.08f,0.08f);
    private static final Vector2 READY_BUTTON_REL = new Vector2(0.93f,0.04f);
    /** Scaling for textures used to indicate # of items in inventory */
    private static final float IND_SCALING = 0.09f;
    /** Drawing for items in the indicator */
    private static final float IND_X = 0.90f;
    private static final float IND_TEXT_X = 0.94f;
    private static final float IND_SNACK_Y = 0.94f;
    private static final float IND_DVD_Y = 0.84f;
    private Image snackInd;
    private Image dvdInd;

    /** Set the player's inventory to inv. */
    public void setPlayerInv(Inventory inv) { playerInv = inv; }

    /** Get the player's inventory. */
    public Inventory getPlayerInv() { return playerInv; }

    /** Load the font. */
    private void loadFont() {
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

    /** Initialize all textures */
    private void initTextures() {
        backgroundTex = new Texture(BACKGROUND_FILE);
        shelfTex = new Texture(SHELF_FILE);
        readyButtonTex = new Texture(READY_BUTTON_FILE);
        dvdTex = new Texture(GNOMECOUNTRY_DVD_FILE);
        snackTex = new Texture(MANGO_FILE);

        itemTextures = new ObjectMap<String,Texture>();
        itemTextures.put(GNOME_COUNTRY, dvdTex);
        itemTextures.put(MANGO, snackTex);
    }

    public RestStopMode(GameCanvas canvas, AssetManager manager, String filename, SoundController sc) {
        this.soundController = sc;
        this.canvas = canvas;
        this.manager = manager;
        SCREEN_DIMENSIONS = new Vector2(canvas.getWidth(),canvas.getHeight());
        active = true;
        fadeIn = true;
        fadeOpacity = 1.0f;
        delay = 0;

        // Textures and load font
        initTextures();
        loadFont();

        // Create images
        shelf = new Image(0.15f,0.0f, 0.9f, shelfTex);
        readyButton = new Image(READY_BUTTON_REL.x, READY_BUTTON_REL.y, READY_BUTTON_SCALING.x, readyButtonTex, GameCanvas.TextureOrigin.MIDDLE);

        // Parse JSON to get item quantities/max number of items player can take
        parseJSON("levels/" + filename);

        // Generate the items, initialize some more variables
        items = new Array<RestStopItem>();
        generateItems();
        numSelected = 0;
        numSnacksSelected = 0; numBooksSelected = 0; numMoviesSelected = 0;

        // Indicator images
        snackInd = new Image(IND_X,IND_SNACK_Y,IND_SCALING,itemTextures.get(MANGO),GameCanvas.TextureOrigin.MIDDLE);
        dvdInd = new Image(IND_X,IND_DVD_Y,IND_SCALING,itemTextures.get(GNOME_COUNTRY), GameCanvas.TextureOrigin.MIDDLE);
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
            numMango = json.getInt("numSnacks");
            numGnomeCountry = json.getInt("numMovies");
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
        float slotX = 0.35f;
        float slotY = 0.66f;

        // TOP SHELF: SNACKS - TODO ADD NAME, SEE STERLOCK BRANCH FOR NEW IMAGE CONSTRUCTOR
        // mangos
        for (int i = 0; i < numMango; i++) {
            items.add(new RestStopItem(UNCLICKED, Inventory.Item.ItemType.SNACK,
                    slotX, slotY, ITEM_SIZE_SCALE, itemTextures.get(MANGO))); // MANGO));
            slotX += 0.09f;
        }


        // MIDDLE SHELF - BOOKS
        //        slotX = 0.35f;
        //        slotY = 0.36f;
        //        for (int i = 0; i < numBooks; i++) {
        //            // TODO change to books
        //            Texture t = snackTexs.get(rand.nextInt(snackTexs.size));
        //            items.add(new RestStopItem(UNCLICKED, Item.ItemType.SNACK,
        //                    slotX, slotY, ITEM_SIZE_SCALE, t));
        //            slotX += 0.09f;
        //        }


        // BOTTOM SHELF - MOVIES
        slotX = 0.35f;
        slotY = 0.23f;
        // gnome country for old men
        for (int i = 0; i < numGnomeCountry; i++) {
            items.add(new RestStopItem(UNCLICKED, Inventory.Item.ItemType.DVD,
                    slotX, slotY, ITEM_SIZE_SCALE, itemTextures.get(GNOME_COUNTRY))); //, GNOME_COUNTRY));
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
    private boolean readyHover;
    private void draw() {
        canvas.beginHUDDrawing();
        // TODO FIX FADE-IN, add fade-out
        if (fadeIn) {
            drawFadeIn();
        } else {
            // Draw background
            canvas.draw(backgroundTex, Color.WHITE, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.9f, 0.9f);

            // Draw the shelf and text on top
            // TODO - minimum of numCanTake and remaining inventory space
            shelf.draw(canvas);
            String grammar = numCanTake == 1 ? " ITEM" : " ITEMS";
            canvas.drawText("TAKE UP TO " + numCanTake + grammar, displayFont,
                    SCREEN_DIMENSIONS.x * SHELF_TEXT_LOC.x,
                    SCREEN_DIMENSIONS.y * SHELF_TEXT_LOC.y, Color.BLACK);

            // Draw the items with a specific overlay
            for (RestStopItem i : items) {
                i.draw(canvas, i.overlay);
            }

            // Draw the ready button - colored if pressed down
            Color readyColor = Color.WHITE;
            if (readyStatus == BUTTON_DOWN || readyHover) {
                readyColor = Color.CHARTREUSE;
            }
            readyButton.draw(canvas, readyColor);

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

        // Snack s
        Color snackcolor = Color.BLACK;
        snackInd.draw(canvas);
        int totalNumSnacks = Math.min(playerInv.getNumSnacks() + numSnacksSelected, SLOT_MAX_QUANTITY);
//        if (totalNumSnacks == SLOT_MAX_QUANTITY) {
//            snackcolor = Color.RED;
//        }
        canvas.drawText("x " + totalNumSnacks, displayFont,
                SCREEN_DIMENSIONS.x*IND_TEXT_X,SCREEN_DIMENSIONS.y*IND_SNACK_Y,
                snackcolor);

        // Books: TODO

        // Movies
        Color dvdcolor = Color.BLACK;
        dvdInd.draw(canvas);
        int totalNumMovies = Math.min(playerInv.getNumMovies() + numMoviesSelected, SLOT_MAX_QUANTITY);
//        if (totalNumMovies == SLOT_MAX_QUANTITY) {
//            dvdcolor = Color.RED;
//        }
        canvas.drawText("x " + totalNumMovies, displayFont,
                SCREEN_DIMENSIONS.x*IND_TEXT_X,SCREEN_DIMENSIONS.y*IND_DVD_Y,
                dvdcolor);
    }

    // SCREEN METHODS

    /** Called when this screen becomes the current screen for a game. */
    public void show () {
        active = true;
        bgMusic.play();
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta) {
        if (active) {
            update(delta);
            draw();

            if (readyStatus == BUTTON_UP && listener != null) {
                // update inventory when player is done
                for (RestStopItem i : items) {
                    if (i.toggleState == SELECTED) {
                        switch (i.type) {
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

                listener.exitScreen(this,0);
            }
        }
    }

    /** Called when this screen should release all resources. */
    public void dispose () {
        bgMusic.stop();
        bgMusic.dispose();
        backgroundTex.dispose();
        backgroundTex = null;
        shelfTex.dispose();
        shelfTex = null;
        readyButtonTex.dispose();
        readyButtonTex = null;
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
        displayFont.getData().setScale(width / (SCREEN_DIMENSIONS.x/displayFont.getScaleX()),
                height / (SCREEN_DIMENSIONS.y/displayFont.getScaleY()));
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

        // Check if ready button was pressed
        Vector2 mousePos = new Vector2(screenX, screenY);
        if (readyButton.inArea(mousePos)) {
            readyStatus = BUTTON_DOWN;
        }

        // If clicking on an item, change click status to button down
        for (RestStopItem i : items) {
            int quantity = (i.type == Inventory.Item.ItemType.SNACK)
                    ? numSnacksSelected + playerInv.getNumSnacks()
                    : numMoviesSelected + playerInv.getNumMovies();
            if (quantity < SLOT_MAX_QUANTITY && i.inArea(mousePos)) {
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
            soundController.playClick();
            readyStatus = BUTTON_UP;
            return false;
        }

        for (RestStopItem i : items) {
            int quantity = (i.type == Inventory.Item.ItemType.SNACK)
                    ? numSnacksSelected + playerInv.getNumSnacks()
                    : numMoviesSelected + playerInv.getNumMovies();
            if (quantity < SLOT_MAX_QUANTITY && i.clickStatus == BUTTON_DOWN) {
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
                        switch (i.type) {
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
                        switch (i.type) {
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
        if(readyButton.inArea(new Vector2(screenX,screenY))){
            readyHover = true;
            soundController.playHoverMouse();
        } else {
            readyHover = false;
        }
        return true;
    }

    /** Called when the mouse wheel was scrolled. Will not  be called on iOS. -- UNSUPPORTED
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed. */
    public boolean scrolled (int amount) {
        return true;
    }

    private class RestStopItem extends Image {
        /** The click status of a rest stop item - unclicked, touch down, or touch up */
        private int clickStatus;
        /** The type of item */
        private Inventory.Item.ItemType type;
        /** The state of the item - selected or unselected */
        private int toggleState;
        /** The item's current overlay color - white when unselected, colored when selected */
        private Color overlay;

        private RestStopItem(int clickStatus, Inventory.Item.ItemType t, float x, float y, float relSca, Texture tex) {
            super(x,y,relSca,tex,GameCanvas.TextureOrigin.MIDDLE);
            this.clickStatus = clickStatus;
            type = t;
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