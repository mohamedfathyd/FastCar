package com.khalej.fastcar.model;

import com.google.gson.annotations.SerializedName;

public class Wallet {
    @SerializedName("id")
    int id;
    @SerializedName("balance")
    double balance;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
