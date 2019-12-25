package com.example.castmeapps.object;

import java.sql.Timestamp;

public class Posting {

    private String user_id, caption, image_url, thumb_uri;
    private Timestamp timestamp;

    public Posting(){
        //must empty
    }

    public Posting(String user_id, String caption, String image_url, String thumb_uri, Timestamp timestamp) {
        this.user_id = user_id;
        this.caption = caption;
        this.image_url = image_url;
        this.thumb_uri = thumb_uri;
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getThumb_uri() {
        return thumb_uri;
    }

    public void setThumb_uri(String thumb_uri) {
        this.thumb_uri = thumb_uri;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
