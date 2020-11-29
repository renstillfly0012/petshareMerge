package com.example.petshare;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiPets {

    @GET("allpets")
    Call getPets();

    @FormUrlEncoded
    @POST("/api/admin/pets/create")
    Call<RegisterResponsePets> registerPet(@FieldMap Map<String,String> fields);

//    @FormUrlEncoded
//    @POST("/api/admin/pets/create/")
//    Call<responseServicePets> registerPet(
//            //@Field("id") int petId,
//            @Field("name") String addPname,
//            //@Field("image") String addPimage,
//            @Field("age") String addPage,
//            @Field("breed") String addPbreed,
//            @Field("status") String addPstatus,
//            @Field("description") String addPdesc
//    );

////    @Headers({"Accept: application/json"})
//    @POST("/api/admin/pets/create")
//    Call<RegisterResponsePets> registerPet(@Body RegisterRequestPets registerRequestPets);

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

    @DELETE("/api/admin/pets/delete/{id}")
    Call<Void> deletePets(
            @Path("id") int id
    );
}
