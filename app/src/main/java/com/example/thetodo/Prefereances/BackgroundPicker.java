package com.example.thetodo.Prefereances;

import static com.example.thetodo.MainActivity.SHARED_PREFS_NAME;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.thetodo.MainActivity;
import com.example.thetodo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BackgroundPicker extends AppCompatActivity {

    Uri img_uri;
    Toolbar toolbar;
    ImageView iv_bg;
    String colorHex = "EFD75C";
    boolean bgIMG = false;
    Bitmap img;
    private String BG_Uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_picker_bg);

        toolbar = findViewById(R.id.BackgroundPicker_Toolbar);
        iv_bg = findViewById(R.id.BackgroundPicker_Background);
        iv_bg.setBackgroundColor(Color.parseColor("#EFD75C"));
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.background_picker_options, menu);
        return true;
    }


    public void saveBackground(MenuItem item) {
        if (bgIMG) {
            BG_Uri = saveToSdCard(img, "Yellow");
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor SPeditor = sharedPreferences.edit();
            SPeditor.putString("BG_COLOR", colorHex);
            SPeditor.putBoolean("IS_BG_PREF_SET", true);
            SPeditor.apply();
            MainActivity.MAIN_ACTIVITY_LAYOUT.setBackgroundColor(Color.parseColor("#" + colorHex));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == RESULT_OK) {
            img_uri = data.getData();

            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(String.valueOf(img_uri)));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Drawable drawable = new BitmapDrawable(getResources(), img);
            iv_bg.setBackground(drawable);
            Log.i("IMG PICKER::::", "Recieved");
            bgIMG = true;
        }
    }

    public void pickImage(View view) {
        Log.i("IMG PICKER::::", "Started");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("IMG PICKER::::", "false");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8);
        } else {
            Log.i("IMG PICKER::::", "true");
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 300);
        }
    }


    public void colorSelected(View view) {
        switch (view.getId()) {
            case R.id.BackgroundPicker_Color_LightYellow:
                colorHex = "EFD75C";
                break;
            case R.id.BackgroundPicker_Color_Lavender:
                colorHex = "D0B9C3";
                break;
            case R.id.BackgroundPicker_Color_Peach:
                colorHex = "FFDAD5";
                break;
            case R.id.BackgroundPicker_Color_Louis:
                colorHex = "b9c3d0";
                break;
            case R.id.BackgroundPicker_Color_Yellow:
                colorHex = "F9DD25";
                break;
            case R.id.BackgroundPicker_Color_LightBlue:
                colorHex = "44A7E3";
                break;
            case R.id.BackgroundPicker_Color_Blue:
                colorHex = "1565C0";
                break;
            case R.id.BackgroundPicker_Color_Red:
                colorHex = "C14646";
                break;
            case R.id.BackgroundPicker_Color_Teal:
                colorHex = "FF018786";
                break;
            case R.id.BackgroundPicker_Color_Green:
                colorHex = "2E7D32";
                break;
            case R.id.BackgroundPicker_Color_Purple:
                colorHex = "6A1B9A";
                break;
            case R.id.BackgroundPicker_Color_LightGreen:
                colorHex = "558B2F";
                break;
        }
        bgIMG = false;
        iv_bg.setBackgroundColor(Color.parseColor("#" + colorHex));

    }


    private String saveToSdCard(Bitmap bitmap, String filename) {

        String stored = null;

        File sdcard = Environment.getExternalStorageDirectory();

        File path = new File(sdcard.getAbsoluteFile(), ".directory");//the dot makes this directory hidden to the user
        if(!path.exists()){
            path.mkdir();
        }
        File file = new File(path.getAbsoluteFile(), filename + ".png");
        Log.i("IMG ::::", filename+"///////"+file);
        stored = path.getAbsoluteFile() + filename + ".png";
        try {
            FileOutputStream out = new FileOutputStream(stored);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }

    public static File getImage(String imagename) {

        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
                return null;

            mediaImage = new File(myDir.getPath() + "/.directory/" + imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }


}