package com.example.raspi_temphum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
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
    Intent SignInActivity;

    boolean balconyLEDON_logstate = true;
    boolean hallLEDON_logstate = true;
    boolean bedRoomLEDON_logstate = true;
    boolean kidsRoomLEDON_logstate = true;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = firebaseDatabase.getReference("sensor");
    DatabaseReference roomRef = firebaseDatabase.getReference("Room");


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        myContext = container.getContext();

        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        familyNameTxtView = (TextView) view.findViewById(R.id.familyNameTxtView);
        tempValueTextView = (TextView) view.findViewById(R.id.tempValueTextView);
        humValueTextView = (TextView) view.findViewById(R.id.humValueTextView);
        dateTxtView = (TextView) view.findViewById(R.id.dateTxtView);
        timeTxtView = (TextView) view.findViewById(R.id.timeTxtView);
        bedTimeValueTxtView = view.findViewById(R.id.bedTimeValueTxtView);

        balconyLEDONTxtView = (TextView) view.findViewById(R.id.balconyLEDONTxtView);
        bedRoomLEDONTxtView = (TextView) view.findViewById(R.id.bedRoomLEDONTxtView);
        kidsRoomLEDONTxtView = (TextView) view.findViewById(R.id.kidsRoomLEDONTxtView);
        hallLEDONTxtView = (TextView) view.findViewById(R.id.hallLEDONTxtView);

        balconyLEDSwitch = (SwitchCompat) view.findViewById(R.id.balconyLEDSwitch);
        bedRoomLEDSwitch = (SwitchCompat) view.findViewById(R.id.bedRoomLEDSwitch);
        kidsRoomLEDSwitch = (SwitchCompat) view.findViewById(R.id.kidsRoomLEDSwitch);
        hallLEDSwitch = (SwitchCompat) view.findViewById(R.id.hallLEDSwitch);
        frameLyt = (FrameLayout) view.findViewById(R.id.frameLyt);
        SignInActivity = new Intent(myContext, SignIn.class);


        SessionManager sessionManager = new SessionManager(myContext, SessionManager.SESSION_USERSESSION);
        String userFullname = sessionManager.getUserDataFromSession().get("fullName");

        if (!sessionManager.checkUserIsLoggedIn()) {
            startActivity(SignInActivity);
        }

        //Methods
        setFamilyNameInDashboard();
        fetchTempHumData();
        fetchRelayData();
        SwitchRelayState();
        FetchBedTime();

        SwitchRelayValue();

        UpdateDateTime();


        displayUserDetails();   // For testing TODO: delete this after testing

        // Inflate the layout for this fragment
        return view;
    }

    private void FetchBedTime() {

        SessionManager roomData = new SessionManager(myContext, SessionManager.SESSION_ROOMSESSION);
        HashMap<String, String> roomhashData = roomData.getRoomDataFromSession();
        String currentfamilyCode = roomhashData.get("FamilCode").toString();

        roomRef.child(currentfamilyCode).child("BedTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long hours = snapshot.child("hours").getValue(long.class);
                long minute = snapshot.child("minute").getValue(long.class);

                bedTimeValueTxtView.setText(hours + " : " + minute);
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
                final String days[] = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
                String months[] = {"January", "February", "March", "April", "May ", "June", "July", "August", "September", "October", " November", "December"};

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

        dbRef.child("Relay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.child("Balcony").getValue(String.class)).equals("ON"))
                    balconyLEDSwitch.setChecked(true);
                else balconyLEDSwitch.setChecked(false);

                if ((snapshot.child("BedRoom").getValue(String.class)).equals("ON"))
                    bedRoomLEDSwitch.setChecked(true);
                else bedRoomLEDSwitch.setChecked(false);

                if ((snapshot.child("KidsRoom").getValue(String.class)).equals("ON"))
                    kidsRoomLEDSwitch.setChecked(true);
                else kidsRoomLEDSwitch.setChecked(false);

                if ((snapshot.child("Hall").getValue(String.class)).equals("ON"))
                    hallLEDSwitch.setChecked(true);
                else hallLEDSwitch.setChecked(false);

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
                    dbRef.child("Relay").child("Balcony").setValue("ON");
                } else {
                    dbRef.child("Relay").child("Balcony").setValue("OFF");
                }
            }
        });

        bedRoomLEDSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    dbRef.child("Relay").child("BedRoom").setValue("ON");
                } else {
                    dbRef.child("Relay").child("BedRoom").setValue("OFF");
                }
            }
        });

        kidsRoomLEDSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    dbRef.child("Relay").child("KidsRoom").setValue("ON");
                } else {
                    dbRef.child("Relay").child("KidsRoom").setValue("OFF");
                }
            }
        });

        hallLEDSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    dbRef.child("Relay").child("Hall").setValue("ON");
                } else {
                    dbRef.child("Relay").child("Hall").setValue("OFF");
                }
            }
        });
    }

    private void fetchRelayData() {

        dbRef.child("Relay").addValueEventListener(new ValueEventListener() {
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

        dbRef.child("dht").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long temp = snapshot.child("Temperature").getValue(long.class);
                long hum = snapshot.child("Humidity").getValue(long.class);

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
        String currentfamilyName = roomhashData.get("FamilName").toString();
        familyNameTxtView.setText(currentfamilyName);


    }
}