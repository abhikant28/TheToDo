package com.example.thetodo.Prefereances;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.thetodo.MainActivity;
import com.example.thetodo.R;

public class Preferences_Main extends AppCompatActivity {

    CardView cv_background;
    ImageView iv_cv_background_Icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferances);

        cv_background=findViewById(R.id.Preference_Background);
        iv_cv_background_Icon=findViewById(R.id.Preference_Background_Icon);

        iv_cv_background_Icon.setBackgroundColor(MainActivity.MAIN_ACTIVITY_LAYOUT.getSolidColor());
        cv_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startActivity(new Intent(Preferences_Main.this, BackgroundPicker.class));
            }
        });

//        dialog = new Dialog(this);
//        dialog.setCancelable(true);
//        dialog.setContentView();

    }

    public void chooseBG(View view) {
        {
            float r = 8;
            ShapeDrawable shape = new ShapeDrawable(new RoundRectShape(new float[]{r, r, r, r, r, r, r, r}, null, null));
            shape.getPaint().setColor(Color.RED);
            view.setBackground(shape);
        }

    }

}