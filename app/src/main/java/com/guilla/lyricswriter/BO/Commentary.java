package com.guilla.lyricswriter.BO;

import java.io.Serializable;
import java.util.Map;

public class Commentary implements Serializable {
    public String commentary_idkey;
    public String commentary_userid;
    public String commentary_username;
    public String commentary_usertext;
    public String commentary_userpicprofil;
    public Map<String,String> timestamp;
    public String elapsedTimeCommentary;
    public String mentioneduser;
    public String mentionuser;
    String mentioneduserid;
    public String answer_idkey;
    public int type;
    public int hasAnswer;
    public String commentary_idkeyTips;
    public String categorie;
    public Commentary(){
        }

        public Commentary(String commentary_idkey, String commentary_userid, String commentary_username,String commentary_usertext,String commentary_userpicprofil){
            this.commentary_idkey=commentary_idkey;
            this.commentary_userid=commentary_userid;
            this.commentary_username=commentary_username;
            this.commentary_usertext=commentary_usertext;
            this.commentary_userpicprofil=commentary_userpicprofil;
        }

    public Commentary(String commentary_idkey, String commentary_userid, String commentary_username, String commentary_usertext, String commentary_userpicprofil, Map<String,String> timestamp){
        this.commentary_idkey=commentary_idkey;
        this.commentary_userid=commentary_userid;
        this.commentary_username=commentary_username;
        this.commentary_usertext=commentary_usertext;
        this.commentary_userpicprofil=commentary_userpicprofil;
        this.timestamp=timestamp;
    }

    public Commentary(String commentary_idkey, String commentary_userid, String commentary_username,String mentionuser,String mentioneduser,String mentioneduserid, String commentary_usertext, String commentary_userpicprofil, String elapsedTimeCommentary,int type,int hasAnswer){
        this.commentary_idkey=commentary_idkey;
        this.commentary_userid=commentary_userid;
        this.commentary_username=commentary_username;
        this.commentary_usertext=commentary_usertext;
        this.commentary_userpicprofil=commentary_userpicprofil;
        this.elapsedTimeCommentary=elapsedTimeCommentary;
        this.mentionuser=mentionuser;
        this.mentioneduser=mentioneduser;
        this.mentioneduserid=mentioneduserid;
        this.type=type;
        this.hasAnswer=hasAnswer;

    }

    public Commentary(String commentary_idkeyTips,String commentary_idkey, String commentary_userid, String commentary_username,String mentionuser,String mentioneduser,String mentioneduserid, String commentary_usertext, String commentary_userpicprofil,String categorie, Map<String,String> timestamp,int type){
        this.commentary_idkeyTips=commentary_idkeyTips;
        this.commentary_idkey=commentary_idkey;
        this.commentary_userid=commentary_userid;
        this.commentary_username=commentary_username;
        this.commentary_usertext=commentary_usertext;
        this.commentary_userpicprofil=commentary_userpicprofil;
        this.timestamp=timestamp;
        this.mentionuser=mentionuser;
        this.mentioneduser=mentioneduser;
        this.mentioneduserid=mentioneduserid;
        this.type=type;
        this.categorie=categorie;

    }

    public Commentary(String commentary_idkeyTips, String commentary_idkey,String answer_idkey, String commentary_userid, String commentary_username,String mentionuser,String mentioneduser,String mentioneduserid, String commentary_usertext, String commentary_userpicprofil,String categorie, Map<String,String> timestamp,int type){
       this.commentary_idkeyTips=commentary_idkeyTips;
        this.commentary_idkey=commentary_idkey;
        this.commentary_userid=commentary_userid;
        this.commentary_username=commentary_username;
        this.commentary_usertext=commentary_usertext;
        this.commentary_userpicprofil=commentary_userpicprofil;
        this.timestamp=timestamp;
        this.mentionuser=mentionuser;
        this.mentioneduser=mentioneduser;
        this.mentioneduserid=mentioneduserid;
        this.answer_idkey=answer_idkey;
        this.type=type;
        this.categorie=categorie;
    }


    public String getCategorie() {
        return categorie;
    }

    public String getCommentary_idkeyTips() {
        return commentary_idkeyTips;
    }

    public int getHasAnswer() {
        return hasAnswer;
    }

    public String getAnswer_idkey() {
        return answer_idkey;
    }

    public int getType() {
        return type;
    }

    public String getMentioneduserid() {
        return mentioneduserid;
    }

    public String getMentionuser() {
        return mentionuser;
    }

    public String getMentioneduser() {
        return mentioneduser;
    }

    public String getElapsedTimeCommentary() {
        return elapsedTimeCommentary;
    }

    public String getCommentary_idkey() {
        return commentary_idkey;
    }

    public String getCommentary_userid() {
        return commentary_userid;
    }

    public String getCommentary_username() {
        return commentary_username;
    }

    public String getCommentary_userpicprofil() {
        return commentary_userpicprofil;
    }

    public String getCommentary_usertext() {
        return commentary_usertext;
    }
}