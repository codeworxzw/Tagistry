package com.pk.tagger.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

/**
 * Created by pk on 10/05/16.
 */
public class StartUpManager {


    Context myContext;
    SharedPreferences sharedPreferences;

    public StartUpManager(Context context)

    {
        sharedPreferences = context.getSharedPreferences("TimeStamp", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("time")) {

            Date myDate = new Date(sharedPreferences.getLong("time", 0));

            Log.d("Date in Mainactivity", myDate.toString());
            // DatabaseStartService.startActionDownload(this, "hello", "hello");
        } else {
            //  DatabaseStartService.startActionBaz(this, "hello", "hello");
        }

    }

}
