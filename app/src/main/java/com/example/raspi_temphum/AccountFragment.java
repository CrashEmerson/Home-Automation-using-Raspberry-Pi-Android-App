package com.example.raspi_temphum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raspi_temphum.CommonFiles.ChangeUsername;
import com.example.raspi_temphum.CommonFiles.ForgetPassword_NewCredentials;
import com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Account.AccountAdapter;
import com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Account.AccountHelperClass;
import com.example.raspi_temphum.CommonFiles.HelperClasses.SessionManager;

import java.util.ArrayList;

public class AccountFragment extends Fragment {

    RecyclerView accountRecyclerView;
    RecyclerView.Adapter adapter;
    TextView account_username_valueTxtView;
    ImageView account_username_edit_icon,account_password_edit_icon;
    Context myContext;
    Intent changeUsername, newCredentialsActivity;

    ArrayList<AccountHelperClass> accountHelperClassArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account, container, false);

        myContext = container.getContext();
        accountRecyclerView = view.findViewById(R.id.accountRecyclerView);
        account_username_valueTxtView = view.findViewById(R.id.account_username_valueTxtView);
        account_username_edit_icon = view.findViewById(R.id.account_username_edit_icon);
        account_password_edit_icon = view.findViewById(R.id.account_password_edit_icon);
        newCredentialsActivity = new Intent(myContext, ForgetPassword_NewCredentials.class);
        changeUsername = new Intent(myContext, ChangeUsername.class);

        accountRecyclerView.setHasFixedSize(true);
        accountRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        setUsernameIn_ChangeUsernam();


        String currentMemberType = new SessionManager(view.getContext(), SessionManager.SESSION_ROOMSESSION).getRoomDataFromSession().get("MemberType");
        if (currentMemberType.equals("admin")) {
            accountHelperClassArrayList.add(new AccountHelperClass(R.drawable.ic_delete_room, "Delete Room"));
        } else {
            accountHelperClassArrayList.add(new AccountHelperClass(R.drawable.ic_leave_room, "Leave Room"));
        }

        accountHelperClassArrayList.add(new AccountHelperClass(R.drawable.ic_logout, "Logout"));

        adapter = new AccountAdapter(accountHelperClassArrayList, view.getContext());
        accountRecyclerView.setAdapter(adapter);

        account_username_edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditUsername();
            }
        });
        account_password_edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditPassword();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void EditPassword() {

        SessionManager sessionManager = new SessionManager(myContext, SessionManager.SESSION_USERSESSION);
        String phoneNo = sessionManager.getUserDataFromSession().get("phoneNo");
        String completePhoneNumber = "+91" + phoneNo;

        newCredentialsActivity.putExtra("requestTochangePassword", completePhoneNumber);
        startActivity(newCredentialsActivity);
    }

    private void EditUsername() {
        changeUsername.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(changeUsername);
    }

    private void setUsernameIn_ChangeUsernam() {

        SessionManager sessionManager = new SessionManager(myContext, SessionManager.SESSION_USERSESSION);
        String username = sessionManager.getUserDataFromSession().get("username");

        account_username_valueTxtView.setText(username);
    }
}