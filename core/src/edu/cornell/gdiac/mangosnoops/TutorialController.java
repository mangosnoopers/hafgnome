package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;
import edu.cornell.gdiac.mangosnoops.roadentity.Enemy;

public class TutorialController extends GameplayController {

    private static final String TUT_KEYS_FILE = "images/Tutorial/tut_keys.png";

    private Texture tutKeysTexture;

    private Image tutKeys;

    public TutorialController(GameCanvas canvas) {
        super(canvas, 500, new Array<Enemy>(), new Array<Event>(), new ObjectMap<String, Radio.Genre>());

    }

    public void start(float x, float y) {
        super.start(x, y);
        tutKeys = new Image(0.5f, 0.5f, 0.12f, tutKeysTexture);
    }

    public void preLoadContent(AssetManager manager, Array<String> assets) {
        super.preLoadContent(manager, assets);
        manager.load(TUT_KEYS_FILE, Texture.class);
        assets.add(TUT_KEYS_FILE);
    }

    public void loadContent(AssetManager manager) {
        super.loadContent(manager);
        tutKeysTexture = createTexture(manager, TUT_KEYS_FILE);
    }

    public void resolveActions(InputController input, float delta) {
        super.resolveActions(input, delta);

    }

    public void draw(GameCanvas canvas) {
        super.draw(canvas);
        tutKeys.draw(canvas);
    }


}
