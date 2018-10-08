package com.example.wongweihsuan.demo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class landingpage extends AppCompatActivity {
private static int SPLASH_TIME_OUT=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcomeIntent=new Intent(landingpage.this,login.class);
                startActivity(welcomeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
