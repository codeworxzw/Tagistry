package com.pk.tagger.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.pk.tagger.services.DatabaseStartPaginatedServiceEvents;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by pk on 10/05/16.
 */
public class StartUpManager {

    Context startContext;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    int PRIVATE_MODE = 1;
    private static final String PREF_NAME = "START_FILE_KEY";

    // Email address (make variable public to access from outside)
    public static final String KEY_TIMESTAMP = "timestamp";

    public StartUpManager(Context context)
    {
        this.startContext = context;
        sharedPref = startContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPref.edit();
    }

    public void setUpApp() {

        if (getTimeStamp()!=0) {
        }
        else {
            Log.d("Timestamp", String.valueOf(getTimeStamp()));
            FilterManager filterManager = new FilterManager(startContext);
            filterManager.setDefault();
            DatabaseStartPaginatedServiceEvents.startActionDownload(startContext, "hello", "hello");
            Log.d("MainActivity", "Sync service started");
            setTimeStamp();
        }
    }

    public void setTimeStamp() {
        Calendar calendarDate = new GregorianCalendar();
        editor.putLong(KEY_TIMESTAMP, calendarDate.getTimeInMillis());
        editor.commit();
    }

    public long getTimeStamp() {
        return sharedPref.getLong(KEY_TIMESTAMP, 0);
    }



}
