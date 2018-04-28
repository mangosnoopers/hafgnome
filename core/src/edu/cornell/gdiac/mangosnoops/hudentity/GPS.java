package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;

public class GPS extends Image {

    public GPS() {

    }

    public GPS(float x, float y, float scale, Texture tex) {
        super(x, y, scale, tex);
    }

    public void update() {

    }
    public void draw(GameCanvas canvas, BitmapFont displayFont) {
        canvas.drawTextCenterOrigin("You haven't bought a GPS yet.", displayFont, 0.85f, 0.24f);
    }
}
