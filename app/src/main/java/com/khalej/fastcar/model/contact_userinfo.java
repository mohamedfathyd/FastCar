package com.khalej.fastcar.model;
import com.google.gson.annotations.SerializedName;


public class contact_userinfo {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("phone")
    String phone;
    @SerializedName("phone_code")
    String phone_code;
    @SerializedName("email")
    String maddress;
    @SerializedName("password")
    String Password;
    @SerializedName("logo")
    String image;
    @SerializedName("created_at")
    String createdAt;
    @SerializedName("country_id")
    int country_id;
    @SerializedName("type")
    int usertype;
    @SerializedName("user")
    user user;
    @SerializedName("user_type")
    String user_type;
    @SerializedName("is_busy")
    String is_busy;

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getIs_busy() {
        return is_busy;
    }

    public void setIs_busy(String is_busy) {
        this.is_busy = is_busy;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public com.khalej.fastcar.model.user getUser() {
        return user;
    }

    public void setUser(com.khalej.fastcar.model.user user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMaddress() {
        return maddress;
    }

    public void setMaddress(String maddress) {
        this.maddress = maddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }
}
