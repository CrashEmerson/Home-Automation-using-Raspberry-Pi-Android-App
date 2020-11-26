package com.example.raspi_temphum.CommonFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.raspi_temphum.LoginActivity;
import com.example.raspi_temphum.MainActivity;
import com.example.raspi_temphum.R;
import com.google.android.material.badge.BadgeUtils;

import java.nio.charset.MalformedInputException;

public class LoginSuccessfull extends AppCompatActivity {

    Button loginBtn;
    Intent LoginActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successfull);

        loginBtn = findViewById(R.id.loginBtn);
        LoginActivity = new Intent(this, SignIn.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(LoginActivity);
            }
        });

    }
}