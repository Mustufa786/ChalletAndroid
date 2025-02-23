package com.octalabs.challetapp.retrofit;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitInstance {

    public static final String BASE_URL = "http://134.209.77.214:6001/api/";
    public static final String BASE_USER_PIC_URL = "http://134.209.77.214:6001/picture/";
    public static final String BASE_IMG_AMENTY_URL = "http://134.209.77.214:6001/amenity/";
    public static final String BASE_IMG_CHALET_URL = "http://134.209.77.214:6001/item/";



    public static Retrofit retrofit;
    public static ApiInterface service;

    public static void createRetrofitInstance() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiInterface.class);
    }

}
