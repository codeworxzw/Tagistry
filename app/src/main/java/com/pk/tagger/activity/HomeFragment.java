package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pk.tagger.R;
import com.pk.tagger.managers.FilterManager;
import com.pk.tagger.recyclerview.Genre;
import com.pk.tagger.recyclerview.GenreAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    ArrayList<Genre> tiles;

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
        final FilterManager filterManager = new FilterManager(getContext());
        Resources res = getResources();
        //final String[] genresValues = res.getStringArray(R.array.genres_values);

        RecyclerView rvGenres = (RecyclerView) rootView.findViewById(R.id.rvGenres);
        tiles = getSampleArrayList();

        GenreAdapter adapter = new GenreAdapter(tiles, new GenreAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Genre mGenre, int position) {
                Log.d("Tile: ", mGenre.getName());
                Log.d("Tile Number: ", String.valueOf(position));

                final FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (position == 0) {
                    filterManager.setOneWeek();
                    ft.replace(R.id.container_body, new ListingsFragment(), "Event Listings");
                    // Set title bar
                    ((MainActivity) getActivity())
                            .setActionBarTitle("Event Listings");
                    ft.commit();
                }
                else if (position == 1) {
                    filterManager.setOneMonth();
                    ft.replace(R.id.container_body, new ListingsFragment(), "Event Listings");
                    // Set title bar
                    ((MainActivity) getActivity())
                            .setActionBarTitle("Event Listings");
                    ft.commit();
                }
                else if (position == 2) {
                    Toast.makeText(getContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
                }
                else if (position == 3) {
                    filterManager.setFestivals();
                    ft.replace(R.id.container_body, new ListingsFragment(), "Event Listings");
                    // Set title bar
                    ((MainActivity) getActivity())
                            .setActionBarTitle("Event Listings");
                    ft.commit();
                }
                else if (position == 4) {
                    ft.replace(R.id.container_body, new GenreFragment(), "Genres");
                    // Set title bar
                    ((MainActivity) getActivity())
                            .setActionBarTitle("Genres");
                    ft.commit();
                }
                else if (position == 5) {
                    Toast.makeText(getContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ft.replace(R.id.container_body, new ListingsFragment(), "Event Listings");
                    // Set title bar
                    ((MainActivity) getActivity())
                            .setActionBarTitle("Event Listings");
                    ft.commit();
                }
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
        String[] genres = res.getStringArray(R.array.homepage);

        for (int i=0; i<genres.length; i++) {
            items.add(new Genre(genres[i]));
        }
        return items;

    }

}


