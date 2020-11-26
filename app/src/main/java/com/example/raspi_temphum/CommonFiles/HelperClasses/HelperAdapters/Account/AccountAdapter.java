package com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Account;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.raspi_temphum.CommonFiles.HelperClasses.SessionManager;
import com.example.raspi_temphum.CommonFiles.SignIn;
import com.example.raspi_temphum.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private ArrayList<AccountHelperClass> accountHelperClassArrayList;
    Context context;


    public AccountAdapter(ArrayList<AccountHelperClass> accountHelperClassArrayList, Context context) {
        this.accountHelperClassArrayList = accountHelperClassArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_design, parent, false);

        final AccountViewHolder accountViewHolder = new AccountViewHolder(view);

        accountViewHolder.accountOptionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (accountViewHolder.getAdapterPosition()) {
                    case 0:

                        SessionManager userData = new SessionManager(context, SessionManager.SESSION_USERSESSION);
                        String phno = userData.getUserDataFromSession().get("phoneNo");

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                        DatabaseReference familyNameInDB = firebaseDatabase.getReference("Users").child("+91" + phno).child("FamilyCode").getRef();
                        familyNameInDB.removeValue();


                        SessionManager roomSession = new SessionManager(context, SessionManager.SESSION_ROOMSESSION);
                        if (roomSession.getRoomDataFromSession().get("MemberType").equals("admin")) {

                            // TODO: delete Room code here
                            Toast.makeText(context, "Delete room", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        roomSession.LogoutRoomFromSession();    //Room data

                        Intent RoomActivity = new Intent(context, com.example.raspi_temphum.RoomActivity.class);
                        RoomActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(RoomActivity);

                        break;

                    case 1: // Logout

                        SessionManager sessionManager = new SessionManager(context, SessionManager.SESSION_USERSESSION);
                        sessionManager.LogoutUserFromSession();

                        Intent SignInActivity = new Intent(context, SignIn.class);
                        SignInActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(SignInActivity);

                }
            }
        });
        return accountViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        AccountHelperClass helperClass = accountHelperClassArrayList.get(position);

        holder.image.setImageResource(helperClass.getImage());
        holder.content.setText(helperClass.getContent());
    }

    @Override
    public int getItemCount() {
        return accountHelperClassArrayList.size();
    }


    public static class AccountViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView content;
        CardView accountOptionCardView;

        public AccountViewHolder(@NonNull final View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.account_icon);
            content = itemView.findViewById(R.id.account_list_content);
            accountOptionCardView = itemView.findViewById(R.id.accountOptionCardView);
        }
    }
}
