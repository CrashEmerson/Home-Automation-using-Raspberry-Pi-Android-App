package com.example.raspi_temphum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button activateBtn;
    TextView tempCircle;
    TextView humCircle;
    Switch hallSwitch;
    RelativeLayout myrelativelayout;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("sensor");

    Intent intentMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);    //Fullscreen
        setContentView(R.layout.activity_main);

        activateBtn = (Button) findViewById(R.id.activateBtn);
        tempCircle = (TextView) findViewById(R.id.tempCircle);
        humCircle = (TextView) findViewById(R.id.humCircle);
        hallSwitch = (Switch) findViewById(R.id.hallSwitch);
        myrelativelayout = (RelativeLayout) findViewById(R.id.myrelativelayout);
        intentMainActivity = new Intent(this, MainActivity.class);

        myRef.child("Relay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if(value == "ON") hallSwitch.setChecked(true);
                else if(value == "OFF") hallSwitch.setChecked(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        hallSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    myRef.child("Relay").setValue("ON");
                }else{
                    myRef.child("Relay").setValue("OFF");
                }
            }
        });
        activateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0, intentMainActivity,
                        PendingIntent.FLAG_CANCEL_CURRENT);


                NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.this,"Personal_Notification")
                        .setSmallIcon(R.drawable.ic_message_notification)
                        //.setLargeIcon(BitmapFactory. decodeResource (getResources(), R.mipmap.ic_notification_largeicon))
                        .setContentTitle("Temperature and Humidity")
                        .setContentText("Temp : 33'     " + "Hum : 83 %")
                        //.setDefaults(NotificationCompat.DEFAULT_ALL)                  //notification sound and vibration
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
                notification.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notification.build());

                if(activateBtn.getText().equals("ACTIVATE"))
                {
                    activateBtn.setText("DEACTIVATE");

                    //Temperature
                    myRef.child("dht").child("Humidity").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            humCircle.setText(value + " %");
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Reading Temp value is failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Humidity
                    myRef.child("dht").child("Temperature").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            tempCircle.setText(value + " c");
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Reading Hum value is failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(activateBtn.getText().equals("DEACTIVATE")) activateBtn.setText("ACTIVATE");
            }
        });
    }
}