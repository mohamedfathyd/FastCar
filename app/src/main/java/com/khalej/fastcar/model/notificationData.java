package com.khalej.fastcar.model;

import com.google.gson.annotations.SerializedName;

public class notificationData {
    @SerializedName("ar_notification_title")
    String name_ar;
    @SerializedName("en_notification_title")
    String name_en;
    @SerializedName("ar_notification_body")
    String body_ar;
    @SerializedName("en_notification_body")
    String body_en;
    @SerializedName("category_image")
    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("id")
    int id;
    @SerializedName("created_at")
    String date;
    @SerializedName("order_id")
    int order_id;
    @SerializedName("type")
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getBody_ar() {
        return body_ar;
    }

    public void setBody_ar(String body_ar) {
        this.body_ar = body_ar;
    }

    public String getBody_en() {
        return body_en;
    }

    public void setBody_en(String body_en) {
        this.body_en = body_en;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
