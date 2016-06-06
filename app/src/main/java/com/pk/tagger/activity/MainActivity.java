package com.pk.tagger.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    private static final int PROFILE_SETTING = 1;
   // private FragmentDrawer drawerFragment;
    private Realm myRealm;
    private boolean isChecked = false;
    // Startup Manager
    StartUpManager startUpManager;
    // Session Manager Class
    SessionManager session;
    //Filter Manager Class
    FilterManager filterManager;
    //Screen Manager Class
    ScreenManager screenManager;
    private IProfile profile;
    // Widget GUI
    private AccountHeader headerResult = null;
    Button btnCalendar, btnTimePicker;
    EditText txtDate, txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        startUpManager = new StartUpManager(getApplicationContext());
        startUpManager.setUpApp();

        screenManager = new ScreenManager(getApplicationContext());

        // Session class instance
        session = new SessionManager(getApplicationContext());

        profile = new ProfileDrawerItem().withName("Log In").withIcon(getResources().getDrawable(R.drawable.ic_profile)).withIdentifier(PROFILE_SETTING);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(
                        profile
                      //  new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add Account").withIdentifier(PROFILE_SETTING)
                ).withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {

                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            //IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.ic_profile));
                            if (headerResult.getProfiles() != null) {
                                displayView(0);
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                //headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 1);
                            } else {
                                displayView(0);
                               // headerResult.addProfiles(newProfile);
                            }
                        }
                        displayView(0);
                        return false;
                    }
                })
                .build();

        if (session.isLoggedIn()) {
                HashMap<String, String> user = session.getUserDetails();
                String email = user.get(SessionManager.KEY_EMAIL);
            headerResult.removeProfileByIdentifier(PROFILE_SETTING);
            //headerResult.addProfile(new ProfileDrawerItem().withEmail(email).withIcon(getResources().getDrawable(R.drawable.ic_profile)), headerResult.getProfiles().size() - 1);
            // headerResult.addProfiles(new ProfileSettingDrawerItem().withName("View Account").withDescription("View Account").withIdentifier(PROFILE_SETTING));
            headerResult.addProfiles(new ProfileDrawerItem().withEmail(email).withIcon(getResources().getDrawable(R.drawable.ic_profile)).withIdentifier(PROFILE_SETTING));
        } else {
                //headerResult.addProfile(profile, headerResult.getProfiles().size() - 1);
            }

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.title_home);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName(R.string.title_my_gigfm);
       // PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName(R.string.title_map);

        SecondaryDrawerItem item4 = new SecondaryDrawerItem();
        item4.withName(R.string.action_publish_event);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem();
        item5.withName(R.string.action_settings);

//create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        item2,
                      //  item3,
                        new DividerDrawerItem(),
                        item4,
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("clicked: ", String.valueOf(position));
                        switch (position) {

                            case 1:
                                displayView(2);     //Listings
                                break;
                            case 2:
                                displayView(1);     //Home
                                break;
                            case 4:
                                displayView(4);     //Publish Event
                                break;
                            case 5:
                                Toast.makeText(getApplicationContext(),"Settings clicked", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                })
                .build();

        myRealm = Realm.getDefaultInstance();
        addNewUser();                                   // creates a test user (id: 0001, username: Michael)

        //session.checkLogin();


//        //inflate the drawerFragment using the recyclerview fragment
//        drawerFragment = (FragmentDrawer)
//                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
//        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
//        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        if (screenManager.getCurrentFragment()!=-1) {
            displayView(screenManager.getCurrentFragment());
        } else {
            displayView(2);
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
            //Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
            //intent.putExtra("TAG", getVisibleFragment().getTag());
            //screenManager.setCurrentFragment(getVisibleFragment().getTag());
//            startActivity(intent);
//            Log.d("MainActivity", "options selected");
//            Log.i("Open Fragment", getVisibleFragment().getTag());
//            Log.i("Open Fragment", String.valueOf(screenManager.getCurrentFragment()));
            displayView(5);
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

//            DatabaseStartServiceEvent.startActionDownload(this, "1005164", "hello");

            DatabaseStartPaginatedServiceEvents.startActionDownload(this, "hello", "hello");      //issues running all three together
            DatabaseStartPaginatedServiceArtists.startActionDownload(this, "hello", "hello");
            DatabaseStartPaginatedServiceVenues.startActionDownload(this, "hello", "hello");

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
//            final Artist artist = myRealm
//                    .where(Artist.class)
//                    .equalTo("id", "53877")
//                    .findFirst();
//            Log.d("Artist", artist.toString());

            String url = "https://www.google.com/";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.primary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));

            return true;
        }

        if(id == R.id.action_listingsview){

            displayView(2);
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
                fragment = new MyGigFMFragment();
                //title = getString(R.string.title_home);
                title = getString(R.string.title_my_gigfm);
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
            case 5:
                fragment = new FilterFragment();
                title = getString(R.string.title_filters);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (title == getString(R.string.title_filters)) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
            } else if (title == getString(R.string.title_listings)) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
            }
            fragmentTransaction.replace(R.id.container_body, fragment, title);

            fragmentTransaction.addToBackStack(title);

            fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if(getSupportFragmentManager().getBackStackEntryCount() == 0) finish();
                }
            });

            fragmentTransaction.commit();


            // set the toolbar title
//            setActionBarTitle(title);
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
//        getSupportActionBar().setTitle(title);
    }

    //create new dummy user to save starred to, TODO: replace with logged in user when working
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}
