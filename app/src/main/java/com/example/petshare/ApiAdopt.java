package com.example.petshare;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiAdopt {

    @Headers({"Accept: application/json"})
    @POST("api/user/adoptPet")
    Call<AdoptResponse> bookAnAppointment(@Body AdoptRequest adoptRequest);
    Call<List<responseService>> bookAnAppointment(int user_id, int requested_pet_id, String requested_date);
}
