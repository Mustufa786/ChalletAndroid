package com.octalabs.challetapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.databinding.ActivitySearchAndFilterResultBinding;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelDetails.ChaletDetails;
import com.octalabs.challetapp.utils.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ActivitySearchAndFilterResult extends AppCompatActivity implements OnMapReadyCallback {

    ArrayList<Chalet> mList;
    private GoogleMap mMap;
    AdapterChalets adapterChalets;

    ActivitySearchAndFilterResultBinding mBinding;
    private final static int REQUEST_FILTER = 256;
    private boolean isMapShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_and_filter_result);


        initializeMap();



        mBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mBinding.getRoot().findViewById(R.id.map).setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        mList = new Gson().fromJson(getIntent().getExtras().getString("searchList"), new TypeToken<List<Chalet>>() {
        }.getType());


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapterChalets = new AdapterChalets(this, mList);
        recyclerView.setAdapter(adapterChalets);

        mBinding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySearchAndFilterResult.this, ActivityFilter.class);
                startActivityForResult(intent, REQUEST_FILTER);
            }
        });


        mBinding.imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySearchAndFilterResult.this, ActivityCart.class));
            }
        });

        mBinding.imgMapList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMapOrList();
            }
        });


    }

    private void initializeMap() {

        SupportMapFragment myMAPF = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (myMAPF != null) {
            myMAPF.getMapAsync(this);
        }


    }

    private void changeMapOrList() {
        isMapShowing = !isMapShowing;
        if (!isMapShowing) {
            mBinding.imgMapList.setBackground(getResources().getDrawable(R.drawable.mapicon_new));
            mBinding.recyclerView.setVisibility(View.VISIBLE);
            mBinding.getRoot().findViewById(R.id.map).setVisibility(View.GONE);
        } else {
            mBinding.imgMapList.setBackground(getResources().getDrawable(R.drawable.listicon));
            mBinding.recyclerView.setVisibility(View.GONE);
            mBinding.getRoot().findViewById(R.id.map).setVisibility(View.VISIBLE);


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILTER) {
                mList = new Gson().fromJson(getIntent().getExtras().getString(Constants.SEARCH_RESULT), new TypeToken<List<Chalet>>() {
                }.getType());
                adapterChalets.setMlist(mList);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        this.mMap = googleMap;
        for (int i = 0; i < mList.size(); i++) {
            Chalet item = mList.get(i);
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

    private Bitmap resizeBitmap(int width, int height) {

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.locationpointer);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }


}
