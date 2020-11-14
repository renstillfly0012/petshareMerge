package com.example.petshare;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("name")
    private String fName;
    @SerializedName("email")
    private String Email;
    @SerializedName("password")
    private String password;
    @SerializedName("confirm")
    private String confirm;

    public void setAll(String name, String email, String password, String confirm){
        this.fName = name;
        this.Email = email;
        this.password = password;
        this.confirm = confirm;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
