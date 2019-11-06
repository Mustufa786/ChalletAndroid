package com.octalabs.challetapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.RegisterActivity;
import com.octalabs.challetapp.adapter.AdapterMarriageHall;
import com.octalabs.challetapp.adapter.CustomSpinnerAdapter;
import com.octalabs.challetapp.databinding.FragmentUserProfileBinding;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.models.ModelCity.CityModel;
import com.octalabs.challetapp.models.ModelCity.StateCity;
import com.octalabs.challetapp.models.ModelCountry.Country;
import com.octalabs.challetapp.models.ModelCountry.CountryModel;
import com.octalabs.challetapp.models.ModelLogin.Login;
import com.octalabs.challetapp.models.ModelState.CountryState;
import com.octalabs.challetapp.models.ModelState.StateModel;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class UserProfileFragment extends Fragment {

    FragmentUserProfileBinding binding;
    Login mData;

    KProgressHUD hud;
    private String countryID;
    private String stateID;
    private String cityID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);


        mData = new Gson().fromJson(getActivity().getSharedPreferences("main", Context.MODE_PRIVATE).getString(Constants.user_profile, ""), Login.class);
        hud = KProgressHUD.create(getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);

        setData();

        return binding.getRoot();

    }

    private void setData() {
//    if(mData.get)

        if(mData.getUserName() != null && !mData.getUserName().equalsIgnoreCase(""))
        {
            binding.userName.setText(mData.getUserName());
        }
        if(mData.getEmail() != null && !mData.getEmail().equalsIgnoreCase(""))
        {
            binding.email.setText(mData.getEmail());
            binding.email.setEnabled(false);
        }
        if(mData.getMobileNo() != null && !mData.getMobileNo().equalsIgnoreCase(""))
        {
            binding.userName.setText(mData.getUserName());
        }
        if(mData.getUserName() != null && !mData.getUserName().equalsIgnoreCase(""))
        {
            binding.userName.setText(mData.getUserName());
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
                binding.state.setSelection(index);
                stateID = userCity.getId();
                getCities(userCity.getId());
            }
        }


    }

}

