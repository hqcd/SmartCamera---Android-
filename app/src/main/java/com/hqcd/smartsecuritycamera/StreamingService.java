package com.hqcd.smartsecuritycamera;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.pedro.rtplibrary.rtsp.RtspCamera2;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;

public class StreamingService extends Service implements ConnectCheckerRtsp {
    public StreamingService() {
    }

    private static final String TAG = "MyService";
    RtspCamera2 rtspCamera2;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand: ");
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
        String url = "rtsp://192.168.1.189:80/test/test";
        Log.d(TAG, ("Streaming to : " + url));
        if(!rtspCamera2.isStreaming()){
            if(rtspCamera2.isRecording() || rtspCamera2.prepareAudio() && rtspCamera2.prepareVideo()){
                rtspCamera2.startStream(url);
                Toast.makeText(this, "Begin Streaming", Toast.LENGTH_SHORT).show();
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
