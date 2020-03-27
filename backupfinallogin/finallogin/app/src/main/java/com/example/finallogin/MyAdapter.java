package com.example.finallogin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class MyAdapter extends ArrayAdapter<String> {
     Context context;
     String[] names;
     double[] ratings;
     Activity activity;

    MyAdapter(Context c, String [] names, double[] ratings)
    {

        super(c, R.layout.custom_row, names);
        this.context=c;
        this.names=names;
        this.ratings=ratings;
        this.activity=(Activity) c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=layoutInflater.inflate(R.layout.custom_row, parent, false);
        TextView name=(TextView) row.findViewById(R.id.name);
        TextView rating=(TextView) row.findViewById(R.id.rating);
        name.setText(names[position]);
        rating.setText(String.valueOf(ratings[position]));

        return row;
    }
}
