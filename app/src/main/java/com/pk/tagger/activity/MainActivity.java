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
import com.pk.tagger.realm.ResetRealm;
import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.event.Event;
import com.pk.tagger.realm.venue.Venue;
import com.pk.tagger.services.DatabaseStartPaginatedService;

import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private FragmentDrawer drawerFragment;
    private Realm myRealm;


    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesDate;

    // Widget GUI
    Button btnCalendar, btnTimePicker;
    EditText txtDate, txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPrefsCurrentFragment = getSharedPreferences("FragmentView", Context.MODE_PRIVATE);

        sharedPreferences = getSharedPreferences("TimeStamp", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("time")) {

            Date myDate = new Date(sharedPreferences.getLong("time", 0));

            Log.d("Date in Mainactivity", myDate.toString());
           // DatabaseStartService.startActionDownload(this, "hello", "hello");
        }

        else {
          //  DatabaseStartService.startActionBaz(this, "hello", "hello");
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        //inflate the drawerFragment using the recyclerview fragment
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);



        int test = sharedPrefsCurrentFragment.getInt("fragment", 0);
        Log.d("Filter MainActivty Test", String.valueOf(test));

        // display the first navigation drawer view on app launch
        if (sharedPrefsCurrentFragment.contains("fragment")) {

            displayView(sharedPrefsCurrentFragment.getInt("fragment", 0));
        } else {
            displayView(0);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {

            return true;
        }  */

        if(id == R.id.action_options){
            Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
            intent.putExtra("TAG", getVisibleFragment().getTag());
            startActivity(intent);
            Log.d("MainActivity", "options selected");
            Log.i("Open Fragment", getVisibleFragment().getTag());
            return true;
        }

  /*      if(id == R.id.action_search){
            //Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
            //startActivity(intent);
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();

            return true;
        } */

        if(id == R.id.action_clear_cache){
            myRealm = Realm.getInstance(getApplicationContext());
            myRealm.beginTransaction();
            myRealm.clear(Event.class);
            myRealm.clear(Artist.class);
            myRealm.clear(Venue.class);
            myRealm.commitTransaction();
            myRealm.close();
//            ResetRealm.resetRealm(this);
            return true;
        }

        if(id == R.id.action_sync){

            DatabaseStartPaginatedService.startActionDownload(this, "hello", "hello");
            Log.d("MainActivity", "Sync service started");
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
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new ListingsFragment();
                title = getString(R.string.title_listings);
                break;
            case 2:
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
