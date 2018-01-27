package com.guilla.lyricswriter.BO;

import java.util.Map;

public class Message {

    private Map<String,String> timestamp;
    private long negatedTimestamp;
    private long dayTimestamp;
    private String body;
    private String from;
    private String to;

    public Message(Map<String,String> timestamp, long negatedTimestamp, long dayTimestamp, String body, String from, String to) {
        this.timestamp = timestamp;
        this.negatedTimestamp = negatedTimestamp;
        this.dayTimestamp = dayTimestamp;
        this.body = body;
        this.from = from;
        this.to = to;
    }

    public Message(Map<String,String> timestamp, String body, String from, String to) {
        this.timestamp = timestamp;
        this.body = body;
        this.from = from;
        this.to = to;
    }


    public Message() {
    }

    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public long getNegatedTimestamp() {
        return negatedTimestamp;
    }

    public String getTo() {
        return to;
    }

    public long getDayTimestamp() {
        return dayTimestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }
}