package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;

public class Inventory extends Image{

    private static Item itemInHand;

    private static Vector2 itemInHandPosition;

    private Slot lastSlotTakenFromState;

    private Slot lastSlotTakenFrom;

    private Array<Slot> slots;

    private final float itemOffset = 0.01f;

    public Inventory(float x_left, float y_bottom, float r, float cb, Texture t, float slotWidth, float slotHeight, int numSlots) {
        super(x_left, y_bottom, r, cb, t);
        slots = new Array<Slot>(numSlots);
        int temp = numSlots-1;
        for (Slot s : slots) {
            s = new Slot(temp, x_left,y_bottom+(temp*slotHeight), slotWidth, slotHeight);
            temp--;
        }
    }

    @Override
    public boolean inArea(Vector2 in){
        return (slotInArea(in) != null);
    }

    public Slot slotInArea(Vector2 in){
        for (Slot s: slots){
            if(s.inHitbox(new Vector2(in.x,SCREEN_DIMENSIONS.y-in.y))){
                return s;
                }
            }
        return null;
    }

    boolean prevMousePressed;
    public void update(Vector2 in, boolean mousePressed){
        for(Slot s : slots) {
            s.setRealHitbox(new Rectangle(s.getHitbox().getX() * SCREEN_DIMENSIONS.x, s.getHitbox().getY() * SCREEN_DIMENSIONS.y,
                    s.getHitbox().getWidth() * SCREEN_DIMENSIONS.x, s.getHitbox().getHeight() * SCREEN_DIMENSIONS.y));
        }

        System.out.println(this.position.x*SCREEN_DIMENSIONS.x +" " + this.position.y*SCREEN_DIMENSIONS.y);
        if(in != null) {
            if(inArea(in)){
                System.out.println("inside inventory");
            }
            if (itemInHand == null && inArea(in) && mousePressed) {
                itemInHand = take(slotInArea(in));
            }
            if (itemInHand != null && mousePressed) {
                itemInHandPosition = in;
            }
            if( (itemInHand!=null)&& (prevMousePressed && !mousePressed)){
                //released mouse, check areas w/ this vector in (store, children, dvd player)
            }
        }
        prevMousePressed = mousePressed;
    }

    public void setItemInHand(Item itemInHand) {
        this.itemInHand = itemInHand;
    }

    @Override
    public void draw (GameCanvas canvas){
        for(Slot s : slots){
            if(s != null && s.getSlotItem() != null){
                float ix = s.getSlotItem().getTexture().getWidth()*0.5f; //center of item
                float iy = s.getSlotItem().getTexture().getHeight()*0.5f;
                float x = s.realHitbox.getX() + 0.5f*s.realHitbox.getWidth(); //centerOfSlot, already in screen dimensions
                float y = s.realHitbox.getY() + 0.5f*s.realHitbox.getHeight();

                for(int i=0; i<s.amount; i++) {
                    canvas.draw(s.getSlotItem().getTexture(), Color.WHITE, ix, iy, x-itemOffset*SCREEN_DIMENSIONS.x, y, 0,
                            s.getSlotItem().relativeScale * SCREEN_DIMENSIONS.y, s.getSlotItem().relativeScale * SCREEN_DIMENSIONS.y);

                }
            }
        }
        if(itemInHand != null) {
            canvas.draw(itemInHand.getTexture(), Color.WHITE, itemInHand.getTexture().getWidth()*0.5f, itemInHand.getTexture().getHeight()*0.5f,
                            itemInHandPosition.x,itemInHandPosition.y,0,itemInHand.relativeScale*SCREEN_DIMENSIONS.y, itemInHand.relativeScale*SCREEN_DIMENSIONS.y);
        }
    }

    /** Returns the item in this slot, removing 1 from its stored amount, if doing so depletes all items in the slot, the slot is
     * made empty (null slotItem, amount of 0).
     * // TODO Also saves state of slot previous to action, in case action is canceled.
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
     * @param i
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
        return;
    }

    private void cancelTake(){
        lastSlotTakenFrom.slotItem = lastSlotTakenFromState.slotItem;
        lastSlotTakenFrom.amount = lastSlotTakenFromState.amount;
        lastSlotTakenFrom = null;
        lastSlotTakenFromState = null;
    }

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
            realHitbox = new Rectangle(x_left*SCREEN_DIMENSIONS.x,y_bottom*SCREEN_DIMENSIONS.y,width*SCREEN_DIMENSIONS.x,height*SCREEN_DIMENSIONS.y);
        }

        public Slot(int invPos, float x_left, float y_bottom, float width, float height, Item.ItemType slotItem, int amount){
            this.invPos = invPos;
            this.slotItem = new Item(slotItem);
            this.amount = amount;
            hitbox = new Rectangle(x_left,y_bottom,width,height);
            realHitbox = new Rectangle(x_left*SCREEN_DIMENSIONS.x,y_bottom*SCREEN_DIMENSIONS.y,width*SCREEN_DIMENSIONS.x,height*SCREEN_DIMENSIONS.y);
        }

        public Slot (Slot s){
            this.invPos = s.invPos;
            this.slotItem = s.slotItem;
            this.amount = s.amount;
            this. hitbox = s. hitbox;
            this.realHitbox = s.realHitbox;
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

        public void setRealHitbox(Rectangle rhitbox) {
            this.realHitbox = rhitbox;
        }



    }

    public static class Item{

        private ItemType itemType;

        private Texture texture;

        private float relativeScale;

        private Vector2 roadPosition;

        private static ObjectMap<ItemType, Texture> itemTextures;
        private static ObjectMap<ItemType, Float> relativeScales;

        public enum ItemType {
            DVD, SNACK;

        }

        public Item (float x, float y,ItemType itemType){
            roadPosition = new Vector2(x,y);
            this.itemType = itemType;
            texture = itemTextures.get(itemType);
            relativeScale = relativeScales.get(itemType);
        }

        public Item (ItemType itemType){
            roadPosition = null;
            this.itemType = itemType;
            texture = itemTextures.get(itemType);
            relativeScale = relativeScales.get(itemType);
        }

        public static void setTexturesAndScales(Texture dvd, float dvdScale, Texture snack, float snackScale){
            itemTextures = new ObjectMap<ItemType, Texture>();
            relativeScales = new ObjectMap<ItemType, Float>();
            itemTextures.put(ItemType.DVD, dvd);
            itemTextures.put(ItemType.SNACK, snack);
            relativeScales.put(ItemType.DVD, dvdScale/(itemTextures.get(ItemType.DVD).getHeight()));
            relativeScales.put(ItemType.SNACK, snackScale/(itemTextures.get(ItemType.SNACK).getHeight()));
        }

        public ItemType getItemType() {
            return itemType;
        }

        public Texture getTexture(){
            return texture;
        }

        public void setRoadPosition(Vector2 roadPosition){
            this.roadPosition = roadPosition;
        }
    }
}

