package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityDetails;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelChalet;

import java.util.ArrayList;

import static com.octalabs.challetapp.utils.Constants.CHALET_OR_MARRAIGE_ID;

public class AdapterChalets extends RecyclerView.Adapter<AdapterChalets.MyViewHolder> {

    private final Activity activity;

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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textChaletName, textLocation, textPrice;
        RatingBar ratingBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textChaletName = itemView.findViewById(R.id.text_chalet_name);
            textLocation = itemView.findViewById(R.id.text_location);
            textPrice = itemView.findViewById(R.id.text_price);
            ratingBar = itemView.findViewById(R.id.rating);

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
