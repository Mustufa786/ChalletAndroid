package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.databinding.ActivityContactUsBinding;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityContactUs extends AppCompatActivity {

    ActivityContactUsBinding mBinding;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        setTextAction(Objects.requireNonNull(getSupportActionBar()), getResources().getString(R.string.contact_us));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);

        mBinding.btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mBinding.inputTitle.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ActivityContactUs.this, "Please Provide Title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mBinding.inputMessage.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ActivityContactUs.this, "Please Provide Details", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    hud.show();
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title", mBinding.inputTitle.getText().toString());
                    jsonObject.put("message", mBinding.inputMessage.getText().toString());
                    RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

                    Call<ResponseBody> call = RetrofitInstance.service.contactUs(requestBody, Helper.getJsonHeaderWithToken(ActivityContactUs.this));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                hud.dismiss();
                                if (response.body() != null) {
                                    JSONObject jsonObject1 = new JSONObject(response.body().string());
                                    if (jsonObject1.has("success")) {
                                        Toast.makeText(ActivityContactUs.this, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                                        finish();

                                    }

                                } else {
                                    Helper.displayDialog("Alert", response.errorBody().string(), ActivityContactUs.this);
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
        TextView textview = new TextView(ActivityContactUs.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setTextColor(Color.WHITE);
//        textview.setGravity(Gravity.CENTER);
        textview.setTextSize(16);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
    }
}
