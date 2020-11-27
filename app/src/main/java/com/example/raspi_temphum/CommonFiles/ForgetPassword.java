package com.example.raspi_temphum.CommonFiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.raspi_temphum.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.base.Verify;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPassword extends AppCompatActivity {

    Button nextBtn;
    CountryCodePicker countryCodePicker;
    Intent frgtPwd_newCredentialActivity, VerifyOTP;
    TextInputLayout phoneNoEdTxt;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userRef = firebaseDatabase.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        nextBtn = findViewById(R.id.nextBtn);
        phoneNoEdTxt = findViewById(R.id.phoneNoEdTxt);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        frgtPwd_newCredentialActivity = new Intent(this, ForgetPassword_NewCredentials.class);
        VerifyOTP = new Intent(this, CodeVertifyOTP.class);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String countryCode = countryCodePicker.getSelectedCountryCode();
                final String phonoNo = phoneNoEdTxt.getEditText().getText().toString();

                final String completeNumber = "+" + countryCode + phonoNo;

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(completeNumber).exists()) {
                            phoneNoEdTxt.setError(null);
                            phoneNoEdTxt.setErrorEnabled(false);

                            VerifyOTP.putExtra("Purpose", "UpdatePassword");
                            VerifyOTP.putExtra("key_phoneNo", phonoNo);
                            VerifyOTP.putExtra("key_selectedCountryCode", countryCode);
                            startActivity(VerifyOTP);


                        } else {
                            phoneNoEdTxt.setError("User does not exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //startActivity(frgtPwd_makeSectionActivity);
            }
        });
    }
}