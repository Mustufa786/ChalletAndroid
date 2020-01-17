package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityCart;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.models.ModelCart;
import com.octalabs.challetapp.models.ModelDetails.ChaletDetails;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {

    private final Activity activity;
    private final ArrayList<ChaletDetails> modelCarts;

    public AdapterCart(Activity activity, ArrayList<ChaletDetails> modelCarts) {
        this.activity = activity;
        this.modelCarts = modelCarts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_cart_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ChaletDetails item = modelCarts.get(position);
        if (item.getPicture() != null && item.getPicture().size() > 0) {
            Picasso.get().load(RetrofitInstance.BASE_IMG_CHALET_URL + item.getPicture().get(0)).into(holder.imgChalet);

        }

        holder.textChaletName.setText(item.getName());
        holder.textLocation.setText(item.getLocation());
        holder.textPrice.setText(item.getPricePerNight() + " SR");
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelCarts.remove(position);
                notifyDataSetChanged();
                activity.getSharedPreferences("main", Context.MODE_PRIVATE).edit().putString(Constants.USER_CART,new Gson().toJson(modelCarts)).apply();
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelCarts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textChaletName, textLocation, textPrice, textBookingDate;
        ImageView imgChalet;
        Button btnRemove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textChaletName = itemView.findViewById(R.id.text_chalet_name);
            textLocation = itemView.findViewById(R.id.text_location);
            textPrice = itemView.findViewById(R.id.text_price);
            imgChalet = itemView.findViewById(R.id.img_chalet);
            textBookingDate = itemView.findViewById(R.id.booking_date);
            btnRemove = itemView.findViewById(R.id.btn_remove);


        }
    }
}
