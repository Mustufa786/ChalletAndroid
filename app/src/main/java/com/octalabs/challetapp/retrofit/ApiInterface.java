package com.octalabs.challetapp.retrofit;

import com.octalabs.challetapp.models.ModelAddReview;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelBookingHistory.ModelBookingHistory;
import com.octalabs.challetapp.models.ModelChangePassword;
import com.octalabs.challetapp.models.ModelCheckout.ModelCheckout;
import com.octalabs.challetapp.models.ModelCity.CityModel;
import com.octalabs.challetapp.models.ModelCountry.CountryModel;
import com.octalabs.challetapp.models.ModelDetails.ModelChaletsDetails;
import com.octalabs.challetapp.models.ModelLocation.LocationModel;
import com.octalabs.challetapp.models.ModelLogin.LoginModel;
import com.octalabs.challetapp.models.ModelRegister.RegisterModel;
import com.octalabs.challetapp.models.ModelRegisterResponce;
import com.octalabs.challetapp.models.ModelState.StateModel;
import com.octalabs.challetapp.models.ModelWishlist.ModelWishlist;

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

    @PUT("auth/password")
    Call<ModelChangePassword> changePassword(@HeaderMap HashMap<String, String> hashMap, @Body RequestBody body);

    @POST("review")
    Call<ApiResponce<ModelAddReview>> addReView(@HeaderMap HashMap<String, String> hashMap, @Body RequestBody body);

    @POST("filter")
    Call<ApiResponce<ModelAddReview>> filter(@HeaderMap HashMap<String, String> hashMap, @Body RequestBody body);


    @POST("user")
    Call<RegisterModel> register(@Body RequestBody body);

    @POST("user")
    Call<RegisterModel> updateProfile(@HeaderMap HashMap<String, String> hashMap, @Body RequestBody body);

    @GET("country")
    Call<CountryModel> getAllCountries();


    @GET("location")
    Call<LocationModel> getAllLocations();

    @GET("state/{id}")
    Call<StateModel> getAllStates(@Path("id") String id);

    @GET("city/{id}")
    Call<CityModel> getAllCities(@Path("id") String id);

    @GET("item/chalet")
    Call<AllChaletsModel> getAllChalets(@HeaderMap HashMap<String, String> hashMap);


    @GET("item/details/{id}")
    Call<ModelChaletsDetails> getChaletsMarraigeDetails(@Path("id") String id, @HeaderMap HashMap<String, String> hashMap);


    @GET("item/hall")
    Call<AllChaletsModel> getAllMarraiges(@HeaderMap HashMap<String, String> hashMap);

    @POST("wishlist")
    Call<ResponseBody> addtoWishList(@Body RequestBody body, @HeaderMap HashMap<String, String> hashMap);


    @GET("wishlist")
    Call<ModelWishlist> getWishList(@HeaderMap HashMap<String, String> hashMap);

    @POST("checkout")
    Call<ModelCheckout> postCheckout(@Body RequestBody body, @HeaderMap HashMap<String, String> hashMap);


    @POST("Item/search")
    Call<AllChaletsModel> searchResults(@Body RequestBody body , @HeaderMap HashMap<String , String> hashMap);

    @GET("checkout")
    Call<ModelBookingHistory> getBookingHistory(@HeaderMap HashMap<String, String> hashMap);


}