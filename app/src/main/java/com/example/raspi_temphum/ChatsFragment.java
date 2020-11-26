package com.example.raspi_temphum;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Chats.ChatsAdapter;
import com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Chats.ChatsHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    RecyclerView chatsRecyclerView;
    RecyclerView.Adapter adapter;
    Context context;
    ArrayList<ChatsHelperClass> chatsHelperClassArrayList;
    Button dummyBtn;
    private NotificationManagerCompat notificationManager;

    public static final String CHANNEL_1_ID = "channel1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();

        notificationManager = NotificationManagerCompat.from(context);

        View view = inflater.inflate(R.layout.fragment_chats, container, false);


        dummyBtn = view.findViewById(R.id.dummyBtn);

        chatsRecyclerView = view.findViewById(R.id.chatsRecyclerView);
        chatsRecyclerView.setHasFixedSize(true);

        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        chatsHelperClassArrayList = new ArrayList<>();

        createNotificationChannels();

        dummyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOnChannel1();
            }
        });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference dbRef = firebaseDatabase.getReference("Room").child("SHA256").child("Logs");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // TODO: change dynamic room name and username

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        String list = dataSnapshot.getValue(String.class);
                        chatsHelperClassArrayList.add(new ChatsHelperClass(list, dataSnapshot.getKey()));
                    }

                    adapter = new ChatsAdapter(chatsHelperClassArrayList);
                    chatsRecyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    public void sendOnChannel1() {
        String title = "Hi Am Title";
        String message = "Hi Am message";
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_logs)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }
}
