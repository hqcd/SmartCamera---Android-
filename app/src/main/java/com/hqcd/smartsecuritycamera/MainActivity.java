package com.hqcd.smartsecuritycamera;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button settingsBtn, logInBtn, logOutButton;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        logInBtn = (Button)findViewById(R.id.toLoginScreen);
        logOutButton = (Button)findViewById(R.id.logOutButton);
        settingsBtn = (Button) findViewById(R.id.settings_button);
        welcomeText = (TextView)findViewById(R.id.welcomeText);

        settingsBtn.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            welcomeText.setText("Welcome " + user.getEmail());
            logInBtn.setEnabled(false);
        }
        else
        {
            welcomeText.setText("Welcome, please Sign In");
            logInBtn.setEnabled(true);
        }
    }

    public void logOut()
    {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
        welcomeText.setText("Welcome, please Sign In");
        logInBtn.setEnabled(true);
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.settings_button:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.toLoginScreen:
                intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
                break;
            case R.id.logOutButton:
                logOut();
                break;
        }
    }

}