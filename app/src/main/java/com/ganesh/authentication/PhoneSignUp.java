package com.ganesh.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class PhoneSignUp extends AppCompatActivity {
    TextView phoneNumberTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sign_up);
        String first_Phone_terms = "Do not enter the country code ";
        String next_Phone_terms = "<font color='#FF4B26'>+91</font>Register";
        TextView phoneNumberTerms = (TextView) findViewById(R.id.phoneNumberTerms);
        phoneNumberTerms.setText(Html.fromHtml(first_Phone_terms+next_Phone_terms));
    }
}