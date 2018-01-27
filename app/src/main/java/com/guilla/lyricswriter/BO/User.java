package com.guilla.lyricswriter.BO;

import java.io.Serializable;

/**
 * Created by Moi on 21/11/15.
 */
public class User implements Serializable{

    //private variables
    public String _id;
    public String _facebookid;
    public String _name;
    public String _email;
    public String _code;
    public byte[] pictureprofil;
    public String picurl;
    public String token;
    public String type;
    public Boolean MutualFollow;

    // Empty constructor
    public User(){

    }
    // constructor
    public User(String id, String name, String email,byte[] pictureprofil){
        this._id = id;
        this._name = name;
        this._email=email;
        this.pictureprofil = pictureprofil;
    }

    public User(String id, String name, String email,String picurl,String token){
        this._id = id;
        this._name = name;
        this._email=email;
        this.picurl = picurl;
        this.token=token;
       this.type=type;
    }
    public User(String id, String _facebookid,String name, String email,String picurl,String token){
        this._id = id;
        this._name = name;
        this._email=email;
        this.picurl = picurl;
        this.token=token;
        this._facebookid=_facebookid;
        this.type=type;
    }


    public User(String id, String name, String email,byte[] pictureprofil,String code){
        this._id = id;
        this._name = name;
        this._email=email;
        this.pictureprofil = pictureprofil;
        this._code=code;
    }

    public User(String id, String name, String email,String picurl,String token,Boolean MutualFollow){
        this._id = id;
        this._name = name;
        this._email=email;
        this.picurl = picurl;
        this.token=token;
        this.type=type;
        this.MutualFollow=MutualFollow;
    }



    // constructor


    public String get_facebookid() {
        return _facebookid==null?"":_facebookid;
    }

    public String getToken() {
        return token;
    }


    public String getType() {
        return type;
    }

    public String getPicurl() {
        return picurl;
    }

    public byte[] getPictureprofil() {
        return pictureprofil;
    }

    public String get_id() {
        return _id;
    }

    public String get_email() {
        return _email;
    }

    public String get_name() {
        return _name;
    }

    public String get_code() {
        return _code;
    }
}
