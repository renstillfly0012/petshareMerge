package com.example.petshare;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiUser {

    @Headers({"Accept: application/json"})
    @POST("/api/guest/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

}
