package com.octalabs.challetapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.RVOnItemClicks.OnItemClicked;
import com.octalabs.challetapp.adapter.AdapterAmenities;
import com.octalabs.challetapp.databinding.ActivityFilterBinding;
import com.octalabs.challetapp.models.ModelAddReview;
import com.octalabs.challetapp.models.ModelDetails.AmenityId;
import com.octalabs.challetapp.retrofit.ApiResponce;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFilter extends AppCompatActivity implements OnItemClicked<AmenityId> {

    ActivityFilterBinding binding;
    private KProgressHUD hud;
    ArrayList<AmenityId> mAmenities;
    ArrayList<String> mSelectedAmenities;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        hud = new KProgressHUD(this);
        mAmenities = new ArrayList<>();
        setTextAction(getSupportActionBar(), getResources().getString(R.string.filter));
        getamenities();
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ament[] = new String[mSelectedAmenities.size()];
                String[] bookingType = new String[2];
                String[] Chaletfor = new String[3];
                String[] ChaletorHall = new String[2];

                for (int i = 0; i < mSelectedAmenities.size(); i++) {
                    ament[i] = mSelectedAmenities.get(i);
                }

                if (binding.chkChalet.isChecked() && binding.chkHall.isChecked()) {
                    ChaletorHall[0] = "Chalet";
                    ChaletorHall[1] = "Marriage Hall";
                } else if (binding.chkChalet.isChecked()) {
                    ChaletorHall[0] = "Chalet";
                } else if (binding.chkHall.isChecked()) {
                    ChaletorHall[0] = "Marriage Hall";
                }

                if (binding.chkCheckBooking.isChecked() && binding.chkInstantBooking.isChecked()) {
                    bookingType[0] = "Instant";
                    bookingType[1] = "Check Availablity";
                } else if (binding.chkInstantBooking.isChecked()) {
                    bookingType[0] = "Instant";
                } else if (binding.chkCheckBooking.isChecked()) {
                    bookingType[0] = "Check Availablity";
                }

                if (binding.chkForFamily.isChecked() && binding.chkForSingle.isChecked()&& binding.chkForOccasion.isChecked()) {
                    Chaletfor[0] = "Singles";
                    Chaletfor[1] = "Families";
                    Chaletfor[2] = "Occasions";
                } else if (binding.chkForSingle.isChecked()) {
                    Chaletfor[0] = "Singles";
                } else if (binding.chkForFamily.isChecked()) {
                    Chaletfor[0] = "Families";
                } else if (binding.chkForOccasion.isChecked()) {
                    Chaletfor[0] = "Occasions";
                }


                filter(String.valueOf(binding.chaletRating.getRating()), binding.tvMin.getText().toString(), binding.tvMax.getText().toString(), ament, bookingType, Chaletfor, ChaletorHall);
            }
        });

        binding.rangeSeekbar5.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                binding.tvMin.setText(String.valueOf(minValue));
                binding.tvMax.setText(String.valueOf(maxValue));
            }
        });

    }

    private void getamenities() {
        AmenityId amenityId = new AmenityId();
        amenityId.setId("5db97e186a6d4e634d9a110e");
        amenityId.setTitle("Swimming Pool");
        AmenityId amenityId1 = new AmenityId();
        amenityId1.setId("5db97e2f6a6d4e634d9a110f");
        amenityId1.setTitle("Baby Toys");
        mAmenities.add(amenityId);
        mAmenities.add(amenityId1);
        mSelectedAmenities = new ArrayList<>();
        AdapterAmenities adapterAmenities = new AdapterAmenities(this, mAmenities, this);
        binding.rvAmenities.setAdapter(adapterAmenities);

    }

    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(ActivityFilter.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(16);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
    }


    private void filter(String rating, String priceFrom, String priceTo, String[] amenity, String[] bookingType, String[] for1, String[] type) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rating", rating);
            jsonObject.put("priceFrom", priceFrom);
            jsonObject.put("priceTo", priceTo);
            jsonObject.put("amenity", amenity);
            jsonObject.put("bookingType", bookingType);
            jsonObject.put("for", for1);
            jsonObject.put("type", type);
            final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            Call<ApiResponce<ModelAddReview>> call = RetrofitInstance.service.filter(Helper.getJsonHeaderWithToken(this), requestBody);


            hud.show();
            call.enqueue(new Callback<ApiResponce<ModelAddReview>>() {
                @Override
                public void onResponse(Call<ApiResponce<ModelAddReview>> call, Response<ApiResponce<ModelAddReview>> response) {
                    try {
                        hud.dismiss();
                        if (response.body() != null) {
                            if (response.body().isSuccess) {
                                if (response.body().msg.equalsIgnoreCase("Review added successfully")) {

                                }
                            } else {

                            }
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponce<ModelAddReview>> call, Throwable t) {
                    t.printStackTrace();
                    hud.dismiss();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    @Override
    public void onClick(int pos, AmenityId obj) {

    }

    @Override
    public void onClick(boolean checked, int pos, AmenityId obj) {
        if (checked) {
            mSelectedAmenities.add(obj.getId());
        } else {
            mSelectedAmenities.remove(obj.getId());
        }
    }
}
