package com.example.raspi_temphum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RoomCreatedSuccessActivity extends AppCompatActivity {

    Button enterRoomBtn,notNowBtn;
    Intent Dashboard, RoomActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_created_success);

        enterRoomBtn = findViewById(R.id.enterRoomBtn);
        notNowBtn = findViewById(R.id.notNowBtn);

        Dashboard = new Intent(this, Dashboard.class);
        RoomActivityIntent = new Intent(this, RoomActivity.class);

        enterRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Dashboard);
                finish();
            }
        });

        notNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RoomActivityIntent);
                finish();
            }
        });

    }
}