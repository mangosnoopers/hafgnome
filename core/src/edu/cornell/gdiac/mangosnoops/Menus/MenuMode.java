package edu.cornell.gdiac.mangosnoops.Menus;

import com.badlogic.gdx.graphics.Texture;
import edu.cornell.gdiac.mangosnoops.GameCanvas;

public abstract class MenuMode {

    private Button[] buttons;
    private Texture background;

    public MenuMode(Button[] b) {
        buttons = b;
    }

    public void draw(GameCanvas canvas) {
        
    }

}
