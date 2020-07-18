package com.khalej.fastcar.model;


import com.google.gson.annotations.SerializedName;

public class Reset {
    @SerializedName("can")
    int can;
    @SerializedName("user_id")
    int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCan() {
        return can;
    }

    public void setCan(int can) {
        this.can = can;
    }
}
