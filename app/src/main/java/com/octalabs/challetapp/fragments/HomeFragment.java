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

import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.adapter.AdapterMarriageHall;
import com.octalabs.challetapp.adapter.MyPagerAdapter;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelAllMarraiges.AllMarraigesModel;
import com.octalabs.challetapp.models.ModelAllMarraiges.Marraige;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Helper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private MyPagerAdapter myPagerAdapter;
    private Button mBtnchalet, mBtnMarriageall;
    RecyclerView mRecyclerView;
    KProgressHUD hud;
    AdapterMarriageHall adapterMarriageHall;
    AdapterChalets adapterChalets;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Init(v);
        hud = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        getAllChalets();

        return v;
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
        Call<AllMarraigesModel> call = RetrofitInstance.service.getAllMarraiges(Helper.getJsonHeaderWithToken(getContext()));
        call.enqueue(new Callback<AllMarraigesModel>() {
            @Override
            public void onResponse(Call<AllMarraigesModel> call, Response<AllMarraigesModel> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    AllMarraigesModel model = response.body();
                    if (model.getSuccess()) {
                        ArrayList arrayList = new ArrayList<>(model.getData());
                        adapterMarriageHall.setMlist(arrayList);

                    } else {
                        displayDialog("Alert", model.getMessage(), getContext());
                    }
                }
            }

            @Override
            public void onFailure(Call<AllMarraigesModel> call, Throwable t) {
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
        adapterMarriageHall = new AdapterMarriageHall(getActivity(), new ArrayList<Marraige>());
        adapterChalets = new AdapterChalets(getActivity(), new ArrayList<Chalet>());
        mRecyclerView.setAdapter(adapterChalets);
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

        }
    }
}


