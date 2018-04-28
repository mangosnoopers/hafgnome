package edu.cornell.gdiac.mangosnoops;

import java.lang.reflect.Array;

public class NormalLevelController extends GameplayController {


    public NormalLevelController(GameCanvas canvas, LevelObject level) {
        super(canvas, level.getLevelEndY(), level.getEnemiez(), level.getEvents(), level.getSongs());
    }


}
