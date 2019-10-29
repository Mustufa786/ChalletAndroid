package com.octalabs.challetapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelRegisterResponce;
import com.octalabs.challetapp.retrofit.ApiInterface;
import com.octalabs.challetapp.retrofit.ApiResponce;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.FilePath;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 123;
    private Button mBtnRegister;
    private EditText mEdtusername, mEdtaddress, mEdtmobileno, mEdtpassword, mEdtconpassword, mEdtemail, mEdtcity;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mBtnRegister = findViewById(R.id.btn_sign_up);
        mEdtusername = findViewById(R.id.user_name);
        mEdtaddress = findViewById(R.id.address);
        mEdtmobileno = findViewById(R.id.mobile);
        mEdtemail = findViewById(R.id.email);
        mEdtcity = findViewById(R.id.city);
        mEdtpassword = findViewById(R.id.password);
        mEdtconpassword = findViewById(R.id.con_password);


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    Register();
                }
            }
        });

    }

    private boolean validation() {
        if (mEdtaddress.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEdtcity.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert City", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEdtconpassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Confirm Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEdtpassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEdtmobileno.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEdtusername.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Username", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEdtemail.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePaths = data.getData();
            filePath = FilePath.getPath(RegisterActivity.this, filePaths);
        }

    }

    private void Register() {

        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), mEdtusername.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), mEdtaddress.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), mEdtpassword.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), mEdtemail.getText().toString());
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), mEdtmobileno.getText().toString());
        RequestBody role = RequestBody.create(MediaType.parse("text/plain"), "admin");
        File file = new File(filePath);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);

        ApiInterface apiInterface = RetrofitInstance.getClient().create(ApiInterface.class);
        Call<ApiResponce<ModelRegisterResponce>> call = apiInterface.userregister(username, email, password, mobile, address, role, part);
        call.enqueue(new Callback<ApiResponce<ModelRegisterResponce>>() {
            @Override
            public void onResponse(Call<ApiResponce<ModelRegisterResponce>> call, Response<ApiResponce<ModelRegisterResponce>> response) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<ApiResponce<ModelRegisterResponce>> call, Throwable t) {

            }
        });
    }
}
