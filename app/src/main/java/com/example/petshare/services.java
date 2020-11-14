package com.example.petshare;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface services {

    @GET("/api/guest/users")
    Call<List<responseService>> getUsersGet();

    @POST("/api/guest/register")
    Call<List<responseService>> getUsersPost(String name, String email, String password, String confirm);



}
