package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityCart;
import com.octalabs.challetapp.models.ModelCart;

import java.util.ArrayList;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {

    private final Activity activity;
    private final ArrayList<ModelCart> modelCarts;

    public AdapterCart(Activity activity, ArrayList<ModelCart> modelCarts) {
        this.activity = activity;
        this.modelCarts = modelCarts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_cart_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
