package com.octalabs.challetapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelLogin.LoginModel;
import com.octalabs.challetapp.models.ModelRegisterResponce;
import com.octalabs.challetapp.retrofit.ApiInterface;
import com.octalabs.challetapp.retrofit.ApiResponce;
import com.octalabs.challetapp.retrofit.RetrofitInstance;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends Activity {


    private Button mBtnSignUP;
    private Button mBtnSignIN;
    EditText inputEmail, inputPassowd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnSignIN = findViewById(R.id.btn_sig_in);
        mBtnSignUP = findViewById(R.id.btn_sig_up);
        inputEmail = findViewById(R.id.user_name);
        inputPassowd = findViewById(R.id.password);


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
                startActivity(new Intent(ActivityLogin.this, MainActivity.class));
            }
        });
    }

    private void loginUser() {
        try {

            if (inputEmail.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please Enter Email To Continue", Toast.LENGTH_SHORT).show();
                return;
            }
            if (inputPassowd.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please Enter Password To Continue", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", inputEmail.getText().toString());
            jsonObject.put("password", inputPassowd.getText().toString());
            RequestBody requestBody = RequestBody.create(MediaType.get("appplication/json"), jsonObject.toString());
            ApiInterface apiInterface = RetrofitInstance.getClient().create(ApiInterface.class);

            Call<ApiResponce<LoginModel>> call = apiInterface.loginUser(requestBody);
            call.enqueue(new Callback<ApiResponce<LoginModel>>() {
                @Override
                public void onResponse(Call<ApiResponce<LoginModel>> call, Response<ApiResponce<LoginModel>> response) {



                    startActivity(new Intent(ActivityLogin.this, MainActivity.class));
                }

                @Override
                public void onFailure(Call<ApiResponce<LoginModel>> call, Throwable t) {

                }
            });

        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }


    }
}
