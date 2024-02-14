package akw.android.thetodo.Preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import akw.android.thetodo.Keys;
import akw.android.thetodo.R;

public class Preferences_Main extends AppCompatActivity {


    Toolbar toolbar;
    ConstraintLayout ll_bg_layout;

    @Override
    protected void onResume() {
        getPreferences();
        super.onResume();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferances);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.PreferencesMain_FrameLayout, new SettingsFragment())
                    .commit();
        }

        intializa();


    }

    private void intializa() {

        ll_bg_layout=findViewById(R.id.Preference_Main_Background);
        toolbar=findViewById(R.id.PreferenceMain_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Preferences");

    }


    public void getPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Keys.SHARED_PREFS_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Keys.SHAREDPREF_IS_BG_PREF_SET, false)) {
            if (sharedPreferences.getBoolean(Keys.SHAREDPREF_IS_BG_IMG, false)) {
                String imgUri = sharedPreferences.getString(Keys.SHAREDPREF_BG_IMG, "");

                try {
                    // Open an input stream from the Uri
                    InputStream inputStream = new FileInputStream(imgUri);

                    // Decode the InputStream into a Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

                    ImageView iv = findViewById(R.id.Pref_IMG_BG);
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(bitmapDrawable.getBitmap());
                    // Close the InputStream
                    inputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("FileNotFoundException", "File not found");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IOException", "Error reading image from URI");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ImageView iv = findViewById(R.id.Pref_IMG_BG);
                iv.setVisibility(View.GONE);
                String colorHex = sharedPreferences.getString(Keys.SHAREDPREF_BG_COLOR, "FFDAD5");
                ll_bg_layout.setBackgroundColor(Color.parseColor("#" + colorHex));
            }
        }
    }

    public void mail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"awasthi.abhikant28@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        startActivityForResult(emailIntent,11);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_main, rootKey);
            Preference changeBackgroundPref= findPreference("Preference_changeBackground");
            Log.i("PREF::::::", "Click");
            if (changeBackgroundPref != null) {
                Log.i("PREF::::::", "Click1");
                changeBackgroundPref.setOnPreferenceClickListener(view->{
                    startActivity(new Intent(this.getContext(), BackgroundPicker.class));
                    return false;
                });
            }

            SwitchPreference addNewTaskToBottom= findPreference("Preference_addNewTaskToBottom");
            if (addNewTaskToBottom != null) {
                addNewTaskToBottom.setOnPreferenceChangeListener((preference, objValue) -> {
                    boolean desc=  (boolean) objValue;
                    Log.i("PREF::::::", "Changed");
                    SharedPreferences shp = getContext().getSharedPreferences(Keys.SHARED_PREFS_NAME, MODE_PRIVATE);
                    shp.edit().putBoolean(Keys.SHAREDPREF_TASKS_ADD_NEW_BOTTOM, desc).apply();

                    return true;
                });
            }
            findPreference("Preference_feedback").setOnPreferenceClickListener(view->{
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"awasthi.abhikant28@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivityForResult(emailIntent,11);
                return false;
            });
        }
    }


}