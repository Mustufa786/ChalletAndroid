package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityDetails;
import com.octalabs.challetapp.models.ModelAllChalets.AllChaletsModel;
import com.octalabs.challetapp.models.ModelChalet;

import java.util.ArrayList;

public class AdapterMarriageHall extends RecyclerView.Adapter<AdapterMarriageHall.MyViewHolder> {

    private final Activity activity;

    public void setMlist(ArrayList<AllChaletsModel> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();
    }

    private ArrayList<AllChaletsModel> mlist;

    public AdapterMarriageHall(Activity activity, ArrayList<AllChaletsModel> mlist) {
        this.activity = activity;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_chalets, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityDetails.class);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
