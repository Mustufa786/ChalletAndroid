package com.octalabs.challetapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterMarriageHall;
import com.octalabs.challetapp.adapter.MyPagerAdapter;
import com.octalabs.challetapp.models.ModelChalet;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private MyPagerAdapter myPagerAdapter;
    private Button mBtnchalet, mBtnMarriageall;
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Init(v);
        return v;
    }

    private void Init(View v) {
        mBtnchalet = v.findViewById(R.id.btn_chalet);
        mBtnMarriageall = v.findViewById(R.id.btn_marriage_hall);
        mBtnchalet.setOnClickListener(this);
        mBtnMarriageall.setOnClickListener(this);
        mRecyclerView = v.findViewById(R.id.rv_marriage_hall);
        AdapterMarriageHall adapterMarriageHall = new AdapterMarriageHall(getActivity(), new ArrayList<ModelChalet>());
        mRecyclerView.setAdapter(adapterMarriageHall);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_chalet:
                mBtnchalet.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                mBtnchalet.setTextColor(getActivity().getResources().getColor(R.color.white));
                mBtnMarriageall.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                mBtnMarriageall.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                break;

            case R.id.btn_marriage_hall:
                mBtnchalet.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                mBtnchalet.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                mBtnMarriageall.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                mBtnMarriageall.setTextColor(getActivity().getResources().getColor(R.color.white));
                break;

        }
    }
}


