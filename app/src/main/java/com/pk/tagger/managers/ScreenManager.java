package com.pk.tagger.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pk on 10/05/16.
 */
public class ScreenManager {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Context prefContext;

    int PRIVATE_MODE = 1;
    private static final String PREF_NAME = "SCREEN_FILE_KEY";

    // Email address (make variable public to access from outside)
    public static final String KEY_FRAGMENT = "fragment";

    public ScreenManager(Context context) {
        this.prefContext = context;
        sharedPref = prefContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPref.edit();
    }

    public void setCurrentFragment(String fragment) {
        int data;
        switch (fragment) {
            case "Home":
                data = 1;
                break;
            case "Event Listings":
                data = 2;
                break;
            case "Events Map":
                data = 3;
                break;
            default:
                data = -1;
                break;
        }
        editor.putInt(KEY_FRAGMENT, data);
        // commit changes
        editor.commit();
    }

    public int getCurrentFragment() {
        return sharedPref.getInt(KEY_FRAGMENT, -1);
    }
}
