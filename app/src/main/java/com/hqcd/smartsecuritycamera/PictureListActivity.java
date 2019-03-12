package com.hqcd.smartsecuritycamera;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PictureListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] listPersonName = new String[2];
//                "ListView Title 1", "ListView Title 2", "ListView Title 3", "ListView Title 4",
//                "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
//        };
//
//
        int[] listPersonImage = new int[2];
//                R.drawable.newface5, R.drawable.newface4, R.drawable.newface3, R.drawable.newface3,
//                R.drawable.newface5, R.drawable.newface4, R.drawable.newface3, R.drawable.newface3,
//        };
//
        String[] listDescription = new String[2];
//                "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
//                "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
//        };

        String[] names = {"Jake","Eric"};
        String[] description = {"Tall guy","bald guy"};
        int[] images = {R.drawable.newface3,R.drawable.newface5};

        for (int i=0;i<names.length;i++){
            listPersonName[i] =  names[i];
            listPersonImage[i] = images[i];
            listDescription[i] = description[i];
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        //This goes through and initializes the values. Later we can pass a variable based on number of people we have
        for (int i = 0; i < 2; i++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("listview_title", listPersonName[i]);
            hashMap.put("listview_discription", listDescription[i]);
            hashMap.put("listview_image", Integer.toString(listPersonImage[i]));
            aList.add(hashMap);
        }
        String[] from = {"listview_image", "listview_title", "listview_discription"};

        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, aList, R.layout.content_picture_list, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(this,IndividualPicturesActivity.class);
                startActivity(intent);

            }
        });
    }


}
