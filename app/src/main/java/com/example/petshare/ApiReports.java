package com.example.petshare;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiReports {

    @FormUrlEncoded
    @PUT("https://pet-share.com/api/admin/reports/all/{id}")
    Call<Void> approveRequest(
            @Path("id") int id,
            @Field("report_status") String status
    );

    @FormUrlEncoded
    @PUT("https://pet-share.com/api/admin/reports/all/{id}")
    Call<Void> declineRequest(
            @Path("id") int id,
            @Field("report_status") String status
    );
}
