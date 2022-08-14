package com.ganesh.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    EditText usernameEmailSignIn, passwordSignInInput;
    TextInputLayout passwordSignIn;
    ImageButton next_button_signin_page;
    TextView forgot_pass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        usernameEmailSignIn = findViewById(R.id.usernameEmailSignIn);
        passwordSignIn = findViewById(R.id.passwordInput);
        next_button_signin_page = findViewById(R.id.next_button_signin_page);
        passwordSignInInput = findViewById(R.id.passwordSignInInput);

        findViewById(R.id.forgot_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,ForgotPassword.class));
            }
        });
        next_button_signin_page.setOnClickListener(view -> {
            loginUser();
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    public void loginUser() {
        String email1 = usernameEmailSignIn.getText().toString();
        String pass1 = passwordSignInInput.getText().toString();
        if (TextUtils.isEmpty(email1)) {
            Toast.makeText(this, "Email is empty!", Toast.LENGTH_SHORT).show();
            usernameEmailSignIn.setError("Email cannot be empty");
            usernameEmailSignIn.requestFocus();
        } else if (TextUtils.isEmpty(pass1)) {
            Toast.makeText(this, "Password is empty!", Toast.LENGTH_SHORT).show();
            passwordSignIn.setError("Password is required!");
            passwordSignIn.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email1, pass1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SignIn.this, Dashboard.class));
                                Toast.makeText(SignIn.this, "Hurray!! Its Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignIn.this, "SignIn failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            /////////////////////////////////////////////////


        }
    }
}
