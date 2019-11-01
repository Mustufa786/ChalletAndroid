package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelDetails.Review;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterDetailReviews extends RecyclerView.Adapter<AdapterDetailReviews.ViewHolder> {


    private final Activity activity;
    private final List<Review> mlist;

    public AdapterDetailReviews(Activity activity, List<Review> mlist) {
        this.activity = activity;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public AdapterDetailReviews.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_detail_reviews, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDetailReviews.ViewHolder holder, int position) {
        Review model = mlist.get(position);
        Picasso.get().load(RetrofitInstance.BASE_USER_PIC_URL + model.getUserId().getPicture()).resize(100 , 100).into(holder.mImg);
        holder.mName.setText(model.getUserId().getUserName() + "");
        holder.mReview.setText(model.getComment() + "");
//        holder.mDate.setText(model.getBookingItemId() + "");
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImg;
        private TextView mName, mReview, mDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img_user);
            mDate = itemView.findViewById(R.id.user_date);
            mReview = itemView.findViewById(R.id.user_review);
            mName = itemView.findViewById(R.id.user_name);

        }
    }
}
