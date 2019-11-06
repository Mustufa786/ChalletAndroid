package com.octalabs.challetapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.adapter.AdapterMarriageHall;
import com.octalabs.challetapp.adapter.MyPagerAdapter;
import com.octalabs.challetapp.databinding.FragmentHomeBinding;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private MyPagerAdapter myPagerAdapter;
    private Button mBtnchalet, mBtnMarriageall;
    private RecyclerView mRecyclerView;
    private KProgressHUD hud;
    private AdapterMarriageHall adapterMarriageHall;
    private AdapterChalets adapterChalets;
    private ArrayList<Marker> markersArray = new ArrayList<>();
    private GoogleMap mMap;

    double LocationLat = 0.0, LocationLong = 0.0;

    FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);


        Init(binding.getRoot());
        hud = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        getAllChalets();

        return binding.getRoot();
    }

    private void initializeMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
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
                        ArrayList arrayList = new ArrayList<>(model.getData());
                        for (int i = 0; i < model.getData().size(); i++) {
                            Chalet item = model.getData().get(i);
                            if (item.getLatitude() != null && item.getLongitude() != null && !item.getLatitude().equalsIgnoreCase("") && !item.getLongitude().equalsIgnoreCase("")) {
                                Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()))).icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap(100, 150))));
                                m.setTag(item);
                                markersArray.add(m);
                            }
                        }
                        adapterChalets.setMlist(arrayList);

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
                        ArrayList arrayList = new ArrayList<>(model.getData());
                        adapterMarriageHall.setMlist(arrayList);

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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_chalet:
                mBtnchalet.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                mBtnchalet.setTextColor(getActivity().getResources().getColor(R.color.white));
                mBtnMarriageall.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                mBtnMarriageall.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                mRecyclerView.setAdapter(adapterChalets);
                break;

            case R.id.btn_marriage_hall:
                mBtnchalet.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                mBtnchalet.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                mBtnMarriageall.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                mBtnMarriageall.setTextColor(getActivity().getResources().getColor(R.color.white));
                mRecyclerView.setAdapter(adapterMarriageHall);
                break;

            case R.id.text_map_or_list:
                changeMapOrList();

        }
    }

    private void changeMapOrList() {
        if (binding.textMapOrList.getText().toString().equalsIgnoreCase("Map")) {
            binding.textMapOrList.setText("List");
            binding.imgMapOrList.setBackground(getActivity().getResources().getDrawable(R.drawable.wishlisticon));
            binding.rvMarriageHall.setVisibility(View.VISIBLE);
            binding.getRoot().findViewById(R.id.map).setVisibility(View.GONE);

        } else {
            binding.textMapOrList.setText("Map");
            binding.imgMapOrList.setBackground(getActivity().getResources().getDrawable(R.drawable.mapicon));
            binding.rvMarriageHall.setVisibility(View.GONE);
            binding.getRoot().findViewById(R.id.map).setVisibility(View.VISIBLE);

        }
    }

    public Bitmap resizeBitmap(int width, int height) {

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.new_avatar);
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


        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                new LatLng(LocationLat, LocationLong), 15);
        mMap.animateCamera(update);
    }
}


