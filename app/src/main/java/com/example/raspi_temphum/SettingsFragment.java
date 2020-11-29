package com.example.raspi_temphum;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Settings.SettingAdapter;
import com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Settings.SettingHelperClass;
import com.example.raspi_temphum.CommonFiles.HelperClasses.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsFragment extends Fragment {

    RecyclerView settingRecyclerView;
    RecyclerView.Adapter adapter;
    Context myContext;

    //CardView darkModeCardView;
    TextView darkmode_value;

    DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference("Room");

    public String bedtime_hour, bedtime_minute;
    public String temp_setpoint, hum_setpoint;
    ArrayList<SettingHelperClass> settingHelperClassArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        myContext = container.getContext();

        settingRecyclerView = view.findViewById(R.id.settingRecyclerView);
        //darkModeCardView = view.findViewById(R.id.darkModeCardView);
        //darkmode_value = view.findViewById(R.id.darkmode_value);

        settingRecyclerView.setHasFixedSize(true);
        settingRecyclerView.setLayoutManager(new LinearLayoutManager(myContext, LinearLayoutManager.VERTICAL, false));

        settingHelperClassArrayList = new ArrayList<>();

        SessionManager sessionManager = new SessionManager(myContext, SessionManager.SESSION_ROOMSESSION);
        final String familyCode = sessionManager.getRoomDataFromSession().get("FamilCode");
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    bedtime_hour = snapshot.child(familyCode).child("BedTime").child("hours").getValue().toString();
                    bedtime_minute = snapshot.child(familyCode).child("BedTime").child("minute").getValue().toString();
                    temp_setpoint = snapshot.child(familyCode).child("Setpoint").child("temperature").getValue().toString();
                    hum_setpoint = snapshot.child(familyCode).child("Setpoint").child("humidity").getValue().toString();


//                    darkModeCardView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            darkMode();
//                        }
//                    });

                    // TODO: add notification info to SESSION

                    settingHelperClassArrayList.add(new SettingHelperClass(R.drawable.ic_darkmode, "Dark Mode", "To prevent eyes", "ON"));
                    settingHelperClassArrayList.add(new SettingHelperClass(R.drawable.ic_temp, "Temperature Setpoint", "To set the trigger value for temperature", temp_setpoint));
                    settingHelperClassArrayList.add(new SettingHelperClass(R.drawable.ic_hum, "Humidity Setpoint", "To set the trigger value for humidity", hum_setpoint));
                    settingHelperClassArrayList.add(new SettingHelperClass(R.drawable.ic_bedtime, "Bed Time", "To set bed time value", bedtime_hour + ":" + bedtime_minute));

//      settingHelperClassArrayList.add(new SettingHelperClass(R.drawable.ic_notification, "Notification", "Turn ON/OFF Notification", "ON"));

                    adapter = new SettingAdapter(settingHelperClassArrayList, myContext);
                    settingRecyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(myContext, "some error " + error, Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void darkMode() {
        if (darkmode_value.getText().equals("ON")) {
            darkmode_value.setText("OFF");
            Toast.makeText(myContext, "light mode", Toast.LENGTH_SHORT).show();
        } else {
            darkmode_value.setText("ON");
            Toast.makeText(myContext, "dark mode", Toast.LENGTH_SHORT).show();
        }
    }
}