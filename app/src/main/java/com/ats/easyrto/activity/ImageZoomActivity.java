package com.ats.easyrto.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ats.easyrto.R;
import com.ats.easyrto.constants.Constants;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageZoomActivity extends AppCompatActivity {

    private ZoomageView zoomageView;

    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        zoomageView = findViewById(R.id.myZoomageView);

        try {

            image = Constants.IMAGE_PATH + "" + getIntent().getExtras().getString("image");
            Log.e("IMAGE PATH : ", " " + image);

            Picasso.get().load(image)
                    .placeholder(R.mipmap.ic_loading)
                    .error(R.mipmap.ic_error)
                    .into(zoomageView);

        } catch (Exception e) {
        }

    }
}
