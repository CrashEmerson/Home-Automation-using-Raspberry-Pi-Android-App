package com.example.raspi_temphum.CommonFiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.raspi_temphum.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassword_NewCredentials extends AppCompatActivity {

    Button updateBtn;
    TextInputLayout newPasswordEdTxt,confirmPasswordEdTxt;
    Intent PaswdUpdatedActivity;
    String newPasswordToUpdate,confirmPasswordTxt;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = firebaseDatabase.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password__new_credentials);

        updateBtn = findViewById(R.id.updateBtn);
        newPasswordEdTxt = findViewById(R.id.newPasswordEdTxt);
        confirmPasswordEdTxt = findViewById(R.id.confirmPasswordEdTxt);

        PaswdUpdatedActivity = new Intent(this, ForgetPassword_PasswordUpdated.class);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                       if(!validateFields()){
                           return;
                       }

                       newPasswordToUpdate = newPasswordEdTxt.getEditText().getText().toString().trim();
                       confirmPasswordTxt = confirmPasswordEdTxt.getEditText().getText().toString().trim();

                       String UserWhoRequestedToChangePassword =  getIntent().getStringExtra("requestTochangePassword");
                       if (UserWhoRequestedToChangePassword != null) {
                           snapshot.child(UserWhoRequestedToChangePassword).child("password").getRef().setValue(confirmPasswordTxt);
                       }

                       startActivity(PaswdUpdatedActivity);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ForgetPassword_NewCredentials.this, "Crash Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });
    }

    private boolean validateFields() {

        if(newPasswordEdTxt.getEditText().getText().toString().trim().isEmpty()){
            newPasswordEdTxt.setError("Type the new password");
            return false;
        }else{
            newPasswordEdTxt.setError(null);
            newPasswordEdTxt.setErrorEnabled(false);
        }
        if (confirmPasswordEdTxt.getEditText().getText().toString().trim().isEmpty()){
            confirmPasswordEdTxt.setError("Type the new password again");
            return false;
        }else{
            confirmPasswordEdTxt.setError(null);
            confirmPasswordEdTxt.setErrorEnabled(false);
        }

        if(!newPasswordEdTxt.getEditText().getText().toString().trim()
                .equals(confirmPasswordEdTxt.getEditText().getText().toString().trim())){
            confirmPasswordEdTxt.setError("Password does not match");
            return false;
        }else{
            confirmPasswordEdTxt.setError(null);
            confirmPasswordEdTxt.setErrorEnabled(false);
            return true;
        }
    }
}