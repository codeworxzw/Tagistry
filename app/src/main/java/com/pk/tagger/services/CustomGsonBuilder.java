package com.pk.tagger.services;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.pk.tagger.realm.RealmString;
import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.venue.Venue;

import java.lang.reflect.Type;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.internal.IOException;

/**
 * Created by Kieran on 23/05/2016.
 *  adopted from https://gist.github.com/cmelchior/1a97377df0c49cd4fca9

 */
public class CustomGsonBuilder {
    public Gson createEvent(Realm realm) {
        Type token = new TypeToken<RealmList<RealmString>>(){}.getType();
        Type tokenArtist = new TypeToken<Artist>(){}.getType();
        Type tokenVenue = new TypeToken<Venue>(){}.getType();
        final Realm realm1 = realm;

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy(){

                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(token, new TypeAdapter<RealmList<RealmString>>(){

                    @Override
                    public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
                        // Ignore
                    }

                    @Override
                    public RealmList<RealmString> read(JsonReader in) throws IOException {
                        RealmList<RealmString> list = new RealmList<RealmString>();
                        try{
                            in.beginArray();
                            while (in.hasNext()) {
                                list.add(new RealmString(in.nextString()));
                            }
                            in.endArray();
                        } catch (Exception e) {
                            Log.d("CustomTypeAdapter", e.toString());
                        }

                        return list;
                    }
                })
                .registerTypeAdapter(tokenArtist, new TypeAdapter<Artist>(){
//                    Realm myRealm;
                    @Override
                    public void write(JsonWriter out, Artist value) throws IOException {
                        // Ignore
                    }

                    @Override
                    public Artist read(JsonReader in) throws IOException {
                        Artist artistReturn;
                        Artist artistIn;
                        Gson gson1 = new Gson();
                        artistIn = gson1.fromJson(in, Artist.class);
//                        Log.d("ArtistIn", artistIn.toString());

//                        myRealm = Realm.getDefaultInstance();
                        Artist artistDB = realm1.where(Artist.class).equalTo("id", artistIn.getId()).findFirst();
//                        myRealm.close();
                        if(artistDB!=null){
//                            Log.d("ArtistOut", artistDB.toString());
                            artistReturn = artistDB;
                        } else {
                            artistReturn = artistIn;
//                            Log.d("ArtistResult", "Doesn't exist yet");
                        }

                        return artistReturn;
                    }
                })
                .registerTypeAdapter(tokenVenue, new TypeAdapter<Venue>(){
//                    Realm myRealm;
                    @Override
                    public void write(JsonWriter out, Venue value) throws IOException {
                        // Ignore
                    }

                    @Override
                    public Venue read(JsonReader in) throws IOException {
                        Venue venueReturn;
                        Venue venueIn;
                        Gson gson1 = new Gson();
                        venueIn = gson1.fromJson(in, Venue.class);
//                        Log.d("ArtistIn", artistIn.toString());

//                        myRealm = Realm.getDefaultInstance();
                        Venue venueDB = realm1.where(Venue.class).equalTo("id", venueIn.getId()).findFirst();
//                        myRealm.close();
                        if(venueDB!=null){
//                            Log.d("ArtistOut", artistDB.toString());
                            venueReturn = venueDB;
                        } else {
                            venueReturn = venueIn;
//                            Log.d("ArtistResult", "Doesn't exist yet");
                        }

                        return venueReturn;
                    }
                })
                .create();
        return gson;
    }

    public Gson createArtistVenue() {
        Type token = new TypeToken<RealmList<RealmString>>(){}.getType();
//        Type tokenArtist = new TypeToken<Artist>(){}.getType();
//        Type tokenVenue = new TypeToken<Venue>(){}.getType();

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy(){

                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(token, new TypeAdapter<RealmList<RealmString>>(){

                    @Override
                    public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
                        // Ignore
                    }

                    @Override
                    public RealmList<RealmString> read(JsonReader in) throws IOException {
                        RealmList<RealmString> list = new RealmList<RealmString>();
                        try{
                            in.beginArray();
                            while (in.hasNext()) {
                                list.add(new RealmString(in.nextString()));
                            }
                            in.endArray();
                        } catch (Exception e) {
                            Log.d("CustomTypeAdapter", e.toString());
                        }

                        return list;
                    }
                })
                .create();
        return gson;
    }

}
