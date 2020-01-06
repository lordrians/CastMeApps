package com.example.castmeapps.object;

import java.util.Date;

public class Comment {
    private String username;
    private String caption;
    private Date timestamp;

    public Comment(String username, String caption, Date timestamp) {
        this.username = username;
        this.caption = caption;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
