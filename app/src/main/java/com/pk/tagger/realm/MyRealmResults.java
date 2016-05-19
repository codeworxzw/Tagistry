package com.pk.tagger.realm;

import android.content.Context;
import android.util.Log;

import com.pk.tagger.realm.event.Event;

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
//    private Realm myRealm2;
    private String searchArtistVenue;
    private int ticketMin, ticketMax;
    private Date startDate;
    private Date endDate;
    private boolean ticketsAvailable;
    private String[] searchGenres;

    public MyRealmResults(Context context, String searchArtistVenue, String[] searchGenres, boolean ticketsAvailable, int ticketMin, int ticketMax, Date startDate, Date endDate){

        this.context = context;
        this.searchArtistVenue = searchArtistVenue;
        this.searchGenres = searchGenres;
        this.ticketsAvailable = ticketsAvailable;
        this.ticketMin = ticketMin;
        this.ticketMax = ticketMax;
        this.startDate = startDate;
        this.endDate = endDate;
        myRealm = Realm.getDefaultInstance();       //needs to be closed somewhere


    }

    public RealmResults getResults(){
//        myRealm = Realm.getDefaultInstance();
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

        String sortField = "date";     //placeholder to allow sorting by other fields later
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
//        myRealm.close();
        return events;
    }

    public long getCount(){
//        myRealm2 = Realm.getDefaultInstance();
        int ticketCheck = 0;
        if(ticketsAvailable){
            ticketCheck = 1;
        }
        if (ticketMax==0) {
            ticketMax = 1001;
        }
        RealmQuery<Event> query = myRealm
                .where(Event.class);

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

        long count = query.count();

//        myRealm.close();

        return count;
    }


}