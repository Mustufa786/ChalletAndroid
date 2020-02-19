package com.octalabs.challetapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.Helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class SplashActivity extends Activity {


    Resources res;
    DisplayMetrics dm;
    android.content.res.Configuration conf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.octalabs.challetapp",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        Fresco.initialize(this);
        RetrofitInstance.createRetrofitInstance();

        SharedPreferences settings = getSharedPreferences("main", MODE_PRIVATE);
        boolean firstRun = settings.getBoolean("firstRun", false);
        if (!firstRun) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", true);
            if (settings.contains(Constants.user_profile)) {
                editor.remove(Constants.user_profile);
            }
            editor.apply();
        }

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
