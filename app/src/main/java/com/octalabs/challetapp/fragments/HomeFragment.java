package com.octalabs.challetapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.MyPagerAdapter;

public class HomeFragment extends Fragment {

    private ViewPager mHomePager;
    private MyPagerAdapter myPagerAdapter;
    private Button mBtnchalet, mBtnMarriageall;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Init(v);
        return v;
    }

    private void Init(View v) {
        mHomePager = v.findViewById(R.id.home_pager);
        mBtnchalet = v.findViewById(R.id.btn_chalet);
        mBtnMarriageall = v.findViewById(R.id.btn_marriage_hall);

        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragmeent(new ChaletFragment(), "Chalet");
        myPagerAdapter.addFragmeent(new ChaletFragment(), "Marriage Hall");
        mHomePager.setAdapter(myPagerAdapter);

        mHomePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mBtnchalet.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                    mBtnchalet.setTextColor(getActivity().getResources().getColor(R.color.white));
                    mBtnMarriageall.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    mBtnMarriageall.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                } else if (position == 1){
                    mBtnchalet.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    mBtnchalet.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                    mBtnMarriageall.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                    mBtnMarriageall.setTextColor(getActivity().getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}


