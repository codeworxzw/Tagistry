package com.pk.tagger.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pk.tagger.R;
import com.pk.tagger.realm.ResetRealm;
import com.pk.tagger.realm.TagData;
import com.pk.tagger.services.DatabaseStartService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private FragmentDrawer drawerFragment;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesDate;

    // Widget GUI
    Button btnCalendar, btnTimePicker;
    EditText txtDate, txtTime;

    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // display the first navigation drawer view on app launch
        displayView(0);
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
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_calendar){


            sharedPreferencesDate = getSharedPreferences("DateFilter", Context.MODE_PRIVATE);

            if (sharedPreferencesDate.contains("Date")) {

                Calendar calendar = new GregorianCalendar();

                calendar.setTimeInMillis(sharedPreferencesDate.getLong("Date", 0));

                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                Log.d("Hello yes", calendar.getTime().toString());

            } else {

                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                Log.d("Hello no", c.toString());

            };

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                            sharedPreferencesDate = getSharedPreferences("DateFilter", Context.MODE_PRIVATE);

                            Calendar calendarDate = new GregorianCalendar();
                            calendarDate.set(mYear, mMonth, mDay);
                            SharedPreferences.Editor editor = sharedPreferencesDate.edit();
                            editor.putLong("Date", calendarDate.getTimeInMillis());
                            editor.commit();

                            Calendar calendar = new GregorianCalendar();
                            calendar.setTimeInMillis(sharedPreferencesDate.getLong("Date", 0));

                            Log.d("Date in Set Date", calendar.getTime().toString());

                        }
                    },   mYear, mMonth, mDay);

            //dpd.updateDate(mYear, mMonth, mDay);
            dpd.show();


            return true;
        }

        if(id == R.id.action_options){
            Toast.makeText(getApplicationContext(), "Options action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(id == R.id.action_search){
            Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_clear_cache){



            return false;
        }

        if(id == R.id.action_sync){

            DatabaseStartService.startActionDownload(this, "hello", "hello");
            return true;
        }

        return false;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new TagsFragment();
                title = getString(R.string.title_tags);
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
            getSupportActionBar().setTitle(title);
        }
    }

    public Fragment getVisibleFragment(){
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

}
