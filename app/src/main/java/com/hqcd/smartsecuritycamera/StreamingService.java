package com.hqcd.smartsecuritycamera;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koushikdutta.ion.Ion;
import com.pedro.rtplibrary.rtsp.RtspCamera2;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;

public class StreamingService extends Service implements ConnectCheckerRtsp {
    public StreamingService() {
    }

    private static final String TAG = "MyService";
    RtspCamera2 rtspCamera2;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userid;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand: ");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userid = user.getUid();
        beginRecording();

        new Thread(new Runnable() {
            @Override
            public void run() {
                beginRecording();
            }
        });

        return START_STICKY;
    }

    public void beginRecording() {
        rtspCamera2 = new RtspCamera2(this, false, this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String ip = sharedPreferences.getString("pref_ip_address", "");
        String port = sharedPreferences.getString("pref_port", "");
        String device = sharedPreferences.getString("pref_device_name", "");
        String httpPort = sharedPreferences.getString("pref_http_port", "");

        String url = sharedPreferences.getString("pref_ip_address", "") + ":" + sharedPreferences.getString("pref_port", "") + "/" + userid + "/" + sharedPreferences.getString("pref_device_name", "");
        Log.d(TAG, ("Streaming to : " + url));
        if(!rtspCamera2.isStreaming()){
            if(rtspCamera2.isRecording() || rtspCamera2.prepareAudio() && rtspCamera2.prepareVideo()){
                String rtspURL = "rtsp://" + url;
                rtspCamera2.startStream(rtspURL);
                Toast.makeText(this, "Begin Streaming", Toast.LENGTH_SHORT).show();

                Ion.with(this).load("http://" + ip + ":" + httpPort + "/startRecording").setBodyParameter("url", rtspURL);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }

    public void stopRecording() {
        rtspCamera2.stopStream();
        Toast.makeText(this, "Stop Streaming", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnectionSuccessRtsp() {

    }

    @Override
    public void onConnectionFailedRtsp(String reason) {

    }

    @Override
    public void onDisconnectRtsp() {

    }

    @Override
    public void onAuthErrorRtsp() {

    }

    @Override
    public void onAuthSuccessRtsp() {

    }
}
