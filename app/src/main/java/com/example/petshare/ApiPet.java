package com.example.petshare;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiPet {

    @Headers({"Accept: application/json"})
    @GET("api/user/viewallpets")
    Call<List<PetResponse>> getAllPets();
}
