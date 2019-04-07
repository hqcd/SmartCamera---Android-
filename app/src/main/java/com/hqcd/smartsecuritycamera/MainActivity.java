package com.hqcd.smartsecuritycamera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private Button settingsBtn, logInBtn, logOutButton;
    private TextView welcomeText;
    private FirebaseUser user;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        logInBtn = (Button)findViewById(R.id.toLoginScreen);
        logOutButton = (Button)findViewById(R.id.logOutButton);
        settingsBtn = (Button) findViewById(R.id.settings_button);
        welcomeText = (TextView)findViewById(R.id.welcomeText);
        recyclerView = (RecyclerView)findViewById(R.id.recentActivityRecyclerView);
        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        settingsBtn.setEnabled(false);

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET}, 1);

        File thumbnailDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Thumbnails");
        if(!thumbnailDir.exists())
        {
            thumbnailDir.mkdirs();
            System.out.println(thumbnailDir.getAbsolutePath());
        }

        initImageBitmaps();

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(user);
        settingsBtn.setVisibility(View.INVISIBLE);
    }

    public void logOut()
    {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
        logInBtn.setEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(user);
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.settings_button:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.toLoginScreen:
                intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
                break;
            case R.id.logOutButton:
                logOut();
                break;
            case R.id.floatingActionButton:
                intent = new Intent(this, CameraRecorder.class);
                startActivity(intent);
                break;
        }
    }

    public void updateUI(FirebaseUser user)
    {

        if(user == null)
        {
            welcomeText.setText("Sign In to Continue");
            logInBtn.setEnabled(true);
            logInBtn.setVisibility(View.VISIBLE);
            logOutButton.setEnabled(false);
            logOutButton.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

        }
        else
        {
            welcomeText.setText("Welcome " + user.getDisplayName());
            logInBtn.setEnabled(false);
            logInBtn.setVisibility(View.INVISIBLE);
            logOutButton.setEnabled(true);
            logOutButton.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Thumbnails");

        mImageUrls.add(path.getAbsolutePath() + "/face1.jpg");
        mNames.add("Seen on 3-17-19 at 4:30");

        mImageUrls.add(path.getAbsolutePath() + "/face2.jpg");
        mNames.add("Seen on 3-12-19 at 11:30");

        mImageUrls.add(path.getAbsolutePath() + "/face3.jpg");
        mNames.add("Seen on 3-05-19 at 14:30");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recycler view");

        RecyclerView recyclerView = findViewById(R.id.recentActivityRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mImageUrls, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}