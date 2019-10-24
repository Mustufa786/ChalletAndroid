package com.octalabs.challetapp.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.MainActivity;
import com.octalabs.challetapp.adapter.AdapterBookingHistory;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.adapter.MyPagerAdapter;
import com.octalabs.challetapp.fragments.HomeFragment;
import com.octalabs.challetapp.models.ModelChalet;

import java.util.ArrayList;

public class FragmentBookingHistory extends Fragment {

    private RecyclerView mRvBookingHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_booking_history, container, false);
        Init(v);
        return v;
    }

    private void Init(View v) {
        mRvBookingHistory = v.findViewById(R.id.rv_booking_history);
        AdapterBookingHistory adapterbookinghistory = new AdapterBookingHistory(getActivity(), new ArrayList<ModelChalet>());
        mRvBookingHistory.setAdapter(adapterbookinghistory);
    }
}
