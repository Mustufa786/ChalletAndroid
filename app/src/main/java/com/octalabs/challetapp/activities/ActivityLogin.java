package com.octalabs.challetapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.octalabs.challetapp.R;

public class ActivityLogin extends Activity {


    private Button mBtnSignUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnSignUP = findViewById(R.id.btn_sig_up);
        mBtnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLogin.this,RegisterActivity.class));
            }
        });
    }
}
