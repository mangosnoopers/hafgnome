package edu.cornell.gdiac.mangosnoops.items;
import com.badlogic.gdx.graphics.Texture;
import edu.cornell.gdiac.mangosnoops.Image;

// ITEM INNER CLASS
public class Item extends Image {
    /** The type of item */
    private ItemType itemType;
    /** The name of this item */
    private String name;

    /** An enum for item types */
    public enum ItemType {
        DVD, SNACK
    }

    /**
     * Create a new item.
     * @param x The relative x-coordinate
     * @param y The relative y-coordinate
     * @param relSca The relative scale of the object
     * @param t The texture of the object
     * @param type The type of object
     * @param n The name of the object
     */
    public Item(float x, float y, float relSca, Texture t, ItemType type, String n) {
        super(x, y, relSca, t);
        itemType = type;
        name = n;
    }

    /** Return the type of this item */
    public ItemType getItemType() {
        return itemType;
    }

    /** Return this item's texture */
    public Texture getTexture() {
        return texture;
    }

    @Override
    /** Print the item's type */
    public String toString() {
        return itemType.toString();
    }

}