package com.ganesh.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageButton next_button_forgot_page;
    EditText usernameEmail_forgotPass;
    TextView forgot_pass_terms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        String first_forgot_pass_text = "<font color='#FF4B26'>*</font>";
        String last_forgot_pass_text =  "We will send you a message to set or reset your new password";

        TextView forgot_pass_terms = (TextView) findViewById(R.id.forgot_pass_terms);
        forgot_pass_terms.setText(Html.fromHtml(first_forgot_pass_text + last_forgot_pass_text));

        mAuth = FirebaseAuth.getInstance();
        usernameEmail_forgotPass = findViewById(R.id.usernameEmail_forgotPass);
        next_button_forgot_page = findViewById(R.id.next_button_forgot_page);
        next_button_forgot_page.setOnClickListener(view -> {
            validateData();

        });
    }
    public void validateData(){
        String email = usernameEmail_forgotPass.getText().toString();
        if(email.isEmpty()){
            usernameEmail_forgotPass.setError("Required");
            Toast.makeText(this, "Email required!!", Toast.LENGTH_SHORT).show();
        }
        else{
            forgotPass();
        }
    }
    public void forgotPass(){
        String email1 = usernameEmail_forgotPass.getText().toString();
        mAuth.sendPasswordResetEmail(email1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword.this,SignIn.class));
                }
                else{
                    Toast.makeText(ForgotPassword.this, "error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}