package com.octalabs.challetapp.retrofit;

import com.octalabs.challetapp.models.ModelCountry.CountryModel;
import com.octalabs.challetapp.models.ModelRegisterResponce;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @POST
    @Multipart
    Call<ApiResponce<ModelRegisterResponce>> userregister(
            @Part("userName") RequestBody userName, @Part("email") RequestBody email,
            @Part("password") RequestBody password, @Part("mobileNo") RequestBody mobileNo,
            @Part("address") RequestBody address, @Part("role") RequestBody role, @Part MultipartBody.Part image);


    @POST("user")
    Call<ApiResponce<ModelRegisterResponce>> register(@Body RequestBody body);

    @GET("country")
    Call<CountryModel> getAllCountries();




}