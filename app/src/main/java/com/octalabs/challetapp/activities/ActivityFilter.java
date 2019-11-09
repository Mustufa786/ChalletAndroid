package com.octalabs.challetapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelAddReview;
import com.octalabs.challetapp.retrofit.ApiInterface;
import com.octalabs.challetapp.retrofit.ApiResponce;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFilter extends AppCompatActivity {

    private LinearLayout mbtnFilter;
    private KProgressHUD hud;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        mbtnFilter = findViewById(R.id.btn_filter);
        hud = new KProgressHUD(this);
        setTextAction(getSupportActionBar(), getResources().getString(R.string.filter));
    }

    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(ActivityFilter.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setTextColor(Color.WHITE);
//        textview.setGravity(Gravity.CENTER);
        textview.setTextSize(16);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
    }

    private void filter(String rating, String priceFrom, String priceTo, String amenity, String bookingType, String for1, String[] type) {
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
}
