package com.example.raspi_temphum.CommonFiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.raspi_temphum.CommonFiles.HelperClasses.CheckInternet;
import com.example.raspi_temphum.CommonFiles.HelperClasses.SessionManager;
import com.example.raspi_temphum.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {


    TextInputLayout usernameEdTxt, passwordEdTxt;
    Button loginBtn, forgetPaswdBtn, createAccountBtn;
    Intent SignUp_01_Activity, forgetPasswordActivity, RoomActivity;
    ScrollView loginScrollView;
    ProgressBar progressBar;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference users = firebaseDatabase.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        usernameEdTxt = findViewById(R.id.usernameEdTxt);
        passwordEdTxt = findViewById(R.id.passwordEdTxt);
        loginBtn = findViewById(R.id.loginBtn);
        loginScrollView = (ScrollView) findViewById(R.id.loginScrollView);
        forgetPaswdBtn = findViewById(R.id.forgetPaswdBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        progressBar = findViewById(R.id.progressBar);

        SignUp_01_Activity = new Intent(this, signup_01.class);
        forgetPasswordActivity = new Intent(this, ForgetPassword.class);
        RoomActivity = new Intent(this, com.example.raspi_temphum.RoomActivity.class);

        progressBar.setVisibility(View.GONE);


        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        if (sessionManager.checkUserIsLoggedIn()) {
            startActivity(RoomActivity);
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckInternet checkInternet = new CheckInternet();
                if (!checkInternet.checkIsConnected(SignIn.this)) {
                    checkInternet.NoInternetPopUpDialog(SignIn.this);
                    return;
                }

                Login();
            }
        });

        // Create Account Button
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SignUp_01_Activity);
            }
        });


        //  Forget Password
        forgetPaswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(forgetPasswordActivity);
            }
        });
    }

    private void Login() {
        
        String key_username = usernameEdTxt.getEditText().getText().toString();     //TODO: Change username to phone Number
        final String key_password = passwordEdTxt.getEditText().getText().toString();

        final String completePhoneNumber = "+91" + key_username;

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.child(completePhoneNumber).exists()) {
                    usernameEdTxt.setError(null);
                    usernameEdTxt.setErrorEnabled(false);
                    String loginPassword = snapshot.child(completePhoneNumber).child("password").getValue(String.class);

                    if (key_password.equals(loginPassword)) {

                        progressBar.setVisibility(View.VISIBLE);

                        passwordEdTxt.setError(null);
                        passwordEdTxt.setErrorEnabled(false);

                        //Shared Preferences
                        String fullName = snapshot.child(completePhoneNumber).child("fullName").getValue(String.class);
                        String username = snapshot.child(completePhoneNumber).child("username").getValue(String.class);
                        String email = snapshot.child(completePhoneNumber).child("emailAddress").getValue(String.class);
                        String phoneNo = snapshot.child(completePhoneNumber).child("phoneNo").getValue(String.class);
                        String gender = snapshot.child(completePhoneNumber).child("gender").getValue(String.class);
                        String password = snapshot.child(completePhoneNumber).child("password").getValue(String.class);

                        // Shared Preferences
                        SessionManager sessionManager = new SessionManager(SignIn.this, SessionManager.SESSION_USERSESSION);
                        sessionManager.createLoginSession(fullName, username, email, phoneNo, gender, password);

                        progressBar.setVisibility(View.GONE);

                        startActivity(RoomActivity);
                        finish();

                    } else {
                        passwordEdTxt.setError("incorrect Password");
                    }
                } else {
                    usernameEdTxt.setError("Phone No is not registered");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignIn.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }
}
