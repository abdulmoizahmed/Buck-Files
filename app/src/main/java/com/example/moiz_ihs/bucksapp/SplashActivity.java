package com.example.moiz_ihs.bucksapp;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.moiz_ihs.bucksapp.utils.DevicePreference;

public class SplashActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv = (TextView) findViewById(R.id.tv);
        if (!DevicePreference.getInstance(SplashActivity.this).getBoolean(DevicePreference.Key.IS_RUNNING, false)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, AgreementActivity.class));
                    SplashActivity.this.finish();
                }
            }, 2000);
        } else {
            tv.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                        SplashActivity.this.finishAffinity();
                    }
                }
            }, 10000);
        }
    }
}
