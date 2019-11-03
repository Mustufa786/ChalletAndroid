package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelCheckout.ModelCheckout;
import com.octalabs.challetapp.models.ModelDetails.ChaletDetails;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityConfirmPayment extends AppCompatActivity {

    Button btnConfirm;
    ArrayList<ChaletDetails> checkoutList;

    SharedPreferences sharedPreferences;
    Gson gson;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        setTextAction(getSupportActionBar(), getResources().getString(R.string.confirm_payment));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        sharedPreferences = getSharedPreferences("main", MODE_PRIVATE);
        gson = new Gson();


        checkoutList = gson.fromJson(sharedPreferences.getString(Constants.USER_CART, "[]"), new TypeToken<List<ChaletDetails>>() {
        }.getType());


        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                makeBooking();


            }
        });


    }

    private void makeBooking() {
        try {

            JSONObject jsonObject = new JSONObject();
            String bookingIdStr = "";
            for (int i = 0; i < checkoutList.size(); i++) {
                bookingIdStr += bookingIdStr + checkoutList.get(i).getId() + ",";
            }

            bookingIdStr = bookingIdStr.substring(0, bookingIdStr.length() - 1);


            jsonObject.put("bookingItemIds", bookingIdStr);
            jsonObject.put("bookingFrom", "2019-10-16T07:56:37.861Z");
            jsonObject.put("bookingTo", "2019-10-31T07:56:37.861Z");
            jsonObject.put("paymentStatus", "Pending");
            jsonObject.put("transactionId", "test123");

            RequestBody body = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());
            hud.show();
            Call<ModelCheckout> call = RetrofitInstance.service.postCheckout(body, Helper.getJsonHeaderWithToken(this));
            call.enqueue(new Callback<ModelCheckout>() {
                @Override
                public void onResponse(Call<ModelCheckout> call, Response<ModelCheckout> response) {
                    hud.dismiss();
                    ModelCheckout modelCheckout = response.body();
                    if (modelCheckout.getSuccess()) {
                        Toast.makeText(ActivityConfirmPayment.this, "Order Made Successfully", Toast.LENGTH_SHORT).show();
                        getSharedPreferences("main", MODE_PRIVATE).edit().remove(Constants.USER_CART).apply();
                        startActivity(new Intent(ActivityConfirmPayment.this, ActivityThankyou.class).putExtra(Constants.ORDER_DETAILS, gson.toJson(modelCheckout.getData())));

                    }
                }

                @Override
                public void onFailure(Call<ModelCheckout> call, Throwable t) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(ActivityConfirmPayment.this);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}