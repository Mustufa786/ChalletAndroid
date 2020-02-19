package com.octalabs.challetapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.RVOnItemClicks.OnItemClicked;
import com.octalabs.challetapp.adapter.AdapterAmenities;
import com.octalabs.challetapp.databinding.ActivityFilterBinding;
import com.octalabs.challetapp.models.ModelAddReview;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelAmeneties.Amenity;
import com.octalabs.challetapp.models.ModelAmeneties.ModelAmenety;
import com.octalabs.challetapp.models.ModelDetails.AmenityId;
import com.octalabs.challetapp.retrofit.ApiResponce;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFilter extends AppCompatActivity implements OnItemClicked<Amenity> {

    ActivityFilterBinding binding;
    private KProgressHUD hud;
    ArrayList<Amenity> mAmenities;
    ArrayList<String> mSelectedAmenities;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        hud = new KProgressHUD(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        mAmenities = new ArrayList<>();
        setTextAction(getSupportActionBar(), getResources().getString(R.string.filter));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));
        getamenities();
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ament[] = new String[mSelectedAmenities.size()];
                String[] bookingType = new String[0];
                String[] Chaletfor = new String[0];
                String[] ChaletorHall = new String[0];

                for (int i = 0; i < mSelectedAmenities.size(); i++) {
                    ament[i] = mSelectedAmenities.get(i);
                }

                if (binding.chkChalet.isChecked() && binding.chkHall.isChecked()) {
                    ChaletorHall = new String[2];
                    ChaletorHall[0] = "Chalet";
                    ChaletorHall[1] = "Marriage Hall";
                } else if (binding.chkChalet.isChecked()) {
                    ChaletorHall = new String[1];
                    ChaletorHall[0] = "Chalet";
                } else if (binding.chkHall.isChecked()) {
                    ChaletorHall = new String[1];
                    ChaletorHall[0] = "Marriage Hall";
                }

                if (binding.chkCheckBooking.isChecked() && binding.chkInstantBooking.isChecked()) {
                    bookingType = new String[2];
                    bookingType[0] = "Instant";
                    bookingType[1] = "Check Availablity";
                } else if (binding.chkInstantBooking.isChecked()) {
                    bookingType = new String[1];
                    bookingType[0] = "Instant";
                } else if (binding.chkCheckBooking.isChecked()) {
                    bookingType = new String[1];
                    bookingType[0] = "Check Availablity";
                }

                if (binding.chkForFamily.isChecked() && binding.chkForSingle.isChecked() && binding.chkForOccasion.isChecked()) {
                    Chaletfor = new String[3];
                    Chaletfor[0] = "Singles";
                    Chaletfor[1] = "Families";
                    Chaletfor[2] = "Occasions";
                } else if (binding.chkForFamily.isChecked() && binding.chkForSingle.isChecked() /*&& binding.chkForOccasion.isChecked()*/) {
                    Chaletfor = new String[2];
                    Chaletfor[0] = "Singles";
                    Chaletfor[1] = "Families";
                } else if (/*binding.chkForFamily.isChecked() && */binding.chkForSingle.isChecked() && binding.chkForOccasion.isChecked()) {
                    Chaletfor = new String[2];
                    Chaletfor[0] = "Singles";
                    Chaletfor[1] = "Occasions";
                } else if (binding.chkForFamily.isChecked() /*&& binding.chkForSingle.isChecked()*/ && binding.chkForOccasion.isChecked()) {
                    Chaletfor = new String[2];
                    Chaletfor[0] = "Families";
                    Chaletfor[1] = "Occasions";
                } else if (binding.chkForSingle.isChecked()) {
                    Chaletfor = new String[1];
                    Chaletfor[0] = "Singles";
                } else if (binding.chkForFamily.isChecked()) {
                    Chaletfor = new String[1];
                    Chaletfor[0] = "Families";
                } else if (binding.chkForOccasion.isChecked()) {
                    Chaletfor = new String[1];
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
        hud.show();
        Call<ModelAmenety> call = RetrofitInstance.service.getAllAmeneites(Helper.getJsonHeader());
        call.enqueue(new Callback<ModelAmenety>() {
            @Override
            public void onResponse(Call<ModelAmenety> call, Response<ModelAmenety> response) {
                hud.dismiss();
                if (response.body() != null) {
                    ModelAmenety modelAmenety = response.body();
                    if (modelAmenety.getSuccess()) {
                        mAmenities = new ArrayList<>(modelAmenety.getData());
                        AdapterAmenities adapterAmenities = new AdapterAmenities(ActivityFilter.this, mAmenities, ActivityFilter.this);
                        binding.rvAmenities.setAdapter(adapterAmenities);

                    }
                }
            }

            @Override
            public void onFailure(Call<ModelAmenety> call, Throwable t) {
                hud.dismiss();
            }
        });


        mSelectedAmenities = new ArrayList<>();


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
            JSONArray amentiy = new JSONArray();
            JSONArray forwho = new JSONArray();
            JSONArray bookingtype = new JSONArray();
            JSONArray typearray = new JSONArray();

            for (int i = 0; i < amenity.length; i++) {
                amentiy.put(amenity[i]);
            }
            for (int i = 0; i < for1.length; i++) {
                forwho.put(for1[i]);
            }
            for (int i = 0; i < bookingType.length; i++) {
                bookingtype.put(bookingType[i]);
            }
            for (int i = 0; i < type.length; i++) {
                typearray.put(type[i]);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rating", rating);
            jsonObject.put("priceFrom", priceFrom);
            jsonObject.put("priceTo", priceTo);
            jsonObject.put("amenity", amentiy);
            jsonObject.put("bookingType", bookingtype);
            jsonObject.put("for", forwho);
            jsonObject.put("type", typearray);
            final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            Call<AllChaletsModel> call = RetrofitInstance.service.filter(Helper.getJsonHeader(), requestBody);


            hud.show();
            call.enqueue(new Callback<AllChaletsModel>() {
                @Override
                public void onResponse(Call<AllChaletsModel> call, Response<AllChaletsModel> response) {
                    try {
                        hud.dismiss();
                        if (response.body() != null) {
                            AllChaletsModel model = response.body();
                            if (model.getSuccess()) {
                                if (model.getData().size() > 0) {
                                    Intent intent = new Intent();
                                    intent.putExtra(Constants.SEARCH_RESULT, new Gson().toJson(model.getData()));
                                    setResult(RESULT_OK, intent);
                                    ActivityFilter.this.finish();
                                } else {
                                    Toast.makeText(ActivityFilter.this, "No Filter Result Found", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        } else {
                            Helper.displayDialog("Alert", response.errorBody().string(), ActivityFilter.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<AllChaletsModel> call, Throwable t) {
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
    public void onClick(int pos, Amenity obj) {

    }

    @Override
    public void onClick(boolean isChecked, int pos, Amenity obj) {
        if (isChecked) {
            mSelectedAmenities.add(obj.getId());
        } else {
            mSelectedAmenities.remove(obj.getId());
        }
    }
}
