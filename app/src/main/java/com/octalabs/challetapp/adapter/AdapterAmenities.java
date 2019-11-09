package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.RVOnItemClicks.OnItemClicked;
import com.octalabs.challetapp.models.ModelDetails.AmenityId;

import java.util.ArrayList;

public class AdapterAmenities extends RecyclerView.Adapter<AdapterAmenities.MyViewHolder> {


    private final Activity activityFilter;
    private final ArrayList<AmenityId> mAmenities;
    private OnItemClicked<AmenityId> click;

    public AdapterAmenities(Activity activityFilter, ArrayList<AmenityId> mAmenities, OnItemClicked<AmenityId> click) {
        this.activityFilter = activityFilter;
        this.mAmenities = mAmenities;
        this.click = click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(activityFilter.getLayoutInflater().inflate(R.layout.adapter_amenities, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mCheckAmeniies.setText(String.format("%s", mAmenities.get(position).getTitle()));
    }

    @Override
    public int getItemCount() {
        return mAmenities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckAmeniies;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckAmeniies = itemView.findViewById(R.id.check_amenities);
            mCheckAmeniies.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    click.onClick(compoundButton.isChecked(), getAdapterPosition(), mAmenities.get(getAdapterPosition()));
                }
            });
        }
    }
}
