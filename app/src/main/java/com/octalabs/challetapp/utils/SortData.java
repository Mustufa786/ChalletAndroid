package com.octalabs.challetapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.octalabs.challetapp.R;

public class SortData extends Dialog implements View.OnClickListener {

    Activity mActivity;

    RadioButton price , rating , men , wommen;

    public SortData(Activity context) {
        super(context);
        this.mActivity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_apply_sorting);


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.price:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.rating:
                if (checked)
                    // Ninjas rule
                    break;

            case R.id.men:
                if (checked)
                    // Ninjas rule
                    break;


            case R.id.woman:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }


    @Override
    public void onClick(View view) {

    }
}
