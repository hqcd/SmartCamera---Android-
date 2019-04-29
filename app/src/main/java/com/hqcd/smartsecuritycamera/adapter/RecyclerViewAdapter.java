package com.hqcd.smartsecuritycamera.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqcd.smartsecuritycamera.R;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mImageNames, ArrayList<String> mImages, Context mContext) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_grid_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        Ion.with(viewHolder.image).load(mImages.get(i));

        String file = mImageNames.get(i);
        int separate = file.indexOf("-");
        String name = file.substring(0, separate);
        String date = "Seen on: " + file.substring(separate+1, file.length()-4);

        viewHolder.nameView.setText(name);
        viewHolder.dateView.setText(date);
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        ImageView image;
        TextView nameView, dateView;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nameView = itemView.findViewById(R.id.name);
            dateView = itemView.findViewById(R.id.date);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

    }


}
