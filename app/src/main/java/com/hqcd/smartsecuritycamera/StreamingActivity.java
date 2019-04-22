package com.hqcd.smartsecuritycamera;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;

public class StreamingActivity extends AppCompatActivity {

    private static final String TAG = "StreamingActivity";
    Button startService, stopService;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        startService = findViewById(R.id.streaming_start_button);
        stopService = findViewById(R.id.streaming_stop_button);
        status = findViewById(R.id.streaming_status);

        if(isMyServiceRunning(StreamingService.class)){
            status.setText("Streaming service is running");
        }
        else{
            status.setText("Streaming service is not running");
        }
    }


    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.streaming_start_button:
                startService(new Intent(this, StreamingService.class));
                status.setText("Streaming service is running");
                break;
            case R.id.streaming_stop_button:
                stopService(new Intent(this, StreamingService.class));
                status.setText("Streaming service is not running");
                break;
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
