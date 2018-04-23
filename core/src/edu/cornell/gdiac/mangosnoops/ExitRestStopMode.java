package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import edu.cornell.gdiac.util.ScreenListener;

public class ExitRestStopMode implements Screen {
    private static final String BACKGROUND_FILE = "images/sky.png";
    private Texture background;
    private GameCanvas canvas;
    private AssetManager manager;
    private int budget;
    private boolean active;
    private ScreenListener listener;
    private boolean ready;

    public ExitRestStopMode(GameCanvas canvas, AssetManager manager, int millis) {
        background = new Texture(BACKGROUND_FILE);
        this.canvas = canvas;
        this.manager = manager;
        budget = millis;
        active = true;
    }

    public void dispose() {
        background.dispose();
        background = null;
    }

    private void update(float delta) {
        manager.update(budget);
        if (budget >= 1.0f) {
            ready = true;
        }
    }

    private void draw() {
        canvas.beginHUDDrawing();
        canvas.draw(background,0.0f,0.0f);
        canvas.endHUDDrawing();
    }

    public void render(float delta) {
        if (active) {
            update(delta);
            draw();

            if (ready && listener != null) {
                listener.exitScreen(this,0);
            }
        }

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
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    /**
     * Called when the Screen is paused.
     *
     * This is usually when it's not active or visible on screen. An Application is
     * also paused before it is destroyed.
     */
    public void pause() {
        // TODO Auto-generated method stub

    }

    /**
     * Called when the Screen is resumed from a paused state.
     *
     * This is usually when it regains focus.
     */
    public void resume() {
        // TODO Auto-generated method stub

    }

    /**
     * Called when this screen becomes the current screen for a Game.
     */
    public void show() {
        // Useless if called in outside animation loop
        active = true;
    }

    /**
     * Called when this screen is no longer the current screen for a Game.
     */
    public void hide() {
        // Useless if called in outside animation loop
        active = false;
    }

    /**
     * Sets the ScreenListener for this mode
     *
     * The ScreenListener will respond to requests to quit.
     */
    public void setScreenListener(ScreenListener listener) {
        this.listener = listener;
    }
}
