package com.guilla.lyricswriter.BO;

import java.io.Serializable;

public class Like implements Serializable {
    public String idtipskey;
    public String userid;
    public String username;
    public String user_likepicprofil;
    public String text;
    public String categorie;



    public Like(){
        }

        public Like(String idtipskey, String userid, String username, String user_likepicprofil){
            this.idtipskey=idtipskey;
            this.userid=userid;
            this.username=username;
            this.user_likepicprofil=user_likepicprofil;
        }


    public Like(String idtipskey, String userid, String username, String user_likepicprofil,String textLiked,String categorie){
        this.idtipskey=idtipskey;
        this.userid=userid;
        this.username=username;
        this.user_likepicprofil=user_likepicprofil;
        this.text=textLiked;
        this.categorie=categorie;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getText() {
        return text;
    }

    public String getUser_likepicprofil() {
        return user_likepicprofil;
    }

    public String getIdtipskey() {
        return idtipskey;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }
}