package com.octalabs.challetapp.fragments;

import android.content.Intent;
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
import com.octalabs.challetapp.activities.ActivityWishList;
import com.octalabs.challetapp.activities.ChangePasswordActivity;

public class FragmentMore extends Fragment implements View.OnClickListener {

    private TextView textChangePassword, textContactUs, textWishList;

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

            default:
                break;
        }
    }
}

