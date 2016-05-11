package com.pk.tagger.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pk.tagger.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kieran on 11/05/2016.
 */
public class SendEventActivity extends AppCompatActivity {

    @Bind(R.id.send_event_name) EditText _event_name;
    @Bind(R.id.send_event_date) EditText _event_date;
    @Bind(R.id.send_event_start_time) EditText _event_start_time;
    @Bind(R.id.send_event_end_time) EditText _event_end_time;
    @Bind(R.id.send_event_tickets) EditText _event_tickets;
    @Bind(R.id.send_event_description) EditText _event_desc;
    @Bind(R.id.send_event_artist_name) EditText _artist_name;
    @Bind(R.id.send_event_artist_description) EditText _artist_desc;
    @Bind(R.id.send_event_venue_name) EditText _venue_name;
    @Bind(R.id.send_event_venue_address) EditText _venue_address;
    @Bind(R.id.send_event_comments) EditText _comments;
    @Bind(R.id.send_contact_number) EditText _contact_number;


    @Bind(R.id.spinner_artist_genre) Spinner _artist_genre;

    @Bind(R.id.btn_feedback_send) Button _btn_send;
    @Bind(R.id.btn_feedback_cancel) Button _btn_cancel;

    String eventName, eventDate, eventStartTime, eventEndTime, eventTickets, eventDesc, artistName, artistDesc, artistGenre, venueName, venueAddress, contactNumber, comments, emailBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_event);
        ButterKnife.bind(this);

        _btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validate()){
                    Toast.makeText(getApplicationContext(),"Sending failed",Toast.LENGTH_LONG).show();
                    return;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("Event Name:\t").append(eventName+"\n")
                        .append("Event Date:\t").append(eventDate+"\n")
                        .append("Event Start Time:\t").append(eventStartTime+"\n")
                        .append("Event End Time:\t").append(eventEndTime+"\n")
                        .append("Ticket Price:\t").append(eventTickets+"\n")
                        .append("Event Desc:\t").append(eventDesc+"\n")
                        .append("Artist Name:\t").append(artistName+"\n")
                        .append("Artist Desc:\t").append(artistDesc+"\n")
                        .append("Artist Genre:\t").append(artistGenre+"\n")
                        .append("Venue Name:\t").append(venueName+"\n")
                        .append("Venue Address:\t").append(venueAddress+"\n")
                        .append("Contact no.:\t").append(contactNumber+"\n")
                        .append("Comments:\t").append(comments+"\n");

                emailBody = sb.toString();
                Log.d("EmailBody", emailBody);

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"elasmolabs.feedback@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "New Event");
                i.putExtra(Intent.EXTRA_TEXT, emailBody);
                try {
                    startActivity(Intent.createChooser(i, "Send event..."));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SendEventActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        _btn_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public boolean validate(){
        boolean valid = true;

        eventName = _event_name.getText().toString();
        eventDate = _event_date.getText().toString();
        eventStartTime = _event_start_time.getText().toString();
        eventEndTime = _event_end_time.getText().toString();
        eventTickets = _event_tickets.getText().toString();
        eventDesc = _event_desc.getText().toString();
        artistName = _artist_name.getText().toString();
        artistDesc = _artist_desc.getText().toString();
        artistGenre = _artist_genre.getSelectedItem().toString();
        venueName = _venue_name.getText().toString();
        venueAddress = _venue_address.getText().toString();
        contactNumber = _contact_number.getText().toString();
        comments = _comments.getText().toString();

//        if (eventName.isEmpty()) {
//            _event_name.setError("Event name is required");
//            valid = false;
//        } else {
//            _event_name.setError(null);
//        }
        if (eventDate.isEmpty()) {
            _event_date.setError("Event date is required");
            valid = false;
        } else {
            _event_date.setError(null);
        }
        if (eventStartTime.isEmpty()) {
            _event_start_time.setError("Start time is required");
            valid = false;
        } else {
            _event_start_time.setError(null);
        }
//        if (eventEndTime.isEmpty()) {
//            _event_end_time.setError("End time is required");
//            valid = false;
//        } else {
//            _event_end_time.setError(null);
//        }
        if (eventTickets.isEmpty()) {
            eventTickets = "Free";
        } else {
            eventTickets = "£" +eventTickets;
        }
//        if (eventDesc.isEmpty()) {
//            _event_desc.setError("Event description is required");
//            valid = false;
//        } else {
//            _event_desc.setError(null);
//        }
        if (artistName.isEmpty()) {
            _artist_name.setError("Artist name is required");
            valid = false;
        } else {
            _artist_name.setError(null);
        }
//        if (artistDesc.isEmpty()) {
//            _artist_desc.setError("Artist description is required");
//            valid = false;
//        } else {
//            _artist_desc.setError(null);
//        }
        if (venueName.isEmpty()) {
            _venue_name.setError("Venue name is required");
            valid = false;
        } else {
            _venue_name.setError(null);
        }
        if (venueAddress.isEmpty()) {
            _venue_address.setError("Venue address is required");
            valid = false;
        } else {
            _venue_address.setError(null);
        }
        return valid;
    }

}
