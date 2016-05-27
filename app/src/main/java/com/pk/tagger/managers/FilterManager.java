package com.pk.tagger.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.pk.tagger.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    public static final String MAX_PRICE = "key_max_price";

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

    public int getMaxPrice() {
        return sharedPref.getInt(MAX_PRICE, 0);
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

    public void setMaxPrice(int MaxPrice) {
        editor.putInt(MAX_PRICE, MaxPrice);
        // commit changes
        editor.commit();
    }

    public void setDefault() {
        Calendar calendarDate = new GregorianCalendar();
        editor.putLong(KEY_DATE_START, calendarDate.getTimeInMillis());
        calendarDate.add(Calendar.MONTH, 6);
        editor.putLong(KEY_DATE_END, calendarDate.getTimeInMillis());
        editor.putBoolean(TICKETS_AVAILABLE, false);
        editor.putString(SEARCH_ARTIST_VENUE, "");
        Set<String> Genres = new HashSet<String> (Arrays.asList(filterContext.getResources().getStringArray(R.array.genres_values)));
        editor.putStringSet(SEARCH_GENRES, Genres);
        editor.putInt(MAX_PRICE, 0);
        editor.commit();
    }

    public void setOneMonth() {
        Calendar calendarDate = new GregorianCalendar();
        editor.putLong(KEY_DATE_START, calendarDate.getTimeInMillis());
        calendarDate.add(Calendar.MONTH, 1);
        editor.putLong(KEY_DATE_END, calendarDate.getTimeInMillis());
        editor.putBoolean(TICKETS_AVAILABLE, false);
        editor.putString(SEARCH_ARTIST_VENUE, "");
        Set<String> Genres = new HashSet<String> (Arrays.asList(filterContext.getResources().getStringArray(R.array.genres_values)));
        editor.putStringSet(SEARCH_GENRES, Genres);
        editor.putInt(MAX_PRICE, 0);
        editor.commit();
    }
    public void setOneWeek() {
        Calendar calendarDate = new GregorianCalendar();
        editor.putLong(KEY_DATE_START, calendarDate.getTimeInMillis());
        calendarDate.add(Calendar.WEEK_OF_MONTH, 1);
        editor.putLong(KEY_DATE_END, calendarDate.getTimeInMillis());
        editor.putBoolean(TICKETS_AVAILABLE, false);
        editor.putString(SEARCH_ARTIST_VENUE, "");
        Set<String> Genres = new HashSet<String> (Arrays.asList(filterContext.getResources().getStringArray(R.array.genres_values)));
        editor.putStringSet(SEARCH_GENRES, Genres);
        editor.putInt(MAX_PRICE, 0);
        editor.commit();
    }
    public void setFestivals() {
        Calendar calendarDate = new GregorianCalendar();
        editor.putLong(KEY_DATE_START, calendarDate.getTimeInMillis());
        calendarDate.add(Calendar.MONTH, 6);
        editor.putLong(KEY_DATE_END, calendarDate.getTimeInMillis());
        editor.putBoolean(TICKETS_AVAILABLE, false);
        editor.putString(SEARCH_ARTIST_VENUE, "");
        String[] festivals = {"35"};
        Set<String> Genres = new HashSet<String> (Arrays.asList(festivals));
        editor.putStringSet(SEARCH_GENRES, Genres);
        editor.putInt(MAX_PRICE, 0);
        editor.commit();
    }
    public void setGenre(String genre) {
        Calendar calendarDate = new GregorianCalendar();
        editor.putLong(KEY_DATE_START, calendarDate.getTimeInMillis());
        calendarDate.add(Calendar.MONTH, 6);
        editor.putLong(KEY_DATE_END, calendarDate.getTimeInMillis());
        editor.putBoolean(TICKETS_AVAILABLE, false);
        editor.putString(SEARCH_ARTIST_VENUE, "");
        String[] genres = {genre};
        Set<String> Genres = new HashSet<String> (Arrays.asList(genres));
        editor.putStringSet(SEARCH_GENRES, Genres);
        editor.putInt(MAX_PRICE, 0);
        editor.commit();
    }
    public void setTennerorLess() {
        Calendar calendarDate = new GregorianCalendar();
        editor.putLong(KEY_DATE_START, calendarDate.getTimeInMillis());
        calendarDate.add(Calendar.MONTH, 6);
        editor.putLong(KEY_DATE_END, calendarDate.getTimeInMillis());
        editor.putBoolean(TICKETS_AVAILABLE, true);
        editor.putString(SEARCH_ARTIST_VENUE, "");
        Set<String> Genres = new HashSet<String> (Arrays.asList(filterContext.getResources().getStringArray(R.array.genres_values)));
        editor.putStringSet(SEARCH_GENRES, Genres);
        editor.putInt(MAX_PRICE, 10);
        editor.commit();
    }

}
