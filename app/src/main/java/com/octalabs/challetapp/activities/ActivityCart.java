package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterCart;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelCart;
import com.octalabs.challetapp.models.ModelDetails.ChaletDetails;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.CustomDialog;
import com.octalabs.challetapp.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityCart extends AppCompatActivity {

    private RecyclerView mRvCart;
    private Button btnCheckout;
    ArrayList<ChaletDetails> checkoutList;
    SharedPreferences sharedPreferences;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mRvCart = findViewById(R.id.rv_cart);
        btnCheckout = findViewById(R.id.btn_checkout);
        sharedPreferences = getSharedPreferences("main", MODE_PRIVATE);
        gson = new Gson();
        checkoutList = gson.fromJson(sharedPreferences.getString(Constants.USER_CART, "[]"), new TypeToken<List<ChaletDetails>>() {
        }.getType());


        AdapterCart adapterCart = new AdapterCart(this, checkoutList );
        mRvCart.setAdapter(adapterCart);
        setTextAction(getSupportActionBar(), getResources().getString(R.string.cart));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isUserLoggedIn(ActivityCart.this)) {
                    if (checkoutList.size() > 0) {
                        startActivity(new Intent(ActivityCart.this, PaymentActivity.class));
                    } else {
                        Toast.makeText(ActivityCart.this, "No Items Found In the Cart", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(ActivityCart.this, ActivityLogin.class);
                    startActivity(intent);
                }


            }
        });
    }

    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(ActivityCart.this);
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
                CustomDialog cd = new CustomDialog(ActivityCart.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                return false;
        }
        return false;
    }



}
