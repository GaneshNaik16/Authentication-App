package com.ganesh.authentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText usernameEmail,passwordInput,confirmPasswordInput;
    TextInputLayout password, confirmPassword;
    ImageButton next_button_register_page,phoneRegister,google_registerPage;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String first_signUp_text = "Already have an ";
        String last_signUp_text = "<font color='#FF4B26'>account? </font>";


        TextView signIn_text = (TextView) findViewById(R.id.signIn_text);

        signIn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignIn.class));
            }
        });
        signIn_text.setText(Html.fromHtml(first_signUp_text + last_signUp_text));

        mAuth = FirebaseAuth.getInstance();
        usernameEmail = findViewById(R.id.usernameEmail);
        password = findViewById(R.id.password);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPassword = findViewById(R.id.confirmPassword);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        next_button_register_page = findViewById(R.id.next_button_register_page);
        phoneRegister = findViewById(R.id.phoneRegister);
        google_registerPage = findViewById(R.id.google_registerPage);

        next_button_register_page.setOnClickListener(view -> {
            createUser();
        });
        phoneRegister.setOnClickListener(view -> {
            phoneLogin();
        });

        // Initializing Google SignIn Options
        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("93243380436-do51qj7ld17bht7ievoi743pjc7ll12o.apps.googleusercontent.com")//you can also use R.string.default_web_client_id
                .requestEmail()
                .build();

        // Initializing Google SignInClient
        gsc= GoogleSignIn.getClient(MainActivity.this
                ,gso);

        // Setting onClickListener for Google Button
        google_registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=gsc.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });



        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        // Check condition
        if(firebaseUser!=null)
        {
            // When user already sign in
            // redirect to profile activity
            startActivity(new Intent(MainActivity.this,Dashboard.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            handleSignInResult(signInAccountTask);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> signInAccountTask) {
        if(signInAccountTask.isSuccessful())
        {
            try {
                GoogleSignInAccount googleSignInAccount=signInAccountTask
                        .getResult(ApiException.class);
                if(googleSignInAccount!=null)
                {
                    AuthCredential authCredential= GoogleAuthProvider
                            .getCredential(googleSignInAccount.getIdToken()
                                    ,null);

                    mAuth.signInWithCredential(authCredential)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Check condition
                                    if(task.isSuccessful())
                                    {

                                        startActivity(new Intent(MainActivity.this
                                                ,Dashboard.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this, "Authentication Failed!" +task.getException()
                                                .getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
            catch (ApiException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void createUser() {
        String email = usernameEmail.getText().toString();
        String pass = passwordInput.getText().toString();
        String cpass = confirmPasswordInput.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is empty!", Toast.LENGTH_SHORT).show();
            usernameEmail.setError("Email cannot be empty");
            usernameEmail.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Password is empty!", Toast.LENGTH_SHORT).show();
            password.setError("Password is required!");
            password.requestFocus();
        } else {

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(MainActivity.this,Dashboard.class));
                                    Toast.makeText(MainActivity.this, "Hurray!! Its Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Registration failed."+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


        }
    }

    private void phoneLogin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Phone SignUp");
        LinearLayout linearLayout=new LinearLayout(this);
        TextView title = new TextView(this);
        final EditText phoneNumber= new EditText(this);
        phoneNumber.setText("Email");
        phoneNumber.setMinEms(16);
        phoneNumber.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(phoneNumber);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=phoneNumber.getText().toString().trim();
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private void googleSignUp(){

        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();

    }

    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }





}
