package com.guilla.lyricswriter.BO;

import java.io.Serializable;


/**
 * Created by Moi on 21/11/15.
 */
public class UserJoined implements Serializable{

    //private variables
    public String _iduser;
    public String _username;
    public String _useremail;
    public String _picprofil;
    // Empty constructor
    public UserJoined(){

    }

    public UserJoined(String _iduser, String _username, String _useremail, String _picprofil){
        this._iduser = _iduser;
        this._username = _username;
        this._useremail=_useremail;
        this._picprofil = _picprofil;
    }

    // constructor



    public String getEmail() {
        return _useremail;
    }

    public String getId() {
        return _iduser;
    }

    public String getName() {
        return _username;
    }

    public String getPicprofil() {
        return _picprofil;
    }
}
