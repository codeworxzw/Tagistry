package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pk.tagger.R;
import com.pk.tagger.managers.FilterManager;
import com.pk.tagger.recyclerview.Genre;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    ArrayList<Genre> tiles;
    ViewPager mPager;
    MyAdapter mAdapter;
    String[] TAB_ITEMS;
    static final int NUM_ITEMS = 4;

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
        TAB_ITEMS = getResources().getStringArray(R.array.homepage);
       /* final FilterManager filterManager = new FilterManager(getContext());
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
                    filterManager.setTennerorLess();
                    ft.replace(R.id.container_body, new ListingsFragment(), "Event Listings");
                    // Set title bar
                    ((MainActivity) getActivity())
                            .setActionBarTitle("Event Listings");
                    ft.commit();
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

       // sharedPref = getActivity().getSharedPreferences("com.pk.tagger.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE); */

        // Inflate the layout for this fragment


        mAdapter = new MyAdapter(getFragmentManager(), getContext());

        mPager = (ViewPager)rootView.findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        // Watch for button clicks.bv
//        Button button = (Button)rootView.findViewById(R.id.goto_first);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mPager.setCurrentItem(0);
//            }
//        });
//        button = (Button)rootView.findViewById(R.id.goto_second);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mPager.setCurrentItem(NUM_ITEMS-3);
//            }
//        });
//        button = (Button)rootView.findViewById(R.id.goto_third);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mPager.setCurrentItem(NUM_ITEMS-2);
//            }
//        });
//        button = (Button)rootView.findViewById(R.id.goto_fourth);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mPager.setCurrentItem(NUM_ITEMS-1);
//            }
//        });

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("This Week"));
        tabLayout.addTab(tabLayout.newTab().setText("This Month"));
        tabLayout.addTab(tabLayout.newTab().setText("Festivals"));
        tabLayout.addTab(tabLayout.newTab().setText("Tenner or Less"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //mPager.setOffscreenPageLimit(0);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FilterManager filterManager = new FilterManager(getContext());

                switch (tab.getPosition()) {
                    case 0:
                        filterManager.setOneWeek();

                        break;
                    case 1:
                        filterManager.setOneMonth();

                        break;
                    case 2:
                        filterManager.setFestivals();

                        break;
                    case 3:
                        filterManager.setTennerorLess();

                        break;
                    default:
                        break;
                }
                mPager.setCurrentItem(tab.getPosition());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }

    public static class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {

            ListingsFragment listingsFragment = new ListingsFragment();
            //Bundle args = new Bundle();
            //args.putBoolean("fab", true);
            //listingsFragment.setArguments(args);
            return listingsFragment;
        }

        //this is called when notifyDataSetChanged() is called
        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return POSITION_NONE;
        }



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


