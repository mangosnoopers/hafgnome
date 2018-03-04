package edu.cornell.gdiac.mangosnoops.entity;

import edu.cornell.gdiac.mangosnoops.*;
import edu.cornell.gdiac.util.*;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

public class Wheel {

    // Coordinates of wheel center]
    private static Vector2 center;

    // Dimensions of wheelzone
    private static Vector2 wheelZone;

    // Vector from center of wheel to its outer radius
    private static Vector2 outerRadius;

    // Vector from center of wheel to its inner radius
    private static Vector2 innerRadius;

    // Current angle of the wheel
    private float ang;

    // The Car that this wheel belongs to
    private Car car;

    //Constructor
    public Wheel(Car car){
        center = new Vector2();
        wheelZone = new Vector2();
        outerRadius = new Vector2();
        innerRadius = new Vector2();
        this.car = car;
    }

    //Rotate wheel by an angle theta
    public void rotate(float theta){return;}

    public void snapBack(float theta){return;}

    public void drawWheel(GameCanvas canvas){

    }
}
