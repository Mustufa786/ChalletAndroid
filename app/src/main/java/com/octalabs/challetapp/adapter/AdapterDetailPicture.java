package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Constants;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

public class AdapterDetailPicture extends RecyclerView.Adapter<AdapterDetailPicture.ViewHolder> {


    private final Activity activity;
    private final List<String> mlist;

    public AdapterDetailPicture(Activity activity, List<String> mlist) {
        this.activity = activity;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public AdapterDetailPicture.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_detail_picture, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDetailPicture.ViewHolder holder, final int position) {
        Picasso.get().load(RetrofitInstance.BASE_IMG_CHALET_URL + mlist.get(position)).resize(120, 120).into(holder.mImg);

        holder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> urls = new ArrayList<>();
                for (int i = 0; i < mlist.size(); i++) {
                    urls.add(RetrofitInstance.BASE_IMG_CHALET_URL + mlist.get(i));
                }

                new ImageViewer.Builder(activity, urls)
                        .setStartPosition(position)
                        .setBackgroundColorRes(R.color.black)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img_detail_picture);


        }
    }
}
