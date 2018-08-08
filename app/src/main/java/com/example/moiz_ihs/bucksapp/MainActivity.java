package com.example.moiz_ihs.bucksapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.moiz_ihs.bucksapp.databinding.ActivityMainBinding;
import com.example.moiz_ihs.bucksapp.utils.CommonUtils;
import com.example.moiz_ihs.bucksapp.utils.DevicePreference;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 23;
    private static final int REQUEST_STORAGE_STATE = 24;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        initPermissions();

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isInternetOn(MainActivity.this)) {
                    if (requestPhoneStatePermission() && requestStoragePermission())
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    else
                        Toast.makeText(MainActivity.this, "This App would not work without giving any permission", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(MainActivity.this, "Internet is not available right now.Please make sure you have valid internet connections", Toast.LENGTH_SHORT).show();
            }
        });


        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isInternetOn(MainActivity.this)) {
                    if (requestPhoneStatePermission() && requestStoragePermission())
                        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                    else
                        Toast.makeText(MainActivity.this, "This App would not work without giving any permission", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(MainActivity.this, "Internet is not available right now.Please make sure you have valid internet connections", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initPermissions() {
        requestPhoneStatePermission();

    }

    private boolean requestPhoneStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        boolean ispermissible;
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            ispermissible = false;
        } else {
            ispermissible = true;
            DevicePreference.getInstance(this).put(DevicePreference.Key.IMEI, CommonUtils.getIMEI(this));
        }
        return ispermissible;
    }

    public boolean requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_STATE);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_STORAGE_STATE:
                if (grantResults.length > 0) {
                    if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED))
                        requestStoragePermission();
                }
                break;
            case REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        requestStoragePermission();
                        DevicePreference.getInstance(MainActivity.this).put(DevicePreference.Key.IMEI, CommonUtils.getIMEI(MainActivity.this));
                    } else
                        requestPhoneStatePermission();
                }
                break;

        }
    }
}
