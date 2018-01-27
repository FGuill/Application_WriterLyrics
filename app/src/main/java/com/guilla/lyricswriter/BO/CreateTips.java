package com.guilla.lyricswriter.BO;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class CreateTips implements Serializable, Comparable<CreateTips> {

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
    public String timelapsedTips;
    public int type;//tips or ads
    public String usernamewho_repost;
    public String user_picurl;

    public String mentioneduser;
    public String mentionuser;
    public String other;
///
public Date date;



    public String id;

    public String hashtag;
    public String latitude;
    public String longitude;
    public int drawable;

    public int numberSafePeople;
    public String follow;
    public Boolean isMutualFollow;



    public CreateTips(){
        }

    public CreateTips(String idkey){
        this.idkey=idkey;
    }

    public CreateTips(String idkey, String creatorid, String creatorname, String picurl, String textTips, String privateorpublic, String group_categorie, Map<String, String> timestamp, int type) {
        this.creatorid=creatorid;
        this.creatorname=creatorname;
        this.privateorpublic=privateorpublic;
        this.idkey=idkey;
        this.textTips=textTips;
        this.group_categorie=group_categorie;
        this.timestamp=timestamp;
        this.type=type;
        this.user_picurl=picurl;
    }

    public String getIdkey() {
        return idkey;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public String getPrivateorpublic() {
        return privateorpublic;
    }

    public String getTextTips() {
        return textTips;
    }

    public String getGroup_categorie() {
        return group_categorie;
    }

    public String getTimelapsedTips() {
        return timelapsedTips;
    }

    public int getType() {
        return type;
    }

    public String getUser_picurl() {
        return user_picurl;
    }

    @Override
    public int compareTo(CreateTips group) {
        return this.idkey.compareTo(group.getIdkey()); // dog name sort in ascending order
    }


    @Override
    public String toString() {
        return this.idkey;
    }
}