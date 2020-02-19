package com.octalabs.challetapp.custome;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.octalabs.challetapp.utils.Helper;

public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
        setTypeface(Helper.getRelativeFont(context));
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Helper.getRelativeFont(context));
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(Helper.getRelativeFont(context));
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeface(Helper.getRelativeFont(context));
    }
}
