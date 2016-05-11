package com.pk.tagger.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

/**
 * Created by pk on 10/05/16.
 */
public class StartUpManager {


    Context startContext;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public StartUpManager(Context context)

    {
        this.startContext = context;



    }

    public void setUpApp() {

        sharedPreferences = startContext.getSharedPreferences("TimeStamp", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("time")) {

            Date myDate = new Date(sharedPreferences.getLong("time", 0));

            Log.d("Date in Mainactivity", myDate.toString());
            // DatabaseStartService.startActionDownload(this, "hello", "hello");
        } else {
            //  DatabaseStartService.startActionBaz(this, "hello", "hello");
        }

    }

}
