package com.hqcd.smartsecuritycamera;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hqcd.smartsecuritycamera.adapter.RecordingListAdapter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class RecordingsFragment extends Fragment implements View.OnClickListener {

    String user, ip, rtspPort, httpPort, device;
    SharedPreferences sharedPreferences;
    ArrayList<String> list;
    private TextView load;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        list = new ArrayList<>();
        user = this.getArguments().getString("user");
        ip = sharedPreferences.getString("pref_ip_address" , "");
        rtspPort = sharedPreferences.getString("pref_port" , "");
        httpPort = sharedPreferences.getString("pref_http_port" , "");
        device = sharedPreferences.getString("pref_device_name" , "");

        fetchRecordings();
        getActivity().setTitle("Recordings");

        View recordingFragmentView = inflater.inflate(R.layout.fragment_recording_list, container, false);

        load = recordingFragmentView.findViewById(R.id.load_recordings_tv);
        recyclerView = recordingFragmentView.findViewById(R.id.recording_list_recycler_view);
        return recordingFragmentView;
    }

    public void fetchRecordings()
    {
        list.clear();
        String url = "http://" + sharedPreferences.getString("pref_ip_address", "") + ":" + sharedPreferences.getString("pref_http_port", "") + "/videos";
        Ion.with(this).load(url)
                .setBodyParameter("user", user)
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
        RecordingListAdapter adapter = new RecordingListAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.recording_name:
                TextView tv = (TextView)view;
                String rec = tv.getText().toString();
                Toast.makeText(getActivity(), rec, Toast.LENGTH_SHORT).show();
                launchVideo(rec);
                break;
        }
    }

    public void launchVideo(String name)
    {
        int vlcRequestCode = 42;
        Uri uri = Uri.parse("http://" + ip + ":" + sharedPreferences.getString("pref_http_port", "") + "/users/" + user + "/videos/" + name);
        Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
        vlcIntent.setPackage("org.videolan.vlc");
        vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
        vlcIntent.setComponent(new ComponentName("org.videolan.vlc", "org.videolan.vlc.gui.video.VideoPlayerActivity"));
        startActivityForResult(vlcIntent, vlcRequestCode);
    }
}
