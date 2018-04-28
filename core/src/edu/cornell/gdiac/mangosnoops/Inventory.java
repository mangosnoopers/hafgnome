package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.items.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Inventory {
    /** Stuff for taking items out of the inventory box */
    private static Item itemInHand;
    private static Vector2 itemInHandPosition;
    private Slot lastSlotTakenFromState;
    private Slot lastSlotTakenFrom;

    /** The filepath to the JSON containing all item data */
    private static final String FILE = "items.json";
    /** A JSON array of all the item data */
    private JSONArray items;
    /** A mapping of item names to textures */
    private ObjectMap<String,Texture> textures;
    /** All of the slots in the inventory */
    private Array<Slot> slots;

    /** Constants for drawing inventory */
    private static final float Y_BOTTOM = 0.0366f;
    private static final float SLOT_WIDTH = 0.146f;
    private static final float SLOT_HEIGHT = 0.15f;
    private static final int NUM_SLOTS = 2;

    /** Constants for drawing items */
    /** The relative y-coordinate of the snack slot */
    private static final float SNACK_SLOT_Y = Y_BOTTOM + SLOT_HEIGHT;
    /** The relative y-coordinate of the DVD/book slot */
    private static final float DVDBOOK_SLOT_Y = Y_BOTTOM;
    /** The relative x-coordinate of the leftmost item of a slot */
    private static final float SLOT_LEFTMOST_X = 0.4756f;
    /** Offset between each item drawn within a slot */
    private static final float ITEM_OFFSET = 0.05f;
    /** Relative scales of items */
    private static final float snackRelSca = 0.135f;
    private static final float bookRelSca = 0.12f;
    private static final float dvdRelSca = 0.12f;

    /** Constants for item names */
    private static final String MANGO = "mango";
    private static final String CHIPS = "chips";
    private static final String GNOME_COUNTRY = "Gnome Country for Old Men";
    private static final String SILENCE_GNOMES = "Silence of the Gnomes";
    private static final String GNOME_STARS = "The Gnome in Our Stars";
    private static final String TEST_PREP = "Test Prep";
    private static final String SCI_FI = "Sci Fi";

    /** The snack slot and its index in the slot array */
    private Slot snackSlot;
    private static final int SNACK_SLOT_IDX = 0;
    /** The DVD/book slot and its index in the slot array */
    private Slot dvdBookSlot;
    private static final int DVDBOOK_SLOT_IDX = 1;

    /** Create an empty inventory with a constant number of slots. */
    public Inventory(ObjectMap<String,Texture> ts) {
        // read the inventory data from the JSON
        readJSON();
        textures = ts;

        // initialize the two slots
        dvdBookSlot = new Slot();
        snackSlot = new Slot();

        // add slots to the slot array
        slots = new Array<Slot>(NUM_SLOTS);
        slots.add(snackSlot); slots.add(dvdBookSlot);
    }

    /** Create an inventory based on a given array of slots. */
    public Inventory(ObjectMap<String,Texture> ts, Array<Slot> s) {
        readJSON();
        textures = ts;
        slots = s;
    }

    /** Read data from the JSON file containing all item information. */
    public void readJSON() {
        try {
            Scanner scanner = new Scanner(new File(FILE));
            JSONObject json = new JSONObject(scanner.useDelimiter("\\A").next());
            scanner.close();
            items = json.getJSONArray("items");
        } catch (IOException e) {
            System.out.println("Error parsing item JSON");
        }

    }

    /** Empties the inventory. */
    public void reset() {
        itemInHand = null;
        itemInHandPosition = null;
        lastSlotTakenFromState = null;
        lastSlotTakenFrom = null;
        slots = new Array<Slot>(slots.size);
    }

    /** Return a copy of this inventory. */
    public Inventory copy() {
        Array<Slot> newSlots = new Array<Slot>();
        for (Slot s : slots) {
            newSlots.add(s.copy());
        }
        return new Inventory(textures, newSlots);
    }

    /**
     * Add an item to the snack slot.
     * @param n the name of the item
     */
    public void addSnack(String n) {
        Snack s = new Snack(0,0,0, textures.get(n), Item.ItemType.SNACK, n);
        snackSlot.addItem(s);
    }

    /**
     * Add a movie to the DVD/book slot.
     * @param n the name of the item
     */
    public void addMovie(String n) {
        JSONObject jsonM = items.getJSONObject(getIndexOf(n));
        Movie m = new Movie(0,0,0,
                            textures.get(n), Item.ItemType.DVD, n,
                            jsonM.getInt("duration"));
        dvdBookSlot.addItem(m);
    }

    /**
     * Add a book to the DVD/book slot.
     * @param n the name of the item
     */
    public void addBook(String n) {
        // TODO
    }

    /**
     * Gets the index of the item with name n in the JSONArray of items.
     * @returns the index of the item if found, -1 otherwise
     */
    private int getIndexOf(String n) {
        if (n.equals(MANGO)) {
            return 0;
        } else if (n.equals(CHIPS)) {
            return 1;
        } else if (n.equals(GNOME_COUNTRY)) {
            return 2;
        } else if (n.equals(SILENCE_GNOMES)) {
            return 3;
        }

        return -1;

    }

    /////

    /**
     * Checks if user's input is in area of any of the slots
     * @param in the location of the user's click
     */
    public boolean inArea(Vector2 in) {
        return (slotInArea(in) != null);
    }

    /**
     * Check if user's input is in area of any of the slots
     * @param in the location of the user's click
     */
    public Slot slotInArea(Vector2 in){
        for (Slot s: slots){
            if(s.inHitbox(new Vector2(in.x, Gdx.graphics.getHeight()-in.y))){
                return s;
                }
            }
        return null;
    }

    public void update(Vector2 in){
        float cw = Gdx.graphics.getWidth();  // canvas width
        float ch = Gdx.graphics.getHeight(); // canvas height

        for(Slot s : slots) {
            s.realHitbox.setPosition(s.getHitbox().getX() * cw, s.getHitbox().getY() * ch);
            s.realHitbox.setSize(s.getHitbox().getWidth() * cw,s.getHitbox().getHeight() * ch);
        }

        if (in != null) {
            if (itemInHand == null && inArea(in)) {
                itemInHand = take(slotInArea(in));
            }
            if (itemInHand != null) {
                itemInHandPosition = new Vector2(in.x, ch - in.y);
            }
        }

    }

    public Item getItemInHand() {
        return itemInHand;
    }

    public void setItemInHand(Item i){
        itemInHand = i;
    }

    public void draw(GameCanvas canvas) {
        for(Slot s : slots){
            if(s != null && s.getSlotItem() != null &&s.getSlotItem().texture!=null){
                float ix = s.getSlotItem().getTexture().getWidth()*0.5f; //center of item
                float iy = s.getSlotItem().getTexture().getHeight()*0.5f;
                float x = s.realHitbox.getX() + 0.5f*s.realHitbox.getWidth(); //centerOfSlot, already in screen dimensions
                float y = s.realHitbox.getY() + 0.7f*s.realHitbox.getHeight() ;

                for(int i=0; i<s.amount; i++) {
                    canvas.draw(s.getSlotItem().getTexture(), Color.WHITE, ix, iy, x-itemOffset*c.getWidth()*i, y, 0,
                            s.getSlotItem().relativeScale * c.getHeight(), s.getSlotItem().relativeScale * c.getHeight());

                }
            }
        }
    }

    public static void drawItemInHand(GameCanvas canvas){
        if(itemInHand != null && itemInHand.texture!=null) {
            canvas.draw(itemInHand.getTexture(), Color.WHITE,
                    itemInHand.getTexture().getWidth()*0.5f,
                    itemInHand.getTexture().getHeight()*0.5f,
                    itemInHandPosition.x,itemInHandPosition.y,0,
                    itemInHand.relativeScale*Gdx.graphics.getHeight(),
                    itemInHand.relativeScale*Gdx.graphics.getHeight());
        }
    }

    /**
     * Returns the item in this slot, removing 1 from its stored amount, if doing
     * so depletes all items in the slot, the slot is made empty (null slotItem, amount of 0).
     * Also saves state of slot previous to action, in case action is canceled.
     * @return slot's Item
     */
    public Item take(Slot s){
        if((s.amount > 0) && (s.slotItem != null)){
            lastSlotTakenFromState = new Slot(s);
            lastSlotTakenFrom = s;
            Item i = s.slotItem;
            s.amount --;
            if(s.amount == 0){
                s.slotItem = null;
            }
            return i;
        }
        return null;
    }

    /** Stores a specified item into the slot in one of two conditions
     * (i) The slot item
     * @param i Item to store
     */
    public void store(Slot s, Item i){
        if((s.slotItem != null) && i.getItemType() == s.slotItem.getItemType()){
            s.amount++;
        }
        else if((s.slotItem == null) && (s.amount == 0)){
            s.slotItem = i;
            s.amount++;
        }
        else if(lastSlotTakenFrom != null){
            cancelTake();
        }
    }

    // TODO: MAKE THESE NOT SUSPICIOUS WHEN IM NOT TIRED -steph
    public Slot getSnackSlot() { return slots.get(1); }
    public Slot getMovieSlot() { return slots.get(0); }
    public int getNumSnacks() { return slots.get(1).amount; }
    public int getNumMovies() { return slots.get(0).amount; }

    /** Cancel the user's taking of an item. */
    public void cancelTake(){
        lastSlotTakenFrom.slotItem = lastSlotTakenFromState.slotItem;
        lastSlotTakenFrom.amount = lastSlotTakenFromState.amount;
        lastSlotTakenFrom = null;
        lastSlotTakenFromState = null;
        itemInHand = null;
        itemInHandPosition = null;
    }

    // SLOT INNER CLASS
    private class Slot {
        /** An array of item objects in this slot */
        private Array<Item> items;
        /** The number of items in this slot */
        private int amount;

        /**
         * Create an empty slot.
         */
        private Slot() {
            items = new Array<Item>();
            amount = 0;
        }

        /**
         * Create a slot from an array of items.
         * @param i an array of items
         */
        private Slot(Array<Item> i) {
            items = i;
            amount = i.size;
        }

        /**
         * Get the item in the slot based on where the user clicked.
         * @param click the location of the user's click
         * @returns the item if the user clicked on one, null otherwise
         */
        private Item getItemAt(Vector2 click) {
            for (Item i : items) {
                if (i.inArea(click)) {
                    return i;
                }
            }

            // click was not on any item in this slot
            return null;
        }

        /**
         * Adds an item with the given name to this slot.
         * @param name the item name
         */
        private void addItem(String name) {

        }

        /**
         * Adds an item to this slot.
         * @param i the item
         */
        private void addItem(Item i) {

        }

        /**
         * Takes an item out of this slot.
         */
        private void removeItem() {

        }

        /**
         * Return a copy of this slot.
         */
        private Slot copy() {
            Slot s = new Slot();
            for (Item i : items) {
                s.addItem(new Item(i.position.x, i.position.y, i.relativeScale, i.texture,
                        i.getItemType(), i.getName()));
            }
            return s;
        }
    }

}

