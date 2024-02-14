package akw.android.thetodo;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new CountDownTimer(750, 1000) {


            @Override
            public void onTick(long l) {

            }

            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }.start();
    }
}