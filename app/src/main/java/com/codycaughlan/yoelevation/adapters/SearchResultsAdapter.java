package com.codycaughlan.yoelevation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codycaughlan.yoelevation.R;
import com.codycaughlan.yoelevation.model.PlaceResult;

import java.util.ArrayList;

public class SearchResultsAdapter extends ArrayAdapter<PlaceResult> {
    private ArrayList<PlaceResult> mList;
    private LayoutInflater mInflater;

    public SearchResultsAdapter(Context context, int resid, ArrayList<PlaceResult> items) {
        super(context, resid, items);
        mList = items;
        mInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        PlaceResult place = getItem(position);

        if (convertView == null) {
            convertView = (LinearLayout)mInflater.inflate(R.layout.place_result, null);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.address = (TextView)convertView.findViewById(R.id.address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.name.setText(place.name);
        holder.address.setText(place.formatted_address);

        return convertView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView address;
    }
}
