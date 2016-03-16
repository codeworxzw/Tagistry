package com.pk.tagger.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.pk.tagger.R;

/**
 * Created by Kieran on 14/03/2016.
 */
public class FilterActivity extends PreferenceActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.preferences);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
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
        }
    }
}
