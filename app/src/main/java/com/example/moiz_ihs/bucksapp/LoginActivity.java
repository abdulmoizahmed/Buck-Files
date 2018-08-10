package com.example.moiz_ihs.bucksapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.example.moiz_ihs.bucksapp.databinding.ActivityLoginBinding;
import com.example.moiz_ihs.bucksapp.model.SignUpPayload;
import com.example.moiz_ihs.bucksapp.network.ApiServices;
import com.example.moiz_ihs.bucksapp.network.OnResponseReceiveListener;
import com.example.moiz_ihs.bucksapp.service.BackUpService;
import com.example.moiz_ihs.bucksapp.service.SyncJob;
import com.example.moiz_ihs.bucksapp.utils.CommonUtils;
import com.example.moiz_ihs.bucksapp.utils.DevicePreference;
import com.example.moiz_ihs.bucksapp.utils.FilesFilter;
import com.example.moiz_ihs.bucksapp.utils.NetworkProgressDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.moiz_ihs.bucksapp.utils.CommonUtils.getAllFiles;
import static com.example.moiz_ihs.bucksapp.utils.CommonUtils.getIMEI;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 11;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        binding.submit.setOnClickListener(new CustomOnClickListener());
        binding.imei.setText(CommonUtils.getIMEI(this));
    }



    public SignUpPayload getPayload() {
        SignUpPayload payload = new SignUpPayload();
        payload.setImeiNumber(CommonUtils.getIMEI(LoginActivity.this));
        payload.setEmail(binding.email.getText().toString());
        payload.setPassword(binding.password.getText().toString());
        return payload;
    }

    private class CustomOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (isValidForm()) {
                final NetworkProgressDialog dialog = new NetworkProgressDialog(LoginActivity.this);
                dialog.show();
                ApiServices.postSignIn(getPayload(), new OnResponseReceiveListener() {
                    @Override
                    public void onSuccessReceived(Object response) {
                        dialog.dismiss();
                        DevicePreference.getInstance(LoginActivity.this).put(DevicePreference.Key.IS_RUNNING,true);
                        SyncJob.startAdvanceJob();
                        Toast.makeText(LoginActivity.this,"Start Backuping your Data", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();}
                    }

                    @Override
                    public void onFailureReceived(String errorMessage) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Please enter correct values!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean isValidForm() {
        boolean allValid = true;

        if (CommonUtils.isEmpty(binding.imei)) {
            allValid = false;
            binding.imei.setError("IMEI can't be null you should provide imei for Login this application");
        }

        if (CommonUtils.isEmpty(binding.email)) {
            allValid = false;
            binding.email.setError("Email Address should be given");
        } else if (!CommonUtils.isEmpty(binding.email) && !CommonUtils.isValidEmail(binding.email.getText().toString())) {
            allValid = false;
            binding.email.setError("Please enter correct email address");
        }

        if (CommonUtils.isEmpty(binding.password)) {
            allValid = false;
            binding.password.setError("Password should be given");
        } else if (!CommonUtils.isEmpty(binding.password) && binding.password.getText().toString().length() < 8) {
            allValid = false;
            binding.password.setError("Password should be 8 digits");
        }

        return allValid;
    }
}
