package com.khalej.fastcar.model;

import com.google.gson.annotations.SerializedName;

public class CarDistance {
    @SerializedName("distance")
    String distance;
    @SerializedName("totalPrice")
    double totalPrice;
    @SerializedName("totalTime")
    double totalTime;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }
}
