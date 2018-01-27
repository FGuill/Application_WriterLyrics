package com.guilla.lyricswriter.BO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Moi on 24/10/2017.
 */

public class CreeateGroupBo implements Serializable, Comparable<com.guilla.lyricswriter.BO.Group> {

        public ArrayList<UserJoined> userJoined;
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



        public String disaster_type;
        public String id;
        public String user_creator;
        public String user_creatorid;
        public String user_creatorpic;
        public String country;
        public String city;
        public String street;
        public String hashtag;
        public String latitude;
        public String longitude;
        public int drawable;

        public int numberSafePeople;
        public String follow;
        public Boolean isMutualFollow;
        public String groupname;
        public String styleMusic;
        public String listusers;

        public CreeateGroupBo(){
        }

        public CreeateGroupBo(String idkey, String groupname, String creatorid, String creatorname, String styleMusic, String privateorpublic,String listusers){
            this.groupname=groupname;
            this.creatorid=creatorid;
            this.creatorname=creatorname;
            this.styleMusic=styleMusic;
            this.privateorpublic=privateorpublic;
            this.listusers=listusers;
            this.idkey=idkey;
        }


        public int getType() {
            return type;
        }

        public String getIdkey() {
            return idkey;
        }

        public void setCreatorid(String creatorid) {
            this.creatorid = creatorid;
        }

        public void setCreatorname(String creatorname) {
            this.creatorname = creatorname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getCreatorid() {
            return creatorid;
        }

        public String getCreatorname() {
            return creatorname;
        }

        public String getGroupname() {
            return groupname;
        }

        public ArrayList<UserJoined> getUserJoined() {
            return userJoined;
        }

        public void setUserJoined(ArrayList<UserJoined> userJoined) {
            this.userJoined = userJoined;
        }

        public String getKey() {
            return idkey;
        }

        public void setKey(String key) {
            idkey = key;
        }

        public String getListusers() {
            return listusers;
        }

        public String getPrivateorpublic() {
            return privateorpublic;
        }

        public String getStyleMusic() {
            return styleMusic;
        }



        public Date getDate() {
            return date;
        }

        public String getFollow() {
            return follow;
        }

        public Boolean getMutualFollow() {
            return isMutualFollow;
        }

        public void setFollow(String follow) {
            this.follow = follow;
        }


        public void setMutualFollow(Boolean mutualFollow) {
            isMutualFollow = mutualFollow;
        }


        public String getMentioneduser() {
            return mentioneduser;
        }

        public String getMentionuser() {
            return mentionuser;
        }

        public String getUser_picurl() {
            return user_picurl;
        }

        public String getUsernamewho_repost() {
            return usernamewho_repost;
        }


        public String getGroup_categorie() {
            return group_categorie;
        }

        public String getTimelapsedTips() {
            return timelapsedTips;
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



        public String getTextTips() {
            return textTips;
        }


        @Override
        public int compareTo(com.guilla.lyricswriter.BO.Group group) {
            return this.idkey.compareTo(group.getGroupname()); // dog name sort in ascending order
        }


        @Override
        public String toString() {
            return this.idkey;
        }
    }

