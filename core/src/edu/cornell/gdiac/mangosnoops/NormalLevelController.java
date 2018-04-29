package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class NormalLevelController extends GameplayController {

    /** Files */
    private static final String BILLBOARD_END_IS_NEAR = "images/billboards/the_end_is_near.png";

    /** Texture types */
    private Texture billboardEndIsNearTexture;

    public NormalLevelController(GameCanvas canvas, LevelObject level) {
        super(canvas, level.getLevelEndY(), level.getEnemiez(), level.getEvents(), level.getSongs());

    }

    public void start(float x, float y) {
        super.start(x, y);
    }

    public void resolveActions(InputController input, float delta) {
        super.resolveActions(input, delta);
    }

    public void preLoadContent(AssetManager manager, Array<String> assets) {
        super.preLoadContent(manager, assets);
        manager.load(BILLBOARD_END_IS_NEAR, Texture.class);
        assets.add(BILLBOARD_END_IS_NEAR);
    }

    public void loadContent(AssetManager manager) {
        super.loadContent(manager);
        billboardEndIsNearTexture = createTexture(manager, BILLBOARD_END_IS_NEAR);
    }

}
