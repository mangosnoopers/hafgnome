/*
* MenuController.java
*
* This class creates all the menu screens. They are stored as
* separate variables that can be called by GameMode.
*
* */

package edu.cornell.gdiac.mangosnoops.Menus;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MenuController {
    Menu title;
    Menu levelSelect;
    Menu levelEditor;

    // GRAPHICS AND SOUND RESOURCES
    /** The file for the background image to scroll */
    private static String BKGD_FILE = "images/background.png";

    // Loaded assets
    /** The background image for the game */
    private Texture background;

    public MenuController(AssetManager a) {
        //construct each of the screens lol
    }

}