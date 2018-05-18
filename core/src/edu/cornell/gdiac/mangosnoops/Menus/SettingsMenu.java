package edu.cornell.gdiac.mangosnoops.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.GDXRoot;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;
import edu.cornell.gdiac.mangosnoops.SoundController;

public class SettingsMenu {

    // EXTERNAL VARIABLES (FOR USER-SET SETTINGS) w/ default values
    protected char gnomeSoundSelected = 'A';
    protected boolean showing = false;
    protected float effectsVolume = 1;
    protected float musicVolume = 1;
    protected Vector2 lastResolution;
    protected Vector2 currentResolution;
    private boolean fullScreen = false;
    // INTERNAL VARIABLES (FOR SETTINGS DISPLAY)
    private GDXRoot gdxRoot;
    private Texture backgroundTexture;
    private Texture sliderTabTexture;
    private Texture sliderBarTexture;
    private Texture buttonATexture;
    private Texture buttonBTexture;
    private Texture buttonCTexture;
    private Texture buttonDTexture;
    private Texture buttonBackTexture;
    private Texture selectButtonTexture;
    private Texture selectedButtonTexture;
    private Texture dropDownTexture;
    private BitmapFont displayFont;
    private static int FONT_SIZE = 35;
    private Image background;
    private Image effectsSliderTab;
    private Image musicSliderTab;
    private Image effectsSliderBar;
    private Image musicSliderBar;
    private Image buttonA;
    private Image buttonB;
    private Image buttonC;
    private Image buttonD;
    private Image buttonBack;
    private Image currentResImage;
    private Image hoverOverImage;
    private Image selectButton;
    private Image selectedButton;
    private final Vector2 currentResImagePos = new Vector2(0.505f,0.61f);
    private final float resImageScale = 0.07f;
    private String currentResText;
    private boolean backHover;
    private boolean wasHoveringOnButton = false;
    private boolean isHoveringOnButton = false;
    private Array<String> assets = new Array<String>();
    private final Color buttonTint = new Color(0.5f,0.3f,0.2f,0.5f);
    private final Vector2 sliderBounds = new Vector2(0.44f,0.763f);
    private boolean showSelectBox;
    private Array<Image> resolutionImages;
    private ObjectMap<Image,String> resolutionText;
    private ObjectMap<Image,Vector2> screenResolutions;
    private AssetManager manager;

    // IMAGE RESOURCES
    private static String BACKGROUND_FILE = "images/SettingsMenuAssets/background.png";
    private static String SLIDER_TAB_FILE = "images/SettingsMenuAssets/sliderTab.png";
    private static String SLIDER_BAR_FILE = "images/SettingsMenuAssets/sliderBar.png";
    private static String BUTTON_A_FILE = "images/SettingsMenuAssets/buttonA.png";
    private static String BUTTON_B_FILE = "images/SettingsMenuAssets/buttonB.png";
    private static String BUTTON_C_FILE = "images/SettingsMenuAssets/buttonC.png";
    private static String BUTTON_D_FILE = "images/SettingsMenuAssets/buttonD.png";
    private static String BUTTON_BACK_FILE = "images/SettingsMenuAssets/buttonBack.png";
    private static String SELECT_BUTTON_FILE = "images/SettingsMenuAssets/selectButton.png";
    private static String SELECTED_BUTTON_FILE = "images/SettingsMenuAssets/selectedButton.png";
    private static String DROP_DOWN_FILE = "images/SettingsMenuAssets/dropDownTexture.png";
    private static String FONT_FILE = "fonts/ComicSansBold.ttf";

    /**
     * Preloads the assets for this game.
     * The asset manager for LibGDX is asynchronous.  That means that you
     * tell it what to load and then wait while it loads them.  This is
     * the first step: telling it what to load.
     *
     * @param manager Reference to global asset manager.
     */
    public void preLoadContent(AssetManager manager) {
        manager.load(BACKGROUND_FILE,Texture.class);
        assets.add(BACKGROUND_FILE);
        manager.load(SLIDER_TAB_FILE,Texture.class);
        assets.add(SLIDER_TAB_FILE);
        manager.load(SLIDER_BAR_FILE,Texture.class);
        assets.add(SLIDER_BAR_FILE);
        manager.load(BUTTON_A_FILE,Texture.class);
        assets.add(BUTTON_A_FILE);
        manager.load(BUTTON_B_FILE,Texture.class);
        assets.add(BUTTON_B_FILE);
        manager.load(BUTTON_C_FILE,Texture.class);
        assets.add(BUTTON_C_FILE);
        manager.load(BUTTON_D_FILE,Texture.class);
        assets.add(BUTTON_D_FILE);
        manager.load(BUTTON_BACK_FILE,Texture.class);
        assets.add(BUTTON_BACK_FILE);
        manager.load(SELECT_BUTTON_FILE,Texture.class);
        assets.add(SELECT_BUTTON_FILE);
        manager.load(SELECTED_BUTTON_FILE,Texture.class);
        assets.add(SELECTED_BUTTON_FILE);
        manager.load(DROP_DOWN_FILE,Texture.class);
        assets.add(DROP_DOWN_FILE);
        // Load the font
        FreetypeFontLoader.FreeTypeFontLoaderParameter size2Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size2Params.fontFileName = FONT_FILE;
        size2Params.fontParameters.size = FONT_SIZE;
        size2Params.fontParameters.color = Color.BLACK;
        size2Params.fontParameters.borderColor = Color.BLACK;
        manager.load(FONT_FILE, BitmapFont.class, size2Params);
        assets.add(FONT_FILE);
        this.manager = manager;

    }

    /**
     * Loads the assets for this game.
     * The asset manager for LibGDX is asynchronous.  That means that you
     * tell it what to load and then wait while it loads them.  This is
     * the second step: extracting assets from the manager after it has
     * finished loading them.
     *
     * @param manager Reference to global asset manager.
     */
    public void loadContent(AssetManager manager) {
        // Allocate assets
        if (manager.isLoaded(BACKGROUND_FILE)) {
            backgroundTexture = manager.get(BACKGROUND_FILE, Texture.class);
        }
        if (manager.isLoaded(SLIDER_TAB_FILE)) {
            sliderTabTexture = manager.get(SLIDER_TAB_FILE, Texture.class);
        }
        if (manager.isLoaded(SLIDER_BAR_FILE)) {
            sliderBarTexture = manager.get(SLIDER_BAR_FILE, Texture.class);
        }
        if (manager.isLoaded(BUTTON_A_FILE)) {
            buttonATexture = manager.get(BUTTON_A_FILE, Texture.class);
        }
        if (manager.isLoaded(BUTTON_B_FILE)) {
            buttonBTexture = manager.get(BUTTON_B_FILE, Texture.class);
        }
        if (manager.isLoaded(BUTTON_C_FILE)) {
            buttonCTexture = manager.get(BUTTON_C_FILE, Texture.class);
        }
        if (manager.isLoaded(BUTTON_D_FILE)) {
            buttonDTexture = manager.get(BUTTON_D_FILE, Texture.class);
        }
        if (manager.isLoaded(BUTTON_BACK_FILE)) {
            buttonBackTexture = manager.get(BUTTON_BACK_FILE, Texture.class);
        }
        if (manager.isLoaded(FONT_FILE)) {
            displayFont = manager.get(FONT_FILE,BitmapFont.class);
        }
        if (manager.isLoaded(SELECT_BUTTON_FILE)) {
            selectButtonTexture = manager.get(SELECT_BUTTON_FILE,Texture.class);
        }
        if (manager.isLoaded(SELECTED_BUTTON_FILE)) {
            selectedButtonTexture = manager.get(SELECTED_BUTTON_FILE,Texture.class);
        }
        if (manager.isLoaded(DROP_DOWN_FILE)) {
            dropDownTexture = manager.get(DROP_DOWN_FILE,Texture.class);
        }

        //Buttons and Background
        background = new Image(0.5f, 0.5f, 1, backgroundTexture, GameCanvas.TextureOrigin.MIDDLE);
        effectsSliderTab = new Image(sliderBounds.y, 0.825f, 0.1f, sliderTabTexture, GameCanvas.TextureOrigin.MIDDLE);
        musicSliderTab = new Image(sliderBounds.y, 0.9277f, 0.1f, sliderTabTexture, GameCanvas.TextureOrigin.MIDDLE);
        effectsSliderBar = new Image(0.6f, 0.825f, 0.015f, sliderBarTexture, GameCanvas.TextureOrigin.MIDDLE);
        musicSliderBar = new Image(0.6f, 0.9277f, 0.015f, sliderBarTexture, GameCanvas.TextureOrigin.MIDDLE);
        buttonA = new Image(0.465f, 0.713f, 0.1f, buttonATexture, GameCanvas.TextureOrigin.MIDDLE);
        buttonB = new Image(0.562f, 0.713f, 0.1f, buttonBTexture, GameCanvas.TextureOrigin.MIDDLE);
        buttonC = new Image(0.662f, 0.713f, 0.1f, buttonCTexture, GameCanvas.TextureOrigin.MIDDLE);
        buttonD = new Image(0.762f, 0.713f, 0.1f, buttonDTexture, GameCanvas.TextureOrigin.MIDDLE);
        buttonBack = new Image(0.13f,0.91f,0.1f,buttonBackTexture, GameCanvas.TextureOrigin.MIDDLE);
        selectButton = new Image(0.762f,0.615f,0.05f,selectButtonTexture, GameCanvas.TextureOrigin.MIDDLE);
        selectedButton = new Image(0.762f,0.615f,0.05f,selectedButtonTexture, GameCanvas.TextureOrigin.MIDDLE);

        //DropDown
        resolutionImages = new Array<Image>();
        float resImageOffset = resImageScale;
        resolutionImages.addAll(new Image(currentResImagePos.x,currentResImagePos.y-resImageOffset, resImageScale,dropDownTexture, GameCanvas.TextureOrigin.MIDDLE),
                new Image(currentResImagePos.x,currentResImagePos.y-2*resImageOffset,resImageScale,dropDownTexture, GameCanvas.TextureOrigin.MIDDLE),
                new Image(currentResImagePos.x,currentResImagePos.y-3*resImageOffset,resImageScale,dropDownTexture, GameCanvas.TextureOrigin.MIDDLE),
                new Image(currentResImagePos.x,currentResImagePos.y-4*resImageOffset,resImageScale,dropDownTexture, GameCanvas.TextureOrigin.MIDDLE),
                new Image(currentResImagePos.x,currentResImagePos.y-5*resImageOffset,resImageScale,dropDownTexture, GameCanvas.TextureOrigin.MIDDLE));

        resolutionText = new ObjectMap<Image, String>();
        resolutionText.put(resolutionImages.get(0), "1920 x 1080");
        resolutionText.put(resolutionImages.get(1),"960 x 540");
        resolutionText.put(resolutionImages.get(2),"2880 x 1620");
        resolutionText.put(resolutionImages.get(3),"1152 x 648");
        resolutionText.put(resolutionImages.get(4),"1600 x 900");

        screenResolutions = new ObjectMap<Image, Vector2>();
        screenResolutions.put(resolutionImages.get(0), new Vector2(1920,1080));
        screenResolutions.put(resolutionImages.get(1), new Vector2(960,540));
        screenResolutions.put(resolutionImages.get(2), new Vector2(2880,1620));
        screenResolutions.put(resolutionImages.get(3), new Vector2(1152,648));
        screenResolutions.put(resolutionImages.get(4), new Vector2(1600,900));

        lastResolution = screenResolutions.get(resolutionImages.get(4));
        currentResolution = screenResolutions.get(resolutionImages.get(4));
        currentResImage = new Image(resolutionImages.get(4));
        currentResImage.updateY(currentResImagePos.y);
        currentResText = resolutionText.get(resolutionImages.get(4));
        hoverOverImage = resolutionImages.get(4);
        resizeScreen();
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


    public SettingsMenu(GDXRoot r){
        this.gdxRoot = r;
    }


    public void update(Vector2 in, SoundController soundController) {

        //Click based input

        //Use isButtonPressed() for sliders
        if (Gdx.input.isButtonPressed(Input.Keys.LEFT)) {
            // EFFECTS SLIDER
            if (effectsSliderBar.inArea(in) || effectsSliderTab.inArea(in)) {
                effectsSliderTab.updateX(in.x / ((float) Gdx.graphics.getWidth()));
                if (effectsSliderTab.getPosition().x < sliderBounds.x) {
                    effectsSliderTab.updateX(sliderBounds.x);
                } else if (effectsSliderTab.getPosition().x > sliderBounds.y) {
                    effectsSliderTab.updateX(sliderBounds.y);
                }
                effectsVolume = (effectsSliderTab.getPosition().x - sliderBounds.x) / (sliderBounds.y - sliderBounds.x);
            }
            //VOLUME SLIDER
            else if (musicSliderBar.inArea(in) || musicSliderTab.inArea(in)) {
                musicSliderTab.updateX(in.x / ((float) Gdx.graphics.getWidth()));
                if (musicSliderTab.getPosition().x < sliderBounds.x) {
                    musicSliderTab.updateX(sliderBounds.x);
                } else if (musicSliderTab.getPosition().x > sliderBounds.y) {
                    musicSliderTab.updateX(sliderBounds.y);
                }
                musicVolume = (musicSliderTab.getPosition().x - sliderBounds.x) / (sliderBounds.y - sliderBounds.x);
            }
        }

        // Use justTouched() for buttons
        if(Gdx.input.justTouched()){
            //'A' BUTTON
            if (buttonA.inArea(in)) {
                gnomeSoundSelected = 'A';
                soundController.gnomeDeathSound();
            }
            //'B' BUTTON
            else if (buttonB.inArea(in)) {
                gnomeSoundSelected = 'B';
                soundController.gnomeDeathSound();
            }
            //'C' BUTTON
            else if (buttonC.inArea(in)) {
                gnomeSoundSelected = 'C';
                soundController.gnomeDeathSound();
            }
            //'D' BUTTON
            else if (buttonD.inArea(in)) {
                gnomeSoundSelected = 'D';
                soundController.gnomeDeathSound();
            }
            // 'BACK' BUTTON
            else if (buttonBack.inArea(in)) {
                soundController.playClick();
                showing = false;
            }
            // FULLSCREEN BUTTON
            else if(selectButton.inArea(in)){
                soundController.playClick();
                fullScreen = !fullScreen;
                resizeScreen();;
            }
            // SELECT BOX
            else if (currentResImage.inArea(in)) {
                soundController.playClick();
                showSelectBox = !showSelectBox;
            }
        }
        // Hover based input
        isHoveringOnButton = false;
        // SELECT BOX
        if (showSelectBox) {
            boolean inSB = false;
            for (Image i : resolutionImages) {
                inSB = inSB || i.inArea(in);
                if (i.inArea(in)) {
                    hoverOverImage = i;
                    if (Gdx.input.justTouched()) {
                        soundController.playClick();
                        lastResolution = currentResolution;
                        currentResolution = screenResolutions.get(i);
                        currentResImage = new Image(i);
                        currentResImage.updateY(currentResImagePos.y);
                        currentResText = resolutionText.get(i);
                        resizeScreen();
                        showSelectBox = false;
                    }
                }
            }
        }
        // 'BACK' BUTTON
        if(buttonBack.inArea(in)){
            isHoveringOnButton = true;
            backHover = true;
        } else{
            backHover = false;
        }

        // HOVER SOUND
        if(wasHoveringOnButton!=isHoveringOnButton && isHoveringOnButton){
            soundController.playHoverMouse();
        }
        wasHoveringOnButton = isHoveringOnButton;
    }

    public void draw(GameCanvas canvas){
        background.draw(canvas);
        effectsSliderBar.draw(canvas);
        musicSliderBar.draw(canvas);
        effectsSliderTab.draw(canvas);
        musicSliderTab.draw(canvas);
        buttonA.draw(canvas);
        buttonB.draw(canvas);
        buttonC.draw(canvas);
        buttonD.draw(canvas);
        selectButton.draw(canvas);
        if(fullScreen){
            selectedButton.draw(canvas);
        }
        buttonBack.draw(canvas);
        if(backHover){
            buttonBack.draw(canvas,buttonTint);
        }
        if(showSelectBox) {
            for (Image i : resolutionImages) {
                i.draw(canvas);
                canvas.drawTextCenterOrigin(resolutionText.get(i),
                        displayFont,
                        i.getPosition().x,
                        i.getPosition().y);
                if(hoverOverImage == i){
                    i.draw(canvas,buttonTint);
                }
            }

        }
        currentResImage.draw(canvas);;
        canvas.drawTextCenterOrigin(currentResText,
                displayFont,
                currentResImage.getPosition().x,
                currentResImage.getPosition().y );
        if(gnomeSoundSelected == 'A') {
            buttonA.draw(canvas, buttonTint);
        } else if(gnomeSoundSelected == 'B') {
            buttonB.draw(canvas, buttonTint);
        } else if(gnomeSoundSelected == 'C') {
            buttonC.draw(canvas, buttonTint);
        } else if(gnomeSoundSelected == 'D') {
            buttonD.draw(canvas, buttonTint);
        }
    }

    /** Resize the screen**/
    boolean exitingFullScreen = false;
    Vector2 lastScale = new Vector2(0,0);
    private void resizeScreen(){
        if(fullScreen){
            gdxRoot.setFullScreen(fullScreen);
            displayFont.getData().setScale(1.5f,1.5f );
            exitingFullScreen = true;
        }else {
            gdxRoot.resize((int) currentResolution.x, (int) currentResolution.y);
            Gdx.graphics.setWindowedMode((int) currentResolution.x, (int) currentResolution.y);
            if(exitingFullScreen){
                displayFont.getData().setScale(lastScale.x,lastScale.y );
            } else {
                lastScale.set(displayFont.getScaleX(), displayFont.getScaleY());
            }
            displayFont.getData().setScale((currentResolution.x/lastResolution.x)*displayFont.getScaleX(),
                    (currentResolution.y/lastResolution.y)*displayFont.getScaleY());
            exitingFullScreen = false;
        }
    }

    /**
     * @return the selected gnome sound
     */
    public char getGnomeSoundSelected(){return gnomeSoundSelected;}
    /**
     * @return whether the window is showing or not
     */
    public boolean isShowing() { return showing; }
    /**
     * Sets whether the settings menu is showing or not;
     * @param showing
     */
    public void setShowing(boolean showing){ this.showing = showing; }

    /**
     * Set the fullscreen parameter and act upon it if the user chooses to do so
     * via the keyboard (used in InputController
     * @param fullScreen
     */
    public void setFullScreen(boolean fullScreen){
        this.fullScreen = fullScreen;
        resizeScreen();
    }

    /**
     * @return whether the screen is fullscreened or not
     */
    public boolean isFullScreen() { return fullScreen; }

    /**
     * @return Volume of sound effects
     */
    public float getEffectsVolume() { return effectsVolume; }
    /**
     * @return Volume of music
     */
    public float getMusicVolume() { return musicVolume; }

}