package com.pk.tagger.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pk.tagger.R;
import com.pk.tagger.managers.FilterManager;
import com.pk.tagger.managers.ScreenManager;
import com.pk.tagger.managers.SessionManager;
import com.pk.tagger.managers.StartUpManager;
import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.event.Event;
import com.pk.tagger.realm.user.User;
import com.pk.tagger.realm.venue.Venue;
import com.pk.tagger.services.DatabaseStartPaginatedServiceEvents;
import com.pk.tagger.services.DatabaseStartPaginatedServiceArtists;
import com.pk.tagger.services.DatabaseStartPaginatedServiceVenues;
import com.pk.tagger.services.DatabaseStartServiceEvent;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;

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
        myRealm = Realm.getDefaultInstance();
        addNewUser();                                   // creates a test user (id: 0001, username: Michael)
        startUpManager = new StartUpManager(getApplicationContext());
        startUpManager.setUpApp();

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

            new AlertDialog.Builder(this)
                    //.setTitle("Title")
                    .setMessage("Warning: This will clear everything!")
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            myRealm = Realm.getDefaultInstance();
                            myRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    myRealm.delete(Event.class);
                                    myRealm.delete(Artist.class);
                                    myRealm.delete(Venue.class);
                                    Toast.makeText(MainActivity.this, "Cleared!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            myRealm.close();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();

            return true;
        }

        if(id == R.id.action_sync){

            DatabaseStartPaginatedServiceEvents.startActionDownload(this, "hello", "hello");
            DatabaseStartPaginatedServiceVenues.startActionDownload(this, "hello", "hello");
            DatabaseStartPaginatedServiceArtists.startActionDownload(this, "hello", "hello");

            Log.d("MainActivity", "Sync service started");
            return true;
        }

        if(id == R.id.send_feedback){

//            Intent i = new Intent(Intent.ACTION_SEND);
//            i.setType("message/rfc822");
//            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"elasmolabs.feedback@gmail.com"});
//            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
//            try {
//                startActivity(Intent.createChooser(i, "Send mail..."));
//            } catch (android.content.ActivityNotFoundException ex) {
//                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//            }

//            getUserStarredEventsArray();

            return true;
        }

        if(id == R.id.action_listingsview){

            displayView(1);
            return true;
        }

        if(id == R.id.action_mapview){

            displayView(3);
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
            case 4:
                Intent intent = new Intent(getApplicationContext(), SendEventActivity.class);
                startActivity(intent);
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



    public void addNewUser(){
        try{
            User user = myRealm.where(User.class).findFirst();
            Log.d("User", "Already exists: " +user.getUsername());

        } catch (Exception e){
            User newUser = new User();
            newUser.setId("0001");
            newUser.setUsername("Michael");

            myRealm.beginTransaction();
            myRealm.copyToRealmOrUpdate(newUser);
            myRealm.commitTransaction();
            Log.d("User", "Created new user: " +newUser.getUsername());

        }

    }

    public void getUserStarredEventsArray(){
        User user = myRealm.where(User.class).findFirst();

        Log.d("Id", user.getId());
        Log.d("Username", user.getUsername());

        Log.d("StarredEvents", Arrays.toString(user.getStarredEventsArray()));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}
