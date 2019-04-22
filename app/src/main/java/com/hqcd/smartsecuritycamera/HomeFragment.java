package com.hqcd.smartsecuritycamera;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hqcd.smartsecuritycamera.adapter.RecyclerViewAdapter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    String user, ip, rtspPort, httpPort, device;
    SharedPreferences sharedPreferences;
    ArrayList<String> mNames;
    ArrayList<String> mImageUrls;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mNames = new ArrayList<>();
        mImageUrls = new ArrayList<>();

        user = this.getArguments().getString("user");
        ip = sharedPreferences.getString("pref_ip_address" , "");
        rtspPort = sharedPreferences.getString("pref_port" , "");
        httpPort = sharedPreferences.getString("pref_http_port" , "");
        device = sharedPreferences.getString("pref_device_name" , "");

        getActivity().setTitle("Home");
        initImageBitmaps();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLiveStream();
            }
        });
        return root;

    }

    private void initImageBitmaps()
    {
        mNames.clear();
        mImageUrls.clear();

        String url = "http://" + sharedPreferences.getString("pref_ip_address", "") + ":" + sharedPreferences.getString("pref_http_port", "") + "/images";
        System.out.println(url);
        Ion.with(this).load(url)
                .setBodyParameter("user", user)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(result != null)
                        {
                            JsonArray jsonArray = result.getAsJsonArray("files");
                            if(jsonArray != null)
                            {
                                for(int i = jsonArray.size() -1 ; i > jsonArray.size() - 4;i--)
                                {
                                    //Remove quotes from the image names
                                    String temp = jsonArray.get(i).toString();
                                    temp = temp.replace("\"", "");

                                    //Add the image paths to the arraylist
                                    mNames.add(temp);

                                }
                                initRecyclerView();
                            }
                        }

                    }
                });
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recycler view");

        for(int i = 0; i < mNames.size(); i++)
        {
            mImageUrls.add("http://" + sharedPreferences.getString("pref_ip_address", "")
                    + ":" + sharedPreferences.getString("pref_http_port", "") + "/users/" + user + "/images/" + mNames.get(i));

        }

        System.out.println(mImageUrls);


        RecyclerView recyclerView = getView().findViewById(R.id.recentActivityRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mImageUrls, getActivity());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    public void viewLiveStream() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Type the device name of the stream you want to view");
        builder.setTitle("View Stream");
        final EditText deviceET = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        deviceET.setLayoutParams(lp);
        builder.setView(deviceET);
        builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (deviceET.getText().toString() == sharedPreferences.getString("pref_device_name", "")) {
                    AlertDialog.Builder invalid = new AlertDialog.Builder(getActivity());
                    invalid.setMessage("Choose a different device");
                    invalid.setTitle("Already viewing this device");
                    builder.setPositiveButton("Ok", null);
                    AlertDialog invalidDialog = invalid.create();
                    invalidDialog.show();
                } else {
                    int vlcRequestCode = 42;
                    Uri uri = Uri.parse("rtsp://" + sharedPreferences.getString("pref_ip_address", "") + ":" + sharedPreferences.getString("pref_port", "") + "/" + user + "/" +
                            deviceET.getText().toString());
                    System.out.println(uri);
                    Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
                    vlcIntent.setPackage("org.videolan.vlc");
                    vlcIntent.setData(uri);
                    vlcIntent.setComponent(new ComponentName("org.videolan.vlc", "org.videolan.vlc.gui.video.VideoPlayerActivity"));
                    startActivityForResult(vlcIntent, vlcRequestCode);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
