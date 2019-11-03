package com.octalabs.challetapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.databinding.ActivityThankyouBinding;
import com.octalabs.challetapp.models.ModelCheckout.Data;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

public class ActivityThankyou extends AppCompatActivity {


    ActivityThankyouBinding mBinding;
    Data item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_thankyou);


        item = new Gson().fromJson(getIntent().getStringExtra(Constants.ORDER_DETAILS), Data.class);



        mBinding.textInvoice.setText(item.getId());
//        mBinding.textBookingDate.setText(Helper.getDate("dd-MMM-yyyy" , "yyyy-MM-dd'T'HH:mm:ss Z" , item.getBookingFrom()));

        mBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityThankyou.this, MainActivity.class));

            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityThankyou.this, MainActivity.class));

    }
}
