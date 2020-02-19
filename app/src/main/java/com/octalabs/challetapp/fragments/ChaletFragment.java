package com.octalabs.challetapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.adapter.AdapterChalets;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelChalet;

import java.util.ArrayList;

public class ChaletFragment extends Fragment {

    private RecyclerView mRvChalet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chalet, container, false);
        Init(v);
        return v;
    }

    private void Init(View v) {
        mRvChalet = v.findViewById(R.id.rv_chalet);
        AdapterChalets adapterChalets = new AdapterChalets(getActivity(),new ArrayList<Chalet>());
        mRvChalet.setAdapter(adapterChalets);

    }
}
