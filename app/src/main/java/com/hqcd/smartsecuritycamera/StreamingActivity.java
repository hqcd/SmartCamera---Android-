package com.hqcd.smartsecuritycamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;

public class StreamingActivity extends AppCompatActivity
        implements ConnectCheckerRtsp, SurfaceHolder.Callback {

    private static final String TAG = "StreamingActivity";
    RtspCamera1 rtspCamera1;
    Button streamButton;
    EditText ipET, portET, userET, deviceET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.streaming_surfaceview);
        streamButton = (Button)findViewById(R.id.streaming_start_button);
        ipET = (EditText)findViewById(R.id.streaming_ip_et);
        portET = (EditText)findViewById(R.id.streaming_port_et);
        userET = (EditText)findViewById(R.id.streaming_user_et);
        deviceET = (EditText)findViewById(R.id.streaming_device_et);
        rtspCamera1 = new RtspCamera1(surfaceView, this);
        surfaceView.getHolder().addCallback(this);

    }

    @Override
    public void onConnectionSuccessRtsp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StreamingActivity.this, "Connection Successful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onConnectionFailedRtsp(String reason) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StreamingActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDisconnectRtsp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StreamingActivity.this, "Disconnected ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthErrorRtsp() {

    }

    @Override
    public void onAuthSuccessRtsp() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        rtspCamera1.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        rtspCamera1.stopPreview();
    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.streaming_start_button:
                if (!rtspCamera1.isStreaming()) {
                    if (rtspCamera1.isRecording()
                            || rtspCamera1.prepareAudio() && rtspCamera1.prepareVideo()) {
                        streamButton.setText("Stop Streaming");
                        String url = "rtsp://" + ipET.getText().toString() + ":" + portET.getText().toString() + "/" + userET.getText().toString() + "/" + deviceET.getText().toString();
                        Log.d(TAG, "Streaming to : " + url);
                        rtspCamera1.startStream(url);
                    } else {
                        Toast.makeText(this, "Error preparing stream, This device cant do it",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    streamButton.setText("Start Stream");
                    rtspCamera1.stopStream();
                }
        }
    }
}
