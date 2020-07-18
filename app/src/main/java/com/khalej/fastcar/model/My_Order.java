package com.khalej.fastcar.model;

import com.google.gson.annotations.SerializedName;

public class My_Order {
    @SerializedName("id")
    int id;
    @SerializedName("from_address")
    String from_address;
    @SerializedName("to_address")
    String to_address;
    @SerializedName("created_at")
    String date;
    @SerializedName("time_spant")
    String time_spanttime_spant;
    @SerializedName("client_rate")
    double client_rate;
    @SerializedName("start_time")
    String start_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_spanttime_spant() {
        return time_spanttime_spant;
    }

    public void setTime_spanttime_spant(String time_spanttime_spant) {
        this.time_spanttime_spant = time_spanttime_spant;
    }

    public double getClient_rate() {
        return client_rate;
    }

    public void setClient_rate(double client_rate) {
        this.client_rate = client_rate;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
}
