package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityDetails;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.octalabs.challetapp.utils.Constants.CHALET_OR_MARRAIGE_ID;
import static com.octalabs.challetapp.utils.Constants.NUM_OF_BOOKING_DAYS;

public class AdapterSearchResults extends RecyclerView.Adapter<AdapterSearchResults.MyViewHolder> {

    private final Activity activity;
    private int numOfBookingDays = 1;
    onItemClickListener itemClickListener;

    public AdapterSearchResults(Activity activitySearchAndFilterResult, ArrayList<Chalet> mList, int numOfBookingDays, onItemClickListener itemClickListener) {
        this.activity = activitySearchAndFilterResult;
        this.mlist = mList;
        this.numOfBookingDays = numOfBookingDays;
        this.itemClickListener = itemClickListener;
    }

    public void setMlist(ArrayList<Chalet> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();
    }

    private ArrayList<Chalet> mlist;


    public interface onItemClickListener {
        void onItemClicked(String id);
    }

    @NonNull
    @Override
    public AdapterSearchResults.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterSearchResults.MyViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_search_results, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchResults.MyViewHolder holder, int position) {
        Chalet item = mlist.get(position);
        if (item.getPicture() != null && item.getPicture().size() > 0) {
            Picasso.get().load(RetrofitInstance.BASE_IMG_CHALET_URL + item.getPicture().get(0)).resize(130, 130).into(holder.imgChalet);

        }
        holder.textChaletName.setText(item.getName());
        holder.textLocation.setText(item.getLocation());
        int totalPrice = item.getPricePerNight() * numOfBookingDays;
        holder.textPrice.setText(totalPrice + " Riyal For " + numOfBookingDays + " Days");
        if (item.getRating() > 0) {
            holder.ratingBar.setRating(item.getRating() + 0f);
        }
//        holder.ratingBar.setRating(4);


        if (item.getFor() != null) {
            if (item.getFor().contains("Singles")) {
                holder.btnSingles.setVisibility(View.VISIBLE);
            }
            if (item.getFor().contains("Families")) {
                holder.btnFamilies.setVisibility(View.VISIBLE);
            }
            if (item.getFor().contains("Ocassions")) {

                holder.btnOcassions.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textChaletName, textLocation, textPrice;
        ImageView imgChalet;
        MaterialRatingBar ratingBar;
        TextView btnSingles, btnFamilies, btnOcassions;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textChaletName = itemView.findViewById(R.id.text_chalet_name);
            textLocation = itemView.findViewById(R.id.text_location);
            textPrice = itemView.findViewById(R.id.text_price);
            ratingBar = itemView.findViewById(R.id.rating);
            btnSingles = itemView.findViewById(R.id.btn_single);
            btnFamilies = itemView.findViewById(R.id.btn_families);
            btnOcassions = itemView.findViewById(R.id.btn_ocassion);

            ratingBar.setNumStars(5);

            imgChalet = itemView.findViewById(R.id.img_chalet);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClicked(mlist.get(getAdapterPosition()).getId());

                }
            });
        }
    }
}

