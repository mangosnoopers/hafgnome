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
import java.util.Scanner;

public class RestStopMode implements Screen, InputProcessor {
    // ASSETS
    private static final String BACKGROUND_FILE = "images/restStopAssets/background.png";
    private static final String SHELF_FILE = "images/restStopAssets/shelf.png";
    private static final String GNOMECOUNTRY_DVD_FILE = "images/Items/Gnome Country for Old Men.png";
    private static final String MANGO_FILE = "images/Items/mango.png";
    private static final String READY_BUTTON_FILE = "images/levelSelectAssets/goButton.png";
    private static String FONT_FILE = "fonts/Roadgeek 2005 Series E.ttf";
    private static String SPEECHFONT_FILE = "fonts/ComicSans.ttf";
    private static final String TUT_SPEECH = "images/Tutorial/speechbubble_small_reverse.png";
    private static final String CHILD_SPEECH = "images/Tutorial/speechbubble_small.png";
    private static final String DVDPOPUP_FILE = "images/restStopAssets/dvdpopup.png";
    private static final String MANGOPOPUP_FILE = "images/restStopAssets/mangopopup.png";
    private static final String TIP_GNOME_FILE = "images/restStopAssets/gameTips/gnometip.png";
    private static final String TIP_REAR_FILE = "images/restStopAssets/gameTips/reartip.png";
    private static final String TIP_CLASSICAL_FILE = "images/restStopAssets/gameTips/classicaltip.png";
    private static final String TIP_MUSIC_FILE = "images/restStopAssets/gameTips/musictip.png";
    private static final String TIP_REQUEST_FILE = "images/restStopAssets/gameTips/requesttip.png";
    private static final String TIP_DVD_FILE = "images/restStopAssets/gameTips/dvdtip.png";
    private static final String TIP_FLAMINGO_FILE = "images/restStopAssets/gameTips/flamingotip.png";
    private static final String TIP_VISOR_FILE = "images/restStopAssets/gameTips/visortip.png";
    private static final String TIP_GRILL_FILE = "images/restStopAssets/gameTips/grilltip.png";
    private static final String TIP_SAT_FILE = "images/restStopAssets/gameTips/sattip.png";

    private Image tutorialModule;
    private Texture tipGnomeTex;
    private Texture tipRearTex;
    private Texture tipClassicalTex;
    private Texture tipMusicTex;
    private Texture tipRequestTex;
    private Texture tipDvdTex;
    private Texture tipFlamningoTex;
    private Texture tipVisorTex;
    private Texture tipGrillTex;
    private Texture tipSatTex;
    private Texture backgroundTex;
    private Texture shelfTex;
    private Texture dvdTex;
    private Texture snackTex;
    private Texture readyButtonTex;
    private Texture tutSpeech;
    private Texture kidSpeech;
    private Texture dvdPopupTex;
    private Texture mangoPopupTex;
    private BitmapFont displayFont;
    private BitmapFont speechFont;
    private ObjectMap<String,Texture> itemTextures;
    private static Music bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/bensound-jazzcomedy.mp3"));

    // BUTTONS
    /** 0: Unclicked, 1: Button Down, 2: Button Up */
    private static final int UNCLICKED = 0;
    private static final int BUTTON_DOWN = 1;
    private static final int BUTTON_UP = 2;
    /** Ready button is clicked when the player is ready to return to the road */
    private int readyStatus;
    /** Counter to check if clicked for tutorial modules  */
    private int generalClick;
    private Image readyButton;

    // IMAGES
    private Image background;
    private Image dvdPopup;
    private Image mangoPopup;

    // KIDS TALKING -- null if no speech bubble
    private String noshDialogue;
    private boolean tintNoshDialogue;
    private String nedDialogue;

    // POPUP STUFF
    private float popupDuration;
    private boolean drawSnackPopup;
    private boolean drawDVDPopup;
    private static final float MAX_POPUP_DURATION = 5.0f;

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
    private static final int FONT_SIZE = 24;
    /** Shelf text location */
    private static final Vector2 SHELF_TEXT_LOC = new Vector2(0.35175f,0.82f);
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
    private static final float IND_X = 0.03f;
    private static final float IND_TEXT_X = 0.07f;
    private static final float IND_SNACK_Y = 0.95f;
    private static final float IND_DVD_Y = 0.85f;
    private Image snackInd;
    private Image dvdInd;

    // Distinguish what level to decide if tutorial module is necessary
    /** True iff is a tutorial level */
    private boolean isTutorial;
    /** What tutoiral it is, null if not a tutorial*/
    private int indexLevel;
    /** whether or not tutorial module is being drawn right now */
    private boolean moduleOpen;

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
        size2Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size2Params.fontFileName = SPEECHFONT_FILE;
        size2Params.fontParameters.size = FONT_SIZE;
        manager.load(SPEECHFONT_FILE, BitmapFont.class, size2Params);


        // Allocate the font
        if (manager.isLoaded(FONT_FILE)) {
            displayFont = manager.get(FONT_FILE,BitmapFont.class);
        } else {
            displayFont = null;
        }
        if (manager.isLoaded(SPEECHFONT_FILE)) {
            speechFont = manager.get(SPEECHFONT_FILE,BitmapFont.class);
        } else {
            speechFont = null;
        }
    }

    /** Initialize all textures */
    private void initTextures() {
        backgroundTex = new Texture(BACKGROUND_FILE);
        shelfTex = new Texture(SHELF_FILE);
        readyButtonTex = new Texture(READY_BUTTON_FILE);
        dvdTex = new Texture(GNOMECOUNTRY_DVD_FILE);
        snackTex = new Texture(MANGO_FILE);
        tutSpeech = new Texture(TUT_SPEECH);
        kidSpeech = new Texture(CHILD_SPEECH);
        dvdPopupTex = new Texture(DVDPOPUP_FILE);
        mangoPopupTex = new Texture(MANGOPOPUP_FILE);
        tipGnomeTex = new Texture(TIP_GNOME_FILE);
        tipRearTex = new Texture(TIP_REAR_FILE);
        tipClassicalTex = new Texture(TIP_CLASSICAL_FILE);
        tipMusicTex = new Texture(TIP_MUSIC_FILE);
        tipRequestTex = new Texture(TIP_REQUEST_FILE);
        tipDvdTex = new Texture(TIP_DVD_FILE);
        tipFlamningoTex = new Texture(TIP_FLAMINGO_FILE);
        tipVisorTex = new Texture(TIP_VISOR_FILE);
        tipGrillTex = new Texture(TIP_GRILL_FILE);
        tipSatTex = new Texture(TIP_SAT_FILE);

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
        drawSnackPopup = false;
        drawDVDPopup = false;
        popupDuration = -1.0f;
        generalClick = UNCLICKED;

        // Textures and load font
        initTextures();
        loadFont();

        // Create images
        tutorialModule = new Image(0.5f, 0.5f, 0.7f, tipGnomeTex, GameCanvas.TextureOrigin.MIDDLE);
        shelf = new Image(0.5f, 0.4f, 0.9f, shelfTex, GameCanvas.TextureOrigin.MIDDLE);
        readyButton = new Image(READY_BUTTON_REL.x, READY_BUTTON_REL.y, READY_BUTTON_SCALING.x, readyButtonTex, GameCanvas.TextureOrigin.MIDDLE);
        background = new Image(0.5f,0.5f,1.0f, backgroundTex, GameCanvas.TextureOrigin.MIDDLE);
        dvdPopup = new Image(0.5f,0.5f,0.5f, dvdPopupTex, GameCanvas.TextureOrigin.MIDDLE);
        mangoPopup = new Image(0.5f,0.5f,0.5f, mangoPopupTex, GameCanvas.TextureOrigin.MIDDLE);

        // Parse JSON to get item quantities/max number of items player can take
        parseJSON("levels/" + filename);

        // Parse filename to decide whether it is a tutorial or not
        isTutorial = filename.contains("tut");
        if(isTutorial)
            indexLevel = Character.getNumericValue(filename.charAt(filename.indexOf("tut") + 3));
        else {
            indexLevel = Character.getNumericValue(filename.charAt(filename.indexOf("level") + 5));
        }

        // Generate the items, initialize some more variables
        items = new Array<RestStopItem>();
        generateItems();
        numSelected = 0;
        numSnacksSelected = 0; numMoviesSelected = 0;

        // Indicator images
        snackInd = new Image(IND_X,IND_SNACK_Y,IND_SCALING,itemTextures.get(MANGO),GameCanvas.TextureOrigin.MIDDLE);
        dvdInd = new Image(IND_X,IND_DVD_Y,IND_SCALING,itemTextures.get(GNOME_COUNTRY), GameCanvas.TextureOrigin.MIDDLE);

        // Handle whether or not to have speech bubbles from kids
        tintNoshDialogue = false;
        if(isTutorial) {
            switch(indexLevel) {
                case 2: //force user to grab movie
                    break;
                default:
                    break;
            }
        } else { //normal level
            switch(indexLevel) {
                default:
                    break;
            }
        }
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
        float slotX = 0.31f;
        float slotY = 0.61f;

        // TOP SHELF: SNACKS
        // mangos
        for (int i = 0; i < numMango; i++) {
            items.add(new RestStopItem(UNCLICKED, Inventory.Item.ItemType.SNACK,
                    slotX, slotY, ITEM_SIZE_SCALE, itemTextures.get(MANGO))); // MANGO));
            slotX += 0.09f;
        }

        // BOTTOM SHELF - MOVIES
        slotX = 0.31f;
        slotY = 0.18f;
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
        if (popupDuration >= 0.0f) {
            popupDuration += delta;
        }

        if (popupDuration >= MAX_POPUP_DURATION) {
            popupDuration = -1.0f;
            generalClick = UNCLICKED;
            if (drawSnackPopup)
                drawSnackPopup = false;
            else if (drawDVDPopup)
                drawDVDPopup = false;
        }
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
            background.draw(canvas);
            // Draw the shelf and text on top
            // TODO - minimum of numCanTake and remaining inventory space
            shelf.draw(canvas);
            String grammar = numCanTake == 1 ? " ITEM" : " ITEMS";
            displayFont.setColor(Color.BLACK);
            if(numCanTake == 0)
                canvas.drawText("THIS 8-12 GOT GNOMED", displayFont,
                        SCREEN_DIMENSIONS.x * (SHELF_TEXT_LOC.x-0.01f),
                        SCREEN_DIMENSIONS.y * SHELF_TEXT_LOC.y);
            else
                canvas.drawText("TAKE UP TO " + numCanTake + grammar, displayFont,
                        SCREEN_DIMENSIONS.x * SHELF_TEXT_LOC.x,
                        SCREEN_DIMENSIONS.y * SHELF_TEXT_LOC.y);

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

            // Draw popups if needed
            if (drawSnackPopup && generalClick != BUTTON_UP) {
                mangoPopup.draw(canvas);
            }

            if (drawDVDPopup && generalClick != BUTTON_UP) {
                dvdPopup.draw(canvas);
            }

        }
        speechBubble();
        drawTutorial();
        canvas.endHUDDrawing();
    }

    private static final float NED_BUBBLE_X = 0.71f;
    private static final float NED_BUBBLE_Y = 0.63f;
    private static final float NOSH_BUBBLE_X = 0.55f;
    private static final float NOSH_BUBBLE_Y = 0.49f;

    public void speechBubble() {
        if(nedDialogue != null) {
            canvas.draw(kidSpeech, GameCanvas.TextureOrigin.BOTTOM_LEFT, NED_BUBBLE_X, NED_BUBBLE_Y, 0.1f, false, Color.NAVY);
            speechFont.setColor(Color.WHITE);
            canvas.drawText(nedDialogue, speechFont,
                    canvas.getWidth()*(NED_BUBBLE_X+0.01f),canvas.getHeight()*(NED_BUBBLE_Y+0.08f));
        }
        if(noshDialogue != null){
            if(tintNoshDialogue)
                canvas.draw(kidSpeech, GameCanvas.TextureOrigin.BOTTOM_LEFT, NOSH_BUBBLE_X, NOSH_BUBBLE_Y, 0.1f, false, Color.RED);
            else
                canvas.draw(kidSpeech, GameCanvas.TextureOrigin.BOTTOM_LEFT, NOSH_BUBBLE_X, NOSH_BUBBLE_Y, 0.1f, false, Color.PURPLE);
            speechFont.setColor(Color.WHITE);
            canvas.drawText(noshDialogue, speechFont,
                    canvas.getWidth()*(NOSH_BUBBLE_X+0.01f),canvas.getHeight()*(NOSH_BUBBLE_Y+0.08f));
        }
    }

    int state = 0;
    /** Draws tutorial items */
    private void drawTutorial() {
        if(isTutorial) {
            float x; float y;
            switch(indexLevel) {
                case 0: //draw module & explain snack item
                    if(generalClick != BUTTON_UP) tutorialModule.draw(canvas);
                    else {
                        x = 0.33f;
                        y = 0.57f;
                        canvas.draw(tutSpeech, GameCanvas.TextureOrigin.BOTTOM_LEFT, x, y, 0.1f, false, Color.YELLOW);
                        speechFont.setColor(Color.BLACK);
                        canvas.drawText("Make your kids happy\nby feeding them.", speechFont,
                                canvas.getWidth()*(x+0.04f),canvas.getHeight()*(y+0.08f),
                                Color.BLACK);
                    }
                    break;
                case 1:
                    tutorialModule.setTexture(tipRearTex);
                    if(generalClick != BUTTON_UP) tutorialModule.draw(canvas);
                    break;
                case 2:
                    switch(state) {
                        case 0:
                            tutorialModule.setTexture(tipClassicalTex);
                            tutorialModule.draw(canvas);
                            if (generalClick == BUTTON_UP){
                                generalClick = UNCLICKED;
                                tutorialModule.setTexture(tipMusicTex);
                                state++;
                            }
                            break;
                        case 1:
                            tutorialModule.draw(canvas);
                            if (generalClick == BUTTON_UP){
                                generalClick = UNCLICKED;
                                tutorialModule.setTexture(tipRequestTex);
                                state++;
                            }
                            break;
                        case 2:
                            tutorialModule.draw(canvas);
                            if (generalClick == BUTTON_UP){
                                state++;
                            }
                            break;
                        default:
                            x = 0.33f;
                            y = 0.57f;
                            canvas.draw(tutSpeech, GameCanvas.TextureOrigin.BOTTOM_LEFT, x, y, 0.1f, false, Color.YELLOW);
                            speechFont.setColor(Color.BLACK);
                            canvas.drawText("Make your kids happy\nby feeding them.", speechFont,
                                    canvas.getWidth()*(x+0.04f),canvas.getHeight()*(y+0.08f),
                                    Color.BLACK);
                            x = 0.33f;
                            y = 0.13f;
                            canvas.draw(tutSpeech, GameCanvas.TextureOrigin.BOTTOM_LEFT, x, y, 0.1f, false,  Color.CYAN);
                            speechFont.setColor(Color.BLACK);
                            canvas.drawText("Keep your kids happy\nduring an entire movie.", speechFont,
                                    canvas.getWidth()*(x+0.04f),canvas.getHeight()*(y+0.08f),
                                    Color.BLACK);
                            break;
                    }
                    break;
                case 3: // explain movie
                    tutorialModule.setTexture(tipMusicTex);
                    if(generalClick != BUTTON_UP) tutorialModule.draw(canvas);
                    break;
                default:
                    break;
            }
        }
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
        snackInd.draw(canvas);
        int totalNumSnacks = Math.min(playerInv.getNumSnacks() + numSnacksSelected, SLOT_MAX_QUANTITY);
        if (totalNumSnacks == SLOT_MAX_QUANTITY) {
            displayFont.setColor(Color.RED);
        } else {
            displayFont.setColor(Color.BLACK);
        }
        canvas.drawText("x " + totalNumSnacks, displayFont,
                SCREEN_DIMENSIONS.x*IND_TEXT_X,SCREEN_DIMENSIONS.y*IND_SNACK_Y);


        // Movies
        dvdInd.draw(canvas);
        int totalNumMovies = Math.min(playerInv.getNumMovies() + numMoviesSelected, SLOT_MAX_QUANTITY);
        if (totalNumMovies == SLOT_MAX_QUANTITY) {
            displayFont.setColor(Color.RED);
        } else {
            displayFont.setColor(Color.BLACK);

        }
        canvas.drawText("x " + totalNumMovies, displayFont,
                SCREEN_DIMENSIONS.x*IND_TEXT_X,SCREEN_DIMENSIONS.y*IND_DVD_Y);

    }

    // SCREEN METHODS

    /** Called when this screen becomes the current screen for a game. */
    public void show () {
        active = true;
        bgMusic.play();
    }

    float stamp=0;

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta) {
        if(stamp > 0) stamp += delta;
        if(isTutorial && indexLevel == 2 && noshDialogue != null && stamp > 2) {
            noshDialogue = null;
        }
        if (active) {
            update(delta);
            draw();

            if (readyStatus == BUTTON_UP && listener != null) {
                if(isTutorial && indexLevel == 2 && numMoviesSelected == 0) {
                    if(noshDialogue == null) {
                        noshDialogue = "I want that movie first!";
                        stamp = 0.01f;
                    }
                    readyStatus = UNCLICKED;
                } else {
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
                                default:
                                    break;
                            }
                        }
                    }
                    listener.exitScreen(this,0);
                }
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
        speechFont.getData().setScale(width / (SCREEN_DIMENSIONS.x/displayFont.getScaleX()),
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
        if(generalClick == UNCLICKED) {
            generalClick = BUTTON_DOWN;
            return true;
        }

        // Check if ready button was pressed
        Vector2 mousePos = new Vector2(screenX, screenY);
        if (readyButton.inArea(mousePos)) {
            readyStatus = BUTTON_DOWN;
        }

        // If clicking on an item, change click status to button down
        for (RestStopItem i : items) {
            if (i.inArea(mousePos)) {
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
        if(generalClick == BUTTON_DOWN) {
            generalClick = BUTTON_UP;
            return false;
        }
        if (readyStatus == BUTTON_DOWN) {
            soundController.playClick();
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

                    // update item quantity
                    // case 1: item was selected and is now unselected - decrease items taken
                    // case 2: item was unselected and is now selected - increase items taken
                    //         or display popup if full
                    if (oldToggleState == SELECTED && i.toggleState == UNSELECTED) {
                        i.switchOverlay();
                        numSelected -= 1;

                        // also update the amount for the specific item
                        switch (i.type) {
                            case SNACK:
                                numSnacksSelected -= 1;
                                break;
                            case DVD:
                                numMoviesSelected -= 1;
                                break;
                            default:
                                break;
                        }
                    } else if (oldToggleState == UNSELECTED && i.toggleState == SELECTED) {
                        int quantity = (i.type == Inventory.Item.ItemType.SNACK)
                                ? numSnacksSelected + playerInv.getNumSnacks()
                                : numMoviesSelected + playerInv.getNumMovies();
                        if (quantity == SLOT_MAX_QUANTITY) {
                            i.switchState();
                            switch (i.type) {
                                case SNACK:
                                    if (popupDuration < MAX_POPUP_DURATION) {
                                        boolean oldPopupState = drawSnackPopup;
                                        drawSnackPopup = true;
                                        generalClick = UNCLICKED;

                                        // if just switching from false to true, set duration
                                        if (!oldPopupState) {
                                            popupDuration = 0.0f;
                                        }
                                    }
                                    break;
                                case DVD:
                                    if (popupDuration < MAX_POPUP_DURATION) {
                                        boolean oldPopupState = drawDVDPopup;
                                        drawDVDPopup = true;
                                        generalClick = UNCLICKED;

                                        // if just switching from false to true, set duration
                                        if (!oldPopupState) {
                                            popupDuration = 0.0f;
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }

                        else {
                            i.switchOverlay();
                            numSelected += 1;

                            // also update the amount for the specific item
                            switch (i.type) {
                                case SNACK:
                                    numSnacksSelected += 1;
                                    break;
                                case DVD:
                                    numMoviesSelected += 1;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }

//                // if at max quantity display a warning
//                else if (quantity == SLOT_MAX_QUANTITY
//                        && oldToggleState == UNSELECTED
//                        && numSelected < numCanTake || oldToggleState == SELECTED) {
//
//                }


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
            System.out.println("width: " + Gdx.graphics.getWidth());
            System.out.println("height: " + Gdx.graphics.getHeight());
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
        if(generalClick == BUTTON_UP && readyButton.inArea(new Vector2(screenX,screenY))){
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