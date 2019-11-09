package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.utils.Constants;

import java.util.Objects;

import static android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK;

public class WebviewActivity extends AppCompatActivity {

    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        String pageTitle = getIntent().getExtras().getString(Constants.PAGE_TITLE);
        String pageURL = getIntent().getExtras().getString(Constants.PAGE_URL);

        if (pageTitle.equalsIgnoreCase("Privacy Policy")) {
            pageTitle = getResources().getString(R.string.privacy_policy);

        } else {
            pageTitle = getResources().getString(R.string.terms_amp_conditions);

        }
        setTextAction(Objects.requireNonNull(getSupportActionBar()), pageTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        WebView mWebView = findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        final AlertDialog alertDialog = new AlertDialog.Builder(WebviewActivity.this).create();
        progressBar = ProgressDialog.show(WebviewActivity.this, "", "Loading...");

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("TAG", "Processing webview url click...");
                view.loadUrl(url);

                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i("TAG", "Finished loading URL: " + url);

                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }


            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("TAG", "Error: " + description);
                Toast.makeText(WebviewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });

        mWebView.loadUrl(pageURL);


    }

    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setTextColor(Color.WHITE);
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
