package com.guilla.lyricswriter.BO;

import java.io.Serializable;

/**
 * Created by Moi on 21/11/15.
 */
public class LocationBusiness implements Serializable {

    //private variables
    private double _latitude;
    private double _longitude;
    String _businessName;
    private String _addresse;
    byte[] picture;
    float distance;
    boolean isoffset;
    private String description;
    private String prix;
    private String reduction;
    private String categorie;
    private boolean istouch;


    public LocationBusiness() {

    }

    // constructor
    public LocationBusiness(double latitude, double longitude, String businessName, String addresse) {
        this._latitude = latitude;
        this._longitude = longitude;
        this._businessName = businessName;
        this._addresse = addresse;
    }

    public LocationBusiness(double latitude, double longitude, String businessName,String categorie, float distance, byte[] picture, boolean istouch, boolean isOffset) {
        this._latitude = latitude;
        this._longitude = longitude;
        this._businessName = businessName;
        this.picture = picture;
        this.distance=distance;
        this.isoffset=isOffset;
        this.categorie=categorie;
        this.istouch=istouch;
    }

    public LocationBusiness(String categorie,double latitude, double longitude, String businessName, float distance, String description,String prix,String reduction, boolean isOffset) {
        this._latitude = latitude;
        this._longitude = longitude;
        this._businessName = businessName;
        this.distance=distance;
        this.isoffset=isOffset;
        this.description=description;
        this.prix=prix;
        this.reduction=reduction;
        this.categorie=categorie;
    }


    // constructor


    public boolean isoffset() {
        return isoffset;
    }

    public float getDistance() {
        return distance;
    }

    public byte[] getPicture() {
        return picture;
    }

    public double get_longitude() {
        return _longitude;
    }

    public String get_businessName() {
        return _businessName;
    }

    public double get_latitude() {
        return _latitude;
    }

    public String get_addresse() {
        return _addresse;
    }

    public String getCategorie() {
        return categorie;
    }

    public boolean istouch() {
        return istouch;
    }

    public void setIstouch(boolean istouch) {
        this.istouch = istouch;
    }

    //Promotion getter

    public String getDescription() {
        return description;
    }

    public String getPrix() {
        return prix;
    }

    public String getReduction() {
        return reduction;
    }
}
