package com.hqcd.smartsecuritycamera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.koushikdutta.ion.Ion;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    private Button registerButton;
    private EditText registerEmail, registerPW, registerDisplayName;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        registerButton = (Button)root.findViewById(R.id.register_registerButton);
        registerEmail = (EditText)root.findViewById(R.id.register_emailET);
        registerPW = (EditText)root.findViewById(R.id.register_pwET);
        registerDisplayName = (EditText)root.findViewById(R.id.register_displayNameET);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        return root;
    }

    public void createAccount(){
        String emailText = registerEmail.getText().toString();
        String pwText = registerPW.getText().toString();
        String displayText = registerDisplayName.getText().toString();

        if((emailText.length() < 1) || (pwText.length() < 1) || (displayText.length() < 1))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Missing Fields");
            builder.setTitle("All Fields Must be Filled In");
            builder.setPositiveButton("Ok", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(pwText.length() < 6)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Password must be at least 6 characters long");
            builder.setTitle("Password Requirement Not Met");
            builder.setPositiveButton("Ok", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        else {
            mAuth.createUserWithEmailAndPassword(registerEmail.getText().toString(), registerPW.getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(registerDisplayName.getText().toString()).build();
                                user.updateProfile(profileChangeRequest);
                                Toast.makeText(getActivity(), "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                                initFileSystem(user.getUid());
                                ((MainActivity)getActivity()).updateUI(user);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }
    }

    public void initFileSystem(String userID)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = "http://" + sharedPreferences.getString("pref_ip_address", "") + ":" + sharedPreferences.getString("pref_http_port", "") + "/initFS";
        Ion.with(getActivity()).load(url).setBodyParameter("uid", userID);
    }
}
