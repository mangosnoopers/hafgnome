package edu.cornell.gdiac.mangosnoops.roadentity;

import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.RoadObject;

public class Flame extends Enemy {

    public Flame(float x, float y) {
        super(x, y, RoadObject.ObjectType.FLAME);
        hoverDistance = 4.3f;
        enemyWidth = 0.07f;
        enemyHeight = 0.04f;
    }

    public Flame(Enemy e) {
        super(e);
        hoverDistance = 4.3f;
        enemyWidth = 0.07f;
        enemyHeight = 0.04f;
    }


}
