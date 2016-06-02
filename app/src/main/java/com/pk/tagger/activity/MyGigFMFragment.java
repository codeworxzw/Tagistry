package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pk.tagger.R;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;


public class MyGigFMFragment extends Fragment {

    public MyGigFMFragment() {
        // Required empty public constructor
    }

    ViewPager mPager;
    MyAdapter mAdapter;
//    String[] TAB_ITEMS;
    TabLayout tabLayout;

    static final int NUM_ITEMS = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_gigfm, container, false);
        ButterKnife.bind(this, rootView);
//        TAB_ITEMS = getResources().getStringArray(R.array.homepage);

        // Inflate the layout for this fragment
        mAdapter = new MyAdapter(getFragmentManager(), getActivity());

        mPager = (ViewPager)rootView.findViewById(R.id.pager);
        mAdapter.setFragments(getActivity());
        mPager.setAdapter(mAdapter);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("This Week"));
        tabLayout.addTab(tabLayout.newTab().setText("This Month"));
        tabLayout.addTab(tabLayout.newTab().setText("Festivals"));
        tabLayout.addTab(tabLayout.newTab().setText("Cheapest"));
        tabLayout.addTab(tabLayout.newTab().setText("Starred"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mPager.setOffscreenPageLimit(1);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

//                switch (tab.getPosition()) {
//                    case 0:
////                        filterManager.setOneWeek();
//                        break;
//                    case 1:
////                        filterManager.setOneMonth();
//                        break;
//                    case 2:
////                        filterManager.setFestivals();
//                        break;
//                    case 3:
////                        filterManager.setTennerorLess();
//                    case 4:
//                        break;
//                    default:
//                        break;
//                }
                mPager.setCurrentItem(tab.getPosition());
//                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
//        filterManager.setOneWeek();
        mPager.setCurrentItem(0);
//        mAdapter.notifyDataSetChanged();
        return rootView;
    }

    public static class MyAdapter extends FragmentStatePagerAdapter {

        ListingsFragment frag1;
        ListingsFragment frag2;
        ListingsFragment frag3;
        ListingsFragment frag4;
        ListingsFragment frag5;

        public MyAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        public void setFragments(Context context){

            // Set up the simple base fragments
            frag1 = new ListingsFragment();
            frag2 = new ListingsFragment();
            frag3 = new ListingsFragment();
            frag4 = new ListingsFragment();
            frag5 = new ListingsFragment();

            Bundle args2 = new Bundle();
            args2.putString("query", "Festivals");
            frag3.setArguments(args2);

            Bundle args = new Bundle();
            args.putString("query", "Cheapest");
            frag4.setArguments(args);

            Bundle args3 = new Bundle();
            args3.putString("query", "Starred");
            frag5.setArguments(args3);

        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment frag;
            switch(position){
                case 0:
                    frag = frag1;
                    break;
                case 1:
                    frag = frag2;
                    break;
                case 2:
                    frag = frag3;
                    break;
                case 3:
                    frag = frag4;
                    break;
                case 4:
                    frag = frag5;
                    break;
                default:
                    frag = frag1;
            }

            return frag;
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
        Log.d("MyGigFMFragment", "onAttach Called");
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
        MenuItem item=menu.findItem(R.id.action_mapview);
        item.setVisible(true);
        MenuItem item2=menu.findItem(R.id.action_listingsview);
        item2.setVisible(false);

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

}


