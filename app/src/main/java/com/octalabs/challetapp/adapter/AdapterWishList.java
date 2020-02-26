package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.activities.ActivityDetails;
import com.octalabs.challetapp.fragments.FragmentSearch;
import com.octalabs.challetapp.models.ModelWishlist.Datum;
import com.octalabs.challetapp.retrofit.RetrofitInstance;
import com.octalabs.challetapp.utils.Helper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octalabs.challetapp.utils.Constants.CHALET_OR_MARRAIGE_ID;

public class AdapterWishList extends RecyclerView.Adapter<AdapterWishList.MyViewHolder> {
    private final Activity activity;
    KProgressHUD hud;

    public void setMlist(ArrayList<Datum> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();

    }

    private ArrayList<Datum> mlist;

    public AdapterWishList(Activity activity, ArrayList<Datum> mlist) {
        this.activity = activity;
        this.mlist = mlist;
        this.hud = KProgressHUD.create(activity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterWishList.MyViewHolder(activity.getLayoutInflater().inflate(R.layout.adapter_chalets, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Datum item = mlist.get(position);
        if (item.getBookingItemId().getPicture() != null && item.getBookingItemId().getPicture().size() > 0) {
            Picasso.get().load(RetrofitInstance.BASE_IMG_CHALET_URL + item.getBookingItemId().getPicture().get(0)).into(holder.imgChalet);

        }

        holder.textChaletName.setText(item.getBookingItemId().getName());
        holder.textLocation.setText(item.getBookingItemId().getLocation());
        holder.textPrice.setText(item.getBookingItemId().getPricePerNight() + " SR");
        if (item.getBookingItemId().getRating() > 0) {
            holder.ratingBar.setRating(item.getBookingItemId().getRating());
        } else
            holder.ratingBar.setRating(0);


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

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textChaletName, textLocation, textPrice;
        RatingBar ratingBar;
        ImageView imgChalet;
        Button btnCheckAvailibality;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textChaletName = itemView.findViewById(R.id.text_chalet_name);
            textLocation = itemView.findViewById(R.id.text_location);
            textPrice = itemView.findViewById(R.id.text_price);
            ratingBar = itemView.findViewById(R.id.rating);
            imgChalet = itemView.findViewById(R.id.img_chalet);
            btnCheckAvailibality = itemView.findViewById(R.id.btn_check_availibilty);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityDetails.class);
                    intent.putExtra(CHALET_OR_MARRAIGE_ID, mlist.get(getAdapterPosition()).getBookingItemId().getId());
                    activity.startActivity(intent);
                }
            });
        }
    }

}
