package com.example.castmeapps.object;

import java.util.Date;

public class Posting extends PostId {

    private String user_id;
    private String caption;
    private String image_url;
    private String thumb_uri;
    private String likescount;

    private Date timestamp;

    public Posting(){
        //must empty
    }

    public Posting(String user_id, String caption, String image_url, String thumb_uri, Date timestamp) {
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getLikescount() {
        return likescount;
    }

    public void setLikescount(String likescount) {
        this.likescount = likescount;
    }
}
