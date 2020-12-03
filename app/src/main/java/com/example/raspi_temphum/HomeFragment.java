package com.example.raspi_temphum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raspi_temphum.CommonFiles.HelperClasses.SessionManager;
import com.example.raspi_temphum.CommonFiles.SignIn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    TextView familyNameTxtView, dateTxtView, timeTxtView;
    TextView tempValueTextView, humValueTextView;
    TextView balconyLEDONTxtView, bedRoomLEDONTxtView, kidsRoomLEDONTxtView, hallLEDONTxtView, bedTimeValueTxtView;
    SwitchCompat balconyLEDSwitch, bedRoomLEDSwitch, kidsRoomLEDSwitch, hallLEDSwitch;
    FrameLayout frameLyt;
    Context myContext;
    Intent SignInActivity, RoomActivity;
    CardView balconyLEDStateCardView, hallLEDStateCardView, kidsRoomLEDStateCardView, bedRoomLEDStateCardView;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference sensorRef = firebaseDatabase.getReference("sensor");
    DatabaseReference roomRef = firebaseDatabase.getReference("Room");
    DatabaseReference dbRef = firebaseDatabase.getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        myContext = container.getContext();

        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        familyNameTxtView = view.findViewById(R.id.familyNameTxtView);
        tempValueTextView = view.findViewById(R.id.tempValueTextView);
        humValueTextView = view.findViewById(R.id.humValueTextView);
        dateTxtView = view.findViewById(R.id.dateTxtView);
        timeTxtView = view.findViewById(R.id.timeTxtView);
        bedTimeValueTxtView = view.findViewById(R.id.bedTimeValueTxtView);

        balconyLEDONTxtView = view.findViewById(R.id.balconyLEDONTxtView);
        bedRoomLEDONTxtView = view.findViewById(R.id.bedRoomLEDONTxtView);
        kidsRoomLEDONTxtView = view.findViewById(R.id.kidsRoomLEDONTxtView);
        hallLEDONTxtView = view.findViewById(R.id.hallLEDONTxtView);

        balconyLEDSwitch = view.findViewById(R.id.balconyLEDSwitch);
        bedRoomLEDSwitch = view.findViewById(R.id.bedRoomLEDSwitch);
        kidsRoomLEDSwitch = view.findViewById(R.id.kidsRoomLEDSwitch);
        hallLEDSwitch = view.findViewById(R.id.hallLEDSwitch);

        bedRoomLEDStateCardView = view.findViewById(R.id.bedRoomLEDStateCardView);
        kidsRoomLEDStateCardView = view.findViewById(R.id.kidsRoomLEDStateCardView);
        hallLEDStateCardView = view.findViewById(R.id.hallLEDStateCardView);
        balconyLEDStateCardView = view.findViewById(R.id.balconyLEDStateCardView);

        frameLyt = view.findViewById(R.id.frameLyt);
        SignInActivity = new Intent(myContext, SignIn.class);
        RoomActivity = new Intent(myContext, com.example.raspi_temphum.RoomActivity.class);


        //Methods
        setFamilyNameInDashboard();
        fetchTempHumData();
        fetchRelayData();
        SwitchRelayState();
        FetchBedTime();


        SwitchRelayValue();

        UpdateDateTime();

        //displayUserDetails();

        // Inflate the layout for this fragment
        return view;
    }


    private void FetchBedTime() {

        SessionManager roomData = new SessionManager(myContext, SessionManager.SESSION_ROOMSESSION);
        HashMap<String, String> roomhashData = roomData.getRoomDataFromSession();
        String currentfamilyCode = roomhashData.get("FamilCode");

        roomRef.child(currentfamilyCode).child("BedTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String hours = snapshot.child("hours").getValue(String.class);
                String minute = snapshot.child("minute").getValue(String.class);

                bedTimeValueTxtView.setText(hours + " : " + minute);   // TODO: change later
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void UpdateDateTime() {


        final Handler somehandler = new Handler(Looper.getMainLooper());


        somehandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                final int day = calendar.get(Calendar.DAY_OF_WEEK);
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                final String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
                String[] months = {"January", "February", "March", "April", "May ", "June", "July", "August", "September", "October", " November", "December"};

                final String currentDay = days[day - 1];
                final int currentDate = date;
                final String currentMonth = months[month - 1];

                final String currentHour = String.format("%02d:%02d", hours, minute);

                dateTxtView.setText(currentDay + ", " + currentDate + " " + currentMonth);
                timeTxtView.setText("" + currentHour);
                somehandler.postDelayed(this, 1000);
            }
        }, 10);

    }

    private void SwitchRelayState() {

        sensorRef.child("Relay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                balconyLEDSwitch.setChecked((snapshot.child("Balcony").getValue(String.class)).equals("ON"));

                bedRoomLEDSwitch.setChecked((snapshot.child("BedRoom").getValue(String.class)).equals("ON"));

                kidsRoomLEDSwitch.setChecked((snapshot.child("KidsRoom").getValue(String.class)).equals("ON"));

                hallLEDSwitch.setChecked((snapshot.child("Hall").getValue(String.class)).equals("ON"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void SwitchRelayValue() {

        SessionManager sessionManager = new SessionManager(myContext, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> fetchedData = sessionManager.getUserDataFromSession();
        final String currentUsername = fetchedData.get("username");


        balconyLEDSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    sensorRef.child("Relay").child("Balcony").setValue("ON");
                    balconyLEDStateCardView.setCardBackgroundColor(Color.parseColor("#FFFF00"));
                } else {
                    sensorRef.child("Relay").child("Balcony").setValue("OFF");
                    balconyLEDStateCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        bedRoomLEDSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    sensorRef.child("Relay").child("BedRoom").setValue("ON");
                    bedRoomLEDStateCardView.setCardBackgroundColor(Color.parseColor("#FFFF00"));
                } else {
                    sensorRef.child("Relay").child("BedRoom").setValue("OFF");
                    bedRoomLEDStateCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        kidsRoomLEDSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    sensorRef.child("Relay").child("KidsRoom").setValue("ON");
                    kidsRoomLEDStateCardView.setCardBackgroundColor(Color.parseColor("#FFFF00"));
                } else {
                    sensorRef.child("Relay").child("KidsRoom").setValue("OFF");
                    kidsRoomLEDStateCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        hallLEDSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    sensorRef.child("Relay").child("Hall").setValue("ON");
                    hallLEDStateCardView.setCardBackgroundColor(Color.parseColor("#FFFF00"));
                } else {
                    sensorRef.child("Relay").child("Hall").setValue("OFF");
                    hallLEDStateCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });
    }

    private void fetchRelayData() {

        sensorRef.child("Relay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                balconyLEDONTxtView.setText(snapshot.child("Balcony").getValue(String.class));
                bedRoomLEDONTxtView.setText(snapshot.child("BedRoom").getValue(String.class));
                hallLEDONTxtView.setText(snapshot.child("Hall").getValue(String.class));
                kidsRoomLEDONTxtView.setText(snapshot.child("KidsRoom").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchTempHumData() {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String temp = snapshot.child("sensor/dht/Temperature").getValue(String.class);
                String hum = snapshot.child("sensor/dht/Humidity").getValue(String.class);

                SessionManager roomData = new SessionManager(myContext, SessionManager.SESSION_ROOMSESSION);
                String familCode = roomData.getRoomDataFromSession().get("FamilCode");

                String temp_setpoint = snapshot.child("Room").child(familCode).child("Setpoint/temperature").getValue(String.class);
                String hum_setpoint = snapshot.child("Room").child(familCode).child("Setpoint/humidity").getValue(String.class);

                int temp_value = Integer.parseInt(temp);
                int hum_value = Integer.parseInt(hum);
                int temp_setpoint_value = Integer.parseInt(temp_setpoint);
                int hum_setpoint_value = Integer.parseInt(hum_setpoint);


                if ((temp_value >= temp_setpoint_value || hum_value >= hum_setpoint_value))
                    dbRef.child("sensor").child("Buzzer/Alarm").setValue("ON");
                else dbRef.child("sensor").child("Buzzer/Alarm").setValue("OFF");

                tempValueTextView.setText("" + temp);
                humValueTextView.setText("" + hum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(myContext, "Temp and huM has not fetched : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayUserDetails() {
        HashMap<String, String> fetchedData;

        SessionManager sessionManager = new SessionManager(myContext, SessionManager.SESSION_USERSESSION);
        fetchedData = sessionManager.getUserDataFromSession();

        String fullName = fetchedData.get("fullName");
        String username = fetchedData.get("username");
        String emailAddress = fetchedData.get("emailAddress");
        String phoneNo = fetchedData.get("phoneNo");
        String gender = fetchedData.get("gender");
        String password = fetchedData.get("password");

        Toast.makeText(myContext, fullName + "\n" + username + "\n" + emailAddress + "\n" + phoneNo + "\n" + gender + "\n" + password, Toast.LENGTH_LONG).show();
    }

    private void setFamilyNameInDashboard() {

        SessionManager roomData = new SessionManager(myContext, SessionManager.SESSION_ROOMSESSION);

        HashMap<String, String> roomhashData = roomData.getRoomDataFromSession();
        String currentfamilyName = roomhashData.get("FamilName");
        familyNameTxtView.setText(currentfamilyName);
    }
}