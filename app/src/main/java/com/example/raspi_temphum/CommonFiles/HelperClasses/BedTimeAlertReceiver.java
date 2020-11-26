package com.example.raspi_temphum.CommonFiles.HelperClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.raspi_temphum.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BedTimeAlertReceiver extends BroadcastReceiver {


    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("sensor/Relay");

    @Override
    public void onReceive(Context context, Intent intent) {

        dbRef.child("Balcony").setValue("ON");
        dbRef.child("BedRoom").setValue("ON");
        dbRef.child("Hall").setValue("ON");
        dbRef.child("KidsRoom").setValue("ON");


//        createNotificationChannels(context);
//        sendOnChannel2(context);

    }
    private void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "channel 2 ",
                    "Channel 2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 2");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    public void sendOnChannel2(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String title = "Bed Time";
        String message = "All Lights will be automatically OFF in 5 minutes";
        Notification notification = new NotificationCompat.Builder(context, "channel 2 ")
                .setSmallIcon(R.drawable.ic_logs)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(2, notification);
    }
}
