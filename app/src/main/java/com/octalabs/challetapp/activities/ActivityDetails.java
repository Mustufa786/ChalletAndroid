package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.databinding.ActivityDetailsBinding;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelDetails.ModelChaletsDetails;
import com.octalabs.challetapp.models.ModelLogin.LoginModel;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class ActivityDetails extends AppCompatActivity implements View.OnClickListener {

    KProgressHUD hud;

    ActivityDetailsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        setTextAction(Objects.requireNonNull(getSupportActionBar()), "Chalet Name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        getDetails(getIntent().getStringExtra(Constants.CHALET_OR_MARRAIGE_ID));

    }

    private void init() {
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        mBinding.layoutAddToWishlist.setOnClickListener(this);

    }

    private void getDetails(String id) {
        hud.show();
        Call<ModelChaletsDetails> call = RetrofitInstance.service.getChaletsMarraigeDetails(id, Helper.getJsonHeaderWithToken(ActivityDetails.this));
        call.enqueue(new Callback<ModelChaletsDetails>() {
            @Override
            public void onResponse(Call<ModelChaletsDetails> call, Response<ModelChaletsDetails> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    ModelChaletsDetails model = response.body();
                    if (model.getSuccess()) {


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
//        textview.setGravity(Gravity.CENTER);
        textview.setTextSize(16);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_add_to_wishlist:
                addToWishList();
                break;


        }
    }


    private void addToWishList() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bookingItemId", getIntent().getStringExtra(Constants.CHALET_OR_MARRAIGE_ID));
            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            Call<ResponseBody> call = RetrofitInstance.service.addtoWishList(requestBody, Helper.getJsonHeaderWithToken(this));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
