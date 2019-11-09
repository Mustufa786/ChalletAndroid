package com.octalabs.challetapp.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.octalabs.challetapp.R;
import com.octalabs.challetapp.models.ModelCity.StateCity;
import com.octalabs.challetapp.models.ModelCountry.Country;
import com.octalabs.challetapp.models.ModelLocation.SampleLocation;
import com.octalabs.challetapp.models.ModelState.CountryState;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<Object> {

    private ArrayList<Object> objects;
    private Context context;
    String type;

    public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Object> objects, String type) {
        super(context, resource, objects);
        this.context = context;
        this.type = type;
        this.objects = objects;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.simple_spinner_item, parent, false);

        if (type.equalsIgnoreCase("countries")) {
            Country model = (Country) objects.get(position);
            TextView label = row.findViewById(R.id.text1);
            label.setText(model.getName());

        }


        if (type.equalsIgnoreCase("states")) {
            CountryState model = (CountryState) objects.get(position);
            TextView label = row.findViewById(R.id.text1);
            label.setText(model.getName());

        }


        if (type.equalsIgnoreCase("cities")) {
            StateCity model = (StateCity) objects.get(position);
            TextView label = row.findViewById(R.id.text1);
            label.setText(model.getName());

        }


        if (type.equalsIgnoreCase("locations")) {
            SampleLocation model = (SampleLocation) objects.get(position);
            TextView label = row.findViewById(R.id.text1);
            label.setText(model.getName());

        }

        return row;
    }
}