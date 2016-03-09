package com.pk.tagger.Activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
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
import com.pk.tagger.Realm.Event;
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
    private int defaultViewHeight;

    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;

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

        //Make volley request to get full event data
        getFullEvent(eventID);

        _baseLayout.setOnTouchListener(this);

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
            if(event.getEventTickets().getPurchase_price()!=0){
                tickets = "Tickets from: £" + String.valueOf(event.getEventTickets().getPurchase_price());
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

        String IMAGE_URL = "http://a.stwv.im/filestore/season/image/katie-melua_000158_1_mainpicture.jpg";
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
                .placeholder(R.drawable.note2)
                .into(_event_image);
    }


    public boolean onTouch(View view, MotionEvent event) {

        // Get finger position on screen
        final int Y = (int) event.getRawY();

        // Switch on motion event type
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                // save default base layout height
                defaultViewHeight = _baseLayout.getHeight();

                // Init finger and view position
                previousFingerPosition = Y;
                baseLayoutPosition = (int) _baseLayout.getY();
                break;

            case MotionEvent.ACTION_UP:
                // If user was doing a scroll up
                if(isScrollingUp){
                    // Reset baselayout position
                    _baseLayout.setY(0);
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false;
                }

                // If user was doing a scroll down
                if(isScrollingDown){
                    // Reset baselayout position
                    _baseLayout.setY(0);
                    // Reset base layout size
                    _baseLayout.getLayoutParams().height = defaultViewHeight;
                    _baseLayout.requestLayout();
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!isClosing){
                    int currentYPosition = (int) _baseLayout.getY();

                    // If we scroll up
                    if(previousFingerPosition >Y){
                        // First time android rise an event for "up" move
                        if(!isScrollingUp){
                            isScrollingUp = true;
                        }

                        // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                        if(_baseLayout.getHeight()<defaultViewHeight){
                            _baseLayout.getLayoutParams().height = _baseLayout.getHeight() - (Y - previousFingerPosition);
                            _baseLayout.requestLayout();
                        }
                        else {
                            // Has user scroll enough to "auto close" popup ?
                            if ((baseLayoutPosition - currentYPosition) > defaultViewHeight / 4) {
                                closeUpAndDismissDialog(currentYPosition);
                                return true;
                            }

                            //
                        }
                        _baseLayout.setY(_baseLayout.getY() + (Y - previousFingerPosition));

                    }
                    // If we scroll down
                    else{

                        // First time android rise an event for "down" move
                        if(!isScrollingDown){
                            isScrollingDown = true;
                        }

                        // Has user scroll enough to "auto close" popup ?
                        if (Math.abs(baseLayoutPosition - currentYPosition) > defaultViewHeight / 2)
                        {
                            closeDownAndDismissDialog(currentYPosition);
                            return true;
                        }

                        // Change base layout size and position (must change position because view anchor is top left corner)
                        _baseLayout.setY(_baseLayout.getY() + (Y - previousFingerPosition));
                        _baseLayout.getLayoutParams().height = _baseLayout.getHeight() - (Y - previousFingerPosition);
                        _baseLayout.requestLayout();
                    }

                    // Update position
                    previousFingerPosition = Y;
                }
                break;
        }
        return true;
    }

    public void closeUpAndDismissDialog(int currentPosition){
        isClosing = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(_baseLayout, "y", currentPosition, -_baseLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });
        positionAnimator.start();
    }

    public void closeDownAndDismissDialog(int currentPosition){
        isClosing = true;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(_baseLayout, "y", currentPosition, screenHeight + _baseLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

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
        Log.d("Full Data", event.toString());

        //update the remaining fields with the new volley data
        //_event_title.setText("Test");

    }
}