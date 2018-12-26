package com.example.jdaviran.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < 30;i++) {
                    final int pos = i;
                    Glide.with(MainActivity.this)
                            .asBitmap()
                            .load("https://cdn.images.dailystar.co.uk/dynamic/184/photos/939000/620x/Super-Mario-Odyssey-Nintendo-Switch-DLC-confirmed-in-new-Nintendo-blog-post-694574.jpg")
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                                    new LongOperation(resource,String.valueOf(pos)).execute();
                                    //saveImage(resource);
                                }
                            });

                }
            }
        });
    }

private class LongOperation extends AsyncTask<String, Void, String> {
Bitmap bitmap;
String name;
public LongOperation(Bitmap bitmap,String name){
    this.bitmap = bitmap;
    this.name = name;
}

        @Override
        protected String doInBackground(String... params) {
            saveImage(bitmap,name);
         return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

        private void saveImage(Bitmap image,String timeNow) {
            //String timeNow = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "Example_" + timeNow + ".jpg";
        String savedImagePath = null;

        //PATH FILE PICTURE
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/YOUR_FILE");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
                galleryAddPic(savedImagePath);
                Toast.makeText(MainActivity.this, "IMAGE SAVED", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery


        }
        //return savedImagePath;
    }

    // solicita al escáner de medios que escanee un archivo y lo agregue a la base de datos de medios. La ruta al archivo está contenida en el campo Intent.mData.
    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }


}
