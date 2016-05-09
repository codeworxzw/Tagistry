package com.pk.tagger.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pk.tagger.AppController;
import com.pk.tagger.Fx;
import com.pk.tagger.R;
import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.event.Event;
import com.pk.tagger.realm.venue.Venue;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by PK on 29/02/2016.
 */
//public class EventDetailActivity extends AppCompatActivity implements View.OnTouchListener{
public class EventDetailActivity extends AppCompatActivity {

    private static String TAG = EventDetailActivity.class.getSimpleName();

    private Realm myRealm;
    SharedPreferences sharedPref;

    // temporary string to show the parsed response
    private String jsonResponse;
    private static final String BASE_QUERY_URL = "http://52.31.31.106:9000/api/event/";
    private static final String BASE_QUERY_URL_ARTIST = "http://52.31.31.106:9000/api/artist/";
    private static final String BASE_QUERY_URL_VENUE = "http://52.31.31.106:9000/api/venue/";

    private int previousFingerPosition = 0;
    private int baseLayoutPosition = 0;
    private int defaultViewWidth;

    private boolean isClosing = false;
    private boolean isScrollingLeft = false;
    private boolean isScrollingRight = false;

    private float anchorLeft = 0;

    @Bind(R.id.base_popup_layout) LinearLayout _baseLayout;
    @Bind(R.id.imageView_event) ImageView _event_image;
    @Bind(R.id.textView_event_title) TextView _event_title;
    @Bind(R.id.textView_description) TextView _event_description;
    @Bind(R.id.textView_event_date) TextView _event_date;
    @Bind(R.id.textView_event_venue) TextView _event_venue;
    @Bind(R.id.textView_event_tickets_price) TextView _event_tickets_price;
    @Bind(R.id.textView_event_tickets_buy) TextView _event_tickets_buy;
    @Bind(R.id.imageButton_close) ImageButton _close;
    @Bind(R.id.tab_summary) TextView _tab_summary;
    @Bind(R.id.tab_artist) TextView _tab_artist;
    @Bind(R.id.tab_venue) TextView _tab_venue;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        sharedPref = getApplicationContext().getSharedPreferences("PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        String eventID = "1038341"; //default should return 'Katie Melua' event
        String artistID = "";
        String venueID = "";
        if (extras != null) {
            eventID = extras.getString("EventID");
        }
       // _tab_summary.setBackgroundColor(Color.parseColor("#e6e6e6"));

//        _baseLayout.setOnTouchListener(this);

//        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) _baseLayout.getLayoutParams();
//        anchorLeft = _baseLayout.getLeft() + lp.leftMargin;

        myRealm = Realm.getDefaultInstance();
        final Event event = myRealm
                .where(Event.class)
                .equalTo("id", eventID)
                .findFirst();

        final Artist artist = myRealm
                .where(Artist.class)
                .equalTo("id", event.getArtist().getId())
                .findFirst();
        Log.d("Artist", artist.toString());
        final Venue venue = myRealm
                .where(Venue.class)
                .equalTo("id", event.getVenue().getId())
                .findFirst();
        Log.d("Venue", venue.toString());

        artistID = artist.getId();
        venueID = venue.getId();

        Log.d("Event", event.getArtist().getName());
        Log.d("Partial Data", event.toString());

        _event_title.setText(event.getArtist().getName());
        _event_description.setVisibility(View.GONE);
        String description = "";
        description = artist.getDescription();        //add back in when full artist data request has been added
        _event_description.setText(description);
        //TODO: should probably parse date properly not just truncate string lol
        _event_date.setText(event.getStartTime().getLocal().toString().substring(0, 16));
        _event_venue.setText(event.getVenue().getName());

        String tickets = "Tickets Unavailable";
        try {
            if(event.getTickets().getTicket_count()!=0){
                tickets = "Tickets from: £" + String.valueOf(event.getTickets().getPurchase_price());
            }
        } catch(Exception e){
            Log.d("Catch tickets", "No tickets");
        }
        _event_tickets_price.setText(tickets);

        _event_tickets_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (event.getUrl() != null) {
                        Toast.makeText(getApplicationContext(), "Show me dem tickets!", Toast.LENGTH_SHORT).show();
                        Log.d("Buying", event.getUrl());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUrl()));
                        startActivity(browserIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "No ticket link :(", Toast.LENGTH_SHORT).show();
                        Log.d("Else", event.getUrl());
                    }
                }catch(Exception e){
                    Log.d("Catch", e.toString());
                }
            }
        });

        String IMAGE_URL = "";
        try{
            if(event.getImage_URL() != null) {
                IMAGE_URL = event.getImage_URL();
            } else if (event.getArtist().getImage_URL() != null){
                IMAGE_URL = event.getArtist().getImage_URL();
            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }
        Picasso.with(getApplicationContext())
                .load(IMAGE_URL)
                .placeholder(R.drawable.ic_profile)
                .into(_event_image);

        //Check if realm already has full event data (via EventURL), if not, make volley request
        try{
            if(event.getUrl()==null){           //TODO: needs to update to get latest ticket info, maybe a timestamp check to be added/separate api route for ticket info
                getFullEvent(eventID);
                Log.d(TAG, "Requesting full data...");
            } else {
                Log.d(TAG, "Already has full data");
            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }

        try{
            if(event.getArtist().getDescription()==null){         //check if full artist data already in realm
                getFullArtist(artistID);
                Log.d(TAG, "Requesting full artist data...");
            } else {
                Log.d(TAG, "Already has full artist data");
            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }

        try{
            if(event.getVenue().getSw_website()==null){         //check if full venue data already in realm
                getFullVenue(venueID);
                Log.d(TAG, "Requesting full venue data...");
            } else {
                Log.d(TAG, "Already has full venue data");
            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }

        _close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void toggle_description(View v){
        if(_event_description.isShown()){
            Fx.slide_up(this, _event_description);              //slide up animation not working
            _event_description.setVisibility(View.GONE);
        }
        else{
            _event_description.setVisibility(View.VISIBLE);
            Fx.slide_down(this, _event_description);
        }
    }

    public void toggle_summary(View v) {
        //Toast.makeText(this, "Summary selected", Toast.LENGTH_SHORT).show();
        _tab_summary.setBackgroundColor(Color.parseColor("#cccccc"));
        _tab_artist.setBackgroundColor(Color.parseColor("#FFFFFF"));
        _tab_venue.setBackgroundColor(Color.parseColor("#FFFFFF"));

    }
    public void toggle_artist(View v) {
       //Toast.makeText(this, "Artist selected", Toast.LENGTH_SHORT).show();
        _tab_summary.setBackgroundColor(Color.parseColor("#FFFFFF"));
        _tab_artist.setBackgroundColor(Color.parseColor("#cccccc"));
        _tab_venue.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }
    public void toggle_venue(View v) {
        //Toast.makeText(this, "Venue selected", Toast.LENGTH_SHORT).show();
        _tab_summary.setBackgroundColor(Color.parseColor("#FFFFFF"));
        _tab_artist.setBackgroundColor(Color.parseColor("#FFFFFF"));
        _tab_venue.setBackgroundColor(Color.parseColor("#cccccc"));
    }
/*
    public boolean onTouch(View view, MotionEvent event) {

        // Get finger position on screen
        final int X = (int) event.getRawX();

        // Switch on motion event type
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                // save default base layout height
                defaultViewWidth = _baseLayout.getWidth();

                // Init finger and view position
                previousFingerPosition = X;
                baseLayoutPosition = (int) _baseLayout.getX();
                break;

            case MotionEvent.ACTION_UP:
                // If user was doing a scroll up
                if(isScrollingLeft){
                    // Reset baselayout position
                    _baseLayout.setX(anchorLeft);
                    // We are not in scrolling up mode anymore
                    isScrollingLeft = false;
                }

                // If user was doing a scroll down
                if(isScrollingRight){
                    // Reset baselayout position
                    _baseLayout.setX(anchorLeft);
                    // Reset base layout size
                    //_baseLayout.getLayoutParams().width = defaultViewWidth;
                    //_baseLayout.requestLayout();
                    // We are not in scrolling down mode anymore
                    isScrollingRight = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!isClosing){
                    int currentXPosition = (int) _baseLayout.getX();

                    // If we scroll up
                    if(previousFingerPosition > X){
                        // First time android rise an event for "up" move
                        if(!isScrollingLeft){
                            isScrollingLeft = true;
                        } else {
                            // Has user scroll enough to "auto close" popup ?
                            if ((baseLayoutPosition - currentXPosition) > defaultViewWidth / 4) {
                                closeLeftAndDismissDialog(currentXPosition);
                                return true;
                            }

                        }
                        _baseLayout.setX(_baseLayout.getX() + (X - previousFingerPosition));

                    }
                    // If we scroll down
                    else if (previousFingerPosition < X){

                        // First time android rise an event for "down" move
                        if(!isScrollingRight){
                            isScrollingRight = true;
                        } else {
                            // Has user scroll enough to "auto close" popup ?
                            if ((currentXPosition - baseLayoutPosition) > defaultViewWidth / 4) {
                                closeRightAndDismissDialog(currentXPosition);
                                return true;
                            }
                        }

                        // Change base layout size and position (must change position because view anchor is top left corner)
                        _baseLayout.setX(_baseLayout.getX() + (X - previousFingerPosition));
                        //_baseLayout.getLayoutParams().width = _baseLayout.getWidth() - (X - previousFingerPosition);
                        //_baseLayout.requestLayout();
                    }

                    // Update position
                    previousFingerPosition = X;
                }
                break;
        }
        return true;

    }

    public void closeLeftAndDismissDialog(int currentPosition){
        isClosing = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(_baseLayout, "x", currentPosition, -_baseLayout.getWidth());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        positionAnimator.start();
    }

    public void closeRightAndDismissDialog(int currentPosition){
        isClosing = true;
        //Display display = getWindowManager().getDefaultDisplay();
        //Point size = new Point();
        //display.getSize(size);
        //int screenWidth = size.x;
        //ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(_baseLayout, "x", currentPosition, screenWidth + _baseLayout.getWidth());
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(_baseLayout, "x", currentPosition, _baseLayout.getWidth());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        positionAnimator.start();

    }
*/
    private void getFullEvent(String id) {

        final String eventID = id;
        final String FULL_QUERY_URL = BASE_QUERY_URL + eventID;
        Log.d("Querying", FULL_QUERY_URL);

        JsonObjectRequest req = new JsonObjectRequest(FULL_QUERY_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        try {

                            myRealm.beginTransaction();
                            myRealm.createOrUpdateObjectFromJson(Event.class, response);
                            myRealm.commitTransaction();

                            //update remaining data fields for event (i.e. data not already in Listings)
                            updateEventData(eventID);

                        } catch (Exception e) {
                            Log.d("Error updating", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                String defaultValue = getResources().getString(R.string.access_token);
                String access_token = sharedPref.getString(getString(R.string.access_token), defaultValue);

                headers.put("Authorization", "Bearer " + access_token);
                Log.d("Headers", headers.toString());
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void getFullArtist (String id) {

        final String artistID = id;
        final String FULL_QUERY_URL = BASE_QUERY_URL_ARTIST + artistID;
        Log.d("Querying", FULL_QUERY_URL);

        JsonObjectRequest req = new JsonObjectRequest(FULL_QUERY_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        try {

                            myRealm.beginTransaction();
                            myRealm.createOrUpdateObjectFromJson(Artist.class, response);
                            myRealm.commitTransaction();

                            //update remaining data fields for artist (i.e. data not already in realm)
                            updateArtistData(artistID);

                        } catch (Exception e) {
                            Log.d("Error updating", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                String defaultValue = getResources().getString(R.string.access_token);
                String access_token = sharedPref.getString(getString(R.string.access_token), defaultValue);

                headers.put("Authorization", "Bearer " + access_token);
                Log.d("Headers", headers.toString());
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void getFullVenue (String id) {

        final String venueID = id;
        final String FULL_QUERY_URL = BASE_QUERY_URL_VENUE + venueID;
        Log.d("Querying", FULL_QUERY_URL);

        JsonObjectRequest req = new JsonObjectRequest(FULL_QUERY_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        try {

                            myRealm.beginTransaction();
                            myRealm.createOrUpdateObjectFromJson(Venue.class, response);
                            myRealm.commitTransaction();

                            //update remaining data fields for venue (i.e. data not already in realm)
                            updateVenueData(venueID);

                        } catch (Exception e) {
                            Log.d("Error updating", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                String defaultValue = getResources().getString(R.string.access_token);
                String access_token = sharedPref.getString(getString(R.string.access_token), defaultValue);

                headers.put("Authorization", "Bearer " + access_token);
                Log.d("Headers", headers.toString());
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void updateEventData(String eventID){
        final Event event = myRealm
                .where(Event.class)
                .equalTo("id", eventID)
                .findFirst();
        Log.d("Updated full event", event.toString());

        //update the remaining fields with the new volley data
        //_event_title.setText("Test");

    }
    public void updateArtistData(String artistID){
        final Artist artist = myRealm
                .where(Artist.class)
                .equalTo("id", artistID)
                .findFirst();
        Log.d("Updated full artist", artist.toString());

        //update the remaining fields with the new volley data
        _event_description.setText(artist.getDescription());

    }
    public void updateVenueData(String venueID){
        final Venue venue = myRealm
                .where(Venue.class)
                .equalTo("id", venueID)
                .findFirst();
        Log.d("Updated full venue", venue.toString());

        //update the remaining fields with the new volley data
        //_event_title.setText("Test");

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        myRealm.close();
    }
}