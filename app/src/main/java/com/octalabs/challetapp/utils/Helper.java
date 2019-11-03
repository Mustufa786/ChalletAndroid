package com.octalabs.challetapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.google.gson.Gson;
import com.octalabs.challetapp.models.ModelLogin.Login;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;

public class Helper {

    public static HashMap<String, String> getJsonHeader() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        return headerMap;
    }

    public static HashMap<String, String> getJsonHeaderWithToken(Context context) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("token", getToken(context));
        return headerMap;
    }

    public static HashMap<String, String> getTokenHeader(String token) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("token", token);
        return headerMap;
    }

    public static RequestBody getRequestBodyFromJSON(String json) {
        RequestBody mBody = RequestBody.create(MediaType.parse("application/json"), json);
        return mBody;

    }

    public static String getToken(Context context) {
        Log.i("tag123", context.getSharedPreferences("main", MODE_PRIVATE).getString(Constants.user_profile, ""));

        return new Gson().fromJson(context.getSharedPreferences("main", MODE_PRIVATE).getString(Constants.user_profile, ""), Login.class).getToken();
//        return  "";
    }

    public static String getUserID(Context context) {

        return new Gson().fromJson(context.getSharedPreferences("main", MODE_PRIVATE).getString(Constants.user_profile, ""), Login.class).getId() + "";
//   return "";
    }


    public static boolean isUserLoggedIn(Context context) {

        return context.getSharedPreferences("main", MODE_PRIVATE).getBoolean(Constants.IS_USER_LOGGED_IN, false);
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public static void displayDialog(String title, String message, Context context) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


        AlertDialog dialog = alertDialog.create();
        dialog.show();

    }


    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public static String getDate(String requiredDateStr, String comingDateFormatStr, String dateStr) {
        try {
            // Create a DateFormatter object for displaying date in specified format.
            SimpleDateFormat formatter = new SimpleDateFormat(comingDateFormatStr);
            SimpleDateFormat formatter1 = new SimpleDateFormat(requiredDateStr);

            Date date = formatter.parse(dateStr);

            assert date != null;
            return formatter1.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static boolean hasInternetConnection(Context context) {
//        final String command = "ping -c 1 google.com";
//        return Runtime.getRuntime().exec(command).waitFor() == 0;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

    }

}
