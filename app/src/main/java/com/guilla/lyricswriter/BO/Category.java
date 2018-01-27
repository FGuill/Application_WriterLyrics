package com.guilla.lyricswriter.BO;

import java.io.Serializable;

/**
 * Created by Moi on 21/11/15.
 */
public class Category implements Serializable{



    String objectid;
    String nom;
    byte[] _image;

    String category_objectid;
    String category_nom;
    String category_image;
    // Empty constructor
    int category_drawables;

    // constructor
    public Category(){

    }
    public Category(String objectid,String nom, byte[] image) {
        this.nom = nom;
        this.objectid = objectid;
        this._image = image;
    }

    public Category(String category_objectid,String category_nom, String category_image) {
        this.category_objectid = category_objectid;
        this.category_nom = category_nom;
        this.category_image = category_image;
    }

    public Category(String category_objectid,String category_nom, int category_image) {
        this.category_objectid = category_objectid;
        this.category_nom = category_nom;
        this.category_drawables = category_image;
    }

    public Category(String category_nom, int category_image) {
        this.category_nom = category_nom;
        this.category_drawables = category_image;
    }

    public int getCategory_drawables() {
        return category_drawables;
    }

    public byte[] get_image() {
        return _image;
    }

    public String getNom() {
        return nom;
    }

    public String getObjectid() {
        return objectid;
    }

    public String getCategory_image() {
        return category_image;
    }

    public String getCategory_nom() {
        return category_nom;
    }

    public String getCategory_objectid() {
        return category_objectid;
    }
}
