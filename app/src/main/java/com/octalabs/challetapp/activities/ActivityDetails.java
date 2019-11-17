package com.octalabs.challetapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.Dialog;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.octalabs.challetapp.fragments.FragmentSearch;
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
import com.octalabs.challetapp.utils.FusedLocationTracker;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;
import static java.lang.Double.valueOf;

public class ActivityDetails extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, FusedLocationTracker.IOnUpdateListener {

    private static final int ALLOW_OPEN_LOCATION = 222;
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
    private MaterialRatingBar mRatingBar;
    private String bookingItemID;
    private AlertDialog deleteDialog;
    boolean isUserLoggedIn;
    private int ADD_REVIEW_REQUEST = 123;
    private int ADD_TO_CART_REQUEST = 124;
    private int ADD_TO_WISHLIST_REQUEST = 125;
    FusedLocationTracker mFusedLocationTracker;
    private static final int RC_LOCATION_PERMISSION = 12345;
    ProgressDialog mProgressDialog;
    Calendar myCalendar;
    boolean isCheckInDate;
    long myDateCheckIn, myDateCheckOut;
    private Integer numOfBookingDays = 1;

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


        mBinding.layoutCheckIn.setOnClickListener(this);
        mBinding.layoutCheckOut.setOnClickListener(this);


        if (FragmentSearch.checkInDateStr != null) {
            mBinding.timeCheckIn.setText(FragmentSearch.checkInDateStr);
            mBinding.timeCheckOut.setText(FragmentSearch.checkOutDateStr);
            myDateCheckIn = FragmentSearch.checkInStr;
            myDateCheckOut = FragmentSearch.checkoutStr;
            numOfBookingDays = FragmentSearch.noOfDays;
            mBinding.textPrice.setText("Price for " + numOfBookingDays + " night");


        } else {
            myCalendar = Calendar.getInstance();
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            myDateCheckIn = myCalendar.getTimeInMillis();
            mBinding.timeCheckIn.setText(sdf.format(myCalendar.getTime()));


            myCalendar.add(Calendar.DAY_OF_MONTH, 1);
            myDateCheckOut = myCalendar.getTimeInMillis();
            mBinding.timeCheckOut.setText(sdf.format(myCalendar.getTime()));

        }


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
                        if (model.getData().getLatitude() != null && !model.getData().getLatitude().equalsIgnoreCase("")) {
                            latLng = new LatLng(valueOf(model.getData().getLatitude()), valueOf(model.getData().getLongitude()));

                        }
                        mapFragment.getMapAsync(ActivityDetails.this);
                        int totalPrice = model.getData().getPricePerNight() * numOfBookingDays;
                        mPrice.setText(totalPrice + " Riyal For " + numOfBookingDays + " Days");
                        mAddress.setText(model.getData().getLocation() + "");

                        mName.setText(model.getData().getName() + "");
                        mTvRating.setText(model.getData().getRating() + "");
                        if (model.getData().getRating() > 0)
                            mBinding.chaletRating.setRating(model.getData().getRating() + 0f);

                        bookingItemID = model.getData().getId();
                        setTextAction(Objects.requireNonNull(getSupportActionBar()), model.getData().getName() + "");
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);


                        if (model.getData().getWhatsapp() != null) {
                            mBinding.imgWhatsApp.setVisibility(View.VISIBLE);
                            mBinding.imgSmsIcon.setVisibility(View.VISIBLE);

                        }

                        if (model.getData().getFacebook() != null) {
                            mBinding.imgFacebook.setVisibility(View.VISIBLE);
                        }
                        if (model.getData().getEmail() != null) {
                            mBinding.imgEmailIcon.setVisibility(View.VISIBLE);
                        }
                        if (model.getData().getTwitter() != null) {
                            mBinding.imgTwitterIcon.setVisibility(View.VISIBLE);
                        }
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


            case R.id.layout_check_in:
                isCheckInDate = true;
                myCalendar = Calendar.getInstance();
                myCalendar.add(Calendar.DAY_OF_MONTH, 1);
                new DatePickerDialog(ActivityDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;


            case R.id.layout_check_out:
                isCheckInDate = false;
                myCalendar = Calendar.getInstance();
                new DatePickerDialog(ActivityDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (isCheckInDate) {
            mBinding.timeCheckIn.setText(sdf.format(myCalendar.getTime()));
            myDateCheckIn = myCalendar.getTimeInMillis();

        } else {
            mBinding.timeCheckOut.setText(sdf.format(myCalendar.getTime()));
            myDateCheckOut = myCalendar.getTimeInMillis();

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
        final MaterialRatingBar mRatingBar = deleteDialogView.findViewById(R.id.review_rating);
//                final EditText mUserName = deleteDialogView.findViewById(R.id.user_name);

        Button mBtnAddReview = deleteDialogView.findViewById(R.id.btn_add_review);
        deleteDialog.setCancelable(true);
        mBtnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEdtComment.getText().toString().equalsIgnoreCase("") && !bookingItemID.equalsIgnoreCase("")) {
                    addReview(deleteDialog, mRatingBar.getRating(), mEdtComment.getText().toString(), bookingItemID);
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
            chaletDetails.setCheckIn(myDateCheckIn + "");
            chaletDetails.setCheckOut(myDateCheckOut + "");
            checkoutList.add(chaletDetails);
            getSharedPreferences("main", Context.MODE_PRIVATE).edit().putString(Constants.USER_CART, new Gson().toJson(checkoutList)).apply();

            Toast.makeText(ActivityDetails.this, "Item Successfully Added to Cart", Toast.LENGTH_SHORT).show();


        }

    }

    private void addReview(final AlertDialog deleteDialog, float rating, String comment, final String bookingItemId) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("comment", comment);
            jsonObject.put("bookingItemId", bookingItemId);
            jsonObject.put("rating", rating + "");
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
                                    getDetails(getIntent().getStringExtra(Constants.CHALET_OR_MARRAIGE_ID));
                                    deleteDialog.dismiss();
//                                    addRating(deleteDialog, bookingItemId);
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
//                                    getDetails(getIntent().getStringExtra(Constants.CHALET_OR_MARRAIGE_ID));
//                                    deleteDialog.dismiss();
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
        if (latLng != null) {
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            googleMap.getUiSettings().setAllGesturesEnabled(false);

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (chaletDetails.getLatitude() != null && chaletDetails.getLongitude() != null) {
                        checkPermissionsAndExecute();
                    } else {
                        Toast.makeText(ActivityDetails.this, "Location Not Available For The Item", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
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
            } else if (requestCode == ALLOW_OPEN_LOCATION) {
                if (resultCode == RESULT_OK) {
                    captureUserLocation();
                }
            }

        }
    }


    @AfterPermissionGranted(RC_LOCATION_PERMISSION)
    private void checkPermissionsAndExecute() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(ActivityDetails.this, perms)) {
            captureUserLocation();
        } else {
            methodRequiresTwoPermission();
        }
    }

    private void buildAlertMessageNoGps() {
        Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), ALLOW_OPEN_LOCATION);
    }

    private void captureUserLocation() {

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return;
        }


        mProgressDialog = new ProgressDialog(ActivityDetails.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Farhty");
        mProgressDialog.setMessage("Please wait while we are working on fetching your location");
        mProgressDialog.show();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mFusedLocationTracker == null) {
                    mFusedLocationTracker = new FusedLocationTracker(ActivityDetails.this, ActivityDetails.this);
                }
                mFusedLocationTracker.startLocationUpdates();
            }
        }, 500);

    }

    @AfterPermissionGranted(RC_LOCATION_PERMISSION)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(ActivityDetails.this, perms)) {
            captureUserLocation();

        } else {
            EasyPermissions.requestPermissions(this, "Farhty would like to get your location to show directions on google map",
                    RC_LOCATION_PERMISSION, perms);
        }
    }

    @Override
    public void onLocationUpdate(Location location) {
        mFusedLocationTracker.stopLocationUpdates();
        if (mProgressDialog.isShowing() && mProgressDialog != null) {
            mProgressDialog.dismiss();
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", location.getLatitude(), location.getLongitude(), Double.parseDouble(chaletDetails.getLatitude()), Double.parseDouble(chaletDetails.getLongitude()));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }
    }
}

