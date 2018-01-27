package com.guilla.lyricswriter.BO;

import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.guilla.lyricswriter.R.id.creatorname;
import static com.guilla.lyricswriter.R.id.group;
import static com.guilla.lyricswriter.R.id.listusers;

/**
 * Created by Moi on 27/10/2017.
 */

public class InviteBO implements Serializable, Comparable<InviteBO> {

  public  String idProject;
   public String idCreatorProject;
   public String idCreatorProjectName;
   public String idCreatorPojectPic;
   public String InUserInvitedid;
   public String InUserInvitedname;
   public String InUserinvitedpic;
   public String InUserInviteToken;
   public Map<String, String> timestamp;
    public int type;
    public String ProjectName;
    public String Instatus;
    public String InUserInvite;
    public String InUserInviteid;


    public InviteBO(){
        }


    public InviteBO(String idProject,String ProjectName, String idCreatorProject, String idCreatorProjectName, String idCreatorPojectPic, String _UserInvite,String _UserInviteid,String _UserInvitedid,  String _UserInvitedname, String _Userinvitedpic,String _UserInviteToken, Map<String, String> timestamp,String _status,int type){
            this.idProject=idProject;
            this.ProjectName=ProjectName;
            this.idCreatorProject=idCreatorProject;
            this.idCreatorProjectName=idCreatorProjectName;
            this.idCreatorPojectPic=idCreatorPojectPic;
            this.InUserInvitedid=_UserInvitedid;
        this.InUserInvitedname=_UserInvitedname;
            this.InUserinvitedpic=_Userinvitedpic;
            this.timestamp=timestamp;
            this.type=type;
        this.InUserInviteToken=_UserInviteToken;
        this.Instatus=_status;
        this.InUserInvite=_UserInvite;
        this.InUserInviteid=_UserInviteid;

    }




        @Override
        public String toString() {
            return this.idProject;
        }

    @Override
    public int compareTo(InviteBO inviteBO) {
        return this.idProject.compareTo(inviteBO.getNameProject()); // dog name sort in ascending order
    }

    public String get_UserInvitedid() {
        return InUserInvitedid;
    }

    public String get_UserInvite() {
        return InUserInvite;
    }

    public String get_status() {
        return Instatus;
    }

    public int getType() {
        return type;
    }

    public String get_UserInviteToken() {
        return InUserInviteToken;
    }

    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public String getIdCreatorPojectPic() {
        return idCreatorPojectPic;
    }

    public String getIdCreatorProject() {
        return idCreatorProject;
    }

    public String getIdCreatorProjectName() {
        return idCreatorProjectName;
    }

    public String getIdProject() {
        return idProject;
    }

    public String getNameProject() {
        return ProjectName;
    }

    public String getUserInvitedid() {
        return InUserInvitedid;
    }

    public String getUserInvitedname() {
        return InUserInvitedname;
    }

    public String getUserInvitedPic() {
        return InUserinvitedpic;
    }
}
