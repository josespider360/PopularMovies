package com.movies.popular.jrvm.com.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainGridViewAdapter extends ArrayAdapter<MainGridViewItem> {

    private Context mContext;
    private int mLayoutID;
    private ArrayList<MainGridViewItem> mData = new ArrayList<>();


    public MainGridViewAdapter(Context context, int resource, ArrayList<MainGridViewItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayoutID = resource;
        mData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridO = convertView;
        MyViewHolder holder = null;

        if (gridO == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            gridO = inflater.inflate(mLayoutID, parent, false);
            holder = new MyViewHolder(gridO);
            gridO.setTag(holder);
        } else {
            holder = (MyViewHolder) gridO.getTag();
        }


        MainGridViewItem item = mData.get(position);
        Picasso.with(mContext).load(item.getdMPoster()).into(holder.ImgVH);

        return gridO;
    }

    public class MyViewHolder {

        ImageView ImgVH;

        MyViewHolder(View v1) {
            ImgVH = (ImageView) v1.findViewById(R.id.main_gridview_image);
        }
    }


}
