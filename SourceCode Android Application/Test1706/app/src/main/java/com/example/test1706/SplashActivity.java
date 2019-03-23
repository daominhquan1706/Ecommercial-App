package com.example.test1706;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread welcomeThread = new Thread() {
            Intent i = new Intent(SplashActivity.this,
                    MainActivity.class);

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3 * 1000);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {


                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
