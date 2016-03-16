package com.pk.tagger.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.pk.tagger.R;
import com.pk.tagger.realm.Event;
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
public class EventDetailActivity extends AppCompatActivity implements View.OnTouchListener{

    private static String TAG = EventDetailActivity.class.getSimpleName();

    private Realm myRealm;
    SharedPreferences sharedPref;


    // temporary string to show the parsed response
    private String jsonResponse;
    private static final String BASE_QUERY_URL = "http://52.31.31.106:9000/apiunsecure/event/";


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
    @Bind(R.id.textView_event_date) TextView _event_date;
    @Bind(R.id.textView_event_venue) TextView _event_venue;
    @Bind(R.id.textView_event_tickets_price) TextView _event_tickets_price;
    @Bind(R.id.textView_event_tickets_buy) TextView _event_tickets_buy;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        sharedPref = getApplicationContext().getSharedPreferences("com.pk.tagger.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        String eventID = "1038341"; //default should return 'Katie Melua' event
        if (extras != null) {
            eventID = extras.getString("EventID");
        }

        _baseLayout.setOnTouchListener(this);

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) _baseLayout.getLayoutParams();
        anchorLeft = _baseLayout.getLeft() + lp.leftMargin;

        myRealm = Realm.getInstance(getApplicationContext());
        final Event event = myRealm
                .where(Event.class)
                .equalTo("eventID", eventID)
                .findFirst();
        Log.d("Event", event.getEventPerformer().getName());
        Log.d("Partial Data", event.toString());

        _event_title.setText(event.getEventPerformer().getName());
        //TODO: should probably parse date properly not just truncate string lol
        _event_date.setText(event.getEventStartTime().getLocal().toString().substring(0, 16));
        _event_venue.setText(event.getEventVenue().getName());

        String tickets = "Tickets Unavailable";
        try {
            if(event.getEventTickets().getTicket_count()!=0){
                tickets = "Tickets from: £" + String.valueOf(event.getEventPurchasePrice());
            }
        } catch(Exception e){
            Log.d("Catch tickets", "No tickets");
        }
        _event_tickets_price.setText(tickets);

        _event_tickets_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (event.getEventURL() != null) {
                        Toast.makeText(getApplicationContext(), "Show me dem tickets!", Toast.LENGTH_SHORT).show();
                        Log.d("Buying", event.getEventURL());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getEventURL()));
                        startActivity(browserIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "No ticket link :(", Toast.LENGTH_SHORT).show();
                        Log.d("Else", event.getEventURL());
                    }
                }catch(Exception e){
                    Log.d("Catch", e.toString());
                }
            }
        });

        String IMAGE_URL = "";
        try{
            if(event.getEventImageURL() != null) {
                IMAGE_URL = event.getEventImageURL();
            } else if (event.getEventPerformer().getImage_URL() != null){
                IMAGE_URL = event.getEventPerformer().getImage_URL();
            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }
        Picasso.with(getApplicationContext())
                .load(IMAGE_URL)
                .placeholder(R.drawable.note_listings)
                .into(_event_image);

        //Check if realm already has full event data (via EventURL), if not, make volley request
        try{
            if(event.getEventURL()==null){
                getFullEvent(eventID);
                Log.d(TAG, "Requesting full data...");
            } else {
                Log.d(TAG, "Already has full data");
            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }
    }


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

    private void getFullEvent(String id) {

        //showpDialog();
        final String eventID = id;
        final String FULL_QUERY_URL = BASE_QUERY_URL + eventID;
        Log.d("Querying", FULL_QUERY_URL);

        JsonObjectRequest req = new JsonObjectRequest(FULL_QUERY_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        try {
                            myRealm = Realm.getInstance(getApplicationContext());

                            myRealm.beginTransaction();
                            myRealm.createOrUpdateObjectFromJson(Event.class, response);
                            myRealm.commitTransaction();

                            //update remaining data fields for event (i.e. data not already in Listings)
                            updateData(eventID);

                        } catch (Exception e) {
                            Log.d("Error updating", e.toString());
                        }

                        //hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                //hidepDialog();
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

    public void updateData(String eventID){
        final Event event = myRealm
                .where(Event.class)
                .equalTo("eventID", eventID)
                .findFirst();
        Log.d("Updated with full data", event.toString());

        //update the remaining fields with the new volley data
        //_event_title.setText("Test");

    }
}