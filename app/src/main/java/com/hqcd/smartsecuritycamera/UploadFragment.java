package com.hqcd.smartsecuritycamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment implements View.OnClickListener{
    private Button pickButton, uploadButton;
    private EditText name;
    private ImageView chosenPic;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upload, container, false);
        pickButton = (Button)root.findViewById(R.id.pick_button);
        uploadButton = (Button)root.findViewById(R.id.upload_button);
        name = (EditText)root.findViewById(R.id.name_et);
        chosenPic = (ImageView)root.findViewById(R.id.chosenImage);

        chosenPic.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);

        pickButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pick_button:
                chooseImage();
                break;
            case R.id.upload_button:
                uploadImage();
                break;
        }
    }

    public void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && data != null && resultCode == RESULT_OK){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                chosenPic.setImageBitmap(bitmap);
                chosenPic.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    public void uploadImage()
    {

    }
}
