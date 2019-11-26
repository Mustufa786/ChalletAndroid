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
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityWishList;
import com.octalabs.challetapp.activities.MainActivity;
import com.octalabs.challetapp.adapter.AdapterBookingHistory;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.adapter.AdapterWishList;
import com.octalabs.challetapp.adapter.MyPagerAdapter;
import com.octalabs.challetapp.fragments.HomeFragment;
import com.octalabs.challetapp.models.ModelBookingHistory.BookingHistoryDetails;
import com.octalabs.challetapp.models.ModelBookingHistory.BookingHistoryItem;
import com.octalabs.challetapp.models.ModelBookingHistory.ModelBookingHistory;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.models.ModelWishlist.ModelWishlist;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Helper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class FragmentBookingHistory extends Fragment {

    private RecyclerView mRvBookingHistory;
    KProgressHUD hud;
    AdapterBookingHistory mAdapter;
    TextView textNoData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_booking_history, container, false);
        hud = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);


        Init(v);
        getAllWishListData();
        return v;
    }

    private void Init(View v) {
        textNoData = v.findViewById(R.id.text_no_data);
        mRvBookingHistory = v.findViewById(R.id.rv_booking_history);
        mAdapter = new AdapterBookingHistory(getActivity(), new ArrayList<BookingHistoryItem>());
        mRvBookingHistory.setAdapter(mAdapter);

    }

    private void getAllWishListData() {
        hud.show();
        Call<ModelBookingHistory> call = RetrofitInstance.service.getBookingHistory(Helper.getJsonHeaderWithToken(getActivity()));
        call.enqueue(new Callback<ModelBookingHistory>() {
            @Override
            public void onResponse(Call<ModelBookingHistory> call, Response<ModelBookingHistory> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    ModelBookingHistory model = response.body();
                    if (model.getSuccess()) {
                        ArrayList arrayList = new ArrayList<>(model.getData());
                        if (arrayList.size() == 0) {
                            textNoData.setVisibility(View.VISIBLE);
                        }
                        mAdapter.setMlist(arrayList);

                    } else {
                        displayDialog("Alert", model.getMessage(), getActivity());
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelBookingHistory> call, Throwable t) {
                hud.dismiss();
            }
        });
    }


}
