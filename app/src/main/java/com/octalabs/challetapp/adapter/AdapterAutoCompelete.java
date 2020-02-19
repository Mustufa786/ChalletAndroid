package com.octalabs.challetapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelCity.StateCity;
import com.octalabs.challetapp.models.modelseatchcity.ModelCity;

import java.util.ArrayList;
import java.util.List;

public class AdapterAutoCompelete extends ArrayAdapter<ModelCity> {
    private Context context;
    private int resourceId;
    private List<ModelCity> items, tempItems, suggestions;

    public AdapterAutoCompelete(@NonNull Context context, int resourceId, ArrayList<ModelCity> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            ModelCity fruit = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.texttv);
            name.setText(fruit.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public ModelCity getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return fruitFilter;
    }

    private Filter fruitFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ModelCity fruit = (ModelCity) resultValue;
            return fruit.getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (ModelCity fruit : tempItems) {
                    if (fruit.getName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(fruit);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<ModelCity> tempValues = (ArrayList<ModelCity>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (ModelCity fruitObj : tempValues) {
                    add(fruitObj);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}
