package edu.cornell.gdiac.mangosnoops.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.ScreenListener;

public class LevelMenuMode implements Screen, InputProcessor {
    // ASSETS
    private Array<Image> staticImages;
    private static final String BACK_BUTTON_FILE = "images/SettingsMenuAssets/buttonBack.png";
    private static final String GO_BUTTON_FILE = "images/levelSelectAssets/goButton.png";
    private static final String FINAL_LEVEL_MARKER_FILE = "images/levelSelectAssets/finalLevelMarker.png";
    private static final String LEVEL_MARKER_FILE = "images/levelSelectAssets/levelMarker.png";
    private static final String SELECTED_LEVEL_MARKER_FILE = "images/levelSelectAssets/selectedLevelMarker.png";
    private static final String MAP_FILE = "images/levelSelectAssets/usMap.png";
    private static final String PATH_FILE = "images/levelSelectAssets/levelsPath.png";
    private Texture backButtonTex;
    private Texture goButtonTex;
    private Texture finalLevelMarkerTex;
    private Texture levelMarkerTex;
    private Texture selectedLevelMarkerTex;
    private Texture mapTex;
    private Texture pathTex;
    /** An array of hover textures for each level. Index 0 is the tutorial. */
    private static final Texture[] HOVER_TEXS = new Texture[] {
            new Texture("images/levelSelectAssets/levelHoverable.png"), // tutorial
            new Texture("images/levelSelectAssets/levelHoverable.png"), // burbs level 1
            new Texture("images/levelSelectAssets/levelHoverable.png"), // burbs level 2
            new Texture("images/levelSelectAssets/levelHoverable.png"), // burbs level 3
            new Texture("images/levelSelectAssets/levelHoverable.png"), // highway level 1
            new Texture("images/levelSelectAssets/levelHoverable.png"), // highway level 2
            new Texture("images/levelSelectAssets/levelHoverable.png"), // highway level 3
            new Texture("images/levelSelectAssets/levelHoverable.png"), // midwest level 1
            new Texture("images/levelSelectAssets/levelHoverable.png"), // midwest level 2
            new Texture("images/levelSelectAssets/levelHoverable.png"), // midwest level 3
            new Texture("images/levelSelectAssets/levelHoverable.png"), // west level 1
            new Texture("images/levelSelectAssets/levelHoverable.png"), // west level 2
            new Texture("images/levelSelectAssets/levelHoverable.png"), // west level 3

    };

    // BUTTONS
    /** 0: Unclicked, 1: Button Down, 2: Button Up */
    private static final int UNCLICKED = 0;
    private static final int BUTTON_DOWN = 1;
    private static final int BUTTON_UP = 2;
    /** Back button is pressed when the player wants to return to the start menu */
    private int backStatus;
    private Image backButton;

    // LEVEL NODES
    /** The level nodes */
    private Array<LevelNode> levelNodes;
    /** The level node the player is currently pressing on */
    private LevelNode activeNode;
    /** Constants for whether an item is selected or unselected */
    private static final int UNSELECTED = 0;
    private static final int SELECTED = 1;

    // DOODADS FOR EXITING THIS MODE
    /** True if the next screen to load is playing mode, false if loading start menu */
    private boolean loadPlaying;
    /** The index of the next level to load */
    private int nextLevelIndex;
    /** True if the level should be loaded from the preset LEVELS array, or false if loading from a save */
    private boolean loadSavedLevel;

    // OTHER DOODADS
    /** AssetManager to be loading in the background */
    private AssetManager manager;
    /** Reference to GameCanvas created by the root */
    private GameCanvas canvas;
    /** SoundController for playing sounds and music **/
    private SoundController soundController;
    /** Listener that will update the player mode when we are done */
    private ScreenListener listener;
    /** Whether or not this player mode is still active */
    private boolean active;
    /** The number of tutorials in this game */
    private int numTutorials;
    /** The number of levels the player has gotten through so far (ie number of saved level files) */
    private int numSaved;
    /** True if a level was chosen and we are leaving to it **/
    private boolean exitingToLevel;


    // DRAWING
    /** Dimensions of the screen **/
    private static Vector2 SCREEN_DIMENSIONS;
    /** Size of the level markers */
    private static final float LEVEL_MARKER_SIZE = 0.03f;
    /** Relative scale of the back and go buttons */
    private static final float BUTTON_SCALE = 0.07f;
    /** Offset of go button relative from its parent level node */
    private static final float GO_BUTTON_OFFX = 0.065f;
    private static final float GO_BUTTON_OFFY = 0.25f;
    /** Offset of selected level marker from its parent node */
    private static final float ACTIVE_IND_OFFY = 0.04f;

    /**
     * Returns true if should load playing mode next, false if start menu
     */
    public boolean loadPlaying() { return loadPlaying; }

    /**
     * Returns true if the next level is being loaded from a saved level, or
     * false if the next level should be loaded from the predetermined LEVELS
     * array in GDXRoot
     */
    public boolean loadSavedLevel() { return loadSavedLevel; }

    /**
     * Returns the index of the next level to load, or -1 if returning to the
     * start screen.
     */
    public int getNextLevelIndex() {
        if (loadPlaying)
            return nextLevelIndex;
        return -1;
    }

    public LevelMenuMode(GameCanvas canvas, AssetManager manager, SoundController soundController, int numTutorials, int numSaved) {
        this.canvas = canvas;
        this.manager = manager;
        this.soundController = soundController;
        this.numTutorials = numTutorials;
        this.numSaved = numSaved;
        SCREEN_DIMENSIONS = new Vector2(canvas.getWidth(),canvas.getHeight());
        active = true;

        // Create textures
        initTextures();

        // Create images
        staticImages = new Array<Image>();
        initStaticImages();

        // Create the level nodes
        levelNodes = new Array<LevelNode>();
        generateNodes();

    }

    /** Initialize all textures */
    private void initTextures() {
        backButtonTex = new Texture(BACK_BUTTON_FILE);
        goButtonTex = new Texture(GO_BUTTON_FILE);
        finalLevelMarkerTex = new Texture(FINAL_LEVEL_MARKER_FILE);
        levelMarkerTex = new Texture(LEVEL_MARKER_FILE);
        selectedLevelMarkerTex = new Texture(SELECTED_LEVEL_MARKER_FILE);
        mapTex = new Texture(MAP_FILE);
        pathTex = new Texture(PATH_FILE);
    }

    /** Initialize all static images */
    private void initStaticImages() {
        // background
        Image bg = new Image(0.5f,0.5f,1.0f, mapTex, GameCanvas.TextureOrigin.MIDDLE);
        staticImages.add(bg);

        // path
        Image path = new Image(0.25f,0.75f,0.215f, pathTex, GameCanvas.TextureOrigin.TOP_LEFT);
        staticImages.add(path);

        // back button
        backButton = new Image(0.02f,0.02f,BUTTON_SCALE, backButtonTex, GameCanvas.TextureOrigin.BOTTOM_LEFT);
        staticImages.add(backButton);

        // draw a star
        Image star = new Image(0.25f, 0.75f, LEVEL_MARKER_SIZE*1.5f, finalLevelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE);
        staticImages.add(star);
    }

    /** Generate the level nodes and their corresponding hoverable windows */
    private void generateNodes() {
        // tutorial - loads into the first tutorial
        LevelNode tutorialDot = new LevelNode(0.835f, 0.537f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, false, 0);
        levelNodes.add(tutorialDot);

        // the burbs
        LevelNode burbs0 = new LevelNode(0.805f, 0.57f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 0);
        levelNodes.add(burbs0);

        LevelNode burbs1 = new LevelNode(0.775f, 0.58f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 1);
        levelNodes.add(burbs1);

        LevelNode burbs2 = new LevelNode(0.75f, 0.585f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 2);
        levelNodes.add(burbs2);

        // highway
        LevelNode hw1 = new LevelNode(0.7f, 0.58f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 3);
        levelNodes.add(hw1);

        LevelNode hw2 = new LevelNode(0.65f, 0.575f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 4);
        levelNodes.add(hw2);

        LevelNode hw3 = new LevelNode(0.6f, 0.57f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 5);
        levelNodes.add(hw3);

        // midwest
        LevelNode mw1 = new LevelNode(0.54f, 0.583f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 6);
        levelNodes.add(mw1);

        LevelNode mw2 = new LevelNode(0.498f, 0.615f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 7);
        levelNodes.add(mw2);

        LevelNode mw3 = new LevelNode(0.425f, 0.613f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 8);
        levelNodes.add(mw3);

        // colorado (west)
        LevelNode w1 = new LevelNode(0.365f, 0.635f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 9);
        levelNodes.add(w1);

        LevelNode w2 = new LevelNode(0.305f, 0.67f, LEVEL_MARKER_SIZE, levelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 10);
        levelNodes.add(w2);

        // last level
        LevelNode w3 = new LevelNode(0.25f, 0.75f, LEVEL_MARKER_SIZE * 1.5f, finalLevelMarkerTex,
                GameCanvas.TextureOrigin.MIDDLE, true, 11);
        levelNodes.add(w3);

    }

    /**
     * Draws a line between each of the level nodes.
     */
    public void drawLines() {
        for (int i = 0; i < levelNodes.size - 1; i++) {
            Vector2 currPos = levelNodes.get(i).getPosition();
            Vector2 nextPos = levelNodes.get(i+1).getPosition();
            Vector2 currAbs = new Vector2(currPos.x*SCREEN_DIMENSIONS.x, currPos.y*SCREEN_DIMENSIONS.y);
            Vector2 nextAbs = new Vector2(nextPos.x*SCREEN_DIMENSIONS.x, nextPos.y*SCREEN_DIMENSIONS.y);
            canvas.drawLine(currAbs,nextAbs);
        }
    }

    /**
     * Sets the ScreenListener for this mode
     *
     * The ScreenListener will respond to requests to quit.
     */
    public void setScreenListener(ScreenListener listener) {
        this.listener = listener;
        soundController.playLevelSelectSong(true);
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
    private float fadeOut=0;
    private void draw() {
        // Draw images in the images array
        canvas.beginHUDDrawing();
        for (Image i : staticImages) {
            i.draw(canvas);
        }
        if(exitingToLevel){
            fadeOut+=0.01;
            if(fadeOut >= 1) fadeOut = 1;
            canvas.drawFade(fadeOut);
        }
        canvas.endHUDDrawing();


        // Draw lines between nodes after static images but before nodes themselves
//        drawLines();

        canvas.beginHUDDrawing();
        // Draw the level nodes - only drawn the ones that have been encountered in-game
        // Draws number saved + 1 more to account for the tutorial node
        for (int i = 0; i <= numSaved; i++) {
            levelNodes.get(i).draw(canvas);
        }

        // Draw the active node's hover and current level selector if the active node is not null
        if (activeNode != null) {
            activeNode.drawHover();
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
    float delay=0;
    public void render (float delta) {
        if (active) {
            update(delta);
            draw();

            // pressed go on the active level hover - start the game
            if (activeNode != null && activeNode.goStatus == BUTTON_UP && listener != null) {
                if (!exitingToLevel) {
                    soundController.playLevelSelectSong(false);
                    soundController.playCarIgnition();
                    exitingToLevel = true;
                }
                if (delay < 2.4) {
                    delay += delta;
                } else {
                    exitingToLevel = false;
                    loadPlaying = true;
                    loadSavedLevel = activeNode.savedLevel;
                    nextLevelIndex = activeNode.levelIndex;
                    listener.exitScreen(this, 0);
                }
            }

            // pressed back - return to start menu
            else if (backStatus == BUTTON_UP && listener != null) {
//                System.out.println("going back now");
                loadPlaying = false;
                soundController.playLevelSelectSong(false);
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
        if (activeNode != null && activeNode.goStatus == BUTTON_UP) {
            return true;
        }

        if (backStatus == BUTTON_UP) {
            return true;
        }

        // Check if back button was pressed
        Vector2 mousePos = new Vector2(screenX, screenY);
        if (backButton.inArea(mousePos)) {
            backStatus = BUTTON_DOWN;
        }

        // If clicking on a node, change click status to button down
        for (LevelNode l : levelNodes) {
            if (l.inArea(mousePos)) {
                l.clickStatus = BUTTON_DOWN;
            }
        }

        // If a hover is active and its go button was pressed, change its status to button down
        if (activeNode != null && activeNode.goButton.inArea(mousePos)) {
            activeNode.goStatus = BUTTON_DOWN;
        }

        return false;
    }

    /** Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        // check buttons
        if (activeNode != null && activeNode.goStatus == BUTTON_DOWN) {
            soundController.playClick();
            activeNode.goStatus = BUTTON_UP;
            return false;
        }

        if (backStatus == BUTTON_DOWN) {
            soundController.playClick();
            backStatus = BUTTON_UP;
            return false;
        }

        // check level nodes
        for (LevelNode l : levelNodes) {
            if (l.clickStatus == BUTTON_DOWN) {
                soundController.playClick();
                l.clickStatus = BUTTON_UP;

                // if clicking on same level node, set active node to null
                if (activeNode != null && activeNode == l) {
                    activeNode = null;
                }

                // else change active node
                else {
                    activeNode = l;
                }

            }

//            return false;
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

    // INNER CLASSES

    private class LevelNode extends Image {
        /** The index of the level corresponding to this node */
        private int levelIndex;
        /** True if this index is stored in the saved levels array, false if in predetermined level array
         * (ie false if tutorial level) */
        private boolean savedLevel;
        /** The click status of a rest stop item - unclicked, touch down, or touch up */
        private int clickStatus;

        /** The hover for this node.
         * Which asset is chosen will be levelIndex + 1 in the hover array for saved levels,
         * and 0 for non-saved levels (ie the tutorial level) */
        private Image hover;
        /** Go button is clicked when the player wants to load a level */
        private int goStatus;
        private Image goButton;
        /** Display marker when this node is clicked on */
        private Image activeMarker;

        private LevelNode(float x, float y, float relSca, Texture t, GameCanvas.TextureOrigin o, boolean savedLevel, int levelIndex) {
            super(x,y,relSca,t,o);
            this.levelIndex = levelIndex;
            this.savedLevel = savedLevel;
            clickStatus = UNCLICKED;
            goStatus = UNCLICKED;

            // create the hover
            createHover();
        }

        /**
         * Creates the hover image for this level node.
         */
        public void createHover() {
            // for normal levels pick texture at index levelIndex + 1 in hover assets array
            // for tutorial pick texture at index 0
            Texture hovTex = HOVER_TEXS[levelIndex + 1];
            if (!savedLevel) {
                hovTex = HOVER_TEXS[0];
            }

            hover = new Image(position.x, position.y, 0.3f, hovTex, GameCanvas.TextureOrigin.TOP_RIGHT);
            goButton = new Image(position.x - GO_BUTTON_OFFX ,position.y - GO_BUTTON_OFFY, BUTTON_SCALE,
                    goButtonTex, GameCanvas.TextureOrigin.MIDDLE);
            activeMarker = new Image(position.x, position.y + ACTIVE_IND_OFFY, LEVEL_MARKER_SIZE * 2.0f,
                    selectedLevelMarkerTex, GameCanvas.TextureOrigin.MIDDLE);
        }

        /**
         * Draws the window that describes thi4s level.
         * Which hover asset is chosen is dependent on the level number.
         * 0 = tutorial
         */
        private void drawHover() {
            if (clickStatus == BUTTON_UP) {
                hover.draw(canvas);
                goButton.draw(canvas);
                activeMarker.draw(canvas);
            }
        }

    }

}
