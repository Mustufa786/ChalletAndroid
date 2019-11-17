package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityDetails;
import com.octalabs.challetapp.activities.ActivitySearchAndFilterResult;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.octalabs.challetapp.utils.Constants.CHALET_OR_MARRAIGE_ID;
import static com.octalabs.challetapp.utils.Constants.NUM_OF_BOOKING_DAYS;

public class AdapterChalets extends RecyclerView.Adapter<AdapterChalets.MyViewHolder> {

    private final Activity activity;
    private int numOfBookingDays = 1;

    public AdapterChalets(Activity activitySearchAndFilterResult, ArrayList<Chalet> mList, int numOfBookingDays) {
        this.activity = activitySearchAndFilterResult;
        this.mlist = mList;
        this.numOfBookingDays = numOfBookingDays;
    }

    public void setMlist(ArrayList<Chalet> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();
    }

    private ArrayList<Chalet> mlist;

    public AdapterChalets(Activity activity, ArrayList<Chalet> mlist) {
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
        Chalet item = mlist.get(position);
        if (item.getPicture() != null && item.getPicture().size() > 0) {
            Picasso.get().load(RetrofitInstance.BASE_IMG_CHALET_URL + item.getPicture().get(0)).into(holder.imgChalet);

        }
        holder.textChaletName.setText(item.getName());
        holder.textLocation.setText(item.getLocation());
        int totalPrice = item.getPricePerNight() * numOfBookingDays;
        holder.textPrice.setText("Booking For " + numOfBookingDays + " Days : " + totalPrice + " Riyal");
        if (item.getRating() > 0) {
            holder.ratingBar.setRating(item.getRating() + 0f);
        }


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


            imgChalet = itemView.findViewById(R.id.img_chalet);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityDetails.class);
                    intent.putExtra(CHALET_OR_MARRAIGE_ID, mlist.get(getAdapterPosition()).getId());
                    intent.putExtra(NUM_OF_BOOKING_DAYS, numOfBookingDays);

                    activity.startActivity(intent);
                }
            });
        }
    }
}
