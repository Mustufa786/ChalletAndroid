package com.octalabs.challetapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivitySearchAndFilterResult;
import com.octalabs.challetapp.activities.PaymentActivity;
import com.octalabs.challetapp.activities.RegisterActivity;
import com.octalabs.challetapp.adapter.AdapterAutoCompelete;
import com.octalabs.challetapp.adapter.AdapterBookingHistory;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.adapter.CustomSpinnerAdapter;
import com.octalabs.challetapp.databinding.FragmentSearchListingBinding;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.models.ModelCity.CityModel;
import com.octalabs.challetapp.models.ModelCity.StateCity;
import com.octalabs.challetapp.models.ModelCountry.Country;
import com.octalabs.challetapp.models.ModelCountry.CountryModel;
import com.octalabs.challetapp.models.ModelLocation.LocationModel;
import com.octalabs.challetapp.models.ModelLocation.SampleLocation;
import com.octalabs.challetapp.models.ModelWishlist.ModelWishlist;
import com.octalabs.challetapp.models.modelseatchcity.ModelCity;
import com.octalabs.challetapp.models.modelseatchcity.ModelCityResponse;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class FragmentSearch extends Fragment implements View.OnClickListener {

    private FragmentSearchListingBinding mBinding;
    private KProgressHUD hud;
    private String mLocationId = "";
    public static Long checkInStr, checkoutStr;

    private Calendar mCheckInn;
    public static int noOfDays = 1;
    public static String checkInDateStr, checkOutDateStr;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_listing, container, false);

        hud = KProgressHUD.create(getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        mBinding.checkIn.setOnClickListener(this);
        mBinding.checkOut.setOnClickListener(this);
        mBinding.btnSearch.setOnClickListener(this);

        mCheckInn = Calendar.getInstance();
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        checkInStr = mCheckInn.getTimeInMillis();
        mBinding.checkIn.setText(sdf.format(mCheckInn.getTime()));
        checkInDateStr = sdf.format(mCheckInn.getTime());


        mCheckInn.add(Calendar.DAY_OF_MONTH, 1);
        checkoutStr = mCheckInn.getTimeInMillis();
        mBinding.checkOut.setText(sdf.format(mCheckInn.getTime()));
        checkOutDateStr = sdf.format(mCheckInn.getTime());

        noOfDays = 1;


        getAllLocations();


        return mBinding.getRoot();
    }

    private void getAllLocations() {
        hud.show();
        Call<ModelCityResponse> call = RetrofitInstance.service.getAllCities();
        call.enqueue(new Callback<ModelCityResponse>() {
            @Override
            public void onResponse(Call<ModelCityResponse> call, Response<ModelCityResponse> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    ModelCityResponse model = response.body();
                    if (model.getSuccess()) {
                        ArrayList<ModelCity> countryArray = new ArrayList<>(model.getData());
                        setLocationData(countryArray);
                    } else {
                        displayDialog("Alert", model.getMessage(), getActivity());
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelCityResponse> call, Throwable t) {
                t.printStackTrace();
                hud.dismiss();
            }
        });
    }

    private void setLocationData(ArrayList<ModelCity> locationArray) {

        ModelCity city = new ModelCity();
        city.setName("الدمام");


        int index = -1;
        for (int i = 0; i < locationArray.size(); i++) {
            if (locationArray.get(i).getName().equalsIgnoreCase("الدمام")) {
                index = i;
                mLocationId = locationArray.get(i).getId();
                break;
            }
        }


        mBinding.location.setThreshold(1);
        AdapterAutoCompelete adapter = new AdapterAutoCompelete(getActivity(), R.layout.adapter_auto_tv, locationArray);
        mBinding.location.setAdapter(adapter);
        mBinding.location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ModelCity obj = (ModelCity) adapterView.getItemAtPosition(i);
                mLocationId = obj.getId() + "";
            }
        });


            mBinding.location.setText("Dammam");



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_in:
                mBinding.checkIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCheckInn = Calendar.getInstance();
                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                mCheckInn.set(Calendar.YEAR, year);
                                mCheckInn.set(Calendar.MONTH, monthOfYear);
                                mCheckInn.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd-MMM-yyyy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                                mBinding.checkIn.setText(sdf.format(mCheckInn.getTime()));
                                checkInStr = mCheckInn.getTimeInMillis();
                                checkInDateStr = mBinding.checkIn.getText().toString();


                            }
                        }, mCheckInn
                                .get(Calendar.YEAR), mCheckInn.get(Calendar.MONTH),
                                mCheckInn.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });
                break;

            case R.id.check_out:
                mBinding.checkOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar myCalendar = Calendar.getInstance();
                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                if (mCheckInn.before(myCalendar) || mCheckInn.equals(myCalendar)) {
                                    String myFormat = "dd-MMM-yyyy";
                                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                    try {
                                        mBinding.checkOut.setText(sdf.format(myCalendar.getTime()));
                                        Date date = (Date) sdf.parse(mBinding.checkOut.getText().toString());
                                        Log.e("TIME STamp out: ", String.valueOf(date));
                                        checkoutStr = myCalendar.getTimeInMillis();
                                        checkOutDateStr = mBinding.checkOut.getText().toString();

                                        noOfDays = (int) Daybetween(mBinding.checkIn.getText().toString(), mBinding.checkOut.getText().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(getActivity(), "Please select upper date from check inn", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });
                break;


            case R.id.btn_search:
                if (!mLocationId.equalsIgnoreCase("0")) {
                    getSearchData();
                } else {
                    Toast.makeText(getContext(), "Select Atleast One Location To Continue and select check in and check out dates", Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }

    private void getSearchData() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cityId", mLocationId);
            jsonObject.put("checkIn", checkInStr);
            jsonObject.put("checkOut", checkoutStr);
            RequestBody body = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());
            hud.show();

            Call<AllChaletsModel> call = RetrofitInstance.service.searchResults(body, Helper.getJsonHeader());
            call.enqueue(new Callback<AllChaletsModel>() {
                @Override
                public void onResponse(Call<AllChaletsModel> call, Response<AllChaletsModel> response) {
                    hud.dismiss();
                    if (response.body() != null) {
                        AllChaletsModel model = response.body();
                        if (model.getSuccess()) {
                            if (model.getData().size() > 0) {
                                Intent intent = new Intent(getContext(), ActivitySearchAndFilterResult.class);
                                intent.putExtra("searchList", new Gson().toJson(model.getData()));
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "No Search Result Found", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            displayDialog("Alert", model.getMessage(), getContext());
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllChaletsModel> call, Throwable t) {
                    hud.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long Daybetween(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date Date1 = null, Date2 = null;
        try {
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000);
    }


}
