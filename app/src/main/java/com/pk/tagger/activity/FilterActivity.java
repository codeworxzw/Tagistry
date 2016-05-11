package com.pk.tagger.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.nfc.Tag;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.DatePicker;

import com.pk.tagger.R;
import com.pk.tagger.managers.FilterManager;
import com.pk.tagger.managers.ScreenManager;
import com.pk.tagger.managers.SessionManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Kieran on 14/03/2016.
 */
public class FilterActivity extends PreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.preferences);

        if (savedInstanceState == null) {
            MyPreferenceFragment prefFragment = new MyPreferenceFragment();
            getFragmentManager().beginTransaction().replace(android.R.id.content, prefFragment).commit();
        }
       // initActionBar();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName("FILTER_FILE_KEY");
            addPreferencesFromResource(R.xml.filters);
          //  final SharedPreferences sharedPreferencesDate;
          //  sharedPreferencesDate = getContext().getSharedPreferences("DateFilter", Context.MODE_PRIVATE);

            final FilterManager filterManager = new FilterManager(getActivity());
            final ScreenManager screenManager = new ScreenManager(getActivity());

            //Set summary to current prefs, if blank, set summary to "Search events for artist"
            Preference searchArtistVenuePrefs = findPreference("search_artist_venue");
            SharedPreferences artistVenuePrefs = getActivity().getSharedPreferences("FILTER_FILE_KEY", Context.MODE_PRIVATE);
            String searchArtistVenue;
            searchArtistVenue = artistVenuePrefs.getString("search_artist_venue", "Search events for artist/venue");
            if(artistVenuePrefs.getString("search_artist_venue", "").equals("")){
                searchArtistVenuePrefs.setSummary("Search events for artist/venue");
            } else {
                searchArtistVenuePrefs.setSummary(searchArtistVenue);
            }

            //Update summary when changed
            searchArtistVenuePrefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.equals("")) {
                        preference.setSummary("Search events for artist/venue");
                    } else {
                        preference.setSummary((String) newValue);
                    }
                    return true;
                }
            });

            final DatePickerDialog.OnDateSetListener mDateStart = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                   int mYear = year;
                   int mMonth = monthOfYear;
                   int mDay = dayOfMonth;

                  // SharedPreferences sharedPreferencesDate = getActivity().getSharedPreferences("DateFilter", Context.MODE_PRIVATE);


                    Calendar calendarDate = new GregorianCalendar();

                    calendarDate.set(mYear, mMonth, mDay);
                 //   SharedPreferences.Editor editor = sharedPreferencesDate.edit();
                    if (filterManager.getDateEnd()!=0) {
                        if (filterManager.getDateEnd()>(calendarDate.getTimeInMillis())) {
                           // editor.putLong("Date", calendarDate.getTimeInMillis());
                           // editor.commit();
                            filterManager.setDateStart(calendarDate.getTimeInMillis());
                        } else {
                            new AlertDialog.Builder(getActivity()).setTitle("InValid Date").setMessage("Start Date must be before End Date").setNeutralButton("Close", null).show();
                        }
                    } else {
                        //editor.putLong("Date", calendarDate.getTimeInMillis());
                        //editor.commit();
                        filterManager.setDateStart(calendarDate.getTimeInMillis());
                    }

                    Calendar calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(filterManager.getDateStart());

                    Log.d("Date in Set Date", calendar.getTime().toString());
                }
            };

            final DatePickerDialog.OnDateSetListener mDateEnd = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

                    int mYearEnd = yearEnd;
                    int mMonthEnd = monthOfYearEnd;
                    int mDayEnd = dayOfMonthEnd;

                    //SharedPreferences sharedPreferencesDate = getActivity().getSharedPreferences("DateFilter", Context.MODE_PRIVATE);

                    Calendar calendarDate = new GregorianCalendar();
                    calendarDate.set(mYearEnd, mMonthEnd, mDayEnd);

                    //SharedPreferences.Editor editor = sharedPreferencesDate.edit();

                    if (filterManager.getDateStart()!=0) {
                        if (filterManager.getDateStart()<(calendarDate.getTimeInMillis())) {
                           // editor.putLong("DateEnd", calendarDate.getTimeInMillis());
                           // editor.commit();
                            filterManager.setDateEnd(calendarDate.getTimeInMillis());
                        } else {
                            new AlertDialog.Builder(getActivity()).setTitle("InValid Date").setMessage("End Date must be after Start Date").setNeutralButton("Close", null).show();
                        }
                    } else {
                       // editor.putLong("DateEnd", calendarDate.getTimeInMillis());
                       // editor.commit();
                        filterManager.setDateEnd(calendarDate.getTimeInMillis());

                    }

                    Calendar calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(filterManager.getDateEnd());

                    Log.d("Date in End Set !", calendar.getTime().toString());

                }
            };


            Preference btnDateStartFilter = (Preference) findPreference("btnDateStartFilter");
            btnDateStartFilter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                //@TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    //SharedPreferences sharedPreferencesDate = getActivity().getSharedPreferences("DateFilter", Context.MODE_PRIVATE);
                    int mYear, mMonth, mDay;
                    if (filterManager.getDateStart()!=0) {

                        Calendar calendar = new GregorianCalendar();

                        calendar.setTimeInMillis(filterManager.getDateStart());

                        mYear = calendar.get(Calendar.YEAR);
                        mMonth = calendar.get(Calendar.MONTH);
                        mDay = calendar.get(Calendar.DAY_OF_MONTH);

                        Log.d("Hello yes", calendar.getTime().toString());

                    } else {

                        // Process to get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        Log.d("Hello no", c.toString());

                    }
                    new DatePickerDialog(getActivity(), mDateStart, mYear, mMonth, mDay).show();
                    return false;
                }
            });

            Preference btnDateEndFilter = (Preference) findPreference("btnDateEndFilter");
            btnDateEndFilter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()

            {

                @Override
                public boolean onPreferenceClick(Preference preference) {

                    //SharedPreferences sharedPreferencesDate = getActivity().getSharedPreferences("DateFilter", Context.MODE_PRIVATE);
                    int mYearEnd, mMonthEnd, mDayEnd;
                    if (filterManager.getDateEnd()!=0) {

                        Calendar calendar = new GregorianCalendar();

                        calendar.setTimeInMillis(filterManager.getDateEnd());

                        mYearEnd = calendar.get(Calendar.YEAR);
                        mMonthEnd = calendar.get(Calendar.MONTH);
                        mDayEnd = calendar.get(Calendar.DAY_OF_MONTH);

                        Log.d("Hello yes", calendar.getTime().toString());

                    } else {

                        // Process to get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYearEnd = c.get(Calendar.YEAR);
                        mMonthEnd = c.get(Calendar.MONTH);
                        mDayEnd = c.get(Calendar.DAY_OF_MONTH);

                        Log.d("Hello no", c.toString());

                    }
                    new DatePickerDialog(getActivity(), mDateEnd, mYearEnd, mMonthEnd, mDayEnd).show();
                        return false;
                }
            });

            Preference button = (Preference)findPreference("Close");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Log.d("Filter 1", String.valueOf(screenManager.getCurrentFragment()));
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(myIntent);

                    return true;
                }
            });

        }

    }
}
