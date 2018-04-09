package edu.cornell.gdiac.mangosnoops.Menus;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;

public class Button extends Image {

    /**  Currently check is false until it is clicked, then will stay true.*/
    boolean check;
    public Button(float x, float y, float r, Texture t) {
        super(x,y,r,t);
        check = false;
    }

    public void checkClick(Vector2 p, GameCanvas canvas) {
        if(inArea(p, canvas)) check = true;
    }

}