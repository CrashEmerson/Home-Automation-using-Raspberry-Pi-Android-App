package com.example.raspi_temphum.CommonFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.raspi_temphum.R;

public class ForgetPassword_MakeSelection extends AppCompatActivity {

    Button via_smsBtn;
    Intent frgtPwd_newCredentialsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password__make_selection);

        via_smsBtn = findViewById(R.id.via_smsBtn);
        frgtPwd_newCredentialsActivity = new Intent(this, ForgetPassword_NewCredentials.class);

        via_smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(frgtPwd_newCredentialsActivity);
            }
        });

    }
}