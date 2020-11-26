package com.example.raspi_temphum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.raspi_temphum.CommonFiles.HelperClasses.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateRoom extends AppCompatActivity {


    TextInputLayout familynameEdTxt, familycodeEdTxt;
    Button createRoomBtn;
    ImageView backBtn;
    ScrollView scrollView;
    Intent RoomActivity, RoomCreatedActivity;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_family);

        familynameEdTxt = findViewById(R.id.familynameEdTxt);
        familycodeEdTxt = findViewById(R.id.familycodeEdTxt);
        createRoomBtn = findViewById(R.id.createRoomBtn);
        backBtn = findViewById(R.id.backBtn);
        scrollView = findViewById(R.id.scrollView);

        RoomActivity = new Intent(this, RoomActivity.class);
        RoomCreatedActivity = new Intent(this, RoomCreatedSuccessActivity.class);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RoomActivity);
                finish();
            }
        });

        createRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String familyName = familynameEdTxt.getEditText().getText().toString().trim();
                final String familyCode = familycodeEdTxt.getEditText().getText().toString().trim();

                SessionManager sessionManager = new SessionManager(CreateRoom.this, SessionManager.SESSION_USERSESSION);
                final String completePhno = "+91" + sessionManager.getUserDataFromSession().get("phoneNo");

                if (familyName.isEmpty()) {
                    familynameEdTxt.setError("Enter Family Name");
                    return;
                } else if (familyCode.isEmpty()) {
                    familynameEdTxt.setError(null);
                    familynameEdTxt.setErrorEnabled(false);
                    familycodeEdTxt.setError("Enter Family Code");
                    return;
                }

                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (snapshot.child("Users").child(completePhno).child("FamilyCode").exists()) {

                            Snackbar mysnackbar =  Snackbar.make(scrollView, "You already in a room", Snackbar.LENGTH_LONG);
                            mysnackbar.show();

                        } else {
                            familycodeEdTxt.setError(null);
                            familycodeEdTxt.setErrorEnabled(false);


                            dbRef.child("Room").child(familyCode).child("FamilyName").setValue(familyName);
                            dbRef.child("Room").child(familyCode).child("AdminNumber").setValue(completePhno);

                            dbRef.child("Users").child(completePhno).child("FamilyCode").setValue(familyCode);

                            startActivity(RoomCreatedActivity);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
}