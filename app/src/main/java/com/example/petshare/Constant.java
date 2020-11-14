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


    public static Retrofit getRetroFit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(URL)
                .client(okHttpClient)
                .build();

        return retrofit;

    }

    public  static  ApiUser getService(){
        ApiUser getservice = getRetroFit().create(ApiUser.class);

        return getservice;
    }

}
