package com.pk.tagger.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
    private static final String BASE_QUERY_URL = "http://52.31.31.106:9000/api/event/";
    private static final String BASE_QUERY_URL_ARTIST = "http://52.31.31.106:9000/api/artist/";
    private static final String BASE_QUERY_URL_VENUE = "http://52.31.31.106:9000/api/venue/";

    String IMAGE_URL = "";
    String IMAGE_URL_VENUE = "";



//    private int previousFingerPosition = 0;
//    private int baseLayoutPosition = 0;
//    private int defaultViewWidth;
//
//    private boolean isClosing = false;
//    private boolean isScrollingLeft = false;
//    private boolean isScrollingRight = false;
//
//    private float anchorLeft = 0;

    @Bind(R.id.base_popup_layout) LinearLayout _baseLayout;
    @Bind(R.id.imageView_event) ImageView _event_image;
    @Bind(R.id.textView_summary_title) TextView _summary_title;
    @Bind(R.id.textView_summary_description) TextView _summary_description;
    @Bind(R.id.textView_summary_date) TextView _summary_date;
    @Bind(R.id.textView_summary_venue) TextView _summary_venue;
    @Bind(R.id.textView_summary_tickets_price) TextView _summary_tickets_price;
    @Bind(R.id.textView_summary_tickets_buy) TextView _summary_tickets_buy;
    @Bind(R.id.textView_artist_title) TextView _artist_title;
    @Bind(R.id.textView_artist_description) TextView _artist_description;
    @Bind(R.id.textView_artist_genre) TextView _artist_genre;
    @Bind(R.id.textView_artist_spotify) TextView _artist_spotify;
    @Bind(R.id.textView_artist_website_official) TextView _artist_website_official;
    @Bind(R.id.textView_artist_website_facebook) TextView _artist_website_facebook;
    @Bind(R.id.textView_artist_website_twitter) TextView _artist_website_twitter;
    @Bind(R.id.textView_artist_website_wiki) TextView _artist_website_wiki;
    @Bind(R.id.textView_venue_title) TextView _venue_title;
    @Bind(R.id.textView_venue_address) TextView _venue_address;
    @Bind(R.id.textView_venue_website_official) TextView _venue_website_official;
    @Bind(R.id.textView_venue_website_sw) TextView _venue_website_sw;
    @Bind(R.id.artist_description_expand) ImageView _artist_desc_expand;
    @Bind(R.id.artist_websites_expand) ImageView _artist_websites_expand;
    @Bind(R.id.artist_current_expand) ImageView _artist_current_expand;
    @Bind(R.id.venue_current_expand) ImageView _venue_current_expand;
    @Bind(R.id.venue_address_expand) ImageView _venue_address_expand;


    @Bind(R.id.imageButton_close) ImageButton _close;
    @Bind(R.id.tab_summary) TextView _tab_summary;
    @Bind(R.id.tab_artist) TextView _tab_artist;
    @Bind(R.id.tab_venue) TextView _tab_venue;
    @Bind(R.id.container_summary) LinearLayout _container_summary;
    @Bind(R.id.container_artist) LinearLayout _container_artist;
    @Bind(R.id.container_venue) LinearLayout _container_venue;
    @Bind(R.id.artist_websites) LinearLayout _artist_websites;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        sharedPref = getApplicationContext().getSharedPreferences("PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        _container_summary.setVisibility(View.VISIBLE);
        _container_artist.setVisibility(View.GONE);
        _container_venue.setVisibility(View.GONE);

        _summary_description.setVisibility(View.GONE);
        _summary_tickets_buy.setVisibility(View.GONE);
        _artist_websites.setVisibility(View.GONE);
        _artist_description.setVisibility(View.GONE);
        _venue_address.setVisibility(View.GONE);

        String eventID = "1038341"; //default should return 'Katie Melua' event
        String artistID = "";
        String venueID = "";
        if (extras != null) {
            eventID = extras.getString("EventID");
        }

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

        setEventData(eventID);
        setArtistData(artistID);
        setVenueData(venueID);

        Log.d("Event", event.getArtist().getName());
        Log.d("Partial Data", event.toString());

        _summary_tickets_buy.setOnClickListener(new View.OnClickListener() {
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



        //Check if realm already has full event data (via EventURL), if not, make volley request
        try{
            if(event.getUrl()==null){           //TODO: needs to update to get latest ticket info, maybe a timestamp check to be added/separate api route for ticket info
                requestFullData(eventID, "Event");
                Log.d(TAG, "Requesting full data...");
            } else {
                Log.d(TAG, "Already has full data");
            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }

        try{
            if(event.getArtist().getDescription()==null){         //check if full artist data already in realm
                requestFullData(artistID, "Artist");
                Log.d(TAG, "Requesting full artist data...");
            } else {
                Log.d(TAG, "Already has full artist data");
            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }

        try{
            if(event.getVenue().getSw_website()==null){         //check if full venue data already in realm
                requestFullData(venueID, "Venue");
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

    public void toggle_summary(View v) {
        //Toast.makeText(this, "Summary selected", Toast.LENGTH_SHORT).show();
        _tab_summary.setBackgroundColor(Color.parseColor("#cccccc"));
        _tab_artist.setBackgroundColor(Color.parseColor("#FFFFFF"));
        _tab_venue.setBackgroundColor(Color.parseColor("#FFFFFF"));
        _container_summary.setVisibility(View.VISIBLE);
        _container_artist.setVisibility(View.GONE);
        _container_venue.setVisibility(View.GONE);

        Picasso.with(getApplicationContext())
                .load(IMAGE_URL)
                .placeholder(R.drawable.ic_profile)
                .into(_event_image);
    }
    public void toggle_artist(View v) {
       //Toast.makeText(this, "Artist selected", Toast.LENGTH_SHORT).show();
        _tab_summary.setBackgroundColor(Color.parseColor("#FFFFFF"));
        _tab_artist.setBackgroundColor(Color.parseColor("#cccccc"));
        _tab_venue.setBackgroundColor(Color.parseColor("#FFFFFF"));
        _container_summary.setVisibility(View.GONE);
        _container_artist.setVisibility(View.VISIBLE);
        _container_venue.setVisibility(View.GONE);

        Picasso.with(getApplicationContext())
                .load(IMAGE_URL)
                .placeholder(R.drawable.ic_profile)
                .into(_event_image);
    }
    public void toggle_venue(View v) {
        //Toast.makeText(this, "Venue selected", Toast.LENGTH_SHORT).show();
        _tab_summary.setBackgroundColor(Color.parseColor("#FFFFFF"));
        _tab_artist.setBackgroundColor(Color.parseColor("#FFFFFF"));
        _tab_venue.setBackgroundColor(Color.parseColor("#cccccc"));
        _container_summary.setVisibility(View.GONE);
        _container_artist.setVisibility(View.GONE);
        _container_venue.setVisibility(View.VISIBLE);

        Picasso.with(getApplicationContext())
                .load(IMAGE_URL_VENUE)
                .placeholder(R.drawable.ic_profile)
                .into(_event_image);
    }

    public void toggle_artist_description(View v){
        if(_artist_description.isShown()){
            Fx.slide_up(this, _artist_description);              //slide up animation not working
            _artist_description.setVisibility(View.GONE);
            _artist_desc_expand.setImageResource(R.drawable.ic_expand_more_black_24dp);
        }
        else{
            _artist_description.setVisibility(View.VISIBLE);
            Fx.slide_down(this, _artist_description);
            _artist_desc_expand.setImageResource(R.drawable.ic_expand_less_black_24dp);
        }
    }

    public void toggle_artist_websites(View v) {
        if(_artist_websites.isShown()){
            Fx.slide_up(this, _artist_websites);              //slide up animation not working
            _artist_websites.setVisibility(View.GONE);
            _artist_websites_expand.setImageResource(R.drawable.ic_expand_more_black_24dp);
        }
        else{
            _artist_websites.setVisibility(View.VISIBLE);
            Fx.slide_down(this, _artist_websites);
            _artist_websites_expand.setImageResource(R.drawable.ic_expand_less_black_24dp);
        }
    }

    public void toggle_artist_current(View v) {
//        if(_artist_websites.isShown()){
//            Fx.slide_up(this, _artist_websites);              //slide up animation not working
//            _artist_websites.setVisibility(View.GONE);
//        }
//        else{
//            _artist_websites.setVisibility(View.VISIBLE);
//            Fx.slide_down(this, _artist_websites);
//        }
    }

    public void toggle_venue_address(View v) {
        if(_venue_address.isShown()){
            Fx.slide_up(this, _venue_address);              //slide up animation not working
            _venue_address.setVisibility(View.GONE);
            _venue_address_expand.setImageResource(R.drawable.ic_expand_more_black_24dp);

        }
        else{
            _venue_address.setVisibility(View.VISIBLE);
            Fx.slide_down(this, _venue_address);
            _venue_address_expand.setImageResource(R.drawable.ic_expand_less_black_24dp);

        }
    }
    public void toggle_venue_current(View v) {
//        if(_artist_websites.isShown()){
//            Fx.slide_up(this, _artist_websites);              //slide up animation not working
//            _artist_websites.setVisibility(View.GONE);
//        }
//        else{
//            _artist_websites.setVisibility(View.VISIBLE);
//            Fx.slide_down(this, _artist_websites);
//        }
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

    private void requestFullData(String id, String query) {

        final String ID = id;
        final String object = query;
        String FULL_QUERY_URL = "";

        if(object == "Event"){
            FULL_QUERY_URL = BASE_QUERY_URL + ID;
        } else if (object == "Artist"){
            FULL_QUERY_URL = BASE_QUERY_URL_ARTIST + ID;
        } else if (object == "Venue"){
            FULL_QUERY_URL = BASE_QUERY_URL_VENUE + ID;
        }
        Log.d("Querying", FULL_QUERY_URL);

        JsonObjectRequest req = new JsonObjectRequest(FULL_QUERY_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        try {
                            myRealm.beginTransaction();

                            if(object == "Event"){
                                myRealm.createOrUpdateObjectFromJson(Event.class, response);
                                myRealm.commitTransaction();
                                setEventData(ID);
                            } else if (object == "Artist"){
                                myRealm.createOrUpdateObjectFromJson(Artist.class, response);
                                myRealm.commitTransaction();
                                setArtistData(ID);
                            } else if (object == "Venue"){
                                myRealm.createOrUpdateObjectFromJson(Venue.class, response);
                                myRealm.commitTransaction();
                                setVenueData(ID);
                            }

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

    public void setEventData(String eventID){
        final Event event = myRealm
                .where(Event.class)
                .equalTo("id", eventID)
                .findFirst();
        Log.d("Full event", event.toString());

        _summary_title.setText(event.getArtist().getName());

        //TODO: should probably parse date properly not just truncate string lol
        _summary_date.setText(event.getStartTime().getLocal().toString().substring(0, 16));
        _summary_venue.setText(event.getVenue().getName());

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

        String tickets = "Tickets unavailable";

        try {
            if(event.getTickets().getTicket_count()!=0){
                tickets = "Tickets from: £" + String.valueOf(event.getTickets().getPurchase_price());
                _summary_tickets_price.setText(tickets);
                _summary_tickets_buy.setVisibility(View.VISIBLE);
            }else {
                _summary_tickets_price.setText(tickets);
            }
        } catch(Exception e){Log.d("Catch tickets", "No tickets");}


    }
    public void setArtistData(String artistID){
        final Artist artist = myRealm
                .where(Artist.class)
                .equalTo("id", artistID)
                .findFirst();
        Log.d("Full artist", artist.toString());

        try{
            if(artist.getSpotify_id().equals("Not found") || artist.getSpotify_id().equals("NA") || artist.getSpotify_id().equals(null)){
                _artist_spotify.setTextColor(Color.BLACK);
                _artist_spotify.setText("Not found :<");
            } else {
                String spotifyURL = "<a href='" + getResources().getString(R.string.spotify_url) + artist.getSpotify_id() + "'>Open in Spotify</a>";
                Log.d("spotifyURl:", spotifyURL);
                _artist_spotify.setText(Html.fromHtml(spotifyURL));
                _artist_spotify.setMovementMethod(LinkMovementMethod.getInstance());
            }
        } catch (Exception e) {
            _artist_spotify.setTextColor(Color.BLACK);
            _artist_spotify.setText("Not found :<");
        }


        //  + artist.getSpotify_id()

        //update the remaining fields with the new volley data
        _artist_title.setText(artist.getName());
        _artist_description.setText(artist.getDescription());
        _artist_genre.setText("Sw Genre ID: " +artist.getSw_genre_id());


        try {_artist_website_official.setText(artist.getWebsite().getWebsite_official());
        } catch (Exception e) {Log.d("Website", "No official website found");}
        try {_artist_website_facebook.setText(artist.getWebsite().getWebsite_fb());
        } catch (Exception e) {Log.d("Website", "No facebook website found");}
        try {_artist_website_twitter.setText(artist.getWebsite().getWebsite_twitter());
        } catch (Exception e) {Log.d("Website", "No twitter website found");}
        try{_artist_website_wiki.setText(artist.getWebsite().getWebsite_wiki());
        } catch (Exception e) {Log.d("Website", "No wiki website found");}

    }
    public void setVenueData(String venueID){
        final Venue venue = myRealm
                .where(Venue.class)
                .equalTo("id", venueID)
                .findFirst();
        Log.d("Full venue", venue.toString());

        //update the remaining fields with the new volley data
        _venue_title.setText(venue.getName());
        try{_venue_website_official.setText(venue.getWebsite());
        } catch (Exception e) {_venue_website_official.setText("NA");}
        try{_venue_website_sw.setText(venue.getSw_website());
        } catch (Exception e) {_venue_website_sw.setText("NA");}

        String address, address1, address2, city, post_code;
        address = address1 = address2 = city = post_code = "";
        try {address1 = venue.getLocation().getAddress_1() + "\n";
        } catch(Exception e){Log.d(TAG, "No address1");}
        try {address2 = venue.getLocation().getAddress_2() +"\n";
        } catch(Exception e){Log.d(TAG, "No address2");}
        try {city = venue.getLocation().getCity() +"\n";
        } catch(Exception e){Log.d(TAG, "No city");}
        try {post_code = venue.getLocation().getPost_code();
        } catch(Exception e){Log.d(TAG, "No post code");}

        address = address1 + address2 + city + post_code;

        _venue_address.setText("Address: " +address);
        Log.d("Venue", "" +venue.getLocation().getAddress_1());

        try{
            if(venue.getImage_URL() != null) {
                IMAGE_URL_VENUE = venue.getImage_URL();
            }
        } catch (Exception e){
            Log.d(TAG, "No venue image");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        myRealm.close();
    }
}