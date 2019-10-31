package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.adapter.AdapterMarriageHall;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Helper;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Helper.displayDialog;

public class ActivityWishList extends AppCompatActivity {


    RecyclerView mRecyclerView;
    KProgressHUD hud;
    AdapterChalets mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        init();
        setTextAction(Objects.requireNonNull(getSupportActionBar()), "WishList");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        getAllWishListData();

    }

    private void getAllWishListData() {
        hud.show();
        Call<AllChaletsModel> call = RetrofitInstance.service.getAllMarraiges(Helper.getJsonHeaderWithToken(this));
        call.enqueue(new Callback<AllChaletsModel>() {
            @Override
            public void onResponse(Call<AllChaletsModel> call, Response<AllChaletsModel> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    AllChaletsModel model = response.body();
                    if (model.getSuccess()) {
                        ArrayList arrayList = new ArrayList<>(model.getData());
                        mAdapter.setMlist(arrayList);

                    } else {
                        displayDialog("Alert", model.getMessage(), ActivityWishList.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<AllChaletsModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void init() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new AdapterChalets(this, new ArrayList<Chalet>());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }


    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(ActivityWishList.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setTextColor(Color.WHITE);
//        textview.setGravity(Gravity.CENTER);
        textview.setTextSize(16);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
    }
}
