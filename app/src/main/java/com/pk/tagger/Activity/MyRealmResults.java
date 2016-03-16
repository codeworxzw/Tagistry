package com.pk.tagger.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pk.tagger.Realm.Event;

import java.util.Arrays;
import java.util.Date;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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
    private int ticketMin, ticketMax;
    private Date startDate;
    private boolean ticketsAvailable;
    private String[] searchGenres;

    public MyRealmResults(Context context, String searchArtistVenue, String[] searchGenres, boolean ticketsAvailable, int ticketMin, int ticketMax, Date startDate){

        this.context = context;
        this.searchArtistVenue = searchArtistVenue;
        this.searchGenres = searchGenres;
        this.ticketsAvailable = ticketsAvailable;
        this.ticketMin = ticketMin;
        this.ticketMax = ticketMax;
        this.startDate = startDate;

        myRealm = Realm.getInstance(context);
    }

    public RealmResults getResults(){

        Log.d("SearchDate", startDate.toString());
        Log.d("SearchArtist/Venue", searchArtistVenue);
        Log.d("TicketsAvailable", String.valueOf(ticketsAvailable));
        Log.d("Ticket Range", ticketMin + " to " + ticketMax);
        Log.d("SearchGenres", Arrays.toString(searchGenres));;
        int ticketCheck = 0;
        if(ticketsAvailable){
            ticketCheck = 1;
        }

        String sortField = "eventDate";     //placeholder to allow sorting by other fields later
        Sort asc = Sort.ASCENDING;
        Sort des = Sort.DESCENDING;

//        RealmResults<Event> events = myRealm
//                .where(Event.class)
//                .greaterThan("eventDate", startDate)
//                .greaterThanOrEqualTo("eventPurchasePrice", ticketCheck)
//                //.greaterThanOrEqualTo("eventPurchasePrice", ticketMin)
//                //.lessThanOrEqualTo("eventPurchasePrice", ticketMax)
//                    .beginGroup()
//                    .contains("eventPerformer.name", searchArtistVenue, Case.INSENSITIVE)
//                    .or()
//                    .contains("eventVenue.name", searchArtistVenue, Case.INSENSITIVE)
//                    .endGroup()
//                .findAllSorted(sortField, asc);

        //RealmResults<Event> events = null;
        RealmQuery<Event> query = myRealm.where(Event.class);

 //       query.equalTo("eventPerformer.sw_genre_id", "");

//        if(searchGenres.length!=0){
//            for(int i = 0; i<searchGenres.length-1; i++) {
//                query.or().contains("eventPerformer.sw_genre_id", searchGenres[i]);
//            }
//            query.or().contains("eventPerformer.sw_genre_id", searchGenres[searchGenres.length-1]);
//        }

        query.beginGroup()
                .greaterThan("eventDate", startDate)
                .greaterThanOrEqualTo("eventPurchasePrice", ticketCheck)
                        //.greaterThanOrEqualTo("eventPurchasePrice", ticketMin)
                        //.lessThanOrEqualTo("eventPurchasePrice", ticketMax)
                    .beginGroup()
                    .contains("eventPerformer.name", searchArtistVenue, Case.INSENSITIVE)
                    .or()
                    .contains("eventVenue.name", searchArtistVenue, Case.INSENSITIVE)
                    .endGroup()
                .endGroup();

        RealmResults<Event> events = query.findAllSorted(sortField, asc);

        return events;
    }

    public long getCount(){

        int ticketCheck = 0;
        if(ticketsAvailable){
            ticketCheck = 1;
        }

//        long count = myRealm
//                .where(Event.class)
//                .greaterThan("eventDate", startDate)
//                .greaterThanOrEqualTo("eventTickets.ticket_count", check)
//                //.greaterThanOrEqualTo("eventPurchasePrice", ticketMin)
//                //.lessThanOrEqualTo("eventPurchasePrice", ticketMax)
//                    .beginGroup()
//                    .contains("eventPerformer.name", searchArtistVenue, Case.INSENSITIVE)
//                    .or()
//                    .contains("eventVenue.name", searchArtistVenue, Case.INSENSITIVE)
//                    .endGroup()
//                .count();

        RealmQuery<Event> query = myRealm
                .where(Event.class);

//        query.equalTo("eventPerformer.sw_genre_id", "");

//        if(searchGenres.length!=0){
//            for(int i = 0; i<searchGenres.length-1; i++) {
//                query.or().contains("eventPerformer.sw_genre_id", searchGenres[i]);
//            }
//            query.or().contains("eventPerformer.sw_genre_id", searchGenres[searchGenres.length-1]);
//        }

        query.beginGroup()
                .greaterThan("eventDate", startDate)
                .greaterThanOrEqualTo("eventPurchasePrice", ticketCheck)
                //.greaterThanOrEqualTo("eventPurchasePrice", ticketMin)
                //.lessThanOrEqualTo("eventPurchasePrice", ticketMax)
                    .beginGroup()
                    .contains("eventPerformer.name", searchArtistVenue, Case.INSENSITIVE)
                    .or()
                    .contains("eventVenue.name", searchArtistVenue, Case.INSENSITIVE)
                    .endGroup()
            .endGroup();

        long count = query.count();

        return count;
    }

}
