package com.khalej.fastcar.model;

import com.google.gson.annotations.SerializedName;

public class Order {
  @SerializedName("id")
    int id;
   @SerializedName("from_longitude")
    double from_longitude;
   @SerializedName("from_latitude")
    double from_latitude;
   @SerializedName("to_longitude")
    double to_longitude;
   @SerializedName("to_latitude")
    double to_latitude;
   @SerializedName("total_cost")
    double total_cost;
   @SerializedName("payment_method")
    String payment_method;
   @SerializedName("user_id")
    int user_id;
   @SerializedName("driver_id")
    int driver_id;
   @SerializedName("user_name")
   String user_name;
   @SerializedName("user_phone")
   String user_phone;
   @SerializedName("from_address")
   String from_address;
   @SerializedName("to_address")
   String to_address;

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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getFrom_longitude() {
        return from_longitude;
    }

    public void setFrom_longitude(double from_longitude) {
        this.from_longitude = from_longitude;
    }

    public double getFrom_latitude() {
        return from_latitude;
    }

    public void setFrom_latitude(double from_latitude) {
        this.from_latitude = from_latitude;
    }

    public double getTo_longitude() {
        return to_longitude;
    }

    public void setTo_longitude(double to_longitude) {
        this.to_longitude = to_longitude;
    }

    public double getTo_latitude() {
        return to_latitude;
    }

    public void setTo_latitude(double to_latitude) {
        this.to_latitude = to_latitude;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }
}
