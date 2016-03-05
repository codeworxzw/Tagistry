package com.pk.tagger.Activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
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

import com.pk.tagger.R;
import com.pk.tagger.Realm.Event;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by PK on 29/02/2016.
 */
public class EventDetailActivity extends AppCompatActivity implements View.OnTouchListener{

    private Realm myRealm;

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

        ButterKnife.bind(this);
        Intent intent = getIntent();
        String eventID = intent.getStringExtra("eventID");

        _baseLayout.setOnTouchListener(this);

        myRealm = Realm.getInstance(getApplicationContext());
        final Event event = myRealm
                .where(Event.class)
                .equalTo("eventID", "3080828")  //Should return 'Radical Face' event
                .findFirst();
        Log.d("Popup: ",event.toString());
        _event_title.setText(event.getEventName());
        //TODO: should probably parse date properly not just truncate string lol
        _event_date.setText(event.getEventStartTime().getLocal().toString().substring(0, 16));
        _event_venue.setText(event.getEventVenue().getEventVenue_Name());
        _event_tickets_price.setText("Min. Ticket Price: TBD");
        _event_tickets_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.getEventURL() != null) {
                    Toast.makeText(getApplicationContext(), "Buying Tickets!", Toast.LENGTH_SHORT).show();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getEventURL()));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "No link avaliable :(", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // String IMAGE_URL = "http://a.stwv.im/filestore/season/image/pinkpop_002474_1_mainpicture.jpg";
        String IMAGE_URL = "https://chairnerd.global.ssl.fastly.net/images/performers-landscape/radical-face-a44d46/12082/huge.jpg";
        Picasso.with(getApplicationContext())
                .load(IMAGE_URL)
                .placeholder(R.drawable.jakebugg)
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

}