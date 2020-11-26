package com.example.raspi_temphum.CommonFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.raspi_temphum.CommonFiles.HelperClasses.SessionManager;
import com.example.raspi_temphum.Dashboard;
import com.example.raspi_temphum.R;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeUsername extends AppCompatActivity {

    TextView current_username;
    TextInputLayout newUsernameEdTxt;
    Button updateBtn,goToDashboard;
    ScrollView changeUsernameScrollView;
    Intent dashboard;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_username);

        current_username = findViewById(R.id.current_username);
        updateBtn = findViewById(R.id.updateBtn);
        newUsernameEdTxt = findViewById(R.id.newUsernameEdTxt);
        goToDashboard = findViewById(R.id.goToDashboard);
        changeUsernameScrollView = findViewById(R.id.changeUsernameScrollView);
        dashboard = new Intent(this, Dashboard.class);

        setCurrentUsername();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateNewUsername();
                setCurrentUsername();
                Snackbar mySnackbar = Snackbar.make(changeUsernameScrollView, "Username Updated Successfully", Snackbar.LENGTH_LONG);
                mySnackbar.show();
            }
        });
        goToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(dashboard);
            }
        });
    }

    private void UpdateNewUsername() {
        String newUsername = newUsernameEdTxt.getEditText().getText().toString().trim();
        SessionManager userData = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        String username = userData.getUserDataFromSession().get("phoneNo");
        userData.changeUsernameInSession(newUsername);

        dbRef.child("+91" + username).child("username").setValue(newUsername);
    }

    private void setCurrentUsername() {
        SessionManager userData = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        String username = userData.getUserDataFromSession().get("username");
        current_username.setText(username);
    }
}