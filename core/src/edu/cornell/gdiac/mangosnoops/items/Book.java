package edu.cornell.gdiac.mangosnoops.items;

import com.badlogic.gdx.graphics.Texture;

public class Book extends Item {
    /** Which child likes this book - 0 for Ned, 1 for Nosh */
    private int likedBy;

    /** Returns true if Ned likes this book */
    public boolean likedByNed() { return likedBy == 0; }

    /** Returns true if Nosh likes this book */
    public boolean likedByNosh() { return likedBy == 1; }

    public Book(float x, float y, float relSca, Texture t, ItemType type, String n, int likedBy) {
        super(x,y,relSca,t,type,n);
        this.likedBy = likedBy;
    }
}