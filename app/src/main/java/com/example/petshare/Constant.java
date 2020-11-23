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
    public Retrofit retrofits;

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

    public static Retrofit getRetroFit1(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://pet-share.com/api/admin/edit-user/")
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

    public ApiUser getApiUser(){
        return retrofits.create(ApiUser.class);
    }

}
