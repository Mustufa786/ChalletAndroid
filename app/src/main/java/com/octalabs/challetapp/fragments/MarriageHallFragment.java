package com.octalabs.challetapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterMarriageHall;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelChalet;

import java.util.ArrayList;

class MarriageHallFragment extends Fragment {

    private RecyclerView mRvMarriageHalls;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_marriage_hall, container, false);
        Init(v);
        return v;
    }

    private void Init(View v) {
        mRvMarriageHalls = v.findViewById(R.id.rv_marriage_hall);
        AdapterMarriageHall adapterMarriageHall = new AdapterMarriageHall(getActivity(), new ArrayList<AllChaletsModel>());
        mRvMarriageHalls.setAdapter(adapterMarriageHall);
    }
}
