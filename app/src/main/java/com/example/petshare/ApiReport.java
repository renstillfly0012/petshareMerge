package com.example.petshare;

import com.example.petshare.reports.ReportResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiReport {


    @Headers({"Accept: application/json"})
    @Multipart
    @POST("user/reports/create")
    Call<ReportResponse> storeReport(
            @Part("user_id") int user_id,
            @Part("address") String address,
            @Part("description") String description,
            @Part MultipartBody.Part image,
            @Part("address_lat") double address_lat,
            @Part("address_lng") double address_lng);


}
