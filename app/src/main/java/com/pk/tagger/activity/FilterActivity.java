package com.pk.tagger.activity;

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
        Intent intent = getIntent();
        final String tag = intent.getExtras().getString("TAG");
        Log.d("From FilterA", tag);
        Bundle bundle = new Bundle();
        bundle.putString("TAG", tag);

        if (savedInstanceState == null) {
            MyPreferenceFragment prefFragment = new MyPreferenceFragment();
            prefFragment.setArguments(bundle);
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
            addPreferencesFromResource(R.xml.filters);
          //  final SharedPreferences sharedPreferencesDate;
          //  sharedPreferencesDate = getContext().getSharedPreferences("DateFilter", Context.MODE_PRIVATE);

            final String getTag = getArguments().getString("TAG");
            Log.i("inFragment ", getTag);
            //Set summary to current prefs, if blank, set summary to "Search events for artist"
            Preference searchArtistVenuePrefs = findPreference("search_artist_venue");
            SharedPreferences artistVenuePrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
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

                   SharedPreferences sharedPreferencesDate = getActivity().getSharedPreferences("DateFilter", Context.MODE_PRIVATE);

                    Calendar calendarDate = new GregorianCalendar();
                    calendarDate.set(mYear, mMonth, mDay);
                    SharedPreferences.Editor editor = sharedPreferencesDate.edit();
                    editor.putLong("Date", calendarDate.getTimeInMillis());
                    editor.commit();

                    Calendar calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(sharedPreferencesDate.getLong("Date", 0));

                    Log.d("Date in Set Date", calendar.getTime().toString());
                }
            };

            final DatePickerDialog.OnDateSetListener mDateEnd = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                    Log.i("dasd", calendar.toString());

                }
            };


            Preference btnDateStartFilter = (Preference) findPreference("btnDateStartFilter");
            btnDateStartFilter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                //@TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    SharedPreferences sharedPreferencesDate = getActivity().getSharedPreferences("DateFilter", Context.MODE_PRIVATE);
                    int mYear, mMonth, mDay;
                    if (sharedPreferencesDate.contains("Date")) {

                        Calendar calendar = new GregorianCalendar();

                        calendar.setTimeInMillis(sharedPreferencesDate.getLong("Date", 0));

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
            btnDateEndFilter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                //@TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // Use the current date as the default date in the picker
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(getActivity(), mDateEnd, year, month, day).show();
                    return false;
                }
            });

            Preference button = (Preference)findPreference("Close");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    int data = -1;
                    switch (getTag) {
                        case "Home":
                            data = 0;
                            break;
                        case "Event Listings":
                            data = 1;
                            break;
                        case "Events Map":
                            data = 2;
                            break;
                        default:
                            data = -1;
                            break;
                    }
                    SharedPreferences sharedPrefsCurrentFragment = getActivity().getSharedPreferences("FragmentView", Context.MODE_PRIVATE);
                    Log.d("Filter", String.valueOf(data));
                    SharedPreferences.Editor editor = sharedPrefsCurrentFragment.edit();
                    editor.putInt("fragment", data);
                    editor.commit();
                    int test = sharedPrefsCurrentFragment.getInt("fragment", 0);
                    Log.d("Filter Test", String.valueOf(test));
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(myIntent);

                    return true;
                }
            });

        }

    }
}
