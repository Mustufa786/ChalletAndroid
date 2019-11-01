package com.octalabs.challetapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityContactUs;
import com.octalabs.challetapp.activities.ActivityLogin;
import com.octalabs.challetapp.activities.ActivityWishList;
import com.octalabs.challetapp.activities.ChangePasswordActivity;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.CustomDialog;

public class FragmentMore extends Fragment implements View.OnClickListener {

    private TextView textChangePassword, textContactUs, textWishList, textChangeLanguage, textLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_more, container, false);

        init(v);

        return v;
    }

    private void init(View v) {
        textChangePassword = v.findViewById(R.id.text_change_password);
        textChangePassword.setOnClickListener(this);
        textContactUs = v.findViewById(R.id.text_contact_us);
        textContactUs.setOnClickListener(this);
        textWishList = v.findViewById(R.id.text_wish_list);
        textWishList.setOnClickListener(this);
        textChangeLanguage = v.findViewById(R.id.text_change_language);
        textChangeLanguage.setOnClickListener(this);
        textLogout = v.findViewById(R.id.text_logout);
        textLogout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_change_password:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;


            case R.id.text_contact_us:
                startActivity(new Intent(getActivity(), ActivityContactUs.class));
                break;


            case R.id.text_wish_list:
                startActivity(new Intent(getContext(), ActivityWishList.class));
                break;


            case R.id.text_change_language:
                CustomDialog cd = new CustomDialog(getActivity());
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                break;


            case R.id.text_logout:
                logoutUser();
                break;

            default:
                break;
        }
    }

    private void logoutUser() {
        SharedPreferences preferences = getActivity().getSharedPreferences("main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Constants.email_address);
        editor.remove(Constants.user_profile);
        editor.remove(Constants.password);
        editor.remove(Constants.USER_CART);

        editor.putBoolean(Constants.IS_USER_LOGGED_IN, false);
        editor.apply();

        getActivity().finishAffinity();
        Intent intent = new Intent(getContext(), ActivityLogin.class);
        startActivity(intent);
    }
}

