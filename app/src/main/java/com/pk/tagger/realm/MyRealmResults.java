package com.pk.tagger.realm;

import android.content.Context;
import android.util.Log;

import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.event.Event;
import com.pk.tagger.realm.user.User;
import com.pk.tagger.realm.venue.Venue;

import java.util.Arrays;
import java.util.Date;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Kieran on 15/03/2016.
 */
public class MyRealmResults {

    private Context context;
    private Realm myRealm;
    private String searchArtistVenue;
    private float ticketMin, ticketMax;
    private Date startDate;
    private Date endDate;
    private boolean ticketsAvailable;
    private String[] searchGenres;
    private String sortField;

    public MyRealmResults(Context context, Realm realm, String searchArtistVenue, String[] searchGenres, boolean ticketsAvailable, int ticketMin, int ticketMax, Date startDate, Date endDate, String sortField){

        this.context = context;
        this.searchArtistVenue = searchArtistVenue;
        this.searchGenres = searchGenres;
        this.ticketsAvailable = ticketsAvailable;
        this.ticketMin = (float) ticketMin;
        this.ticketMax = (float) ticketMax;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sortField = sortField;
        this.myRealm = realm;
    }


    public RealmResults getResults(){

        Log.d("SearchDate", startDate.toString());
        Log.d("EndDate", endDate.toString());
        Log.d("SearchArtist/Venue", searchArtistVenue);
        Log.d("TicketsAvailable", String.valueOf(ticketsAvailable));
        Log.d("Ticket Range", ticketMin + " to " + ticketMax);
        Log.d("SearchGenres", Arrays.toString(searchGenres));
        int ticketCheck = 0;
        if(ticketsAvailable){
            ticketCheck = 1;
        }
        if (ticketMax==0) {
            ticketMax = 1001;
        }

        if(sortField.isEmpty()){
            sortField = "date";     //placeholder to allow sorting by other fields later
        }

        Sort asc = Sort.ASCENDING;
        Sort des = Sort.DESCENDING;

        //RealmResults<Event> events = null;
        RealmQuery<Event> query = myRealm.where(Event.class);

        query.beginGroup()
                .greaterThan("startTime.local", startDate)
                .lessThan("startTime.local", endDate)
                .greaterThanOrEqualTo("tickets.ticket_count", ticketCheck)
                        //.greaterThanOrEqualTo("purchasePrice", ticketMin)
                        .lessThanOrEqualTo("purchasePrice", ticketMax)
                    .beginGroup()
                    .contains("artist.name", searchArtistVenue, Case.INSENSITIVE)
                    .or()
                    .contains("venue.name", searchArtistVenue, Case.INSENSITIVE)
                    .endGroup()
                .endGroup();

        if (searchGenres.length!=0) {
            query.beginGroup();
            if (searchGenres.length==1) {
                query.equalTo("artist.sw_genre_id", searchGenres[0]);
            }
            else {
                for (int i = 0; i < searchGenres.length; i++) {
                    query.or().equalTo("artist.sw_genre_id", searchGenres[i]);
                }
            }
            query.endGroup();
        }
        else {
            query = query.equalTo("artist.sw_genre_id", "10000");
        }

        RealmResults<Event> events = query.findAllSorted(sortField, asc);
       // RealmResults<Event> events = query.findAll();
        return events;
    }

    public RealmResults getFestivals(){

        RealmResults<Event> events = myRealm.where(Event.class).equalTo("artist.sw_genre_id", "35").findAllSorted(sortField, Sort.ASCENDING);

        return events;

    }

    public RealmResults getMyStarredEvents(){

        User user = myRealm.where(User.class).findFirst();
        String[] starred = user.getStarredEventsArray();


        RealmQuery<Event> query = myRealm.where(Event.class);

        if (starred.length!=0) {
            query.beginGroup();
            if (starred.length==1) {
                query.equalTo("id", starred[0]);
            }
            else {
                for (int i = 0; i < starred.length; i++) {
                    query.or().equalTo("id", starred[i]);
                }
            }
            query.endGroup();
        }
        else {
            query = query.equalTo("id", "0");
        }

        RealmResults<Event> events = query.findAllSorted(sortField, Sort.ASCENDING);

        return events;

    }



    public RealmResults getMyStarredArtists(){

        User user = myRealm.where(User.class).findFirst();
        String[] starred = user.getStarredArtistsArray();

        RealmQuery<Artist> query = myRealm.where(Artist.class);

        if (starred.length!=0) {
            query.beginGroup();
            if (starred.length==1) {
                query.equalTo("id", starred[0]);
            }
            else {
                for (int i = 0; i < starred.length; i++) {
                    query.or().equalTo("id", starred[i]);
                }
            }
            query.endGroup();
        }
        else {
            query = query.equalTo("id", "0");
        }

        RealmResults<Artist> artists = query.findAllSorted(sortField, Sort.ASCENDING);

        return artists;
    }

    public RealmResults getMyStarredVenues(){

        User user = myRealm.where(User.class).findFirst();
        String[] starred = user.getStarredVenuesArray();

        RealmQuery<Venue> query = myRealm.where(Venue.class);

        if (starred.length!=0) {
            query.beginGroup();
            if (starred.length==1) {
                query.equalTo("id", starred[0]);
            }
            else {
                for (int i = 0; i < starred.length; i++) {
                    query.or().equalTo("id", starred[i]);
                }
            }
            query.endGroup();
        }
        else {
            query = query.equalTo("id", "0");
        }

        RealmResults<Venue> venues = query.findAllSorted(sortField, Sort.ASCENDING);

        return venues;
    }
}
