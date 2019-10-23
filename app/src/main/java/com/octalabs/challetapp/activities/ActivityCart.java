package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterCart;
import com.octalabs.challetapp.models.ModelCart;

import java.util.ArrayList;

public class ActivityCart extends AppCompatActivity {

    private RecyclerView mRvCart;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mRvCart = findViewById(R.id.rv_cart);
        btnCheckout = findViewById(R.id.btn_checkout);
        AdapterCart adapterCart = new AdapterCart(this, new ArrayList<ModelCart>());
        mRvCart.setAdapter(adapterCart);
        setTextAction(getSupportActionBar(), getResources().getString(R.string.cart));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityCart.this, PaymentActivity.class));
            }
        });
    }

    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(ActivityCart.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setTextColor(Color.WHITE);
        textview.setGravity(Gravity.CENTER);
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

    ;
}
