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

import com.google.gson.Gson;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityBookingHistoryDetails;
import com.octalabs.challetapp.activities.ActivityDetails;
import com.octalabs.challetapp.models.ModelBookingHistory.BookingHistoryDetails;
import com.octalabs.challetapp.models.ModelBookingHistory.BookingHistoryItem;
import com.octalabs.challetapp.models.ModelChalet;
import com.octalabs.challetapp.models.ModelWishlist.Datum;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.octalabs.challetapp.utils.Constants.BOOKING_HISTORY_DETAILS;
import static com.octalabs.challetapp.utils.Constants.CHALET_OR_MARRAIGE_ID;

public class AdapterBookingHistory extends RecyclerView.Adapter<AdapterBookingHistory.MyViewHolder> {
    private final Activity activity;

    public void setMlist(ArrayList<BookingHistoryItem> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();

    }

    private ArrayList<BookingHistoryItem> mlist;

    public AdapterBookingHistory(Activity activity, ArrayList<BookingHistoryItem> mlist) {
        this.activity = activity;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public AdapterBookingHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterBookingHistory.MyViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_booking_history, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBookingHistory.MyViewHolder holder, int position) {

        BookingHistoryItem item = mlist.get(position);

        holder.textOrderTitle.setText("Order : " + position + 1 + "");
        holder.textNumOfItems.setText("No of items : " + item.getBookingItemIds().size() + "");
        holder.textBookingDate.setText(Helper.getDate(Long.parseLong(item.getCreatedAt()), "dd-MMM-yyyy"));
        try {
            if (item.getBookingItemIds() != null && item.getBookingItemIds().size() > 0) {
                List<String> pictures = item.getBookingItemIds().get(0).getPicture();
                if (pictures != null && pictures.size() > 0) {
                    String imageName = pictures.get(0);
                    Picasso.get().load(RetrofitInstance.BASE_IMG_CHALET_URL + imageName).into(holder.itemImage);
                } else {
                    holder.itemImage.setImageBitmap(null);
                }
            } else {
                holder.itemImage.setImageBitmap(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textOrderTitle, textBookingDate, textNumOfItems;
        ImageView itemImage;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderTitle = itemView.findViewById(R.id.text_order_title);

            itemImage = itemView.findViewById(R.id.item_image);
            textBookingDate = itemView.findViewById(R.id.text_booking_date);
            textNumOfItems = itemView.findViewById(R.id.text_num_of_bookings);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityBookingHistoryDetails.class);
                    intent.putExtra(BOOKING_HISTORY_DETAILS, new Gson().toJson(mlist.get(getAdapterPosition()).getBookingItemIds()));
                    activity.startActivity(intent);
                }
            });
        }
    }

}
