package com.hqcd.smartsecuritycamera;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hqcd.smartsecuritycamera.adapter.ImageAdapter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ImageListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> paths = new ArrayList<>();
    TextView loadingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        loadingTV = (TextView)findViewById(R.id.loading_tv);

        Ion.with(this).load("https://api.myjson.com/bins/18z1pc")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        JsonArray jsonArray = result.getAsJsonArray("files");
                        for(int i = 0; i < jsonArray.size();i++)
                        {
                            //Remove quotes from the image names
                            String temp = jsonArray.get(i).toString();
                            temp = temp.replace("\"", "");

                            //Add the image paths to the arraylist
                            paths.add(temp);
                        }
                        initRecyclerView();
                    }
                });
    }
    public void initRecyclerView()
    {
        loadingTV.setVisibility(View.INVISIBLE);
        ImageAdapter adapter = new ImageAdapter(paths, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }
}