package com.pk.tagger.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.pk.tagger.R;
import com.pk.tagger.managers.FilterManager;
import com.pk.tagger.managers.ScreenManager;
import com.pk.tagger.managers.SessionManager;
import com.pk.tagger.managers.StartUpManager;
import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.event.Event;
import com.pk.tagger.realm.venue.Venue;
import com.pk.tagger.services.DatabaseStartPaginatedService;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private FragmentDrawer drawerFragment;
    private Realm myRealm;

    // Startup Manager
    StartUpManager startUpManager;
    // Session Manager Class
    SessionManager session;
    //Filter Manager Class
    FilterManager filterManager;
    //Screen Manager Class
    ScreenManager screenManager;

    // Widget GUI
    Button btnCalendar, btnTimePicker;
    EditText txtDate, txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startUpManager = new StartUpManager(getApplicationContext());
        startUpManager.setUpApp();
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("gigit.realm")
                //.schemaVersion(0)
                //.migration(new MyRealmMigration()) // Migration to run instead of throwing an exception
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        screenManager = new ScreenManager(getApplicationContext());

        // Session class instance
        session = new SessionManager(getApplicationContext());
        //session.checkLogin();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        //inflate the drawerFragment using the recyclerview fragment
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        if (screenManager.getCurrentFragment()!=-1) {
            displayView(screenManager.getCurrentFragment());
        } else {
            displayView(1);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_options){
            Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
            intent.putExtra("TAG", getVisibleFragment().getTag());
            screenManager.setCurrentFragment(getVisibleFragment().getTag());
            startActivity(intent);
            Log.d("MainActivity", "options selected");
            Log.i("Open Fragment", getVisibleFragment().getTag());
            Log.i("Open Fragment", String.valueOf(screenManager.getCurrentFragment()));
            return true;
        }

  /*      if(id == R.id.action_search){
            //Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
            //startActivity(intent);
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();

            return true;
        } */

        if(id == R.id.action_clear_cache){
            myRealm = Realm.getDefaultInstance();
            myRealm.beginTransaction();
            myRealm.clear(Event.class);
            myRealm.clear(Artist.class);
            myRealm.clear(Venue.class);
            myRealm.commitTransaction();

//            Log.d("MainActivity", myRealm.getPath());
            myRealm.close();
            return true;
        }

        if(id == R.id.action_sync){

            DatabaseStartPaginatedService.startActionDownload(this, "hello", "hello");
            Log.d("MainActivity", "Sync service started");
            return true;
        }

        if(id == R.id.report_issue){
            return true;
        }

        return false;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

   final public void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                if (session.isLoggedIn()) {
                    Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            case 1:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 2:
                fragment = new ListingsFragment();
                title = getString(R.string.title_listings);
                break;
            case 3:
                fragment = new MapFragment();
                title = getString(R.string.title_map);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();

            // set the toolbar title
            setActionBarTitle(title);
        }
    }

    public final Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

}
