package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.fragments.FragmentSearch;
import com.octalabs.challetapp.models.ModelAllChalets.Chalet;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Helper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSearchResults extends RecyclerView.Adapter<AdapterSearchResults.MyViewHolder> {

    private final Activity activity;
    private int numOfBookingDays = 1;
    onItemClickListener itemClickListener;
    KProgressHUD hud;

    public AdapterSearchResults(Activity activitySearchAndFilterResult, ArrayList<Chalet> mList, int numOfBookingDays, onItemClickListener itemClickListener) {
        this.activity = activitySearchAndFilterResult;
        this.mlist = mList;
        this.numOfBookingDays = numOfBookingDays;
        this.itemClickListener = itemClickListener;
        this.hud = KProgressHUD.create(activity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);

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
        final Chalet item = mlist.get(position);
        if (item.getPicture() != null && item.getPicture().size() > 0) {
            Picasso.get().load(RetrofitInstance.BASE_IMG_CHALET_URL + item.getPicture().get(0)).resize(130, 130).into(holder.imgChalet);

        }
        holder.textChaletName.setText(item.getName());
        holder.textLocation.setText(item.getLocation());
        DecimalFormat decim = new DecimalFormat("#,###.##");
        float totalPrice = item.getPricePerNight() * numOfBookingDays;
//        holder.textPrice.setText(totalPrice + " Riyal For " + numOfBookingDays + " Days");
//        holder.textPrice.setText(decim.format(totalPrice)  + activity.getResources().getString(R.string.riyal_for) + numOfBookingDays + activity.getResources().getString(R.string.days));

        holder.textPrice.setText(decim.format(totalPrice)  + " " +  activity.getResources().getString(R.string.riyal_for) + " " + numOfBookingDays + " " +  activity.getResources().getString(R.string.days));

        if (item.getRating() > 0) {
            holder.ratingBar.setRating(item.getRating() + 0f);
        } else
            holder.ratingBar.setRating(0);
//        holder.ratingBar.setRating(4);

        holder.btnSingles.setVisibility(View.GONE);
        holder.btnFamilies.setVisibility(View.GONE);
        holder.btnOcassions.setVisibility(View.GONE);


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

        holder.btnCheckAvailibality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForAvailibality(item.getId());
            }
        });

    }

    private void checkForAvailibality(String id) {
        try {
            hud.show();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bookingItemId", id);
            jsonObject.put("bookingFrom", FragmentSearch.checkInStr);
            jsonObject.put("bookingTo", FragmentSearch.checkoutStr);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitInstance.service.checkForAvailability(requestBody, Helper.getJsonHeader());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hud.dismiss();
                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response.body().string());
                            if (jsonObject1.getBoolean("success")) {
                                Toast.makeText(activity, "Chalet Available On These Dates", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(activity, "Chalet Not Available On These Dates . Try Selecting Some Other Dates", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hud.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
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
        Button btnCheckAvailibality;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textChaletName = itemView.findViewById(R.id.text_chalet_name);
            textLocation = itemView.findViewById(R.id.text_location);
            textPrice = itemView.findViewById(R.id.text_price);
            ratingBar = itemView.findViewById(R.id.rating);
            btnSingles = itemView.findViewById(R.id.btn_single);
            btnFamilies = itemView.findViewById(R.id.btn_families);
            btnOcassions = itemView.findViewById(R.id.btn_ocassion);
            btnCheckAvailibality = itemView.findViewById(R.id.btn_check_availibilty);

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

