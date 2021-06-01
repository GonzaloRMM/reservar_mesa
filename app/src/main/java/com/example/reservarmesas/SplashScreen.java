package com.example.reservarmesas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private Intent main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        getSupportActionBar().hide();
        Bundle b = getIntent().getExtras();
        main = new Intent(this, login.class);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivityForResult(main, 1);
            }
        }, 2500);
    }
}
