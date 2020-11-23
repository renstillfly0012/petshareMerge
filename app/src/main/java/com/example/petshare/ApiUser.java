package com.example.petshare;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiUser {

    @Headers({"Accept: application/json"})
    @POST("/api/guest/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET("/api/guest/users")
    Call<responseService> getUsersGet();

    @FormUrlEncoded
    @PUT("/api/admin/edit-user/{id}")
    Call<responseService> updateUser(
            @Path("id") int id,
            @Field("name") String name
            //@Field("email") String email,
            //@Field("role_id") String role,
            //@Field("status") String status
            //@Field("password") String password,
            //@Field("confirm") String confirm
    );

    
    @DELETE("/api/admin/delete/{id}")
    Call<Void> deleteUser(
            @Path("id") int id
    );
}
