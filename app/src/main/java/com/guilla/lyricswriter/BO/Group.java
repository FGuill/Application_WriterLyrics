package com.guilla.lyricswriter.BO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Group implements Serializable, Comparable<Group> {

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
    public String commentaryKeyId;
    public String other;
    ///
    public Date date;
    public String location;



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


    public String _ProjectName;
    public String _UserInvite;
    public String _UserInviteid;
    public String _UserInvitedid;
    public String _UserInvitedName;
    public String _status;
    public String _idCreatorProject;
    public String _idCreatorProjectName;
    public String _idProject;
    public ArrayList<Group> listParticiant;

    //
    public String Participantidkey;
    public String Participantid;
    public String Participantname;
    public String Participantpicurl;

    public Group(){
        }

    public Group(String Participantidkey, String Participantid, String Participantname,String Participantpicurl){
        this.Participantid=Participantid;
        this.Participantname=Participantname;
        this.Participantpicurl=Participantpicurl;
        this.Participantidkey=Participantidkey;
    }


    public Group(String idkey, String groupname, String creatorid, String creatorname, String styleMusic, String privateorpublic,String listusers){
            this.groupname=groupname;
            this.creatorid=creatorid;
            this.creatorname=creatorname;
            this.styleMusic=styleMusic;
            this.privateorpublic=privateorpublic;
            this.listusers=listusers;
            this.idkey=idkey;
        }

    public Group(String idkey, String groupname, String creatorid, String creatorname, String styleMusic, String privateorpublic,String listusers,int type){
        this.groupname=groupname;
        this.creatorid=creatorid;
        this.creatorname=creatorname;
        this.styleMusic=styleMusic;
        this.privateorpublic=privateorpublic;
        this.listusers=listusers;
        this.idkey=idkey;
        this.type=type;
    }

    public Group(String idkey, String groupname, String creatorid, String creatorname, String styleMusic, String privateorpublic,String listusers, String timelapsedTips,int type){
        this.groupname=groupname;
        this.creatorid=creatorid;
        this.creatorname=creatorname;
        this.styleMusic=styleMusic;
        this.privateorpublic=privateorpublic;
        this.listusers=listusers;
        this.idkey=idkey;
        this.timelapsedTips=timelapsedTips;
        this.type=type;
    }

    public Group(String idkey, String groupname, String creatorid, String creatorname, String styleMusic, String privateorpublic,String listusers, String timelapsedTips,ArrayList<Group> listParticiant,int type){
        this.groupname=groupname;
        this.creatorid=creatorid;
        this.creatorname=creatorname;
        this.styleMusic=styleMusic;
        this.privateorpublic=privateorpublic;
        this.listusers=listusers;
        this.idkey=idkey;
        this.timelapsedTips=timelapsedTips;
        this.listParticiant=listParticiant;
        this.type=type;
    }

    public Group(String idkey,String userid,String username, String user_picurl,int numberSafePeople,String follow,Boolean isMutualFollow,String location){
        this.idkey=idkey;
        this.user_picurl=user_picurl;
        this.creatorid=userid;
        this.creatorname=username;
        this.numberSafePeople=numberSafePeople;
        this.follow=follow;
        this.isMutualFollow=isMutualFollow;
        this.location=location;
    }

        public Group(String idkey, String groupname, String creatorid, String creatorname, String styleMusic, String privateorpublic,ArrayList<UserJoined> userJoined){
            this.groupname=groupname;
            this.creatorid=creatorid;
            this.creatorname=creatorname;
            this.styleMusic=styleMusic;
            this.privateorpublic=privateorpublic;
            this.userJoined=userJoined;
            this.idkey=idkey;
        }


    public Group(String idkey, String creatorid, String creatorname,String picurl, String textTips, String privateorpublic,String like,boolean likeTips,String commentaire,String group_categorie,String timelapsedTips,String usernamewho_repost, int type){
        this.creatorid=creatorid;
        this.creatorname=creatorname;
        this.privateorpublic=privateorpublic;
        this.idkey=idkey;
        this.textTips=textTips;
        this.like=like;
        this.commentaire=commentaire;
        this.group_categorie=group_categorie;
        this.likeTips=likeTips;
        this.timelapsedTips=timelapsedTips;
        this.type=type;
        this.usernamewho_repost=usernamewho_repost;
        this.user_picurl=picurl;
    }


    public Group(String name) {
    }

    public Group(String key, String safe_userid, String safe_username, String safe_userpicprofil, int i, String follow, boolean b, Date time, int i1) {
    }

    public Group(String commentary_idkeyTips, String commentary_idkey, Object o, String commentary_userid, String commentary_username, String commentary_userpicprofil, String commentary_usertext, String commentary, String commentary_usertext1, boolean b, String s, String categorie, Date elapsedTimeTipsAnswer, String s1, int i) {
    }

    public Group(String idtips, String userid, String creatorname, String user_picurl, String textTips, String s, String s1, String s2, String theylike, String like, boolean b, String s3, String categorie, Date elapsedTimeTips, String s4, int i) {
    }

    public Group(String commentary_idkeyTips, String commentary_idkey, String o, String commentary_userid, String commentary_username, String commentary_userpicprofil, String commentary_usertext, String commentary, String commentary_usertext1, boolean b, String s, String categorie, String s1, String s11, int i) {
    }

    public Group(String s, int category_animalpets) {
    }

    public Group(String name, String group,String truc) {
        this.creatorname=name;
        this.groupname=group;
    }

    public Group(String idkey, String idProject,String projectName, String idCreatorProject, String idCreatorProjectName,String InvitedUserId,String InvitedUserName,String InviteUserId,String InviteUserName,String status, Date  date, int type) {
    this.idkey=idkey;
       this._ProjectName=projectName;
        this._idProject=idProject;
        this._UserInvite=InviteUserName;
        this._UserInviteid=InviteUserId;
        this._UserInvitedid=InvitedUserId;
        this._UserInvitedName=InvitedUserName;
        this._idCreatorProject=idCreatorProject;
        this._idCreatorProjectName=idCreatorProjectName;
        this._status=status;
        this.type=type;
        this.date=date;
    }

    public String getParticipantid() {
        return Participantid;
    }

    public String getParticipantidkey() {
        return Participantidkey;
    }

    public String getParticipantname() {
        return Participantname;
    }

    public String getParticipantpicurl() {
        return Participantpicurl;
    }

    public String get_UserInvitedid() {
        return _UserInvitedid;
    }

    public String get_UserInvite() {
        return _UserInvite;
    }

    public String get_status() {
        return _status;
    }

    public String get_idCreatorProject() {
        return _idCreatorProject;
    }

    public String get_idProject() {
        return _idProject;
    }

    public String get_ProjectName() {
        return _ProjectName;
    }

    public String get_UserInviteid() {
        return _UserInviteid;
    }

    public String get_UserInvitedName() {
        return _UserInvitedName;
    }

    public String get_idCreatorProjectName() {
        return _idCreatorProjectName;
    }


    public ArrayList<Group> getListParticiant() {
        return listParticiant;
    }

    public String getLocation() {
        return location;
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

    public String getCommentaryKeyId() {
        return commentaryKeyId;
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
    public int compareTo(Group group) {
        return this.idkey.compareTo(group.getGroupname()); // dog name sort in ascending order
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Group) && this.creatorid == ((Group)obj).getCreatorid();
    }

    @Override
    public String toString() {
        return this.idkey;
    }
}