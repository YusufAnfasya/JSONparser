package com.example.simplecamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ImageButton b1;
    ImageView iv;
    private static final int kodekamera = 222;
    String nmFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (ImageButton) findViewById(R.id.button);
        iv = (ImageView) findViewById(R.id.imageView);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "HasilFoto");
//                imagesFolder.mkdirs();
//                Date d = new Date();
//                CharSequence s = DateFormat.format("yyyyMMdd-hh-mm-ss", d.getTime());
//                nmFile = imagesFolder + File.separator + s.toString() + ".jpg";
//                File image = new File(nmFile);
//
//                Uri uriSavedImage = Uri.fromFile(image);
//                it.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(it, kodekamera);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case (kodekamera):
                    prosesKamera(data);
                    break;
            }
        }
    }

    private void prosesKamera(Intent datanya) {
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        try {
            bm = BitmapFactory.decodeFile(nmFile, options);
            iv.setImageBitmap(bm); // Set imageview to image that was
            Toast.makeText(this, "Data Telah Terload ke ImageView" + nmFile, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Data Error" + nmFile, Toast.LENGTH_SHORT).show();
        }
    }
}







