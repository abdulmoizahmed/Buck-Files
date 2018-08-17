package com.example.moiz_ihs.bucksapp;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.moiz_ihs.bucksapp.databinding.ActivityChangePassBinding;
import com.example.moiz_ihs.bucksapp.network.ApiServices;
import com.example.moiz_ihs.bucksapp.network.OnResponseReceiveListener;
import com.example.moiz_ihs.bucksapp.utils.CommonUtils;
import com.example.moiz_ihs.bucksapp.utils.DevicePreference;
import com.example.moiz_ihs.bucksapp.utils.NetworkProgressDialog;

public class ChangePassActivity extends AppCompatActivity {

    boolean isNetworkCall = false;

    ActivityChangePassBinding binding;
    NetworkProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ChangePassActivity.this, R.layout.activity_change_pass);
        dialog = new NetworkProgressDialog(ChangePassActivity.this);
        binding.resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEmail()) {
                    dialog.show();
                    ApiServices.checkIMEI(binding.email.getText().toString(), DevicePreference.getInstance().getString(DevicePreference.Key.IMEI), new OnResponseReceiveListener() {
                        @Override
                        public void onSuccessReceived(Object response) {

                            postResetCall();
                        }

                        @Override
                        public void onFailureReceived(String errorMessage) {
                            if (!ChangePassActivity.this.isFinishing())
                                dialog.dismiss();
                            Toast.makeText(ChangePassActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            isNetworkCall = false;
                        }
                    });
                }
            }
        });
    }

    private void postResetCall() {
        ApiServices.postResetPass(binding.email.getText().toString(), new OnResponseReceiveListener() {
            @Override
            public void onSuccessReceived(Object response) {
                if(!ChangePassActivity.this.isFinishing())
                    dialog.dismiss();
                Toast.makeText(ChangePassActivity.this, "Check your email for new password", Toast.LENGTH_SHORT).show();
                ChangePassActivity.this.finish();
            }

            @Override
            public void onFailureReceived(String errorMessage) {
                if (!ChangePassActivity.this.isFinishing())
                    dialog.dismiss();
                Toast.makeText(ChangePassActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                isNetworkCall = false;
            }
        });
    }

    private boolean validateEmail() {
        boolean allValid = true;
        if (CommonUtils.isEmpty(binding.email)) {
            allValid = false;
            binding.email.setError("Email Address should be given");
        } else if (!CommonUtils.isEmpty(binding.email) && !CommonUtils.isValidEmail(binding.email.getText().toString())) {
            allValid = false;
            binding.email.setError("Please enter correct email address");
        }
        return allValid;
    }

    @Override
    public void onBackPressed() {
        if (!isNetworkCall)
            super.onBackPressed();
    }
}
