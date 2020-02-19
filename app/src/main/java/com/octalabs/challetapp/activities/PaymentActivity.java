package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.CustomSpinnerAdapter;
import com.octalabs.challetapp.databinding.ActivityPaymentBinding;
import com.octalabs.challetapp.models.ModelCity.CityModel;
import com.octalabs.challetapp.models.ModelCity.StateCity;
import com.octalabs.challetapp.models.ModelCountry.Country;
import com.octalabs.challetapp.models.ModelCountry.CountryModel;
import com.octalabs.challetapp.models.ModelDetails.ChaletDetails;
import com.octalabs.challetapp.models.ModelState.CountryState;
import com.octalabs.challetapp.models.ModelState.StateModel;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.CustomDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class PaymentActivity extends AppCompatActivity {


    ActivityPaymentBinding mBinding;
    KProgressHUD hud;
    private String countryID;
    private String stateID;
    private String cityID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment);


        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);


        getCountries();
        onClickListeners();


        setTextAction(getSupportActionBar(), getResources().getString(R.string.payment));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));
    }

    private void onClickListeners() {


        mBinding.btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cardHolderName", mBinding.inputName.getText().toString());
                    jsonObject.put("cardNumber", mBinding.inputCardNumber.getText().toString());
                    jsonObject.put("expiryDate", mBinding.inputExpiryDate.getText().toString());
                    jsonObject.put("cvv", mBinding.inputCvv.getText().toString());
                    jsonObject.put("billingAddress", mBinding.inputBillingAddress.getText().toString());

                    jsonObject.put("countryId", countryID);
                    jsonObject.put("stateId", stateID);
                    jsonObject.put("cityId", cityID);
                    jsonObject.put("postalCode", mBinding.inputPostalCode.getText().toString());



                    startActivity(new Intent(PaymentActivity.this, ActivityConfirmPayment.class).putExtra(Constants.PAYMENT_DETAILS, jsonObject.toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        mBinding.inputExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                DatePickerDialog mDialog = new DatePickerDialog(PaymentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        try {
                            i1++;
                            String dateTempStr = i + "-" + i1 + "-" + i2;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                            Date expiryDate = format.parse(dateTempStr);

                            SimpleDateFormat mFormat = new SimpleDateFormat("dd-MM-yyyy");
                            mBinding.inputExpiryDate.setText(mFormat.format(expiryDate));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });
    }

    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(PaymentActivity.this);
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
                CustomDialog cd = new CustomDialog(PaymentActivity.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                return false;
        }
        return false;
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
                        displayDialog("Alert", model.getMessage(), PaymentActivity.this);
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

        ArrayAdapter<Object> mAdapter = new CustomSpinnerAdapter(PaymentActivity.this, R.layout.simple_spinner_item, (ArrayList<Object>) (Object) countryArray, "countries");
        mAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        mBinding.country.setAdapter(mAdapter);

        mBinding.country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinnerAdapter adapter = (CustomSpinnerAdapter) parent.getAdapter();
                Country item = (Country) adapter.getItem(position);

                countryID = item.getName() + "";
                String countryNewId = item.getId();
                if (item.getId() != null && !item.getId().equalsIgnoreCase("0")) {
                    getStates(countryNewId);
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
                            displayDialog("Alert", model.getMessage(), PaymentActivity.this);
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

        ArrayAdapter<Object> mAdapter = new CustomSpinnerAdapter(PaymentActivity.this, R.layout.simple_spinner_item, (ArrayList<Object>) (Object) stateArray, "states");
        mAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        mBinding.state.setAdapter(mAdapter);
        mBinding.state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinnerAdapter adapter = (CustomSpinnerAdapter) parent.getAdapter();

                CountryState item = (CountryState) adapter.getItem(position);
                stateID = item.getName() + "";
                String stateNewID = item.getId() + "";

                if (!item.getId().equalsIgnoreCase("0")) {
                    getCities(stateNewID);
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
                        displayDialog("Alert", model.getMessage(), PaymentActivity.this);
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


        ArrayAdapter<Object> mAdapter = new CustomSpinnerAdapter(PaymentActivity.this, R.layout.simple_spinner_item, (ArrayList<Object>) (Object) citiesArray, "cities");
        mAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        mBinding.city.setAdapter(mAdapter);

        mBinding.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinnerAdapter adapter = (CustomSpinnerAdapter) parent.getAdapter();
                StateCity item = (StateCity) adapter.getItem(position);
                cityID = item.getName() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


}
