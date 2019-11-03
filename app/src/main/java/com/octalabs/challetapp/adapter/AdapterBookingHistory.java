package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityDetails;
import com.octalabs.challetapp.models.ModelBookingHistory.BookingHistoryDetails;
import com.octalabs.challetapp.models.ModelBookingHistory.BookingHistoryItem;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.models.ModelWishlist.Datum;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.octalabs.challetapp.utils.Constants.CHALET_OR_MARRAIGE_ID;

public class AdapterBookingHistory extends RecyclerView.Adapter<AdapterBookingHistory.MyViewHolder> {
    private final Activity activity;

    public void setMlist(ArrayList<BookingHistoryDetails> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();

    }

    private ArrayList<BookingHistoryDetails> mlist;

    public AdapterBookingHistory(Activity activity, ArrayList<BookingHistoryDetails> mlist) {
        this.activity = activity;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public AdapterBookingHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterBookingHistory.MyViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_chalets, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBookingHistory.MyViewHolder holder, int position) {

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


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textChaletName, textLocation, textPrice;
        RatingBar ratingBar;
        ImageView imgChalet;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textChaletName = itemView.findViewById(R.id.text_chalet_name);
            textLocation = itemView.findViewById(R.id.text_location);
            textPrice = itemView.findViewById(R.id.text_price);
            ratingBar = itemView.findViewById(R.id.rating);
            imgChalet = itemView.findViewById(R.id.img_chalet);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityDetails.class);
                    intent.putExtra(CHALET_OR_MARRAIGE_ID, mlist.get(getAdapterPosition()).getId());
                    activity.startActivity(intent);
                }
            });
        }
    }

}
