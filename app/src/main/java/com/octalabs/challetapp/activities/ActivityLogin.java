package com.octalabs.challetapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelLogin.Login;
import com.octalabs.challetapp.models.ModelLogin.LoginModel;
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

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class ActivityLogin extends Activity {


    private Button mBtnSignUP;
    private Button mBtnSignIN;
    EditText inputEmail, inputPassowd;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnSignIN = findViewById(R.id.btn_sig_in);
        mBtnSignUP = findViewById(R.id.btn_sig_up);
        inputEmail = findViewById(R.id.user_name);
        inputPassowd = findViewById(R.id.password);
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        mBtnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLogin.this, RegisterActivity.class));
            }
        });

        mBtnSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();

            }
        });
    }

    private void loginUser() {
        try {

            if (inputEmail.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please Enter Email To Continue", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Helper.isValidEmail(inputEmail.getText().toString())) {
                Toast.makeText(this, "Please Enter Valid Email To Continue", Toast.LENGTH_SHORT).show();
                return;
            }

            if (inputPassowd.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please Enter Password To Continue", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", inputEmail.getText().toString());
            jsonObject.put("password", inputPassowd.getText().toString());
            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            Call<LoginModel> call = RetrofitInstance.service.loginUser(requestBody);


            hud.show();
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    try {
                        hud.dismiss();
                        if (response.body() != null) {
                            LoginModel model = response.body();
                            if (model.getSuccess()) {
                                Gson gson = new Gson();
                                JSONObject object = new JSONObject(gson.toJson(model.getData(), Login.class));
                                SharedPreferences mPref = getSharedPreferences("main", MODE_PRIVATE);
                                mPref.edit().putString(Constants.user_profile, object.toString()).apply();
                                mPref.edit().putString(Constants.email_address, inputEmail.getText().toString()).apply();
                                mPref.edit().putString(Constants.password, inputEmail.getText().toString()).apply();
                                startActivity(new Intent(ActivityLogin.this, MainActivity.class));
                                finish();
                                Log.i("tag", object.toString());
                            } else {
                                displayDialog("Alert", model.getMessage(), ActivityLogin.this);
                            }
                        } else {
                            displayDialog("Alert", "Invalid Username or Password", ActivityLogin.this);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                    hud.dismiss();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
