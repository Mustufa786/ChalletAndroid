package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.adapter.AdapterMarriageHall;
import com.octalabs.challetapp.adapter.AdapterWishList;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.models.ModelWishlist.Datum;
import com.octalabs.challetapp.models.ModelWishlist.ModelWishlist;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.CustomDialog;
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
        AdapterWishList mAdapter;

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
        Call<ModelWishlist> call = RetrofitInstance.service.getWishList(Helper.getJsonHeaderWithToken(this));
        call.enqueue(new Callback<ModelWishlist>() {
            @Override
            public void onResponse(Call<ModelWishlist> call, Response<ModelWishlist> response) {
                if (response.body() != null) {
                    hud.dismiss();
                    ModelWishlist model = response.body();
                    if (model.getSuccess()) {
                        ArrayList arrayList = new ArrayList<>(model.getData());
                        mAdapter.setMlist(arrayList);

                    } else {
                        displayDialog("Alert", model.getMessage(), ActivityWishList.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelWishlist> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void init() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new AdapterWishList(this, new ArrayList<Datum>());
        mRecyclerView.setAdapter(mAdapter);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_home_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.options:
                CustomDialog cd = new CustomDialog(ActivityWishList.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                return false;
        }
        return false;
    }

}
