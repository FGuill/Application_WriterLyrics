package com.guilla.lyricswriter.BO;

import java.io.Serializable;

public class Repost implements Serializable {
    public String repost_id_key;
    public String repost_userid;
    public String repost_userusername;




    public Repost(){
        }

        public Repost(String repost_id_key, String repost_userid, String repost_userusername){
            this.repost_id_key=repost_id_key;
            this.repost_userid=repost_userid;
            this.repost_userusername=repost_userusername;
        }

    public String getRepost_id_key() {
        return repost_id_key;
    }

    public String getRepost_userid() {
        return repost_userid;
    }

    public String getRepost_userusername() {
        return repost_userusername;
    }
}