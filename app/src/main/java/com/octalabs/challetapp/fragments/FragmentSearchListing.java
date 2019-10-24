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
import com.octalabs.challetapp.adapter.AdapterBookingHistory;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.models.ModelChalet;

import java.util.ArrayList;

public class FragmentSearchListing extends Fragment {

    private RecyclerView mRvBookingHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_listing, container, false);
        Init(v);
        return v;
    }

    private void Init(View v) {
        mRvBookingHistory = v.findViewById(R.id.rv_booking_history);
        AdapterChalets adapterbookinghistory = new AdapterChalets(getActivity(), new ArrayList<ModelChalet>());
        mRvBookingHistory.setAdapter(adapterbookinghistory);
    }
}
