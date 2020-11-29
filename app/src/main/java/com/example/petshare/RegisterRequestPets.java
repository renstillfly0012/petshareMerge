package com.example.petshare;

import com.google.gson.annotations.SerializedName;

public class RegisterRequestPets {


    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("age")
    private String age;

    @SerializedName("breed")
    private String breed;

    @SerializedName("status")
    private String status;

    @SerializedName("description")
    private String description;

    public void setAll(String name, String image, String age, String breed, String status, String description) {
        this.name = name;
        this.image = image;
        this.age = age;
        this.breed = breed;
        this.status = status;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
