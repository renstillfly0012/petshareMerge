package com.example.petshare;

import com.google.gson.annotations.SerializedName;

public class AdoptRequest {

    @SerializedName("user_id")
    int user_id;
    @SerializedName("requested_pet_id")
    int requested_pet_id;
    @SerializedName("requested_date")
    String requested_date;



    public AdoptRequest(int user_id, int requested_pet_id, String requested_date) {
        this.user_id = user_id;
        this.requested_pet_id = requested_pet_id;
        this.requested_date = requested_date;
    }

    public AdoptRequest() {

    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRequested_pet_id() {
        return requested_pet_id;
    }

    public void setRequested_pet_id(int requested_pet_id) {
        this.requested_pet_id = requested_pet_id;
    }

    public String getRequested_date() {
        return requested_date;
    }

    public void setRequested_date(String requested_date) {
        this.requested_date = requested_date;
    }
}
