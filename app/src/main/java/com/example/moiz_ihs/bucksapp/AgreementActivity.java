package com.example.moiz_ihs.bucksapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.example.moiz_ihs.bucksapp.databinding.ActivityAgreementBinding;

public class AgreementActivity extends AppCompatActivity {
    ActivityAgreementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(AgreementActivity.this,R.layout.activity_agreement);
        String text = "By accessing this website we assume you accept these terms and conditions in full. Do not continue to use BucksFile's website if you do not accept all of hte terms and conditions stated on this page. The following terminology applies to these Terms and Conditions. Privacy Statement and Disclaimer Notice any or all Agreements.";
        String header = "<font color='#26B5ED'>Welcome to BucksFile!</font><br><br>";
        String first = "These terms and condition outline the rules and regulations for the use of BucksFile's Website.<br><br>";
        String second ="<font color='#26B5ED'>BucksFile</font> is located at: "+getApplicationInfo().dataDir + "<br><br>";
        binding.content.setText(Html.fromHtml(header+ first + second +text));

        binding.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.cbCheck.isChecked())
                   startActivity(new Intent(AgreementActivity.this,MainActivity.class));
                else
                    Toast.makeText(AgreementActivity.this, "You must Agree with the condition before using this App", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
