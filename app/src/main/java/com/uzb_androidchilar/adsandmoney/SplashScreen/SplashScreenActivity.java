package com.uzb_androidchilar.adsandmoney.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uzb_androidchilar.adsandmoney.DescriptionActivity;
import com.uzb_androidchilar.adsandmoney.R;
import com.uzb_androidchilar.adsandmoney.RegistrActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();


    }
    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, DescriptionActivity.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, RegistrActivity.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}