package com.khalej.fastcar.model;

import com.google.gson.annotations.SerializedName;

public class Track {
    @SerializedName("latitude")
    double latitude;
    @SerializedName("longitude")
    double longitude;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
