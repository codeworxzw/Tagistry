package com.pk.tagger.managers;

/**
 * Created by pk on 06/05/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.pk.tagger.activity.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Context prefContext;

    int PRIVATE_MODE = 1;
    private static final String PREF_NAME = "PREFERENCE_FILE_KEY";

    private static final String IS_LOGIN = "LoggedIn";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Token (make variable public to access from outside)
    public static final String KEY_TOKEN = "token";
    // Password - only used in Registration Process
    public static final String KEY_PASSWORD = "password";
    // Constructor
    public SessionManager(Context context) {
        this.prefContext = context;
        sharedPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPref.edit();
    }

    public void createLoginSession(String email, String token) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing token in pref
        editor.putString(KEY_TOKEN, token);

        // commit changes
        editor.commit();
    }

    public void createRegisterSession(String email, String password) {

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing password in pref
        editor.putString(KEY_PASSWORD, password);

        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        // user email id
        user.put(KEY_EMAIL, sharedPref.getString(KEY_EMAIL, null));

        // user token
        user.put(KEY_TOKEN, sharedPref.getString(KEY_TOKEN, null));

        // return user
        return user;
    }

    public HashMap<String, String> getRegistrationDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        // user email id
        user.put(KEY_EMAIL, sharedPref.getString(KEY_EMAIL, null));

        // user password
        user.put(KEY_PASSWORD, sharedPref.getString(KEY_PASSWORD, null));

        // return user
        return user;
    }

    public void clearRegistrationDetails() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            Toast.makeText(prefContext, "Not Logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(prefContext, LoginActivity.class);
            prefContext.startActivity(intent);
        } else {

            Toast.makeText(prefContext, "Logged in", Toast.LENGTH_SHORT).show();

        }
    }

    public boolean isLoggedIn(){
        return sharedPref.getBoolean(IS_LOGIN, false);
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Losing Activity
        Intent i = new Intent(prefContext, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        prefContext.startActivity(i);
    }

}