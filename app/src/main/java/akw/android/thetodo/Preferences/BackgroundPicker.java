package akw.android.thetodo.Preferences;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import akw.android.thetodo.Keys;
import akw.android.thetodo.MainActivity;
import akw.android.thetodo.R;

public class BackgroundPicker extends AppCompatActivity {

    private static final int IMAGE_RESULT = 300;
    Uri img_uri;
    Toolbar toolbar;
    ImageView iv_bg;
    String colorHex = "EFD75C";
    boolean bgIMG = false;
    Bitmap img;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.background_picker_options, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_RESULT && resultCode == RESULT_OK && data != null) {
            img_uri = data.getData();

            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(String.valueOf(img_uri)));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Drawable drawable = new BitmapDrawable(getResources(), img);
            iv_bg.setImageDrawable(drawable);
            ////Log.i("IMG PICKER::::", "Recieved");
            bgIMG = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 8:
                // Handle the result for READ_EXTERNAL_STORAGE permission
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // READ_EXTERNAL_STORAGE permission granted, call your method here
                    pickImage(null);
                }
                break;
            case 9:
                // Handle the result for WRITE_EXTERNAL_STORAGE permission
                saveBackground(null);
                break;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_picker_bg);

        iv_bg = findViewById(R.id.BackgroundPicker_Background);
        iv_bg.setBackgroundColor(Color.parseColor("#EFD75C"));
        toolbar = findViewById(R.id.BackgroundPicker_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Background");

    }




    public void saveBackground(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences(Keys.SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor SPeditor = sharedPreferences.edit();
        if (bgIMG) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9);
                return;
            }
            String BG_Uri = saveToSdCard(this,img);
            SPeditor.putString(Keys.SHAREDPREF_BG_IMG, BG_Uri);
            SPeditor.putBoolean(Keys.SHAREDPREF_IS_BG_IMG, true);

        } else {
            SPeditor.putString(Keys.SHAREDPREF_BG_COLOR, colorHex);
            MainActivity.MAIN_ACTIVITY_LAYOUT.setBackgroundColor(Color.parseColor("#" + colorHex));
            SPeditor.putBoolean(Keys.SHAREDPREF_IS_BG_IMG, false);

        }
        SPeditor.putBoolean(Keys.SHAREDPREF_IS_BG_PREF_SET, true);
        SPeditor.apply();
        onBackPressed();
    }



    public void pickImage(View view) {
        //Log.i("IMG PICKER::::", "Started");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 8);
        }
        else {
            //Log.i("IMG PICKER::::", "true");
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_RESULT);
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
            case R.id.BackgroundPicker_Color_Black:
                colorHex = "010400";
                break;
        }
        bgIMG = false;
        iv_bg.setBackgroundColor(Color.parseColor("#" + colorHex));
    }


    public static String saveToSdCard(Context context, Bitmap bitmap) {
        String imagePath = null;

        try {
            // Get the internal storage directory
            File internalStorageDir = context.getFilesDir();

            // Create a directory if it doesn't exist
            File imageDir = new File(internalStorageDir, "images");
            if (!imageDir.exists()) {
                if (!imageDir.mkdir()) {
                    Log.e("DirectoryCreation", "Failed to create directory");
                    return imagePath;
                }
            }

            // Create a file for the image
            File imageFile = new File(imageDir, "yellow.png");
            imagePath = imageFile.getAbsolutePath();

            // Save the bitmap to the file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imagePath;
    }
}