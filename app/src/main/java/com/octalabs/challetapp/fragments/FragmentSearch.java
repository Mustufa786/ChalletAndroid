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
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private String checkInStr, checkoutStr;
    private Calendar mCheckInn;
    private String noOfDays = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_listing, container, false);

        hud = KProgressHUD.create(getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        mBinding.checkIn.setOnClickListener(this);
        mBinding.checkOut.setOnClickListener(this);
        mBinding.btnSearch.setOnClickListener(this);
        getAllLocations();
        checkInStr = "";
        checkoutStr = "";


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
        ModelCity datum = new ModelCity();
        datum.setId("0");
        datum.setName("Select Location");
        locationArray.add(0, datum);
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

                                try {
                                    mBinding.checkIn.setText(sdf.format(mCheckInn.getTime()));
                                    Date date = (Date) sdf.parse(String.valueOf(mBinding.checkIn.getText()));
                                    Log.e("TIME STamp inn: ", String.valueOf(date.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

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
                                        noOfDays = getCountOfDays(mBinding.checkIn.getText().toString(), mBinding.checkOut.getText().toString());
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
                if (!mLocationId.equalsIgnoreCase("0") && !noOfDays.equalsIgnoreCase("")) {
                    getSearchData();
                } else {
                    Toast.makeText(getContext(), "Select Atleast One Location To Continue", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Select Atleast One Location To Continue", Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }

    private void getSearchData() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("locationId", mLocationId);
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
                                intent.putExtra("numOfDays", noOfDays);
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

    public String getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ("" + (int) dayCount + " Days");
    }
}
