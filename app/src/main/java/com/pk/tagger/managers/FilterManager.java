package com.pk.tagger.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pk on 09/05/16.
 */
public class FilterManager {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Context filterContext;

    int PRIVATE_MODE = 1;
    private static final String PREF_NAME = "FILTER_FILE_KEY";

    public static final String KEY_DATE_START = "start_date";
    public static final String KEY_DATE_END = "end_date";
    public static final String SEARCH_ARTIST_VENUE = "search_artist_venue";
    public static final String SEARCH_GENRES = "search_genres";
    public static final String TICKETS_AVAILABLE = "tickets_available";

    // Constructor
    public FilterManager(Context context) {
        this.filterContext = context;
        sharedPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPref.edit();
    }

    public long getDateStart() {
        return sharedPref.getLong(KEY_DATE_START, 0);
    }

    public long getDateEnd() {
        return sharedPref.getLong(KEY_DATE_END, 0);
    }

    public String getSearchArtistVenue() {
        return sharedPref.getString(SEARCH_ARTIST_VENUE, null);
    }

    public Set<String> getSearchGenres() {
        return sharedPref.getStringSet(SEARCH_GENRES, null);
    }

    public Boolean getTicketsAvailable() {
        return sharedPref.getBoolean(TICKETS_AVAILABLE, false);
    }

    public void setDateStart(long dateStart) {
        editor.putLong(KEY_DATE_START, dateStart);
        // commit changes
        editor.commit();
    }

    public void setDateEnd(long dateEnd) {
        editor.putLong(KEY_DATE_END, dateEnd);
        // commit changes
        editor.commit();
    }

    public void setSearchArtistVenue(String searchArtistVenue) {
        editor.putString(SEARCH_ARTIST_VENUE, searchArtistVenue);
        // commit changes
        editor.commit();
    }

    public void setSearchGenres(Set<String> set) {
        editor.putStringSet(SEARCH_GENRES, set);
        // commit changes
        editor.commit();
    }

    public void setTicketsAvailable(boolean ticketsAvailable) {
       editor.putBoolean(TICKETS_AVAILABLE, ticketsAvailable);
        // commit changes
        editor.commit();
    }

}
