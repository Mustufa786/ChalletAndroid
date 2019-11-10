package com.octalabs.challetapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

import java.util.Locale;

public class SplashActivity extends Activity {


    Resources res;
    DisplayMetrics dm;
    android.content.res.Configuration conf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        RetrofitInstance.createRetrofitInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                res = getResources();
                dm = res.getDisplayMetrics();
                conf = res.getConfiguration();

                if (getSharedPreferences("main", MODE_PRIVATE).getString(Constants.SELECTED_LANGUAGE, "en").equalsIgnoreCase("en")) {

                    conf.setLocale(new Locale("en"));
                    res.updateConfiguration(conf, dm);
                } else if (getSharedPreferences("main", MODE_PRIVATE).getString(Constants.SELECTED_LANGUAGE, "en").equalsIgnoreCase("ar")) {
                    conf.setLocale(new Locale("ar"));
                    res.updateConfiguration(conf, dm);
                }
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                if (Helper.isUserLoggedIn(SplashActivity.this)) {
//
//                } else {
//                    startActivity(new Intent(SplashActivity.this, ActivityLogin.class));
//
//                }

                finish();

            }
        }, 2000);
    }
}
