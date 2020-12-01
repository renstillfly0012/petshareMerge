package com.example.petshare;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constant {
    public static final String  URL = "https://pet-share.com";
    public static final String  HOME = URL+"/api";
    public static final String  LOGIN = HOME+"/guest/login";
    public static final String  REGISTER = HOME+"/guest/register/";
    private static Constant mInstance;
    private static Retrofit retrofit;

    public static Retrofit getRetroFit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(URL)
                .client(okHttpClient)
                .build();
        return retrofit;

    }

    public static synchronized Constant getInstance(){
        if(mInstance == null){
            mInstance = new Constant();
        }
        return mInstance;
    }

    public  static  ApiUser getService(){

        return getRetroFit().create(ApiUser.class);
    }

    public  static  ApiPets getServicePets(){

        return getRetroFit().create(ApiPets.class);
    }

    static Retrofit getApiClient(){

        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

    public ApiUser getApiUser(){
        return retrofit.create(ApiUser.class);
    }

    public static ApiPet getPetService(){
        ApiPet getservice = getRetroFit().create(ApiPet.class);

        return getservice;
    }

    public static ApiAdopt getAdoptService(){
        ApiAdopt getservice = getRetroFit().create(ApiAdopt.class);

        return getservice;
    }

    public static ApiDonate getDonateService(){

        ApiDonate getservice = getRetroFit().create(ApiDonate.class);

        return getservice;

    }

    public static ApiReport getReportService(){

        ApiReport getservice = getRetroFit().create(ApiReport.class);

        return getservice;
    }

}

