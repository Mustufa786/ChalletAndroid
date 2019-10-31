package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelDetails.AmenityId;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterDetailsAmenities extends RecyclerView.Adapter<AdapterDetailsAmenities.ViewHolder> {


    private final Activity activity;
    private final List<AmenityId> mlist;

    public AdapterDetailsAmenities(Activity activity, List<AmenityId> mlist) {
        this.activity = activity;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public AdapterDetailsAmenities.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_detail_amenities, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDetailsAmenities.ViewHolder holder, int position) {
        holder.mTvTitle.setText(mlist.get(position).getTitle() + "");
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.title_amenity);

        }
    }
}
