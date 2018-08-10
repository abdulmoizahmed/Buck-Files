package com.example.moiz_ihs.bucksapp;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.moiz_ihs.bucksapp.databinding.ActivitySignUpBinding;
import com.example.moiz_ihs.bucksapp.model.QuestionResponse;
import com.example.moiz_ihs.bucksapp.model.SignUpPayload;
import com.example.moiz_ihs.bucksapp.network.ApiServices;
import com.example.moiz_ihs.bucksapp.network.OnResponseReceiveListener;
import com.example.moiz_ihs.bucksapp.service.BackUpService;
import com.example.moiz_ihs.bucksapp.service.SyncJob;
import com.example.moiz_ihs.bucksapp.utils.CommonUtils;
import com.example.moiz_ihs.bucksapp.utils.DevicePreference;
import com.example.moiz_ihs.bucksapp.utils.NetworkProgressDialog;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private static final int REQUEST_READ_PHONE_STATE = 22;
    ActivitySignUpBinding binding;
    private SignUpPayload payload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SignUpActivity.this, R.layout.activity_sign_up);
        //requestPhoneStatePermission();
        binding.imei.setText(DevicePreference.getInstance().getString(DevicePreference.Key.IMEI));
        initAdapter();
        binding.submit.setOnClickListener(new CustomClickListener());
    }

    private void initAdapter() {
        final NetworkProgressDialog dialog = new NetworkProgressDialog(SignUpActivity.this);
        dialog.show();
        ApiServices.getAllQuestions(new OnResponseReceiveListener() {
            @Override
            public void onSuccessReceived(Object response) {
                dialog.dismiss();
                ArrayAdapter adapter = new ArrayAdapter<>(SignUpActivity.this, android.R.layout.simple_list_item_1, ((List<QuestionResponse>) response));
                binding.spQuestionsFirst.setAdapter(adapter);
                binding.spQuestionsSecond.setAdapter(adapter);
                binding.spQuestionsSecond.setSelection(2); //Select second position
                //
            }

            @Override
            public void onFailureReceived(String errorMessage) {
                dialog.dismiss();
                Toast.makeText(SignUpActivity.this, "You don't have internet connetion please try later", Toast.LENGTH_SHORT).show();
                SignUpActivity.this.finish();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    binding.imei.setText(CommonUtils.getIMEI(this));
                    Toast.makeText(this, CommonUtils.getIMEI(this), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    public List<String> getQuestionsIds() {
        List questions = new ArrayList();
        questions.add(((QuestionResponse) binding.spQuestionsFirst.getSelectedItem()).getSqId());
        questions.add(((QuestionResponse) binding.spQuestionsSecond.getSelectedItem()).getSqId());
        return questions;
    }

    public List<String> getAnswers() {
        List answers = new ArrayList();
        answers.add(binding.answerFirst.getText().toString());
        answers.add(binding.answerSecond.getText().toString());
        return answers;
    }

    public SignUpPayload getPayload() {
        SignUpPayload payload = new SignUpPayload();
        payload.setImeiNumber(CommonUtils.getIMEI(SignUpActivity.this));
        payload.setUsername(binding.username.getText().toString());
        payload.setEmail(binding.email.getText().toString());
        payload.setPassword(binding.password.getText().toString());
        payload.setQuestionsId(getQuestionsIds());
        payload.setAnswers(getAnswers());
        return payload;
    }

    private class CustomClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (binding.contentSignup.getVisibility() == View.VISIBLE) {
                if (isValidForm()) {
                    binding.contentSignup.setVisibility(View.GONE);
                    binding.contentQuestions.setVisibility(View.VISIBLE);
                    binding.submit.setText("Register");
                } else {
                    Toast.makeText(SignUpActivity.this, "Some fields are missing or invalid", Toast.LENGTH_SHORT).show();
                }

            } else if (binding.layoutSuccess.getVisibility() == View.VISIBLE) {
                Toast.makeText(SignUpActivity.this, "Start Backuping", Toast.LENGTH_SHORT).show();
                DevicePreference.getInstance(SignUpActivity.this).put(DevicePreference.Key.IS_RUNNING,true);
                SyncJob.startAdvanceJob();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
            } else {
                if (validateQuestions())
                    postUser();

            }
        }
    }

    private boolean validateQuestions() {
        boolean allValid = true;
        if (binding.spQuestionsFirst.getSelectedItem().toString().equals(binding.spQuestionsSecond.getSelectedItem().toString())) {
            allValid = false;
            Toast.makeText(this, "You can't select same Question for both the answers.", Toast.LENGTH_LONG).show();
        }
        return allValid;
    }

    @Override
    public void onBackPressed() {
        if (binding.contentQuestions.getVisibility() == View.VISIBLE) {
            binding.contentSignup.setVisibility(View.VISIBLE);
            binding.contentQuestions.setVisibility(View.GONE);
            binding.submit.setText("Next");
        } else
            super.onBackPressed();
    }

    private void postUser() {
        final NetworkProgressDialog dialog = new NetworkProgressDialog(SignUpActivity.this);
        dialog.show();

        ApiServices.postSignUp(getPayload(), new OnResponseReceiveListener() {
            @Override
            public void onSuccessReceived(Object response) {
                dialog.dismiss();
                binding.submit.setText("Continue");
                binding.layoutSuccess.setVisibility(View.VISIBLE);
                binding.contentQuestions.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, ((String) response), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailureReceived(String errorMessage) {
                dialog.dismiss();
                Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidForm() {
        boolean allValid = true;

        if (CommonUtils.isEmpty(binding.imei)) {
            allValid = false;
            binding.imei.setError("IMEI can't be null you should provide imei for Login this application");
        }

        if (CommonUtils.isEmpty(binding.username)) {
            allValid = false;
            binding.username.setError("Username should be given");
        } else if (!CommonUtils.isEmpty(binding.username) && binding.username.getText().toString().length() < 3) {
            allValid = false;
            binding.username.setError("Username is too short");
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
