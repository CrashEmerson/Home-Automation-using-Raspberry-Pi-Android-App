package com.example.raspi_temphum.CommonFiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.raspi_temphum.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class signup_02 extends AppCompatActivity {

    Button nextBtn;
    Intent codeVerifyActivity;
    RadioGroup genderRadioGroup;
    TextInputLayout phoneNoEdTxt;
    ScrollView SignUp2_scrollView;
    CountryCodePicker countryCodePicker;
    String key_fullname, key_username, key_emailAddress, key_password, key_gender, key_selectedCountryCode, key_phoneNo;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userRef = firebaseDatabase.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup_02);

        nextBtn = (Button) findViewById(R.id.nextBtn);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        phoneNoEdTxt = findViewById(R.id.phoneNoEdTxt);
        codeVerifyActivity = new Intent(this, CodeVertifyOTP.class);
        SignUp2_scrollView = findViewById(R.id.SignUp2_scrollView);
        countryCodePicker = findViewById(R.id.countryCodePicker);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedGender = (RadioButton) findViewById(i);
                key_gender = (String) selectedGender.getText();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateRadioGroup() | !validatePhoneNumber()) {
                    return;
                }

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String completePhoneNumber = phoneNoEdTxt.getEditText().getText().toString();

                        if (snapshot.child(completePhoneNumber).exists()) {
                            phoneNoEdTxt.setError("Number Already Registered");
                            return;

                        } else {
                            phoneNoEdTxt.setError(null);
                            phoneNoEdTxt.setErrorEnabled(false);

                            GetAllData();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(signup_02.this, "Data cant read", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void GetAllData() {
        key_fullname = getIntent().getStringExtra("key_fullname");
        key_username = getIntent().getStringExtra("key_username");
        key_emailAddress = getIntent().getStringExtra("key_emailAddress");
        key_password = getIntent().getStringExtra("key_password");



        key_selectedCountryCode = countryCodePicker.getSelectedCountryCode();
        key_phoneNo = phoneNoEdTxt.getEditText().getText().toString();

        putDataToNxtActivity();
        Toast.makeText(this, key_gender, Toast.LENGTH_SHORT).show();
        startActivity(codeVerifyActivity);
    }

    private void putDataToNxtActivity() {
        codeVerifyActivity.putExtra("key_fullname", key_fullname);
        codeVerifyActivity.putExtra("key_username", key_username);
        codeVerifyActivity.putExtra("key_emailAddress", key_emailAddress);
        codeVerifyActivity.putExtra("key_password", key_password);
        codeVerifyActivity.putExtra("key_gender", key_gender);
        codeVerifyActivity.putExtra("key_selectedCountryCode", key_selectedCountryCode);
        codeVerifyActivity.putExtra("key_phoneNo", key_phoneNo);
    }

    private boolean validateRadioGroup() {
        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(signup_02.this, "Please select gender", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePhoneNumber() {

        String val_phono = phoneNoEdTxt.getEditText().getText().toString();

        if (val_phono.isEmpty()) {
            phoneNoEdTxt.setError("Phone No is mandatory for verification");
            return false;
        } else if (val_phono.length() < 10) {
            phoneNoEdTxt.setError("Mobile Number can't lesser than 10 characters");
            return false;
        } else {
            phoneNoEdTxt.setError(null);
            phoneNoEdTxt.setErrorEnabled(false);
            return true;
        }
    }
}