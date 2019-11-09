package com.octalabs.challetapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelDetails.ChaletDetails;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ActivitySearchAndFilterResult extends AppCompatActivity {

    ArrayList<Chalet> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_filter_result);


        mList = new Gson().fromJson( getIntent().getExtras().getString("searchList") , new TypeToken<List<Chalet>>() {
        }.getType());
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        AdapterChalets adapterChalets = new AdapterChalets(this, new ArrayList<Chalet>());
        recyclerView.setAdapter(adapterChalets);




    }
}
