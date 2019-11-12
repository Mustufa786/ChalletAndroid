package com.octalabs.challetapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityFilter;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.adapter.AdapterMarriageHall;
import com.octalabs.challetapp.adapter.MyPagerAdapter;
import com.octalabs.challetapp.databinding.FragmentHomeBinding;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private MyPagerAdapter myPagerAdapter;
    private Button mBtnchalet, mBtnMarriageall;
    private RecyclerView mRecyclerView;
    private KProgressHUD hud;
    private AdapterMarriageHall adapterMarriageHall;
    private AdapterChalets adapterChalets;
    private GoogleMap mMap;
    private final static int REQUEST_FILTER = 256;

    double LocationLat = 0.0, LocationLong = 0.0;
    boolean isAscendingOrder = true;

    private FragmentHomeBinding binding;
    private ArrayList<Chalet> chaletArrayList;
    private ArrayList<Chalet> marraigeHallList;
    private boolean isMapShowing;
    private boolean isChaletTab = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        initializeMap();


        Init(binding.getRoot());
        hud = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        getAllChalets();
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ActivityFilter.class), REQUEST_FILTER);
            }
        });


        return binding.getRoot();
    }

    private void initializeMap() {

        SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (myMAPF != null) {
            myMAPF.getMapAsync(this);
        }


    }


    private void getAllChalets() {
        hud.show();
        Call<AllChaletsModel> call = RetrofitInstance.service.getAllChalets(Helper.getJsonHeaderWithToken(getContext()));
        call.enqueue(new Callback<AllChaletsModel>() {
            @Override
            public void onResponse(Call<AllChaletsModel> call, Response<AllChaletsModel> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    AllChaletsModel model = response.body();
                    if (model.getSuccess()) {
                        getAllMarrigeHalls();
                        chaletArrayList = new ArrayList<>(model.getData());
                        for (int i = 0; i < model.getData().size(); i++) {
                            Chalet item = model.getData().get(i);
                            if (item.getLatitude() != null && item.getLongitude() != null && !item.getLatitude().equalsIgnoreCase("") && !item.getLongitude().equalsIgnoreCase("")) {
                                Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()))));
                                m.setTag(item);

                                if (i == 0) {
                                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude())), 15);
                                    mMap.animateCamera(update);
                                }

                            }
                        }
                        adapterChalets.setMlist(chaletArrayList);

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
    }


    private void getAllMarrigeHalls() {
        hud.show();
        Call<AllChaletsModel> call = RetrofitInstance.service.getAllMarraiges(Helper.getJsonHeaderWithToken(getContext()));
        call.enqueue(new Callback<AllChaletsModel>() {
            @Override
            public void onResponse(Call<AllChaletsModel> call, Response<AllChaletsModel> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    AllChaletsModel model = response.body();
                    if (model.getSuccess()) {
                        marraigeHallList = new ArrayList<>(model.getData());
                        adapterMarriageHall.setMlist(marraigeHallList);

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
    }


    private void Init(View v) {
        mBtnchalet = v.findViewById(R.id.btn_chalet);
        mBtnMarriageall = v.findViewById(R.id.btn_marriage_hall);
        mBtnchalet.setOnClickListener(this);
        mBtnMarriageall.setOnClickListener(this);
        mRecyclerView = v.findViewById(R.id.rv_marriage_hall);
        adapterMarriageHall = new AdapterMarriageHall(getActivity(), new ArrayList<Chalet>());
        adapterChalets = new AdapterChalets(getActivity(), new ArrayList<Chalet>());
        mRecyclerView.setAdapter(adapterChalets);
        binding.textMapOrList.setOnClickListener(this);
        binding.getRoot().findViewById(R.id.map).setVisibility(View.GONE);
        binding.rvMarriageHall.setVisibility(View.VISIBLE);
        binding.btnSort.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_chalet:
                showChalletTab();

                break;

            case R.id.btn_marriage_hall:
                showMarraigeTab();

                break;

            case R.id.text_map_or_list:

                changeMapOrList();
                break;

            case R.id.btn_sort:
                sortData();
                break;

            default:
                break;

        }
    }

    private void showMarraigeTab() {
        mBtnchalet.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        mBtnchalet.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        mBtnMarriageall.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        mBtnMarriageall.setTextColor(getActivity().getResources().getColor(R.color.white));
        Collections.sort(marraigeHallList, new Comparator<Chalet>() {
            @Override
            public int compare(Chalet datum, Chalet t1) {
                return datum.getId().compareToIgnoreCase(t1.getId());
            }
        });
        mRecyclerView.setAdapter(adapterMarriageHall);
        isChaletTab = false;
        isAscendingOrder = true;
        if (marraigeHallList != null && this.mMap != null) {
            mMap.clear();
            for (int i = 0; i < marraigeHallList.size(); i++) {
                Chalet item = marraigeHallList.get(i);
                if (item.getLatitude() != null && item.getLongitude() != null && !item.getLatitude().equalsIgnoreCase("") && !item.getLongitude().equalsIgnoreCase("")) {
                    Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()))));
                    m.setTag(item);

                    if (i == 0) {
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                                new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude())), 15);
                        mMap.animateCamera(update);
                    }

                }
            }
        }
    }

    private void showChalletTab() {
        mBtnchalet.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        mBtnchalet.setTextColor(getActivity().getResources().getColor(R.color.white));
        mBtnMarriageall.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        mBtnMarriageall.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        Collections.sort(chaletArrayList, new Comparator<Chalet>() {
            @Override
            public int compare(Chalet datum, Chalet t1) {
                return datum.getId().compareToIgnoreCase(t1.getId());
            }
        });
        mRecyclerView.setAdapter(adapterChalets);
        isChaletTab = true;
        isAscendingOrder = true;
        if (chaletArrayList != null && this.mMap != null) {
            mMap.clear();
            for (int i = 0; i < chaletArrayList.size(); i++) {
                Chalet item = chaletArrayList.get(i);
                if (item.getLatitude() != null && item.getLongitude() != null && !item.getLatitude().equalsIgnoreCase("") && !item.getLongitude().equalsIgnoreCase("")) {
                    Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()))));
                    m.setTag(item);

                    if (i == 0) {
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                                new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude())), 15);
                        mMap.animateCamera(update);
                    }

                }
            }
        }
    }

    private void sortData() {
        if (Helper.hasInternetConnection(getActivity())) {
            isAscendingOrder = !isAscendingOrder;

        } else {
            Toast.makeText(getActivity(), "Check your internet connection for filter", Toast.LENGTH_SHORT).show();
            return;

        }
        if (isChaletTab) {
            Collections.sort(chaletArrayList, new Comparator<Chalet>() {
                @Override
                public int compare(Chalet datum, Chalet t1) {
                    return datum.getId().compareToIgnoreCase(t1.getId());
                }
            });

            if (isAscendingOrder) {
                adapterChalets.setMlist(chaletArrayList);
            } else {
                Collections.reverse(chaletArrayList);
                adapterChalets.setMlist(chaletArrayList);
            }
        } else {
            Collections.sort(marraigeHallList, new Comparator<Chalet>() {
                @Override
                public int compare(Chalet datum, Chalet t1) {
                    return datum.getId().compareToIgnoreCase(t1.getId());
                }
            });

            if (isAscendingOrder) {
                adapterMarriageHall.setMlist(marraigeHallList);
            } else {
                Collections.reverse(marraigeHallList);
                adapterMarriageHall.setMlist(marraigeHallList);
            }
        }

    }

    private void changeMapOrList() {
        isMapShowing = !isMapShowing;

        if (!isMapShowing) {
            binding.textMapOrList.setText("Map");
            binding.imgMapOrList.setBackground(getActivity().getResources().getDrawable(R.drawable.mapicon));
            binding.rvMarriageHall.setVisibility(View.VISIBLE);
            binding.getRoot().findViewById(R.id.map).setVisibility(View.GONE);
        } else {
            binding.textMapOrList.setText("List");
            binding.imgMapOrList.setBackground(getActivity().getResources().getDrawable(R.drawable.listicon));
            binding.rvMarriageHall.setVisibility(View.GONE);
            binding.getRoot().findViewById(R.id.map).setVisibility(View.VISIBLE);

        }


    }

    private Bitmap resizeBitmap(int width, int height) {

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.locationpointer);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
//        return imageBitmap;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        this.mMap = googleMap;


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILTER) {
                ArrayList<Chalet> mList = new Gson().fromJson(data.getExtras().getString(Constants.SEARCH_RESULT), new TypeToken<List<Chalet>>() {
                }.getType());
                adapterChalets.setMlist(mList);
            }
        }
    }
}


