package com.octalabs.challetapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.MainActivity;
import com.octalabs.challetapp.activities.RegisterActivity;
import com.octalabs.challetapp.adapter.CustomSpinnerAdapter;
import com.octalabs.challetapp.databinding.FragmentUserProfileBinding;
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
import com.octalabs.challetapp.utils.Helper;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class UserProfileFragment extends Fragment {

    FragmentUserProfileBinding binding;
    Login mData;

    KProgressHUD hud;
    private String countryID;
    private String stateID;
    private String cityID;
    private String filePath;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);


        mData = new Gson().fromJson(getActivity().getSharedPreferences("main", MODE_PRIVATE).getString(Constants.user_profile, ""), Login.class);
        hud = KProgressHUD.create(getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);

        getCountries();
        setData();

        return binding.getRoot();

    }

    private void setData() {
//    if(mData.get)

        if (mData.getUserName() != null && !mData.getUserName().equalsIgnoreCase("")) {
            binding.userName.setText(mData.getUserName());
        }
        if (mData.getEmail() != null && !mData.getEmail().equalsIgnoreCase("")) {
            binding.email.setText(mData.getEmail());
            binding.email.setEnabled(false);
        }
        if (mData.getMobileNo() != null && !mData.getMobileNo().equalsIgnoreCase("")) {
            binding.mobile.setText(mData.getMobileNo());
        }
        if (mData.getAddress() != null && !mData.getAddress().equalsIgnoreCase("")) {
            binding.address.setText(mData.getAddress());
        }

        binding.layoutChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 190);
            }
        });

        binding.btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    UpdateProfile();
                }
            }
        });

    }

    private boolean validation() {
        if (binding.userName.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please insert Username", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.email.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please insert Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.mobile.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please insert Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cityID.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please insert City", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (countryID.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please insert Country", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (stateID.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please insert State", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 190) {
            Uri filePaths = data.getData();
            binding.profileImage.setImageURI(filePaths);
            filePath = FilePath.getPath(getActivity(), filePaths);
        }
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
                        ArrayList<Country> countryArray = new ArrayList<>(model.getData());

                        setCountryData(countryArray);
                    } else {
                        displayDialog("Alert", model.getMessage(), getContext());
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void setCountryData(ArrayList<Country> countryArray) {
        Country datum = new Country();
        datum.setId("0");
        datum.setName("Select Country");
        countryArray.add(0, datum);

        ArrayAdapter<Object> mAdapter = new CustomSpinnerAdapter(getContext(), R.layout.simple_spinner_item, (ArrayList<Object>) (Object) countryArray, "countries");
        mAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        binding.country.setAdapter(mAdapter);

        binding.country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        if (mData.getCountryId() != null) {


            Country userCountry = new Country();
            userCountry.setId(mData.getCountryId().getId());
            userCountry.setName(mData.getCountryId().getName());

            int index = Collections.binarySearch(countryArray, userCountry, new Comparator<Country>() {
                @Override
                public int compare(Country country, Country t1) {
                    return country.getId().compareTo(t1.getId());
                }
            });

            if (index >= 0) {
                binding.country.setSelection(index);
                countryID = userCountry.getId();
                getStates(userCountry.getId());
            }
        }

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
                            displayDialog("Alert", model.getMessage(), getContext());
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

    private void setCountryStateData(ArrayList<CountryState> stateArray) {
        CountryState datum = new CountryState();
        datum.setId("0");
        datum.setName("Select State");
        stateArray.add(0, datum);

        ArrayAdapter<Object> mAdapter = new CustomSpinnerAdapter(getContext(), R.layout.simple_spinner_item, (ArrayList<Object>) (Object) stateArray, "states");
        mAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        binding.state.setAdapter(mAdapter);
        binding.state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        if (mData.getStateId() != null) {


            CountryState userState = new CountryState();
            userState.setId(mData.getStateId().getId());
            userState.setName(mData.getStateId().getName());

            int index = Collections.binarySearch(stateArray, userState, new Comparator<CountryState>() {
                @Override
                public int compare(CountryState country, CountryState t1) {
                    return country.getId().compareTo(t1.getId());
                }
            });

            if (index >= 0) {
                binding.state.setSelection(index);
                stateID = userState.getId();
                getCities(userState.getId());
            }
        }

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
                        displayDialog("Alert", model.getMessage(), getContext());
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


        ArrayAdapter<Object> mAdapter = new CustomSpinnerAdapter(getContext(), R.layout.simple_spinner_item, (ArrayList<Object>) (Object) citiesArray, "cities");
        mAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        binding.city.setAdapter(mAdapter);

        binding.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        if (mData.getCityId() != null) {


            StateCity userCity = new StateCity();
            userCity.setId(mData.getCityId().getId());
            userCity.setName(mData.getCityId().getName());

            int index = Collections.binarySearch(citiesArray, userCity, new Comparator<StateCity>() {
                @Override
                public int compare(StateCity country, StateCity t1) {
                    return country.getId().compareTo(t1.getId());
                }
            });

            if (index >= 0) {
                binding.city.setSelection(index);
                stateID = userCity.getId();
            }
        }


    }

    private void UpdateProfile() {
        hud.show();
        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBody.addFormDataPart("userName", binding.userName.getText().toString());
        multipartBody.addFormDataPart("email", binding.email.getText().toString());
        multipartBody.addFormDataPart("mobileNo", binding.mobile.getText().toString());
        multipartBody.addFormDataPart("address", binding.address.getText().toString());
        multipartBody.addFormDataPart("role", "end_user");
        multipartBody.addFormDataPart("countryId", countryID);
        multipartBody.addFormDataPart("stateId", stateID);
        multipartBody.addFormDataPart("cityId", cityID);

        if (filePath != null && !filePath.equalsIgnoreCase("")) {
            File file = new File(filePath);
            multipartBody.addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }

        RequestBody mBody = multipartBody.build();

        Call<RegisterModel> call = RetrofitInstance.service.updateProfile(Helper.getJsonHeaderWithToken(getActivity()), mBody);
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
                            SharedPreferences mPref = getActivity().getSharedPreferences("main", MODE_PRIVATE);
                            mPref.edit().putString(Constants.user_profile, object.toString()).apply();
                            mPref.edit().putString(Constants.email_address, binding.email.getText().toString()).apply();
                            mPref.edit().putBoolean(Constants.IS_USER_LOGGED_IN, true).apply();

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                                    .setTitle("Success")
                                    .setMessage(model.getMessage())
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            getActivity().finish();

                                        }
                                    });

                            AlertDialog dialog = alertDialog.create();
                            dialog.show();
                            Log.i("tag", object.toString());
                        } else {
                            displayDialog("Alert", model.getMessage(), getActivity());
                        }
                    } else {
                        displayDialog("Alert", "Invalid Username or Password", getActivity());
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

