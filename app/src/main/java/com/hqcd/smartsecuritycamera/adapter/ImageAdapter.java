package com.hqcd.smartsecuritycamera.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqcd.smartsecuritycamera.R;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private static final String TAG = "ImageAdapter";

    private ArrayList<String> imagePaths = new ArrayList<>();
    private Context context;
    private String ip;
    private String user;
    private String device;
    private String folder = "images";
    private SharedPreferences sharedPreferences;

    public ImageAdapter(ArrayList<String> imagePaths, Context context, String ip, String user, String device) {
        this.imagePaths = imagePaths;
        this.context = context;
        this.ip = ip;
        this.user = user;
        this.device = device;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_grid_item,viewGroup,false );
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String url = "http://" + ip + ":" + sharedPreferences.getString("pref_http_port", "") + "/users/" + user +  "/" + folder + "/" + imagePaths.get(i);
        Ion.with(viewHolder.imageView).load(url);
        String file = imagePaths.get(i);
        int separate = file.indexOf("-");
        String name = file.substring(0, separate);
        String date = "Seen on: " + file.substring(separate+1, file.length()-4);

        viewHolder.nameView.setText(name);
        viewHolder.dateView.setText(date);
        System.out.println("Url loaded: " + url);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nameView, dateView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            nameView = itemView.findViewById(R.id.name);
            dateView = itemView.findViewById(R.id.date);
        }
    }
}