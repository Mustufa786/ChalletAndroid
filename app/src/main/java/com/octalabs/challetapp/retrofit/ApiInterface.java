package com.octalabs.challetapp.retrofit;

import com.octalabs.challetapp.models.ModelRegisterResponce;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @POST
    @Multipart
    Call<ApiResponce<ModelRegisterResponce>> userregister(
            @Part("userName") RequestBody userName, @Part("email") RequestBody email,
            @Part("password") RequestBody password, @Part("mobileNo") RequestBody mobileNo,
            @Part("address")  RequestBody address, @Part("role") RequestBody role, @Part MultipartBody.Part image);

}