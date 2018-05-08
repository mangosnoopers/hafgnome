package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class RoadImage extends RoadObject {
    private String name;
    private int miles;
    private Texture texture;

    /**
     * Return the type of RoadObject
     */
    public ObjectType getType() {
        return ObjectType.IMAGE;
    }

    /**
     * Return the name of the RoadObject
     */
    public String getName() { return name; }

    /**
     * Construct a roadside image at the given position.
     * @param x the x-position of the image
     * @param y the y-position of the image
     * @param name the name of the roadside image
     */
    public RoadImage(float x, float y, String name) {
        super();
        position = new Vector2(x,y);
        this.name = name;
        // TODO velocity, moving stuff
    }

    /**
     * Construct a roadside image at the given position associated with a
     * certain mileage number. Used for exit signs.
     *
     * @param x the x-position of the image
     * @param y the y-position of the image
     * @param name the name of the roadside image
     * @param miles the mileage associated with this image
     */
    public RoadImage(float x, float y, String name, int miles) {
        super();
        position = new Vector2(x,y);
        this.name = name;
        this.miles = miles;
    }

//    /** Set the texture of this object */
//    public void setTexture(Texture t) {
//        texture = t;
//    }
//
//    @Override
//    public void draw(GameCanvas canvas) {
//        System.out.println(texture == null);
//        canvas.draw(texture, Color.WHITE, origin.x, origin.y, position.x, position.y,
//                0.0f, 1.0f, 1.0f);
//    }

    public void draw(GameCanvas canvas, Texture t) {
        canvas.draw(t, Color.WHITE, origin.x, origin.y, position.x, position.y,
                0.0f, 1.0f, 1.0f);
    }
}
