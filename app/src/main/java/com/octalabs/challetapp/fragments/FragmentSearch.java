package com.octalabs.challetapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivitySearchAndFilterResult;
import com.octalabs.challetapp.activities.PaymentActivity;
import com.octalabs.challetapp.activities.RegisterActivity;
import com.octalabs.challetapp.adapter.AdapterBookingHistory;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.adapter.CustomSpinnerAdapter;
import com.octalabs.challetapp.databinding.FragmentSearchListingBinding;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.models.ModelCountry.Country;
import com.octalabs.challetapp.models.ModelCountry.CountryModel;
import com.octalabs.challetapp.models.ModelLocation.LocationModel;
import com.octalabs.challetapp.models.ModelLocation.SampleLocation;
import com.octalabs.challetapp.models.ModelWishlist.ModelWishlist;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        Call<LocationModel> call = RetrofitInstance.service.getAllLocations();
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    LocationModel model = response.body();
                    if (model.getSuccess()) {
                        ArrayList countryArray = new ArrayList<>(model.getData());

                        setLocationData(countryArray);
                    } else {
                        displayDialog("Alert", model.getMessage(), getActivity());
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void setLocationData(ArrayList locationArray) {
        SampleLocation datum = new SampleLocation();
        datum.setId("0");
        datum.setName("Select Location");
        locationArray.add(0, datum);

        ArrayAdapter<Object> mAdapter = new CustomSpinnerAdapter(getContext(), R.layout.simple_spinner_item, (ArrayList<Object>) (Object) locationArray, "locations");
        mAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        mBinding.location.setAdapter(mAdapter);

        mBinding.location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinnerAdapter adapter = (CustomSpinnerAdapter) parent.getAdapter();
                SampleLocation item = (SampleLocation) adapter.getItem(position);
                mLocationId = item.getId() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                checkInStr = selectedHour + ":" + selectedMinute + ":00";
                                String timeStr = ((selectedHour > 12) ? selectedHour % 12 : selectedHour) + ":" + (selectedMinute < 10 ? ("0" + selectedMinute) : selectedMinute) + " " + ((selectedHour >= 12) ? "PM" : "AM");
                                mBinding.checkIn.setText(timeStr);

                            }
                        }, hour, minute, false);
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();

                    }
                });
                break;

            case R.id.check_out:
                mBinding.checkOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                checkoutStr = selectedHour + ":" + selectedMinute + "00";
                                String timeStr = ((selectedHour > 12) ? selectedHour % 12 : selectedHour) + ":" + (selectedMinute < 10 ? ("0" + selectedMinute) : selectedMinute) + " " + ((selectedHour >= 12) ? "PM" : "AM");
                                mBinding.checkOut.setText(timeStr);
                            }
                        }, hour, minute, false);//Yes 24 hour time
                        mTimePicker.setTitle("Select Check Out Time");
                        mTimePicker.show();

                    }
                });
                break;


            case R.id.btn_search:
                if (!mLocationId.equalsIgnoreCase("0")) {
                    getSearchData();
                } else {
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
}
