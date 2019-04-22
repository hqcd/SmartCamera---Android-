package com.hqcd.smartsecuritycamera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hqcd.smartsecuritycamera.adapter.ImageAdapter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class VisitorFragment extends Fragment {

    String user, ip, rtspPort, httpPort, device;
    SharedPreferences sharedPreferences;
    ArrayList<String> paths;
    RecyclerView recyclerView;
    TextView loadingTV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        paths = new ArrayList<>();

        user = this.getArguments().getString("user");
        ip = sharedPreferences.getString("pref_ip_address" , "");
        rtspPort = sharedPreferences.getString("pref_port" , "");
        httpPort = sharedPreferences.getString("pref_http_port" , "");
        device = sharedPreferences.getString("pref_device_name" , "");



        String url = "http://" + sharedPreferences.getString("pref_ip_address", "") + ":" + sharedPreferences.getString("pref_http_port", "") + "/images";
        Ion.with(this).load(url).setBodyParameter("user", user)
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

        getActivity().setTitle("Visitors");
        View visitorFragmentRoot = inflater.inflate(R.layout.fragment_visitors, container, false);
        recyclerView = visitorFragmentRoot.findViewById(R.id.recycler_view);
        loadingTV = visitorFragmentRoot.findViewById(R.id.loading_tv);

        return visitorFragmentRoot;
    }

    public void initRecyclerView()
    {
        loadingTV.setVisibility(View.INVISIBLE);
        ImageAdapter adapter = new ImageAdapter(paths, getActivity(),
                sharedPreferences.getString("pref_ip_address", ""),
                user,
                sharedPreferences.getString("pref_device_name", ""));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
