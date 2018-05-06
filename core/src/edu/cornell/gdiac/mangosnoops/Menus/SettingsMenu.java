package edu.cornell.gdiac.mangosnoops.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;
import edu.cornell.gdiac.mangosnoops.SoundController;

public class SettingsMenu {

    // EXTERNAL VARIABLES (FOR USER-SET SETTINGS)
    protected char gnomeSoundSelected;
    protected boolean showing;
    protected float effectsVolume = 1;
    protected float musicVolume = 1;

    // INTERNAL VARIABLES (FOR SETTINGS DISPLAY)
    private Texture backgroundTexture;
    private Texture sliderTabTexture;
    private Texture sliderBarTexture;
    private Texture buttonATexture;
    private Texture buttonBTexture;
    private Texture buttonCTexture;
    private Texture buttonDTexture;
    private Texture buttonBackTexture;
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
    private boolean backPressed;
    private Array<String> assets = new Array<String>();
    private final Color buttonTint = new Color(0.5f,0.3f,0.2f,0.5f);
    private final Vector2 sliderBounds = new Vector2(0.44f,0.763f);




    // IMAGE RESOURCES
    private static String BACKGROUND_FILE = "images/SettingsMenuAssets/background.png";
    private static String SLIDER_TAB_FILE = "images/SettingsMenuAssets/sliderTab.png";
    private static String SLIDER_BAR_FILE = "images/SettingsMenuAssets/sliderBar.png";
    private static String BUTTON_A_FILE = "images/SettingsMenuAssets/buttonA.png";
    private static String BUTTON_B_FILE = "images/SettingsMenuAssets/buttonB.png";
    private static String BUTTON_C_FILE = "images/SettingsMenuAssets/buttonC.png";
    private static String BUTTON_D_FILE = "images/SettingsMenuAssets/buttonD.png";
    private static String BUTTON_BACK_FILE = "images/SettingsMenuAssets/buttonBack.png";

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


        background = new Image(0.5f, 0.5f, 1, backgroundTexture, GameCanvas.TextureOrigin.MIDDLE);
        effectsSliderTab = new Image(sliderBounds.y, 0.825f, 0.1f, sliderTabTexture, GameCanvas.TextureOrigin.MIDDLE);
        musicSliderTab = new Image(sliderBounds.y, 0.9277f, 0.1f, sliderTabTexture, GameCanvas.TextureOrigin.MIDDLE);
        effectsSliderBar = new Image(0.6f, 0.825f, 0.015f, sliderBarTexture, GameCanvas.TextureOrigin.MIDDLE);
        musicSliderBar = new Image(0.6f, 0.9277f, 0.015f, sliderBarTexture, GameCanvas.TextureOrigin.MIDDLE);
        buttonA = new Image(0.465f, 0.713f, 0.1f, buttonATexture, GameCanvas.TextureOrigin.MIDDLE);
        buttonB = new Image(0.562f, 0.713f, 0.1f, buttonBTexture, GameCanvas.TextureOrigin.MIDDLE);
        buttonC = new Image(0.662f, 0.713f, 0.1f, buttonCTexture, GameCanvas.TextureOrigin.MIDDLE);
        buttonD = new Image(0.762f, 0.713f, 0.1f, buttonDTexture, GameCanvas.TextureOrigin.MIDDLE);
        buttonBack = new Image(0.16f,0.91f,0.1f,buttonBackTexture, GameCanvas.TextureOrigin.MIDDLE);

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


    public SettingsMenu(){
    }


    public void update(Vector2 in, SoundController soundController){
        if(in==null) return;
        // EFFECTS SLIDER
        if(effectsSliderBar.inArea(in) || effectsSliderTab.inArea(in)){
            effectsSliderTab.updateX(in.x/((float)Gdx.graphics.getWidth()));
            if(effectsSliderTab.getPosition().x < sliderBounds.x){
                effectsSliderTab.updateX(sliderBounds.x);
            } else if(effectsSliderTab.getPosition().x > sliderBounds.y){
                effectsSliderTab.updateX(sliderBounds.y);
            }
        }
        //VOLUME SLIDER
        else if(musicSliderBar.inArea(in) || musicSliderTab.inArea(in)){
            musicSliderTab.updateX(in.x/((float)Gdx.graphics.getWidth()));
            if(musicSliderTab.getPosition().x < sliderBounds.x){
                musicSliderTab.updateX(sliderBounds.x);
            } else if(musicSliderTab.getPosition().x > sliderBounds.y){
                musicSliderTab.updateX(sliderBounds.y);
            }
        }
        //'A' BUTTON
        else if(buttonA.inArea(in)){
            gnomeSoundSelected = 'A';
            soundController.gnomeDeathSound();
        }
        //'B' BUTTON
        else if(buttonB.inArea(in)){
            gnomeSoundSelected = 'B';
            soundController.gnomeDeathSound();
        }
        //'C' BUTTON
        else if(buttonC.inArea(in)){
            gnomeSoundSelected = 'C';
            soundController.gnomeDeathSound();
        }
        //'D' BUTTON
        else if(buttonD.inArea(in)){
            gnomeSoundSelected = 'D';
            soundController.gnomeDeathSound();
        }
        // 'BACK' BUTTON
        else if(buttonBack.inArea(in)){
            showing = false;
        }
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
        buttonBack.draw(canvas);
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
     * @return Volume of sound effects
     */
    public float getEffectsVolume() { return effectsVolume; }
    /**
     * @return Volume of music
     */
    public float getMusicVolume() { return musicVolume; }
}
