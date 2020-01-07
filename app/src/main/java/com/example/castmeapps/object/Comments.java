package com.example.castmeapps.object;

import java.util.Date;

public class Comments extends PostId {
    private String userId;
    private String caption;
    private Date timestamp;

    public Comments(){
        //must empty
    }

    public Comments(String userId, String caption, Date timestamp) {
        this.userId = userId;
        this.caption = caption;
        this.timestamp = timestamp;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
