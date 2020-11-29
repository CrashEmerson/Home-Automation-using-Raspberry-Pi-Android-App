package com.example.raspi_temphum.CommonFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raspi_temphum.CommonFiles.HelperClasses.CheckInternet;
import com.example.raspi_temphum.R;
import com.google.android.material.textfield.TextInputLayout;

public class signup_01 extends AppCompatActivity {


    TextView signup_titleText;
    ImageView signup_backBtn;
    TextInputLayout fullnameEdTxt, usernameEdTxt, emailAddressEdTxt, passwordEdTxt;
    Button nextBtn, signInBtn;
    Intent signIn_02_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup_01);

        nextBtn = findViewById(R.id.nextBtn);
        signup_titleText = findViewById(R.id.signup_titleText);
        signInBtn = findViewById(R.id.signInBtn);
        fullnameEdTxt = findViewById(R.id.fullnameEdTxt);
        usernameEdTxt = findViewById(R.id.usernameEdTxt);
        emailAddressEdTxt = findViewById(R.id.emailAddressEdTxt);
        passwordEdTxt = findViewById(R.id.passwordEdTxt);

        signIn_02_Activity = new Intent(this, signup_02.class);          // Intent


        nextBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   CheckInternet checkInternet = new CheckInternet();
                   if (!checkInternet.checkIsConnected(signup_01.this)) {
                       checkInternet.NoInternetPopUpDialog(signup_01.this);
                       return;
                   }
                   if (!validateFullname() | !validateUsername() | !validateEmail() | !validatePassword()) {
                       return;
                   }

                   String key_fullname = fullnameEdTxt.getEditText().getText().toString().trim();
                   String key_username = usernameEdTxt.getEditText().getText().toString().trim();
                   String key_emailAddress = emailAddressEdTxt.getEditText().getText().toString().trim();
                   String key_password = passwordEdTxt.getEditText().getText().toString().trim();

                   signIn_02_Activity.putExtra("key_fullname", key_fullname);
                   signIn_02_Activity.putExtra("key_username", key_username);
                   signIn_02_Activity.putExtra("key_emailAddress", key_emailAddress);
                   signIn_02_Activity.putExtra("key_password", key_password);


                   Pair[] pairs = new Pair[3];

                   pairs[0] = new Pair<View, String>(signup_titleText, "transition_titleText");
                   pairs[1] = new Pair<View, String>(nextBtn, "transition_nextBtn");
                   pairs[2] = new Pair<View, String>(signInBtn, "transition_signInBtn");

                   ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(signup_01.this, pairs);
                   startActivity(signIn_02_Activity, activityOptions.toBundle());

                   //startActivity(signIn_02_Activity);

                   // TransitionAnimation();        TODO: Do Animation later
               }
           }
        );
    }

    private boolean validateUsername() {
        String val_username = usernameEdTxt.getEditText().getText().toString().trim();
        String whitespaces = "\\A\\w{1,20}\\z";

        if (val_username.isEmpty()) {
            usernameEdTxt.setError("Field can not be empty !");
            return false;
        } else if (!val_username.matches(whitespaces)) {
            usernameEdTxt.setError("Whitespaces are not allowed !");
            return false;
        } else if (val_username.length() > 20) {
            usernameEdTxt.setError("Username should exceed 20 characters");
            return false;
        } else {
            usernameEdTxt.setError(null);
            usernameEdTxt.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val_emailAddress = emailAddressEdTxt.getEditText().getText().toString().trim();
        String regexEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val_emailAddress.isEmpty()) {
            emailAddressEdTxt.setError("Field can not be empty !");
            return false;
        } else if (!val_emailAddress.matches(regexEmail)) {
            emailAddressEdTxt.setError("Invalid email address !");
            return false;
        } else {
            emailAddressEdTxt.setError(null);
            emailAddressEdTxt.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val_password = passwordEdTxt.getEditText().getText().toString().trim();
        String maxFourChar = ".{4,}";

        if (val_password.isEmpty()) {
            passwordEdTxt.setError("Field can not be empty !");
            return false;
        } else if (!val_password.matches(maxFourChar)) {
            passwordEdTxt.setError("Password should contain atleast 4 characters!");
            return false;
        } else {
            passwordEdTxt.setError(null);
            passwordEdTxt.setErrorEnabled(false);
            return true;
        }
    }


    private void TransitionAnimation() {
        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View, String>(signup_titleText, "transition_titleText");
        pairs[1] = new Pair<View, String>(nextBtn, "transition_nextBtn");
        pairs[2] = new Pair<View, String>(signInBtn, "transition_signInBtn");

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(signup_01.this, pairs);
        startActivity(signIn_02_Activity, activityOptions.toBundle());
    }


    //Validation

    private boolean validateFullname() {
        String val_fullname = fullnameEdTxt.getEditText().getText().toString().trim();

        if (val_fullname.isEmpty()) {
            fullnameEdTxt.setError("Fields can not be empty");
            return false;
        } else {
            fullnameEdTxt.setError(null);
            fullnameEdTxt.setErrorEnabled(false);
            return true;
        }
    }

}