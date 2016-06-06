package com.pk.tagger.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LogWriter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.pk.tagger.R;
import com.pk.tagger.managers.FilterManager;
import com.pk.tagger.managers.ScreenManager;
import com.pk.tagger.recyclerview.Genre;
import com.pk.tagger.recyclerview.GenreAdapter;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import me.angrybyte.numberpicker.view.ActualNumberPicker;

/**
 * Created by pk on 26/05/16.
 */
public class FilterFragment extends Fragment {

    FilterManager filterManager;
    GenreAdapter adapter;
    ScreenManager screenManager;
    ArrayList<Genre> items;
    SeekBar seekBar;
    TextView seekBarTextView;

    private ActualNumberPicker mPicker;
    ArrayList<Genre> tiles;
    TextView genreTextView;
    String genreText = "";
    String gChosen = "<b>" + "Genres Chosen:" + "</b> ";

    Map<String, String> sets;

    public FilterFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        Log.d("Filter Fragment", "onCreateView called");

        genreTextView = (TextView) rootView.findViewById(R.id.genreschosen);

        filterManager = new FilterManager(getContext());

        Switch ticketSwitch = (Switch) rootView.findViewById(R.id.switchId);
        ticketSwitch.setChecked(filterManager.getTicketsAvailable());

        ticketSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filterManager.setTicketsAvailable(true);
                } else {
                    filterManager.setTicketsAvailable(false);
                }
            }
        });

        sets = new HashMap<String,String>();

//        ToggleButton toggle = (ToggleButton) rootView.findViewById(R.id.toggle_tickets);
//        toggle.setChecked(filterManager.getTicketsAvailable());
//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    filterManager.setTicketsAvailable(true);
//                } else {
//                    filterManager.setTicketsAvailable(false);
//                }
//            }
//        });

        Button button = (Button) rootView.findViewById(R.id.btn_close);

        screenManager = new ScreenManager(getContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("Filter 1", String.valueOf(screenManager.getCurrentFragment()));
                //Intent myIntent = new Intent(getActivity(), MainActivity.class);
                //startActivity(myIntent);
                //screenManager.getCurrentFragment();

                ListingsFragment fragment2 = new ListingsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction =        fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
                fragmentTransaction.replace(R.id.container_body, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

//        NumberPicker np = (NumberPicker) rootView.findViewById(R.id.pricePicker);
//        np.setMaxValue(200);
//        np.setMinValue(0);
//
//        np.setValue(filterManager.getMaxPrice());
//        Log.d("fragment", String.valueOf(filterManager.getMaxPrice()));
//        np.setWrapSelectorWheel(true);
//        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
//                //Display the newly selected number from picker
//
//                Log.d("number", String.valueOf(newVal));
//                filterManager.setMaxPrice(newVal);
//                Log.d("fragment", String.valueOf(filterManager.getMaxPrice()));
//            }
//        });

        seekBarTextView = (TextView) rootView.findViewById(R.id.priceSliderValue);
        seekBarTextView.setText("£" + filterManager.getMaxPrice());

        seekBar = (SeekBar) rootView.findViewById(R.id.priceSlider);
        seekBar.setProgress(filterManager.getMaxPrice());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTextValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        genreTextView.setText(Html.fromHtml(gChosen));

        RecyclerView rvGenres = (RecyclerView) rootView.findViewById(R.id.rvGenres);
        tiles = getSampleArrayList();

        adapter = new GenreAdapter(tiles, new GenreAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Genre mGenre, int position, List<Genre> mGenres) {
                Log.d("Tile: ", mGenre.getName());
                Log.d("Tile Number: ", String.valueOf(position));
                String[] genresV = getResources().getStringArray(R.array.genres_values);
                String[] genres = getResources().getStringArray(R.array.genres);
                Set<String> searchGenresTemp = filterManager.getSearchGenres();
                String[] searchGenresTempArray = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);
                if (!mGenre.getName().equals(genres[0])) {
                    mGenre.setgSelected(!mGenre.getgSelected());
                } else {
                    if(!mGenre.getgSelected()) {
                        mGenre.setgSelected(!mGenre.getgSelected());
                    }
                }
                Log.d("genre", String.valueOf(mGenre.getgSelected()));
                if (mGenre.getgSelected()) {
                    if (mGenre.getName().equals(genres[0])) {
                        Log.d("mGenre Name", mGenre.getName() + " = " + genresV[0]);
                        searchGenresTemp = new HashSet<>(Arrays.asList(getContext().getResources().getStringArray(R.array.genres_values)));
                        filterManager.setSearchGenres(searchGenresTemp);
                        mGenres.clear();
                        for (int i=0; i<genresV.length; i++) {
                            //12
                                mGenres.add(new Genre(genres[i], true));
                                Log.d("search true", (genres[i]));
                                genreText = genreText + genres[i] + " ";

                        }
                       // adapter.setGenres(mGenres);
                        //searchGenresTemp.
                        //filterManager.setSearchGenres(genresV);

                    } else {
                        searchGenresTemp.add(genresV[position]);
                        String[] searchGenres = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);
                        Log.d("true", Arrays.toString(searchGenres));
                        filterManager.setSearchGenres(searchGenresTemp);
                    }
                } else {
                    if (searchGenresTemp.size()==genres.length) {
                    searchGenresTemp.clear();
                    searchGenresTemp.add(genresV[position]);
                    String[] searchGenres = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);
                    filterManager.setSearchGenres(searchGenresTemp);
                        mGenres.clear();
                        for (int i=0; i<genresV.length;) {
                            //12
                            for (String s : genresV) if (Arrays.asList(searchGenres).contains(s))
                            //12
                            {
                                mGenres.add(new Genre(genres[i], true));
                                Log.d("search true", (genres[i]));
                                genreText = genreText + genres[i] + " ";
                                i++;
                            }
                            else {
                                mGenres.add(new Genre(genres[i], false));
                                Log.d("search false", (genres[i]));
                                genreText = genreText + genres[i] + " ";
                                i++;
                            }
                            adapter.setGenres(mGenres);
                        }

                    } else {
                        searchGenresTemp.remove(genresV[position]);
                        String[] searchGenres = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);
                        Log.d("false", Arrays.toString(searchGenres));
                        filterManager.setSearchGenres(searchGenresTemp);
                    }
                }
                refresh();
            }

        });

        rvGenres.setAdapter(adapter);
        //rvGenres.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvGenres.setLayoutManager(new LinearLayoutManager(getContext()));

            return rootView;
    }

    public void updateTextValue(int progress) {
        Log.d("seekBarValue", String.valueOf(progress));
        if (progress==0) {
            seekBarTextView.setText("-");
        } else {
            seekBarTextView.setText("£" + progress);
        }
        filterManager.setMaxPrice(progress);
    }

    public void refresh() {

        adapter.notifyDataSetChanged();
        String[] genres = getResources().getStringArray(R.array.genres);
        Set<String> searchGenresTemp = filterManager.getSearchGenres();
        String[] searchGenres = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);

        String updated = "";
        if (searchGenres.length == genres.length) {
            updated = "All";
        } else if (searchGenres.length == 0) {
            updated = "None";
        }

        else {

            for (int i = 0; i < searchGenres.length; i++) {
                if (i < searchGenres.length - 1) {
                    updated = updated + sets.get(searchGenres[i]) + ", ";
                } else {
                    updated = updated + sets.get(searchGenres[i]);
                }
            }
        }
        genreTextView.setText(Html.fromHtml(gChosen + updated));
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("FilterFragment", "onAttach called");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("FilterFragment", "onDetach called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FilterFragment", "onPause called");

    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("FilterFragment", "onStop called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FilterFragment", "onResume called");
        //eventsRealmAdapter.notifyDataSetChanged();        //doesnt do anything...
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item=menu.findItem(R.id.action_mapview);
        item.setVisible(false);
        MenuItem item2=menu.findItem(R.id.action_listingsview);
        item2.setVisible(false);
        MenuItem item3=menu.findItem(R.id.action_search);
        item3.setVisible(false);
        MenuItem item4=menu.findItem(R.id.action_sync);
        item4.setVisible(false);
        MenuItem item5=menu.findItem(R.id.action_clear_cache);
        item5.setVisible(false);
        MenuItem item6=menu.findItem(R.id.action_options);
        item6.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            default:
                return false;
            // /return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Genre> getSampleArrayList() {

        items = new ArrayList<>();

        Resources res = getResources();
        String[] genres = res.getStringArray(R.array.genres);
        String[] genresV = res.getStringArray(R.array.genres_values);

        for(int i=0;i<genres.length;i++) {
            sets.put(genresV[i], genres[i]);
        }

        filterManager = new FilterManager(getContext());

        Set<String> searchGenresTemp = filterManager.getSearchGenres();
        String[] searchGenres = searchGenresTemp.toArray(new String[searchGenresTemp.size()]);

        Log.d("searchFilters", Arrays.toString(searchGenres));
        Log.d("searches", Arrays.toString(genres));

        for (int i=0; i<genresV.length;) {
            //12
            for (String s : genresV) if (Arrays.asList(searchGenres).contains(s))
            //12
            {
                items.add(new Genre(genres[i], true));
                Log.d("search true", (genres[i]));
                genreText = genreText + genres[i] + " ";
                i++;
            }
            else {
                items.add(new Genre(genres[i], false));
                Log.d("search false", (genres[i]));
                genreText = genreText + genres[i] + " ";
                i++;
            }
        }

        String updated = "";
        if (searchGenres.length == genres.length) {
            updated = "All";
        } else if (searchGenres.length == 0) {
            updated = "None";
        }

        else {

            for (int i = 0; i < searchGenres.length; i++) {
                if (i < searchGenres.length - 1) {
                    updated = updated + sets.get(searchGenres[i]) + ", ";
                } else {
                    updated = updated + sets.get(searchGenres[i]);
                }
            }
        }
        genreTextView.setText(Html.fromHtml(gChosen + updated));

        return items;

    }

}
