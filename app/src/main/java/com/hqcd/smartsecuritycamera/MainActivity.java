package com.hqcd.smartsecuritycamera;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hqcd.smartsecuritycamera.adapter.RecyclerViewAdapter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private Button imageButton;
    private TextView welcomeText, allImageText;
    private FirebaseUser user;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SharedPreferences sharedPreferences;
    private DrawerLayout drawer;
    NavigationView navigationView;
    Bundle state;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        state = savedInstanceState;
        //Obtain Current User if Logged In
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Initialize Default Settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //Initialize views;
//        welcomeText = (TextView)findViewById(R.id.welcomeText);
//        allImageText = (TextView)findViewById(R.id.all_images);
//        recyclerView = (RecyclerView)findViewById(R.id.recentActivityRecyclerView);
//        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);


        //Request Permissions
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, 1);

        //Obtain Prefs
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            updateUI(currentUser);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (user == null) {
            menu.findItem(R.id.item_logout).setEnabled(false);
        }

        else {
            menu.findItem(R.id.item_logout).setEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_logout:
                logOut();
                break;
            case R.id.item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.item_stream:
                startActivity(new Intent(this, StreamingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
        user = FirebaseAuth.getInstance().getCurrentUser();
        invalidateOptionsMenu();
        updateUI(user);
        mNames.clear();
        mImageUrls.clear();
    }


    public void updateUI(FirebaseUser user) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (user == null) {
            LoginFragment loginFragment = new LoginFragment();
            ft.replace(R.id.fragment_container, loginFragment);
            ft.commit();
            View navHeader = navigationView.getHeaderView(0);
            TextView tv = navHeader.findViewById(R.id.header_displayName);
            tv.setText("");
        } else {
            HomeFragment home = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user", user.getUid());
            home.setArguments(bundle);
            ft.replace(R.id.fragment_container, home);
            ft.commit();
            navigationView.setCheckedItem(R.id.nav_home);
            View navHeader = navigationView.getHeaderView(0);
            TextView tv = navHeader.findViewById(R.id.header_displayName);
            tv.setText(user.getDisplayName());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        user = mAuth.getCurrentUser();
        bundle.putString("user", user.getUid());

        switch (item.getItemId()) {
            case R.id.nav_home:
                HomeFragment home = new HomeFragment();
                home.setArguments(bundle);
                ft.replace(R.id.fragment_container, home);
                ft.commit();
                break;
            case R.id.nav_visitor:
                VisitorFragment visitorFragment = new VisitorFragment();
                visitorFragment.setArguments(bundle);
                ft.replace(R.id.fragment_container, visitorFragment);
                ft.commit();
                break;
            case R.id.nav_recordings:
//                RecordingsFragment recordingsFragment = new RecordingsFragment();
//                recordingsFragment.setArguments(bundle);
//                ft.replace(R.id.fragment_container, recordingsFragment);
//                ft.commit();
                startActivity(new Intent(this, RecordingListActivity.class));
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}