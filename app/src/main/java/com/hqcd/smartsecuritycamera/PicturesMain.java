package com.hqcd.smartsecuritycamera;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class PicturesMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button goBack = (Button)findViewById(R.id.goBack);

    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.goBack:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.pictureList:
                intent = new Intent(this,PictureListActivity.class);
                startActivity(intent);
                break;

        }
    }




}
