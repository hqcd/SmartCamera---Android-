package com.hqcd.smartsecuritycamera;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hqcd.smartsecuritycamera.adapter.RecordingListAdapter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class RecordingListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ArrayList<String> list;
    private SharedPreferences sharedPreferences;
    private TextView load;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_list);

        //Initialize
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        load = (TextView)findViewById(R.id.load_recordings_tv);
        recyclerView = (RecyclerView)findViewById(R.id.recording_list_recycler_view);
        list = new ArrayList<>();


        fetchRecordings();


    }

    public void fetchRecordings()
    {
        Ion.with(this).load("https://api.myjson.com/bins/lgon8")
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
                            list.add(temp);
                        }
                        initRecyclerView();
                    }
                });
    }

    public void initRecyclerView()
    {
        load.setVisibility(View.INVISIBLE);
        RecordingListAdapter adapter = new RecordingListAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.recording_name:
                break;
        }
    }

    public void launchVideo(String name)
    {

    }


}
