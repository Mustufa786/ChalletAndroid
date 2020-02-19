package com.octalabs.challetapp.custome;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.octalabs.challetapp.utils.Helper;

public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
        setTypeface(Helper.getRelativeFont(context));
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Helper.getRelativeFont(context));
    }

    public CustomTextView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Helper.getRelativeFont(context));
    }
}
