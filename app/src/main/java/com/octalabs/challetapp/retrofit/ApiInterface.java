package com.octalabs.challetapp.retrofit;

import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelCity.CityModel;
import com.octalabs.challetapp.models.ModelCountry.CountryModel;
import com.octalabs.challetapp.models.ModelLogin.LoginModel;
import com.octalabs.challetapp.models.ModelRegisterResponce;
import com.octalabs.challetapp.models.ModelState.StateModel;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST
    @Multipart
    Call<ApiResponce<ModelRegisterResponce>> userregister(
            @Part("userName") RequestBody userName, @Part("email") RequestBody email,
            @Part("password") RequestBody password, @Part("mobileNo") RequestBody mobileNo,
            @Part("address") RequestBody address, @Part("role") RequestBody role, @Part MultipartBody.Part image);


    @PUT("auth")
    Call<LoginModel> loginUser(@Body RequestBody body);



    @POST("user")
    Call<ApiResponce<ModelRegisterResponce>> register(@Body RequestBody body);

    @GET("country")
    Call<CountryModel> getAllCountries();

    @GET("state/{id}")
    Call<StateModel> getAllStates(@Path("id") String id);

    @GET("city/{id}")
    Call<CityModel> getAllCities(@Path("id") String id);

    @GET("item/chalet")
    Call<AllChaletsModel> getAllChalets(@HeaderMap HashMap<String, String> hashMap);


    @GET("item/details/{id}")
    Call<AllChaletsModel> getChaletsMarraigeDetails(@Path("id") String id, @HeaderMap HashMap<String, String> hashMap);


    @GET("item/hall")
    Call<AllChaletsModel> getAllMarraiges(@HeaderMap HashMap<String, String> hashMap);

    @POST("wishlist")
    Call<ResponseBody> postWishList(@Body RequestBody body, @HeaderMap HashMap<String, String> hashMap);


    @GET("wishlist")
    Call<ResponseBody> getWishList(@HeaderMap HashMap<String, String> hashMap);



}