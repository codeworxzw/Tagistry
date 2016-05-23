package com.pk.tagger.realm;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.lang.reflect.Type;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.internal.IOException;

/**
 * Created by Kieran on 23/05/2016.
 *  adopted from https://gist.github.com/cmelchior/1a97377df0c49cd4fca9

 */
public class CustomGsonBuilder {
    public Gson create() {
        Type token = new TypeToken<RealmList<RealmString>>(){}.getType();

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
