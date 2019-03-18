package com.hqcd.smartsecuritycamera;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;

public class RecordingService extends Service {
    MediaRecorder recorder;
    boolean recording = false;


    public RecordingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        return START_STICKY;
    }
}
