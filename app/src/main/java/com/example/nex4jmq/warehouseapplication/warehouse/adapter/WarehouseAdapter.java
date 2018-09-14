package com.example.nex4jmq.warehouseapplication.warehouse.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nex4jmq.warehouseapplication.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.ArrayList;

public class WarehouseAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    private ArrayList<String> url;
    private ArrayList<String> barcode;

    public WarehouseAdapter(Context context, ArrayList<String> values, ArrayList<String> url, ArrayList<String> barcode) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.url = url;
        this.barcode = barcode;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.product_list_item, parent, false);
        TextView tvProductName = (TextView) rowView.findViewById(R.id.product_name);
        ImageView imProductURL = (ImageView) rowView.findViewById(R.id.imProductURL);

        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        tvProductName.setText(values.get(position));
        // change the icon for Windows and iPhone


        Picasso.get()
                .load(url.get(position))
                .placeholder(R.drawable.warehouse)
                .error(R.mipmap.ic_launcher)
                .resize(280, 00)
                .centerInside()
                .tag(context)
                .into(imProductURL);

        return rowView;
    }
}