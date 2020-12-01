package com.example.petshare;

import com.example.petshare.donation.DonationRequest;
import com.example.petshare.donation.DonationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiDonate {

    @Headers({"Accept: application/json"})
    @POST("api/user/donation/create")
    Call<DonationResponse> storeDonation(@Body DonationRequest donationRequest);
}
