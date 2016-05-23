package com.pk.tagger.activity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.event.Event;
import com.pk.tagger.realm.event.EventResponse;
import com.pk.tagger.realm.venue.Venue;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Kieran on 22/05/2016.
 */
public interface MyApiEndpointInterface {

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://gigitch.duckdns.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("event/{id}")
    Call<Event> getEvent(@Path("id") String eventID);

    @GET("artist/{id}")
    Call<Artist> getArtist(@Path("id") String artistID);

    @GET("venue/{id}")
    Call<Venue> getVenue(@Path("id") String venueID);

    @GET("events/{page}")
    Call<EventResponse> getAllEvents(@Path("page") int pageNumber);

}
