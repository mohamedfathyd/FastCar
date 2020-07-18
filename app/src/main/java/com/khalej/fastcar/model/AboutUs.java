package com.khalej.fastcar.model;

import com.google.gson.annotations.SerializedName;

public class AboutUs {
    @SerializedName("id")
    int id;
    @SerializedName("content")
    String ar_content;
    @SerializedName("link")
    String link;


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAr_content() {
        return ar_content;
    }

    public void setAr_content(String ar_content) {
        this.ar_content = ar_content;
    }

}
