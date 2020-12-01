package com.example.petshare.reports;

import com.google.gson.annotations.SerializedName;

public class ReportRequest {
    @SerializedName("user_id")
    int user_id;
    @SerializedName("address")
    String address;
    @SerializedName("description")
    String description;
    @SerializedName("image")
    String image;
    @SerializedName("address_lat")
    double address_lat;
    @SerializedName("address_lng")
    double address_lng;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getAddress_lat() {
        return address_lat;
    }

    public void setAddress_lat(double address_lat) {
        this.address_lat = address_lat;
    }

    public double getAddress_lng() {
        return address_lng;
    }

    public void setAddress_lng(double address_lng) {
        this.address_lng = address_lng;
    }
}
