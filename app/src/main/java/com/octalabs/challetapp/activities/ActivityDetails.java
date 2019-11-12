package com.octalabs.challetapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterDetailPicture;
import com.octalabs.challetapp.adapter.AdapterDetailReviews;
import com.octalabs.challetapp.adapter.AdapterDetailsAmenities;
import com.octalabs.challetapp.databinding.ActivityDetailsBinding;
import com.octalabs.challetapp.models.ModelAddReview;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelDetails.AmenityId;
import com.octalabs.challetapp.models.ModelDetails.ChaletDetails;
import com.octalabs.challetapp.models.ModelChangePassword;
import com.octalabs.challetapp.models.ModelDetails.ModelChaletsDetails;
import com.octalabs.challetapp.models.ModelLogin.LoginModel;
import com.octalabs.challetapp.retrofit.ApiResponce;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;
import static java.lang.Double.valueOf;

public class ActivityDetails extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    KProgressHUD hud;

    ActivityDetailsBinding mBinding;

    ArrayList<ChaletDetails> checkoutList;
    SharedPreferences sharedPreferences;
    ChaletDetails chaletDetails;
    Gson gson;

    private RecyclerView mRvPictures, mRvAmenities, mRvReview;
    private TextView mCheckIn, mCheckOut, mPrice, mAddress, mName, mTvRating;
    SupportMapFragment mapFragment;
    private LatLng latLng;
    private RatingBar mRatingBar;
    private String bookingItemID;
    private AlertDialog deleteDialog;
    boolean isUserLoggedIn;
    private int ADD_REVIEW_REQUEST = 123;
    private int ADD_TO_CART_REQUEST = 124;
    private int ADD_TO_WISHLIST_REQUEST = 125;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        init();
        isUserLoggedIn = sharedPreferences.getBoolean(Constants.IS_USER_LOGGED_IN, false);

        getDetails(getIntent().getStringExtra(Constants.CHALET_OR_MARRAIGE_ID));


    }

    private void init() {
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        mBinding.layoutAddToWishlist.setOnClickListener(this);

        mBinding.btnAddToCart.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("main", MODE_PRIVATE);
        gson = new Gson();
        checkoutList = gson.fromJson(sharedPreferences.getString(Constants.USER_CART, "[]"), new TypeToken<List<ChaletDetails>>() {
        }.getType());

        mBinding.layoutAddReview.setOnClickListener(this);
        mRvPictures = findViewById(R.id.rv_details);
        mRvReview = findViewById(R.id.rv_detail_review);
        mRvAmenities = findViewById(R.id.rv_amenities);
        mCheckIn = findViewById(R.id.time_check_in);
        mCheckOut = findViewById(R.id.time_check_out);
        mPrice = findViewById(R.id.chalet_price_nit);
        mName = findViewById(R.id.chalet_name);
        mAddress = findViewById(R.id.chalet_address);
        mTvRating = findViewById(R.id.tv_rating);
        mRatingBar = findViewById(R.id.chalet_rating);


    }

    private void getDetails(String id) {
        hud.show();
        Call<ModelChaletsDetails> call = RetrofitInstance.service.getChaletsMarraigeDetails(id, Helper.getJsonHeader());
        call.enqueue(new Callback<ModelChaletsDetails>() {
            @Override
            public void onResponse(Call<ModelChaletsDetails> call, Response<ModelChaletsDetails> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    ModelChaletsDetails model = response.body();
                    if (model.getSuccess()) {
                        chaletDetails = model.getData();

                        AdapterDetailPicture adapter = new AdapterDetailPicture(ActivityDetails.this, model.getData().getPicture());
                        mRvPictures.setAdapter(adapter);

                        AdapterDetailsAmenities adapteramenities = new AdapterDetailsAmenities(ActivityDetails.this, model.getData().getAmenityIds());
                        mRvAmenities.setAdapter(adapteramenities);

                        AdapterDetailReviews adapterReview = new AdapterDetailReviews(ActivityDetails.this, model.getData().getReviews());
                        mRvReview.setAdapter(adapterReview);
                        latLng = new LatLng(valueOf(model.getData().getLatitude()), valueOf(model.getData().getLongitude()));
                        mapFragment.getMapAsync(ActivityDetails.this);
                        mPrice.setText(model.getData().getPricePerNight() + " Riyal");
                        mAddress.setText(model.getData().getLocation() + "");
                        mCheckIn.setText(model.getData().getCheckIn() + "");
                        mCheckOut.setText(model.getData().getCheckOut() + "");
                        mName.setText(model.getData().getName() + "");
                        mTvRating.setText(model.getData().getRating() + "");
                        if (model.getData().getRating() > 0)
                            mBinding.chaletRating.setRating(model.getData().getRating() + 0f);

                        bookingItemID = model.getData().getId();
                        setTextAction(Objects.requireNonNull(getSupportActionBar()), model.getData().getName() + "");
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                    } else {
                        displayDialog("Alert", model.getMessage(), ActivityDetails.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelChaletsDetails> call, Throwable t) {
                hud.dismiss();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }


    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(ActivityDetails.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(16);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_add_to_wishlist:
                if (isUserLoggedIn) {
                    addToWishList();

                } else {
                    Intent intent = new Intent(ActivityDetails.this, ActivityLogin.class);
                    startActivityForResult(intent, ADD_TO_WISHLIST_REQUEST);
                }
                break;

            case R.id.btn_add_to_cart:
//                if (isUserLoggedIn) {
//
//                } else {
//                    Intent intent = new Intent(ActivityDetails.this, ActivityLogin.class);
//                    startActivityForResult(intent, ADD_TO_CART_REQUEST);
//                }
                addToLocalCart();
                break;

            case R.id.layout_add_review:
                if (isUserLoggedIn) {
                    openReviewDialogBox();
                } else {
                    Intent intent = new Intent(ActivityDetails.this, ActivityLogin.class);
                    startActivityForResult(intent, ADD_REVIEW_REQUEST);
                }


                break;
        }
    }

    private void openReviewDialogBox() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_review, null);
        deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteDialog.setView(deleteDialogView);
        deleteDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));
        final EditText mEdtComment = deleteDialogView.findViewById(R.id.comment);
        final RatingBar mRatingBar = deleteDialogView.findViewById(R.id.review_rating);
//                final EditText mUserName = deleteDialogView.findViewById(R.id.user_name);

        Button mBtnAddReview = deleteDialogView.findViewById(R.id.btn_add_review);
        deleteDialog.setCancelable(true);
        mBtnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEdtComment.getText().toString().equalsIgnoreCase("") && !bookingItemID.equalsIgnoreCase("")) {
                    addReview(deleteDialog, mEdtComment.getText().toString(), bookingItemID);
                }
            }
        });
        deleteDialog.show();
    }

    private void addToLocalCart() {

        int index = Collections.binarySearch(checkoutList, chaletDetails, new Comparator<ChaletDetails>() {
            @Override
            public int compare(ChaletDetails chalet, ChaletDetails t1) {
                return chalet.getId().compareTo(t1.getId());
            }
        });
        if (index >= 0) {

            Toast.makeText(ActivityDetails.this, "Item Already Added to Cart", Toast.LENGTH_SHORT).show();

        } else {
            checkoutList.add(chaletDetails);
            getSharedPreferences("main", Context.MODE_PRIVATE).edit().putString(Constants.USER_CART, new Gson().toJson(checkoutList)).apply();

            Toast.makeText(ActivityDetails.this, "Item Successfully Added to Cart", Toast.LENGTH_SHORT).show();


        }

    }

    private void addReview(final AlertDialog deleteDialog, String comment, final String bookingItemId) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("comment", comment);
            jsonObject.put("bookingItemId", bookingItemId);
            final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            Call<ApiResponce<ModelAddReview>> call = RetrofitInstance.service.addReView(Helper.getJsonHeaderWithToken(this), requestBody);


            hud.show();
            call.enqueue(new Callback<ApiResponce<ModelAddReview>>() {
                @Override
                public void onResponse(Call<ApiResponce<ModelAddReview>> call, Response<ApiResponce<ModelAddReview>> response) {
                    try {
                        hud.dismiss();
                        if (response.body() != null) {
                            if (response.body().isSuccess) {
                                if (response.body().msg.equalsIgnoreCase("Review added successfully")) {
                                    addRating(deleteDialog, bookingItemId);
                                }
                            } else {
                                deleteDialog.dismiss();
                            }
                        } else {
                            deleteDialog.dismiss();
                        }
                    } catch (Exception e) {
                        deleteDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponce<ModelAddReview>> call, Throwable t) {
                    t.printStackTrace();
                    hud.dismiss();
                    deleteDialog.dismiss();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addRating(final AlertDialog deleteDialog, String bookingItemId) {
        try {
            final RatingBar mRatingBar = deleteDialog.findViewById(R.id.review_rating);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rating", mRatingBar.getRating() + "");
            jsonObject.put("bookingItemId", bookingItemId);
            final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            Call<ApiResponce<ModelAddReview>> call = RetrofitInstance.service.addRating(Helper.getJsonHeaderWithToken(this), requestBody);


            hud.show();
            call.enqueue(new Callback<ApiResponce<ModelAddReview>>() {
                @Override
                public void onResponse(Call<ApiResponce<ModelAddReview>> call, Response<ApiResponce<ModelAddReview>> response) {
                    try {
                        hud.dismiss();
                        if (response.body() != null) {
                            if (response.body().isSuccess) {
                                if (response.body().msg.equalsIgnoreCase("Rating added successfully")) {
                                    getDetails(getIntent().getStringExtra(Constants.CHALET_OR_MARRAIGE_ID));
                                    deleteDialog.dismiss();
                                }
                            } else {

                            }
                        } else {
                            deleteDialog.dismiss();
                        }
                    } catch (Exception e) {
                        deleteDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponce<ModelAddReview>> call, Throwable t) {
                    t.printStackTrace();
                    hud.dismiss();
                    deleteDialog.dismiss();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addToWishList() {
        try {
            hud.show();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bookingItemId", getIntent().getStringExtra(Constants.CHALET_OR_MARRAIGE_ID));
            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            Call<ResponseBody> call = RetrofitInstance.service.addtoWishList(requestBody, Helper.getJsonHeaderWithToken(this));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    hud.dismiss();
                    try {

                        if (response.body() != null) {
                            JSONObject jsonObject1 = new JSONObject(response.body().string());
                            if (jsonObject1.getBoolean("success")) {
                                Toast.makeText(ActivityDetails.this, "Added to WishList", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivityDetails.this, "Already Exist In Your WishList", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hud.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if (deleteDialog != null && deleteDialog.isShowing()) {
            deleteDialog.dismiss();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_REVIEW_REQUEST) {
                openReviewDialogBox();
            } else if (requestCode == ADD_TO_CART_REQUEST) {
                addToLocalCart();
            } else if (requestCode == ADD_TO_WISHLIST_REQUEST) {
                addToWishList();
            }

        }
    }
}

