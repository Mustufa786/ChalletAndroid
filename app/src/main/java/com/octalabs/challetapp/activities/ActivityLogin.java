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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelLogin.Login;
import com.octalabs.challetapp.models.ModelLogin.LoginModel;
import com.octalabs.challetapp.models.ModelRegister.RegisterModel;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class ActivityLogin extends Activity {


    private Button mBtnSignUP;
    private Button mBtnSignIN;
    private ImageView imgFb, imgTwitter, imgGmail;
    EditText inputEmail, inputPassowd;
    KProgressHUD hud;
    ImageView imgClose;
    TextView textForgotPassword;
    CallbackManager callbackManager;
    private static final int REQ_CODE = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        mBtnSignIN = findViewById(R.id.btn_sig_in);
        mBtnSignUP = findViewById(R.id.btn_sig_up);
        inputEmail = findViewById(R.id.user_name);
        inputPassowd = findViewById(R.id.password);
        imgFb = findViewById(R.id.img_facebook);
        imgGmail = findViewById(R.id.img_gmail_icon);
        userGoogleData();

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


        imgFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(ActivityLogin.this, Arrays.asList(
                        "public_profile", "email"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {

                                            @Override
                                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                                Log.i("tag", object.toString());
                                                try {
                                                    socialMediaLogin(object.getString("email") , object.getString("name"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,email");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onError(FacebookException error) {

                            }
                        });
            }
        });

        imgGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignin();
            }
        });

    }


    private void userGoogleData() {

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
//            hideDialog();
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            if (resultCode == RESULT_OK) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }


    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);

            // Signed in googleSignInAccount, show authenticated UI.
//            hideDialog();

            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();
            String id = googleSignInAccount.getId();
            String idToken = googleSignInAccount.getIdToken();
//            String getObfuscatedIdentifier = googleSignInAccount.getObfuscatedIdentifier();
            String serverAuthCode = googleSignInAccount.getServerAuthCode();
            String account = googleSignInAccount.getAccount().toString();
            String requestedScopes = String.valueOf(googleSignInAccount.getRequestedScopes());

            socialMediaLogin(email, name);
//            loginWithGoogle(id, email, "", "Google");
            Log.e("TAG", "Google DATA:Name: " + name + "##Email:" + email + "##Id: " + id + "##id_token:" + idToken + "##Server Auth Code: " + serverAuthCode + "##Account: " + account + "##Requested Scope: " + requestedScopes);


//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    private void loginWithGoogle(final String googleUserId, final String email, final String accessToken, final String provider) {


    }


    private void googleSignin() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_CODE);
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

    private void socialMediaLogin(String emailAddress, String username) {

        hud.show();

        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        multipartBody.addFormDataPart("userName", username);
        multipartBody.addFormDataPart("socialId", emailAddress);
        multipartBody.addFormDataPart("role", "end_user");


        RequestBody mBody = multipartBody.build();


        Call<RegisterModel> call = RetrofitInstance.service.socialMediaLogin(mBody);
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {


                try {
                    hud.dismiss();
                    if (response.body() != null) {
                        RegisterModel model = response.body();
                        if (model.getSuccess()) {
                            Gson gson = new Gson();
                            JSONObject object = new JSONObject(gson.toJson(model.getData(), Login.class));
                            SharedPreferences mPref = getSharedPreferences("main", MODE_PRIVATE);
                            mPref.edit().putString(Constants.user_profile, object.toString()).apply();
                            mPref.edit().putBoolean(Constants.IS_USER_LOGGED_IN, true).apply();

                            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ActivityLogin.this)
                                    .setTitle("Success")
                                    .setMessage(model.getMessage())
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            startActivity(new Intent(ActivityLogin.this, MainActivity.class));
                                            finishAffinity();

                                        }
                                    });


                            android.app.AlertDialog dialog = alertDialog.create();
                            dialog.show();
                            Log.i("tag", object.toString());
                        } else {
                            displayDialog("Alert", model.getMessage(), ActivityLogin.this);
                        }
                    } else {
//                        displayDialog("Alert", "Invalid Username or Password", ActivityLogin.this);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }
}
