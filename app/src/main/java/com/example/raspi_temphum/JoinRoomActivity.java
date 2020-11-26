package com.example.raspi_temphum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.raspi_temphum.CommonFiles.HelperClasses.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinRoomActivity extends AppCompatActivity {

    TextInputLayout joinroomCodeEdTxt;
    Button joinBtn;

    Intent HomeActivity;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_room);

        joinroomCodeEdTxt = findViewById(R.id.joinroomCodeEdTxt);
        joinBtn = findViewById(R.id.joinBtn);

        HomeActivity = new Intent(this, Dashboard.class);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String joinCode = joinroomCodeEdTxt.getEditText().getText().toString().trim();

                SessionManager sessionManager = new SessionManager(JoinRoomActivity.this, SessionManager.SESSION_USERSESSION);
                final String userPhoneNo = sessionManager.getUserDataFromSession().get("phoneNo");
                final String username = sessionManager.getUserDataFromSession().get("username");


                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (joinroomCodeEdTxt.getEditText().getText().toString().trim().isEmpty()) {
                            joinroomCodeEdTxt.setError("Field is empty");
                            return;
                        }

                        if (snapshot.child("Room").child(joinCode).exists()) {
                            joinroomCodeEdTxt.setError(null);
                            joinroomCodeEdTxt.setErrorEnabled(false);

                            if (!snapshot.child("Users").child("+91" + userPhoneNo).child("FamilyCode").exists()
                                    || snapshot.child("Users").child("+91" + userPhoneNo).child("FamilyCode").getValue().equals(joinCode)) {


                                dbRef.child("Users").child("+91" + userPhoneNo).child("FamilyCode").setValue(joinCode);
                                dbRef.child("Room").child(joinCode).child("Members").child(username).setValue(userPhoneNo);

                                String FamilyName = snapshot.child("Room").child(joinCode).child("FamilyName").getValue().toString();
                                String AdminNumber = snapshot.child("Room").child(joinCode).child("AdminNumber").getValue().toString();
                                String FamilyCode = dbRef.child("Room").child(joinCode).getKey();

                                SessionManager roomSession = new SessionManager(JoinRoomActivity.this, SessionManager.SESSION_ROOMSESSION);

                                String memberType;
                                if (AdminNumber.equals("+91" + userPhoneNo))
                                    memberType = "admin";
                                else memberType = "member";

                                roomSession.createRoomSession(FamilyName, FamilyCode, memberType);

                                startActivity(HomeActivity);
                                finish();
                            } else {
                                joinroomCodeEdTxt.setError("You are already in a group");
                                return;
                            }
                        } else {
                            joinroomCodeEdTxt.setError("Invalid Room Code");
                            return;
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