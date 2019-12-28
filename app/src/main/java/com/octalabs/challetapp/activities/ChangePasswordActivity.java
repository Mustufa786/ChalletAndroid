package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelChangePassword;
import com.octalabs.challetapp.models.ModelLogin.Login;
import com.octalabs.challetapp.models.ModelLogin.LoginModel;
import com.octalabs.challetapp.retrofit.ApiResponce;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.CustomDialog;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdtOldPassword, mEdtNewPassword, mEdtConPassword;
    private Button mBtnUpdate;
    private SharedPreferences mPref;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTextAction(Objects.requireNonNull(getSupportActionBar()), getResources().getString(R.string.change_password));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        mEdtConPassword = findViewById(R.id.edt_con_password);
        mEdtOldPassword = findViewById(R.id.edt_old_password);
        mEdtNewPassword = findViewById(R.id.edt_new_password);
        mBtnUpdate = findViewById(R.id.btn_process);
        mBtnUpdate.setOnClickListener(this);
        mPref = getSharedPreferences("main", MODE_PRIVATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_home_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.options:
                CustomDialog cd = new CustomDialog(ChangePasswordActivity.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                return false;
        }
        return false;
    }


    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(ChangePasswordActivity.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(16);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_process) {
            if (validation()) {
                changePassword();
            }
        }
    }

    private void changePassword() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("oldPassword", mEdtOldPassword.getText().toString());
            jsonObject.put("newPassword", mEdtConPassword.getText().toString());
            final RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            Call<ModelChangePassword> call = RetrofitInstance.service.changePassword(Helper.getJsonHeaderWithToken(this), requestBody);


            hud.show();
            call.enqueue(new Callback<ModelChangePassword>() {
                @Override
                public void onResponse(Call<ModelChangePassword> call, Response<ModelChangePassword> response) {
                    try {
                        hud.dismiss();
                        if (response.body() != null) {
                            ModelChangePassword model = response.body();
                            if (model.getSuccess()) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this)
                                        .setTitle("Success")
                                        .setMessage(model.getMessage())
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                finish();
                                            }
                                        });


                                AlertDialog dialog = alertDialog.create();
                                dialog.show();

                            }
                        } else {
                            displayDialog("Alert", response.errorBody().string(), ChangePasswordActivity.this);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<ModelChangePassword> call, Throwable t) {
                    t.printStackTrace();
                    hud.dismiss();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean validation() {
        if (mEdtOldPassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert old password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEdtNewPassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert new password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEdtConPassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Confirm password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mEdtConPassword.getText().toString().equalsIgnoreCase(mEdtNewPassword.getText().toString())) {
            Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (!mEdtOldPassword.getText().toString().equalsIgnoreCase(mPref.getString(Constants.password, ""))) {
//            Toast.makeText(this, "Current password is not correct", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return true;
    }
}
