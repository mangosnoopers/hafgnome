package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import edu.cornell.gdiac.mangosnoops.Image;

public class Inventory extends Image{

    private Array<Slot> slots = new Array<Slot>(8);

    public enum Item {
        DVD, SNACK, BOOK;

    }

    public Inventory(float x, float y, float r, Texture t) {
        super(x, y, r, t);

        for (Slot s : slots) {
            s = new Slot(null);
        }
    }

    private class Slot{
        Item slotItem;

        public Slot(Item item){
            this.slotItem = item;
        }

        public Item take(){
            Item i = slotItem;
            slotItem = null;
            return i;
        }

        public void store(Item i){
            if(slotItem == null){
                slotItem = i;
            }
            return;
        }

    }
}

