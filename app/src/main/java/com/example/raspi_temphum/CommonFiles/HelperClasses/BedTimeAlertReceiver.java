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
    DatabaseReference modificationdb = FirebaseDatabase.getInstance().getReference();

    @Override
    public void onReceive(final Context context, Intent intent) {

        dbRef.child("Balcony").setValue("OFF");
        dbRef.child("BedRoom").setValue("OFF");
        dbRef.child("Hall").setValue("OFF");
        dbRef.child("KidsRoom").setValue("OFF");

        createNotificationChannels(context);
        displayNotification(context);
    }

    private void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "channel 2 ",
                    "Bed Time Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 2");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }


    public void displayNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String title = "Bed Time";
        String message = "Time to bed !";
        Notification notification = new NotificationCompat.Builder(context, "channel 2 ")
                .setSmallIcon(R.drawable.ic_logs)
                .setContentTitle(title)
                .setContentText("Time to bed !")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(2, notification);
    }

}
