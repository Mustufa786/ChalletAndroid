package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterBookingHistoryDetails;
import com.octalabs.challetapp.models.ModelBookingHistory.BookingHistoryDetails;
import com.octalabs.challetapp.models.ModelDetails.ChaletDetails;
import com.octalabs.challetapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityBookingHistoryDetails extends AppCompatActivity {


    RecyclerView mRecyclerView;
    KProgressHUD hud;
    ArrayList<BookingHistoryDetails> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history_details);

        init();

        setTextAction(Objects.requireNonNull(getSupportActionBar()), "Booking History Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);

    }

    private void init() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mList = new Gson().fromJson(getIntent().getStringExtra(Constants.BOOKING_HISTORY_DETAILS), new TypeToken<List<BookingHistoryDetails>>() {
        }.getType());
        AdapterBookingHistoryDetails mAdapter = new AdapterBookingHistoryDetails(this , mList);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

}
