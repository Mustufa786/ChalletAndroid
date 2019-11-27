package com.octalabs.challetapp.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class ActivityLogin extends Activity {


    private Button mBtnSignUP;
    private Button mBtnSignIN;
    EditText inputEmail, inputPassowd;
    KProgressHUD hud;
    ImageView imgClose;
    TextView textForgotPassword;

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
        imgClose = findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();

            }
        });

        textForgotPassword = findViewById(R.id.text_forgot_password);
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ActivityLogin.this);
                    final EditText edittext = new EditText(ActivityLogin.this);
                    edittext.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//                    alert.setMessage("Enter your email address . We will send the further instructions on your email");
                    alert.setTitle("Enter Your Email Address");

                    alert.setView(edittext);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //What ever you want to do with the value
                            Editable YouEditTextValue = edittext.getText();
                            if (Helper.isValidEmail(YouEditTextValue)) {
                                forgotPassword(YouEditTextValue.toString());
                            } else
                                Toast.makeText(ActivityLogin.this, "Please Enter Correct Email Address ", Toast.LENGTH_SHORT).show();


                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // what ever you want to do with No option.
                            dialog.dismiss();
                        }
                    });

                    alert.show();


                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                                mPref.edit().putBoolean(Constants.IS_USER_LOGGED_IN, true).apply();
                                startActivity(new Intent(ActivityLogin.this, MainActivity.class));
                                finishAffinity();
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


    private void forgotPassword(String youEditTextValue) {
        if (!youEditTextValue.equalsIgnoreCase("")) {
            try {
                JSONObject object = new JSONObject();
                object.put("email", youEditTextValue);
                RequestBody mBody = RequestBody.create(MediaType.parse("application/json"), object.toString());
                hud.show();
                Call<ResponseBody> call = RetrofitInstance.service.forgotPassword(mBody, Helper.getJsonHeader());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            hud.dismiss();
                            if (response != null) {
                                if (response.body() != null) {
                                    JSONObject model = new JSONObject(response.body().string());

                                    displayDialog("Alert", "Email has been sent with further instructions on your email address", ActivityLogin.this);
                                } else {
                                    Toast.makeText(ActivityLogin.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
//                                    displayDialog("Alert", "Invalid Username or Password", LoginActivity.this);
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
    }
}
