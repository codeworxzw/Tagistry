package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.pk.tagger.R;
import com.pk.tagger.realm.EventsAdapter;
import com.pk.tagger.recyclerview.Genre;
import com.pk.tagger.recyclerview.GenreAdapter;
import com.pk.tagger.services.DatabaseSyncService;

import java.util.ArrayList;

import butterknife.ButterKnife;
import io.realm.Realm;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    ArrayList<Genre> genres;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        Resources res = getResources();
        final String[] genresValues = res.getStringArray(R.array.genres_values);

        RecyclerView rvGenres = (RecyclerView) rootView.findViewById(R.id.rvGenres);
        genres = getSampleArrayList();

        GenreAdapter adapter = new GenreAdapter(genres, new GenreAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Genre mGenre, int position) {
                Log.d("Thing", mGenre.getName());
                Log.d("Thing 1", genresValues[position].toString());


                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container_body, new ListingsFragment(), "Event Listings");
                // Set title bar
                ((MainActivity) getActivity())
                        .setActionBarTitle("Event Listings");
                ft.commit();

            }
        });

        rvGenres.setAdapter(adapter);

        rvGenres.setLayoutManager(new GridLayoutManager(getContext(), 2));

       // sharedPref = getActivity().getSharedPreferences("com.pk.tagger.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
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
        String[] genres = res.getStringArray(R.array.genres);

        for (int i=0; i<genres.length; i++) {
            items.add(new Genre(genres[i]));
        }
        return items;

    }

}


