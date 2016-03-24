package com.pk.tagger.activity;

import android.content.Context;
import android.util.Log;

import com.pk.tagger.realm.Event;

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

        myRealm = Realm.getInstance(context);

    }

    public RealmResults getResults(){

        Log.d("SearchDate", startDate.toString());
        Log.d("EndDate", endDate.toString());
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
                .lessThan("eventDate", endDate)
                .greaterThanOrEqualTo("eventPurchasePrice", ticketCheck)
                        //.greaterThanOrEqualTo("eventPurchasePrice", ticketMin)
                        //.lessThanOrEqualTo("eventPurchasePrice", ticketMax)
                    .beginGroup()
                    .contains("eventPerformer.name", searchArtistVenue, Case.INSENSITIVE)
                    .or()
                    .contains("eventVenue.name", searchArtistVenue, Case.INSENSITIVE)
                    .endGroup()
                .endGroup();

        if (searchGenres.length!=0) {
            query.beginGroup();
            if (searchGenres.length==1) {
                query.equalTo("eventPerformer.sw_genre_id", searchGenres[0]);
            }
            else {
                for (int i = 0; i < searchGenres.length; i++) {
                    query.or().equalTo("eventPerformer.sw_genre_id", searchGenres[i]);
                    //query = query.equalTo("eventPerformer.sw_genre_id", searchGenres[i]);
                }
            }
            query.endGroup();
        }
        else {
            query = query.equalTo("eventPerformer.sw_genre_id", "10000");
        }

        RealmResults<Event> events = query.findAllSorted(sortField, asc);
        //RealmResults<Event> events = query.findAll();
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
                .lessThan("eventDate", endDate)
                .greaterThanOrEqualTo("eventPurchasePrice", ticketCheck)
                //.greaterThanOrEqualTo("eventPurchasePrice", ticketMin)
                //.lessThanOrEqualTo("eventPurchasePrice", ticketMax)
                    .beginGroup()
                    .contains("eventPerformer.name", searchArtistVenue, Case.INSENSITIVE)
                    .or()
                    .contains("eventVenue.name", searchArtistVenue, Case.INSENSITIVE)
                    .endGroup()
            .endGroup();

        if (searchGenres.length!=0) {
            query.beginGroup();
            if (searchGenres.length==1) {
                query.equalTo("eventPerformer.sw_genre_id", searchGenres[0]);
            }
            else {
                for (int i = 0; i < searchGenres.length; i++) {
                    query.or().equalTo("eventPerformer.sw_genre_id", searchGenres[i]);
                    //query = query.equalTo("eventPerformer.sw_genre_id", searchGenres[i]);
                }
            }
            query.endGroup();
        }
        else {
            query = query.equalTo("eventPerformer.sw_genre_id", "10000");
        }

        long count = query.count();

        return count;
    }

}
