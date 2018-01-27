package com.guilla.lyricswriter.BO;

import java.io.Serializable;
import java.util.Map;

public class Tips implements Serializable {
    public String idkey;
    public String creatorid;
    public String creatorname;
    public String textTips;
    public String privateorpublic;

    public String like;
    public String commentaire;
    public String group_categorie;
    Map<String, String> timestamp;
    public boolean likeTips;


    public Tips(){
        }

    public Tips(String idkey, String creatorid, String creatorname, String textTips, String privateorpublic){
        this.creatorid=creatorid;
        this.creatorname=creatorname;
        this.privateorpublic=privateorpublic;
        this.idkey=idkey;
        this.textTips=textTips;
    }



    public Tips(String idkey, String creatorid, String creatorname, String textTips, String privateorpublic, String like, boolean likeTips, String commentaire, String group_categorie,Map<String, String> timestamp){
        this.creatorid=creatorid;
        this.creatorname=creatorname;
        this.privateorpublic=privateorpublic;
        this.idkey=idkey;
        this.textTips=textTips;
        this.like=like;
        this.commentaire=commentaire;
        this.group_categorie=group_categorie;
        this.likeTips=likeTips;
        this.timestamp=timestamp;
    }

    public Tips(String idkey, String creatorid, String creatorname, String textTips, String privateorpublic, String group_categorie, Map<String, String> timestamp) {
        this.creatorid=creatorid;
        this.creatorname=creatorname;
        this.privateorpublic=privateorpublic;
        this.idkey=idkey;
        this.textTips=textTips;
        this.group_categorie=group_categorie;
        this.timestamp=timestamp;
    }




    public String getGroup_categorie() {
        return group_categorie;
    }


    public void setLikeTips(boolean likeTips) {
        this.likeTips = likeTips;
    }

    public boolean isLikeTips() {
        return likeTips;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getLike() {
        return like;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public String getIdkey() {
        return idkey;
    }

    public String getPrivateorpublic() {
        return privateorpublic;
    }

    public String getTextTips() {
        return textTips;
    }

}