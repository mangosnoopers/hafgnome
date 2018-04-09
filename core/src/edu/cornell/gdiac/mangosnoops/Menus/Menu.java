package edu.cornell.gdiac.mangosnoops.Menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;

public abstract class Menu {

    /** Non-clickable objects on screen. */
    private Image[] images;
    /** Clickable objects on screen. */
    private Button[] buttons;
    /** Background of menu */
    private Texture background;

    public Menu(Image[] i, Button[] b) {
        images = i;
        buttons = b;
    }

    /**
     * Returns the index of buttons of a button that has been clicked.
     * Returns -1 if none of the buttons have been clickedl
     * */
    public int checkClicked() {
        for(int i = 0; i < buttons.length; i++) {
            if(buttons[i].check) return i;
        }
        return -1;
    }

    public void draw(GameCanvas canvas) {
        canvas.clearScreen();
        canvas.drawBackground(background, 0, 0);
        for(int i = 0; i < buttons.length; i++) {
            if(buttons[i].check) buttons[i].draw(canvas, Color.BLUE); //temp -- tint if clicked
            else buttons[i].draw(canvas, Color.WHITE);
        }
        for(int i = 0; i < images.length; i++) {
            images[i].draw(canvas, Color.WHITE);
        }
    }

}
