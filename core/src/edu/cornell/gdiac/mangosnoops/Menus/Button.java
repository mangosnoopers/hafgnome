package edu.cornell.gdiac.mangosnoops.Menus;

import com.badlogic.gdx.graphics.Texture;
import edu.cornell.gdiac.mangosnoops.Image;

public class Button extends Image {

    boolean check;
    public Button(float x, float y, float r, Texture t) {
        super(x,y,r,t);
        check = false;
    }


}
