package com.example.raspi_temphum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEdTxt,passwordEdTxt;
    Button loginBtn;
    RelativeLayout loginLinearLyt;
    ImageView pi;
    Intent intentMainActivity, intentRegisterActivity;
    TextView signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_login);

        usernameEdTxt = (EditText) findViewById(R.id.usernameEdTxt);
        passwordEdTxt = (EditText) findViewById(R.id.passwordEdTxt);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginLinearLyt = (RelativeLayout) findViewById(R.id.loginLinearLyt);
        signUpBtn = (TextView) findViewById(R.id.signUpBtn);
        pi = (ImageView) findViewById(R.id.pi);

        intentMainActivity = new Intent(this, MainActivity.class);
        intentRegisterActivity = new Intent(this, RegisterActivity.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        pi.setOnLongClickListener(new View.OnLongClickListener() {                  // Double tap pi icon -----> for testing
            @Override
            public boolean onLongClick(View view) {
                startActivity(intentMainActivity);
                return false;
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentRegisterActivity);
            }
        });
    }

    private void login() {
        if(usernameEdTxt.getText().toString().equals("") || passwordEdTxt.getText().toString().equals("")){
            Snackbar validate = Snackbar.make(loginLinearLyt, "Please Enter Username/Password",Snackbar.LENGTH_LONG);
            validate.show();
        }else if(usernameEdTxt.getText().toString().equals("admin") &&
                (passwordEdTxt.getText().toString().equals("admin"))){
            startActivity(intentMainActivity);
        }
        else{
            Snackbar elseSnackbar = Snackbar.make(loginLinearLyt, "Username/Password is Incorrect", Snackbar.LENGTH_LONG);
            elseSnackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    usernameEdTxt.setText("");
                    passwordEdTxt.setText("");
                }
            });
            elseSnackbar.show();
        }
    }
}