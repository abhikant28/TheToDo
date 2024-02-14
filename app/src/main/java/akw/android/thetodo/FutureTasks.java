package akw.android.thetodo;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import akw.android.thetodo.Adapter.FutureTask_RecyclerView_Adapter;
import akw.android.thetodo.DataBase.TheViewModel;

public class FutureTasks extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_tasks);

        RecyclerView rv_futureTasks = findViewById(R.id.FutureTask_Task_RecyclerView);
        FutureTask_RecyclerView_Adapter task_adapter = new FutureTask_RecyclerView_Adapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_futureTasks.setLayoutManager(layoutManager);
        rv_futureTasks.setItemAnimator(new DefaultItemAnimator());
        rv_futureTasks.setAdapter(task_adapter);
        rv_futureTasks.setOnLongClickListener(view -> false);
        TheViewModel viewModel = ViewModelProviders.of(this).get(TheViewModel.class);
        viewModel.getFutureTasks().observe(this,tasks->{
        if (tasks.isEmpty()){
            task_adapter.submitList(null);
            findViewById(R.id.FutureTask_TextView).setVisibility(View.VISIBLE);
        } else {
            task_adapter.submitList(tasks);
        }});

        getPreferences();

        toolbar = findViewById(R.id.FutureTasks_Toolbar);
        toolbar.setTitle("Upcoming Tasks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean getPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Keys.SHARED_PREFS_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Keys.SHAREDPREF_IS_BG_PREF_SET, false)) {
            ImageView iv = findViewById(R.id.FutureTask_IMG_BG);
            ConstraintLayout sc_searchBG = findViewById(R.id.FutureTask_Layout);
            if (sharedPreferences.getBoolean(Keys.SHAREDPREF_IS_BG_IMG, false)) {
                String imgUri = sharedPreferences.getString(Keys.SHAREDPREF_BG_IMG, "");

                try {
                    // Open an input stream from the Uri
                    InputStream inputStream = new FileInputStream(imgUri);

                    // Decode the InputStream into a Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

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
                iv.setVisibility(View.GONE);
                String colorHex = sharedPreferences.getString(Keys.SHAREDPREF_BG_COLOR, "FFDAD5");
                sc_searchBG.setBackgroundColor(Color.parseColor("#" + colorHex));

            }
        }
        return sharedPreferences.getBoolean(Keys.SHAREDPREF_TASKS_ADD_NEW_BOTTOM, false);
    }

}