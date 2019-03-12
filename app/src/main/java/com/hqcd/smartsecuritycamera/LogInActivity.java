package com.hqcd.smartsecuritycamera;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button registerButton, loginButton;
    private FirebaseAuth mAuth;
    private final String TAG = "Smart Security Camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailField = (EditText)findViewById(R.id.emailET);
        passwordField = (EditText)findViewById(R.id.pwET);
        registerButton = (Button)findViewById(R.id.registerButton);
        loginButton = (Button)findViewById(R.id.loginButton);
        mAuth = FirebaseAuth.getInstance();



    }

    public void goToPicturesMain(View v){
        startActivity(new Intent(LogInActivity.this, PicturesMain.class));
    }

    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                            //updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void onClick(View view)
    {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        switch (view.getId())
        {
            case R.id.registerButton:
                createAccount(email, password);
                break;
            case R.id.loginButton:
                signIn(email, password);
                break;
            case R.id.picturesButton:
                startActivity(new Intent(LogInActivity.this, PicturesMain.class));
        }
    }
}
