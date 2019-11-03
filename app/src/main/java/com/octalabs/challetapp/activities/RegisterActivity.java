package com.octalabs.challetapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.CustomSpinnerAdapter;
import com.octalabs.challetapp.models.ModelCity.CityModel;
import com.octalabs.challetapp.models.ModelCity.StateCity;
import com.octalabs.challetapp.models.ModelCountry.Country;
import com.octalabs.challetapp.models.ModelCountry.CountryModel;
import com.octalabs.challetapp.models.ModelLogin.Login;
import com.octalabs.challetapp.models.ModelRegister.RegisterModel;
import com.octalabs.challetapp.models.ModelState.CountryState;
import com.octalabs.challetapp.models.ModelState.StateModel;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.FilePath;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class RegisterActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 123;
    private Button mBtnRegister;
    private EditText mEdtusername, mEdtaddress, mEdtmobileno, mEdtpassword, mEdtconpassword, mEdtemail, mEdtcity;
    private String filePath;
    Spinner spinnerCountry, spinnerStates, spinnerCitites;
    private String countryID;
    private String stateID;
    private String cityID;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mBtnRegister = findViewById(R.id.btn_sign_up);
        mEdtusername = findViewById(R.id.user_name);
        mEdtaddress = findViewById(R.id.address);
        mEdtmobileno = findViewById(R.id.mobile);
        mEdtemail = findViewById(R.id.email);
        spinnerCitites = findViewById(R.id.city);
        mEdtpassword = findViewById(R.id.password);
        mEdtconpassword = findViewById(R.id.con_password);
        spinnerCountry = findViewById(R.id.country);
        spinnerStates = findViewById(R.id.state);

        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);

        getCountries();

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
        if (mEdtusername.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Username", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEdtemail.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Email", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (mEdtmobileno.getText().toString().equalsIgnoreCase("")) {
//            Toast.makeText(this, "Please insert Mobile Number", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (mEdtpassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEdtconpassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please insert Confirm Password", Toast.LENGTH_SHORT).show();
            return false;
        }


//        if (mEdtaddress.getText().toString().equalsIgnoreCase("")) {
//            Toast.makeText(this, "Please insert Address", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//
//        if (countryID == null) {
//            Toast.makeText(this, "Please Select Country To Continue", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (stateID == null) {
//            Toast.makeText(this, "Please Select State To Continue", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//
//        if (cityID == null) {
//            Toast.makeText(this, "Please Select City To Continue", Toast.LENGTH_SHORT).show();
//            return false;
//        }

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

        hud.show();

        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        multipartBody.addFormDataPart("userName", mEdtusername.getText().toString());
        multipartBody.addFormDataPart("email", mEdtemail.getText().toString());
        multipartBody.addFormDataPart("password", mEdtpassword.getText().toString());
        multipartBody.addFormDataPart("mobileNo", mEdtmobileno.getText().toString());
        multipartBody.addFormDataPart("address", mEdtaddress.getText().toString());
        multipartBody.addFormDataPart("role", "end_user");
        multipartBody.addFormDataPart("countryId", countryID);
        multipartBody.addFormDataPart("stateId", stateID);
        multipartBody.addFormDataPart("cityId", cityID);

        if (filePath != null && !filePath.equalsIgnoreCase("")) {
            File file = new File(filePath);
            multipartBody.addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }


        RequestBody mBody = multipartBody.build();


        Call<RegisterModel> call = RetrofitInstance.service.register(mBody);
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
                            mPref.edit().putString(Constants.email_address, mEdtemail.getText().toString()).apply();
                            mPref.edit().putString(Constants.password, mEdtpassword.getText().toString()).apply();
                            mPref.edit().putBoolean(Constants.IS_USER_LOGGED_IN, true).apply();

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("Success")
                                    .setMessage(model.getMessage())
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            finish();

                                        }
                                    });


                            AlertDialog dialog = alertDialog.create();
                            dialog.show();
                            Log.i("tag", object.toString());
                        } else {
                            displayDialog("Alert", model.getMessage(), RegisterActivity.this);
                        }
                    } else {
                        displayDialog("Alert", "Invalid Username or Password", RegisterActivity.this);

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


    private void getCountries() {
        hud.show();
        Call<CountryModel> call = RetrofitInstance.service.getAllCountries();
        call.enqueue(new Callback<CountryModel>() {
            @Override
            public void onResponse(Call<CountryModel> call, Response<CountryModel> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    CountryModel model = response.body();
                    if (model.getSuccess()) {
                        ArrayList countryArray = new ArrayList<>(model.getData());

                        setCountryData(countryArray);
                    } else {
                        displayDialog("Alert", model.getMessage(), RegisterActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void setCountryData(ArrayList countryArray) {
        Country datum = new Country();
        datum.setId("0");
        datum.setName("Select Country");
        countryArray.add(0, datum);

        ArrayAdapter<Object> mAdapter = new CustomSpinnerAdapter(RegisterActivity.this, R.layout.simple_spinner_item, (ArrayList<Object>) (Object) countryArray, "countries");
        mAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinnerCountry.setAdapter(mAdapter);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinnerAdapter adapter = (CustomSpinnerAdapter) parent.getAdapter();
                Country item = (Country) adapter.getItem(position);

                countryID = item.getId() + "";
                if (item.getId() != null && !item.getId().equalsIgnoreCase("0")) {
                    getStates(countryID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void getStates(String countryID) {
        hud.show();
        Call<StateModel> call = RetrofitInstance.service.getAllStates(countryID);
        call.enqueue(new Callback<StateModel>() {
            @Override
            public void onResponse(Call<StateModel> call, Response<StateModel> response) {
                hud.dismiss();
                try {
                    if (response.body() != null) {

                        StateModel model = response.body();
                        if (model.getSuccess()) {
                            ArrayList stateArray = new ArrayList<>(model.getData());
                            setCountryStateData(stateArray);
                        } else {
                            displayDialog("Alert", model.getMessage(), RegisterActivity.this);
                        }
                    } else
                        Log.i("error State", response.errorBody().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(Call<StateModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void setCountryStateData(ArrayList stateArray) {
        CountryState datum = new CountryState();
        datum.setId("0");
        datum.setName("Select State");
        stateArray.add(0, datum);

        ArrayAdapter<Object> mAdapter = new CustomSpinnerAdapter(RegisterActivity.this, R.layout.simple_spinner_item, (ArrayList<Object>) (Object) stateArray, "states");
        mAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinnerStates.setAdapter(mAdapter);
        spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinnerAdapter adapter = (CustomSpinnerAdapter) parent.getAdapter();

                CountryState item = (CountryState) adapter.getItem(position);
                stateID = item.getId() + "";
                if (!item.getId().equalsIgnoreCase("0")) {
                    getCities(stateID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void getCities(String stateID) {
        hud.show();
        Call<CityModel> call = RetrofitInstance.service.getAllCities(stateID);
        call.enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    CityModel model = response.body();
                    if (model.getSuccess()) {
                        ArrayList<StateCity> citiesArray = new ArrayList<>(model.getData());
                        setCitiesData(citiesArray);

                    } else {
                        displayDialog("Alert", model.getMessage(), RegisterActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void setCitiesData(ArrayList<StateCity> citiesArray) {
        StateCity datum = new StateCity();
        datum.setId("0");
        datum.setName("Select City");
        citiesArray.add(0, datum);


        ArrayAdapter<Object> mAdapter = new CustomSpinnerAdapter(RegisterActivity.this, R.layout.simple_spinner_item, (ArrayList<Object>) (Object) citiesArray, "cities");
        mAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinnerCitites.setAdapter(mAdapter);

        spinnerCitites.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinnerAdapter adapter = (CustomSpinnerAdapter) parent.getAdapter();
                StateCity item = (StateCity) adapter.getItem(position);
                cityID = item.getId() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }
}



