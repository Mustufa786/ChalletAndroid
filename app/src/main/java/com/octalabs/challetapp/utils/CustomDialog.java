package com.octalabs.challetapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.MainActivity;

import java.util.Locale;


public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {


    Resources res;
    DisplayMetrics dm;
    android.content.res.Configuration conf;

    private Button mBtnEng, mBtnAr;
    public Activity c;

    public CustomDialog(Activity context) {
        super(context);
        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_change_language);
        mBtnEng = (Button) findViewById(R.id.btn_eng);
        mBtnAr = (Button) findViewById(R.id.btn_ar);

        mBtnEng.setOnClickListener(this);
        mBtnAr.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        if (v == mBtnEng) {
            res = c.getResources();
            dm = res.getDisplayMetrics();
            conf = res.getConfiguration();
            conf.setLocale(new Locale("en"));
            res.updateConfiguration(conf, dm);
            c.getSharedPreferences("main", Context.MODE_PRIVATE).edit().putString(Constants.SELECTED_LANGUAGE, "en").apply();
            dismiss();
            Intent intent = new Intent(c, MainActivity.class);
            c.startActivity(intent);
            c.finish();

        } else if (v == mBtnAr) {

            res = c.getResources();
            dm = res.getDisplayMetrics();
            conf = res.getConfiguration();
            conf.setLocale(new Locale("ar"));
            res.updateConfiguration(conf, dm);
            c.getSharedPreferences("main", Context.MODE_PRIVATE).edit().putString(Constants.SELECTED_LANGUAGE, "ar").apply();
            dismiss();
            Intent intent = new Intent(c, MainActivity.class);
            c.startActivity(intent);
            c.finish();
        }


    }
}
