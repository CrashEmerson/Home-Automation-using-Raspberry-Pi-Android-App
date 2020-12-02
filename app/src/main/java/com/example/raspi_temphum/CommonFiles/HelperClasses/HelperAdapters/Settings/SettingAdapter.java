package com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Settings;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.raspi_temphum.CommonFiles.HelperClasses.BedTimeAlertReceiver;
import com.example.raspi_temphum.CommonFiles.HelperClasses.SessionManager;
import com.example.raspi_temphum.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {

    ArrayList<SettingHelperClass> settingHelperClassArrayList;
    Context context;

    public SettingAdapter(ArrayList<SettingHelperClass> settingHelperClassArrayList, Context context) {
        this.settingHelperClassArrayList = settingHelperClassArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_list_design, parent, false);

        final SettingViewHolder settingViewHolder = new SettingViewHolder(view);

        SessionManager sessionManager = new SessionManager(context, SessionManager.SESSION_ROOMSESSION);
        final String familyCode = sessionManager.getRoomDataFromSession().get("FamilCode");

        final DatabaseReference RoomRef = FirebaseDatabase.getInstance().getReference("Room");

        settingViewHolder.setting_optionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (settingViewHolder.getAdapterPosition()) {
                    case 0:


                        FrameLayout frameLayout = view.findViewById(R.id.setting_frameLyt);

                        if (settingViewHolder.value.getText().equals("ON")) {
//                            frameLayout.setBackgroundColor(view.getResources().getColor(R.color.lightBlack));
                            settingViewHolder.frameLayout.setBackgroundColor(Color.BLACK);
                            settingViewHolder.value.setText("OFF");
                            Toast.makeText(context, "light mode", Toast.LENGTH_SHORT).show();
                        } else {
                            settingViewHolder.value.setText("ON");
                            Toast.makeText(context, "dark mode", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:

                        AlertDialog.Builder tempe_setpointDialog = new AlertDialog.Builder(context);
                        tempe_setpointDialog.setTitle("Temperature Set-point");

                        View temp_viewInflated = LayoutInflater.from(context).inflate(R.layout.popup_dialog_layout, (ViewGroup) view, false);

                        final TextInputEditText temp_input = (TextInputEditText) temp_viewInflated.findViewById(R.id.input);

                        temp_input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        tempe_setpointDialog.setView(temp_viewInflated);

                        // positive and negative btns take color for colorAccent
                        tempe_setpointDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String temp_Setpoint = temp_input.getEditableText().toString();
                                settingViewHolder.value.setText(temp_Setpoint);
                                RoomRef.child(familyCode).child("Setpoint").child("temperature").setValue(temp_Setpoint);


                            }
                        });

                        tempe_setpointDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        tempe_setpointDialog.show();
                        break;
                    case 2:

                        AlertDialog.Builder hum_setpointDialog = new AlertDialog.Builder(context);
                        hum_setpointDialog.setTitle("Humidity Set-point");

                        View hum_viewInflated = LayoutInflater.from(context).inflate(R.layout.popup_dialog_layout, (ViewGroup) view, false);

                        final TextInputEditText hum_input = (TextInputEditText) hum_viewInflated.findViewById(R.id.input);

                        hum_input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        hum_setpointDialog.setView(hum_viewInflated);

                        // positive and negative btns take color for colorAccent
                        hum_setpointDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String hum_Setpoint = hum_input.getEditableText().toString();
                                settingViewHolder.value.setText(hum_Setpoint);
                                RoomRef.child(familyCode).child("Setpoint").child("humidity").setValue(hum_Setpoint);
                            }
                        });

                        hum_setpointDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        hum_setpointDialog.show();
                        break;
                    case 3:
                        final Calendar c = Calendar.getInstance();

                        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                                c.set(
                                        c.get(Calendar.YEAR),
                                        c.get(Calendar.MONTH),
                                        c.get(Calendar.DAY_OF_MONTH),
                                        hourOfDay,
                                        minute,
                                        0
                                );

                                RoomRef.child(familyCode).child("BedTime").child("hours").setValue(Integer.toString(hourOfDay));
                                RoomRef.child(familyCode).child("BedTime").child("minute").setValue(Integer.toString(minute));

                                Snackbar bedtimeSnackbar = Snackbar.make(settingViewHolder.setting_optionCardView, "BedTime is added Sucessfully", Snackbar.LENGTH_SHORT);
                                bedtimeSnackbar.show();

                                settingViewHolder.value.setText(hourOfDay + ":" + minute);

                                startBedTimeNotification(c);

                            }


                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);


                        timePickerDialog.show();
                        break;

                    case 4:
                        if (settingViewHolder.value.getText().equals("ON")) {
                            settingViewHolder.value.setText("OFF");
                            Toast.makeText(context, "Notification is ON", Toast.LENGTH_SHORT).show();
                        } else {
                            settingViewHolder.value.setText("ON");
                            Toast.makeText(context, "Notification is OFF", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }

        });


        return settingViewHolder;
    }

    private void startBedTimeNotification(Calendar c) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BedTimeAlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {

        SettingHelperClass settings = settingHelperClassArrayList.get(position);

        holder.image.setImageResource(settings.getImage());
        holder.content.setText(settings.getContent());
        holder.desc.setText(settings.getDesc());
        holder.value.setText(settings.getValue());
    }

    @Override
    public int getItemCount() {
        return settingHelperClassArrayList.size();
    }

    public static class SettingViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView content, desc, value;
        CardView setting_optionCardView;
        FrameLayout frameLayout;

        public SettingViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.setting_icon);
            content = itemView.findViewById(R.id.setting_list_content);
            desc = itemView.findViewById(R.id.setting_list_des);
            value = itemView.findViewById(R.id.setting_list_value);
            setting_optionCardView = itemView.findViewById(R.id.setting_optionCardView);
            frameLayout = itemView.findViewById(R.id.setting_frameLyt);

        }
    }

}