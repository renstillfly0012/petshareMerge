package com.example.petshare;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiPets {

    @GET("allpets")
    Call getPets();

    @FormUrlEncoded
    @PUT("updatePet/{id}")
    Call<RegisterResponse> updatePets(
            @Path("id") int id,
            @Field("name") String Pname,
            @Field("image") String Pimage,
            @Field("age") String Page,
            @Field("breed") String Pbreed,
            @Field("status") String Pstatus,
            @Field("description") String Pdesc
    );
}
