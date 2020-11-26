package com.example.raspi_temphum.CommonFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.raspi_temphum.R;

public class ForgetPassword_PasswordUpdated extends AppCompatActivity {


    Button doneBtn;
    Intent SignInActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password__password_updated);

        doneBtn = findViewById(R.id.doneBtn);
        SignInActivity = new Intent(this, SignIn.class);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(SignInActivity);
            }
        });

    }
}