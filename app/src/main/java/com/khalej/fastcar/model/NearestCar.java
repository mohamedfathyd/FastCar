package com.khalej.fastcar.model;

import com.google.gson.annotations.SerializedName;

public class NearestCar {
    @SerializedName("logo")
    String logo;
    @SerializedName("rating")
    double rating;
    @SerializedName("phone")
    String phone;
    @SerializedName("name")
    String name;
    @SerializedName("phone_code")
    String phone_code;
    @SerializedName("arrival_time")
    double arrival_time;
    @SerializedName("car")
    Car car;

    public double getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(double arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public class Car{
       @SerializedName("brand")
       String brand;
       @SerializedName("model")
       String model;
       @SerializedName("number")
       String number;
       @SerializedName("driver_id")
       int driver_id;

       public String getBrand() {
           return brand;
       }

       public void setBrand(String brand) {
           this.brand = brand;
       }

       public String getModel() {
           return model;
       }

       public void setModel(String model) {
           this.model = model;
       }

       public String getNumber() {
           return number;
       }

       public void setNumber(String number) {
           this.number = number;
       }

       public int getDriver_id() {
           return driver_id;
       }

       public void setDriver_id(int driver_id) {
           this.driver_id = driver_id;
       }
   }

}
