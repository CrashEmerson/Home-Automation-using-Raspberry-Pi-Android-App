package com.example.raspi_temphum.CommonFiles.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_ROOMSESSION = "userRoomSession";


    //      user session
//      private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_FULLNAME = "fullName";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "emailAddress";
    private static final String KEY_PHONENUMBER = "phoneNo";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PASSWORD = "password";


    public SessionManager(Context context, String SessionName) {
        this.context = context;

        usersSession = context.getSharedPreferences(SessionName, Context.MODE_PRIVATE);
        editor = usersSession.edit();
        editor.apply();
    }

    public void createLoginSession(String fullname, String username, String email, String phnNo, String gender, String password) {

        editor.putBoolean("IsLogin", true);

        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONENUMBER, phnNo);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }

    public void changeUsernameInSession(String new_username) {
        editor.putString(KEY_USERNAME, new_username);
        editor.commit();
    }

    public HashMap<String, String> getUserDataFromSession() {

        HashMap<String, String> usersData = new HashMap<>();

        usersData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME, null));
        usersData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        usersData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));
        usersData.put(KEY_PHONENUMBER, usersSession.getString(KEY_PHONENUMBER, null));
        usersData.put(KEY_GENDER, usersSession.getString(KEY_GENDER, null));
        usersData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));

        return usersData;
    }

    public boolean checkUserIsLoggedIn() {
        if (usersSession.getBoolean("IsLogin", false)) {
            return true;
        } else {
            return false;
        }
    }

    public void LogoutUserFromSession() {
        editor.clear();
        editor.commit();
    }

    public void createRoomSession(String familyName, String familyCode, String memberType) {
        editor.putBoolean("IsUserJoinedRoom", true);
        editor.putString("FamilName", familyName);
        editor.putString("FamilCode", familyCode);
        editor.putString("MemberType", memberType);

        editor.commit();
    }

    public HashMap<String, String> getRoomDataFromSession() {

        HashMap<String, String> roomData = new HashMap<>();

        roomData.put("IsUserJoinedRoom", String.valueOf(usersSession.getBoolean("IsUserJoinedRoom", true)));
        roomData.put("FamilName", usersSession.getString("FamilName", null));
        roomData.put("FamilCode", usersSession.getString("FamilCode", null));
        roomData.put("MemberType", usersSession.getString("MemberType", null));

        return roomData;
    }

    public void LogoutRoomFromSession() {
        editor.remove(SessionManager.SESSION_ROOMSESSION);
        editor.commit();
    }
}