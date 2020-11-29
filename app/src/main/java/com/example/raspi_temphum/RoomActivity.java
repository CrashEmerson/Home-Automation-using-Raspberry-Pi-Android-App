package com.example.raspi_temphum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.raspi_temphum.CommonFiles.HelperClasses.SessionManager;
import com.example.raspi_temphum.CommonFiles.SignIn;

public class RoomActivity extends AppCompatActivity {

    TextView hiUserTxtView;
    Button createRoom,joinRoom;

    Intent createRoomActivity, joinRoomActivity,SignInActivity, Dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        hiUserTxtView = findViewById(R.id.hiUserTxtView);
        createRoom = findViewById(R.id.createRoom);
        joinRoom = findViewById(R.id.joinRoom);

        createRoomActivity = new Intent(this, com.example.raspi_temphum.CreateRoom.class);
        joinRoomActivity = new Intent(this, JoinRoomActivity.class);
        SignInActivity = new Intent(this, SignIn.class);
        Dashboard = new Intent(this, Dashboard.class);

        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        String userFullname = sessionManager.getUserDataFromSession().get("fullName");


//        SessionManager roomSession = new SessionManager(this, SessionManager.SESSION_ROOMSESSION);
//        if (roomSession.checkUserJoinedRoom()) {
//            startActivity(Dashboard);
//        }

        hiUserTxtView.setText("Hi, " + userFullname);

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(createRoomActivity);
            }
        });

        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(joinRoomActivity);
            }
        });


    }
}