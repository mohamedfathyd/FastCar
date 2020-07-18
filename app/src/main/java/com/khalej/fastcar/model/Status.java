package com.khalej.fastcar.model;

import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("id")
    int id;
    @SerializedName("is_busy")
    String is_busy;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIs_busy() {
        return is_busy;
    }

    public void setIs_busy(String is_busy) {
        this.is_busy = is_busy;
    }
}
