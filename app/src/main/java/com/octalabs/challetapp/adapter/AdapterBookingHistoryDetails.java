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
import com.octalabs.challetapp.models.ModelBookingHistory.BookingHistoryDetails;
import com.octalabs.challetapp.models.ModelWishlist.Datum;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.octalabs.challetapp.utils.Constants.CHALET_OR_MARRAIGE_ID;

public class AdapterBookingHistoryDetails extends RecyclerView.Adapter<AdapterBookingHistoryDetails.MyViewHolder> {


    private final Activity activity;
    private ArrayList<BookingHistoryDetails> mlist;

    public AdapterBookingHistoryDetails(Activity activity, ArrayList<BookingHistoryDetails> mlist) {
        this.activity = activity;
        this.mlist = mlist;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterBookingHistoryDetails.MyViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_chalets, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BookingHistoryDetails item = mlist.get(position);
        if (item.getPicture() != null && item.getPicture().size() > 0) {
            Picasso.get().load(RetrofitInstance.BASE_IMG_CHALET_URL + item.getPicture().get(0)).into(holder.imgChalet);

        }

        holder.textChaletName.setText(item.getName());
        holder.textLocation.setText(item.getLocation());
        holder.textPrice.setText(item.getPricePerNight() + " Riyal");
        if (item.getRating() > 0) {
            holder.ratingBar.setNumStars(item.getRating());
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

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textChaletName, textLocation, textPrice;
        ImageView imgChalet;
        TextView btnSingles, btnFamilies, btnOcassions;
        MaterialRatingBar ratingBar;
        Button btnCheckAvailibilty;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textChaletName = itemView.findViewById(R.id.text_chalet_name);
            textLocation = itemView.findViewById(R.id.text_location);
            textPrice = itemView.findViewById(R.id.text_price);
            ratingBar = itemView.findViewById(R.id.rating);
            imgChalet = itemView.findViewById(R.id.img_chalet);
            btnSingles = itemView.findViewById(R.id.btn_single);
            btnFamilies = itemView.findViewById(R.id.btn_families);
            btnOcassions = itemView.findViewById(R.id.btn_ocassion);
            btnCheckAvailibilty = itemView.findViewById(R.id.btn_check_availibilty);
            btnCheckAvailibilty.setVisibility(View.GONE);

        }
    }

}
