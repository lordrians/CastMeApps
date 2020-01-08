package com.example.castmeapps.object;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Comments extends PostId {
    private String comment_text;
    private String user_id;
    @ServerTimestamp
    private Date timestamp;

    public Comments(){
        //must empty
    }

    public Comments(String comment_text, Date timestamp, String user_id) {
        this.comment_text = comment_text;
        this.timestamp = timestamp;
        this.user_id = user_id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
