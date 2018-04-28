package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.items.*;

public class Inventory {
    /** Stuff for taking items out of the inventory box */
    private static Item itemInHand;
    private static Vector2 itemInHandPosition;
    private Slot lastSlotTakenFromState;
    private Slot lastSlotTakenFrom;
    /** All of the slots in the inventory */
    private Array<Slot> slots;
    /** Dimensions of the slot */
    private Vector2 slotsDimensions;
    /** Offset between each item drawn within a slot */
    private float itemOffset = 0.01f;
    /** A mapping of item names to textures */
    private ObjectMap<String,Texture> textures;

    /** Constants for drawing inventory */
    private static final float X_LEFT = 0.4756f;
    private static final float Y_BOTTOM = 0.0366f;
    private static final float SLOT_WIDTH = 0.146f;
    private static final float SLOT_HEIGHT = 0.15f;
    private static final float RELSCA = 0.0f;
    private static final float CB = 0.0f;
    private static final int NUM_SLOTS = 2;

    /** Constants for drawing items */
    private static final float snackRelSca = 0.135f;
    private static final float bookRelSca = 0.12f;
    private static final float dvdRelSca = 0.12f;

    /** The snack slot */
    private Slot snackSlot;
    /** The DVD/book slot */
    private Slot dvdBookSlot;

    /** Create an empty inventory with a constant number of slots. */
    public Inventory(ObjectMap<String,Texture> ts) {
        textures = ts;
        slotsDimensions = new Vector2(SLOT_WIDTH, SLOT_HEIGHT);

        // initialize the two slots, then add them to the slots array
        dvdBookSlot = new Slot(0, X_LEFT, Y_BOTTOM, SLOT_WIDTH, SLOT_HEIGHT);
        snackSlot = new Slot(1, X_LEFT, Y_BOTTOM+SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
        slots = new Array<Slot>(NUM_SLOTS);
        slots.add(snackSlot); slots.add(dvdBookSlot);
    }

    /** Empties the inventory. */
    public void reset() {
        itemInHand = null;
        itemInHandPosition = null;
        lastSlotTakenFromState = null;
        lastSlotTakenFrom = null;
        slots = new Array<Slot>(slots.size);
    }

    /** TODO: Return a copy of this inventory. */
    public Inventory copy() {
        return new Inventory(textures);
    }

    /**
     * Add an item to the snack slot.
     * @param n the name of the item
     */
    public void addSnack(String n) {
        Snack s = new Snack(0,0,0, textures.get(n), Item.ItemType.SNACK, n);
    }

    /**
     * Add a movie to the DVD/book slot.
     * @param n the name of the item
     */
    public void addMovie(String n) {
        Movie m = new Movie(0,0,0,textures.get(n), Item.ItemType.DVD, n);

    }

    /**
     * Add a book to the DVD/book slot.
     * @param n the name of the item
     */
    public void addBook(String n) {
        // TODO
    }

    /////

    /** Loads an array of slots into this inventory. */
    public void load(Array<Slot> inv){
        // If the given number of slots is fewer than inventory size, pad with nulls
        if(inv.size < slots.size){
            for(int i=0; i<(slots.size-inv.size); i++){
                inv.add(null);
            }
        }
        // If inventory is smaller than number of given slots, truncate
        if(inv.size > slots.size){
            for(int i=0; i<(inv.size-slots.size); i++){
                inv.pop();
            }
        }
        slots = inv;
    }

    /** Checks if user's input is in area of any of the slots */
    public boolean inArea(Vector2 in){
        return (slotInArea(in) != null);
    }

    /** Check if user's input is in area of any of the slots */
    public Slot slotInArea(Vector2 in){
        for (Slot s: slots){
            if(s.inHitbox(new Vector2(in.x,c.getHeight()-in.y))){
                return s;
                }
            }
        return null;
    }

    public void update(Vector2 in, boolean mousePressed){
        for(Slot s : slots) {
            s.realHitbox.setPosition(s.getHitbox().getX() * c.getWidth(), s.getHitbox().getY() * c.getHeight());
            s.realHitbox.setSize(s.getHitbox().getWidth() * c.getWidth(),s.getHitbox().getHeight() * c.getHeight());
        }

        if (in != null) {
            if (itemInHand == null && inArea(in)) {
                itemInHand = take(slotInArea(in));
            }
            if (itemInHand != null) {
                itemInHandPosition = new Vector2(in.x, c.getHeight() - in.y);
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
            canvas.draw(itemInHand.getTexture(), Color.WHITE, itemInHand.getTexture().getWidth()*0.5f, itemInHand.getTexture().getHeight()*0.5f,
                    itemInHandPosition.x,itemInHandPosition.y,0,itemInHand.relativeScale*c.getHeight(), itemInHand.relativeScale*c.getHeight());
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
    private class Slot{
        private int invPos;
        private Item slotItem;
        private int amount;
        private Rectangle hitbox;
        private Rectangle realHitbox;

        public Slot(int invPos, float x_left, float y_bottom, float width, float height){
            this.invPos = invPos;
            this.slotItem = null;
            this.amount = 0;
            hitbox = new Rectangle(x_left,y_bottom,width,height);
            realHitbox = new Rectangle(x_left*c.getWidth(),y_bottom*c.getHeight(),width*c.getWidth(),height*c.getHeight());
        }

        public Slot(int invPos, float x_left, float y_bottom, float width, float height, Item.ItemType slotItem, int amount){
            this.invPos = invPos;
            this.slotItem = new Item(slotItem);
            this.amount = amount;
            hitbox = new Rectangle(x_left,y_bottom,width,height);
            realHitbox = new Rectangle(x_left*c.getWidth(),y_bottom*c.getHeight(),width*c.getWidth(),height*c.getHeight());
        }

        public Slot (Slot s){
            this.invPos = s.invPos;
            this.slotItem = s.slotItem;
            this.amount = s.amount;
            this.hitbox = s.hitbox;
            this.realHitbox = s.realHitbox;
        }

        public Slot(Array<Slot> slots, Inventory inv, Item.ItemType slotItem, int amount){
            this.invPos = slots.size;
            this.slotItem = new Item(slotItem);
            this.amount = amount;
            this.hitbox = new Rectangle(inv.position.x, inv.position.y+invPos*inv.slotsDimensions.y,
                                        inv.slotsDimensions.x, inv.slotsDimensions.y);
            this.realHitbox = new Rectangle(hitbox.getX()*c.getWidth(),hitbox.getY()*c.getHeight(),
                                            hitbox.getWidth()*c.getWidth(),hitbox.getHeight()*c.getHeight());
        }

        private boolean inHitbox( Vector2 in){
            return realHitbox.contains(in);
        }

        public Item getSlotItem(){
            return slotItem;
        }

        public Rectangle getHitbox() {
            return hitbox;
        }

        public Rectangle getRealHitbox() {
            return realHitbox;
        }

        public void setRealHitbox(Rectangle rhitbox) {
            this.realHitbox = rhitbox;
        }

        public String toString(){
            return ("Slot #"+invPos) ;
        }

        /** Increment the amount by i */
        public void incAmount(int i) { amount += i; }
    }

}

