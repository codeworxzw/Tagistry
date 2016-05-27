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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.pk.tagger.R;
import com.pk.tagger.managers.FilterManager;
import com.pk.tagger.managers.ScreenManager;
import com.pk.tagger.recyclerview.Genre;
import com.pk.tagger.recyclerview.GenreAdapter;

import java.util.ArrayList;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;

/**
 * Created by pk on 26/05/16.
 */
public class FilterFragment extends Fragment {

    FilterManager filterManager;

    ScreenManager screenManager;

    ArrayList<Genre> tiles;

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

        filterManager = new FilterManager(getContext());
        ToggleButton toggle = (ToggleButton) rootView.findViewById(R.id.toggle_tickets);
        toggle.setChecked(filterManager.getTicketsAvailable());
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filterManager.setTicketsAvailable(true);
                } else {
                    filterManager.setTicketsAvailable(false);
                }
            }
        });

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

        NumberPicker np = (NumberPicker) rootView.findViewById(R.id.pricePicker);
        np.setMaxValue(200);
        np.setMinValue(0);
        np.setValue(filterManager.getMaxPrice());
        Log.d("fragment", String.valueOf(filterManager.getMaxPrice()));
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                Log.d("number", String.valueOf(newVal));
                filterManager.setMaxPrice(newVal);
                Log.d("fragment", String.valueOf(filterManager.getMaxPrice()));
            }
        });

        RecyclerView rvGenres = (RecyclerView) rootView.findViewById(R.id.rvGenres);
        tiles = getSampleArrayList();

        GenreAdapter adapter = new GenreAdapter(tiles, new GenreAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Genre mGenre, int position) {
                Log.d("Tile: ", mGenre.getName());
                Log.d("Tile Number: ", String.valueOf(position));

                if (position == 0) {

                }
                else if (position == 1) {

                }
                else if (position == 2) {

                }
                else if (position == 3) {

                }
                else if (position == 4) {

                }
                else if (position == 5) {
                    Toast.makeText(getContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
                }
                else
                {

                }
            }
        });

        rvGenres.setAdapter(adapter);
        rvGenres.setLayoutManager(new GridLayoutManager(getContext(), 2));

            return rootView;
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

        ArrayList<Genre> items = new ArrayList<>();

        Resources res = getResources();
        String[] genres = res.getStringArray(R.array.homepage);

        for (int i=0; i<genres.length; i++) {
            items.add(new Genre(genres[i]));
        }
        return items;

    }

}
