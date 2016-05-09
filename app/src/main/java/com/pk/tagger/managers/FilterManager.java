package com.pk.tagger.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pk on 09/05/16.
 */
public class FilterManager {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Context filterContext;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "FILTER_FILE_KEY";

    // Constructor
    public FilterManager(Context context) {
        this.filterContext = context;
        sharedPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPref.edit();
    }



}
