//package com.pk.tagger.realm;
//
//import android.util.Log;
//
//import com.google.gson.TypeAdapter;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonWriter;
//
//import io.realm.RealmList;
//import io.realm.internal.IOException;
//
///**
// * Created by Kieran on 19/05/2016.
// * adopted from https://gist.github.com/cmelchior/1a97377df0c49cd4fca9
// */
//public class CustomTypeAdapter extends TypeAdapter<RealmList<RealmString>> {
//
//    @Override
//    public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
//        // Ignore
//    }
//
//    @Override
//    public RealmList<RealmString> read(JsonReader in) throws IOException {
//        RealmList<RealmString> list = new RealmList<RealmString>();
//        try{
//            in.beginArray();
//            while (in.hasNext()) {
//                list.add(new RealmString(in.nextString()));
//            }
//            in.endArray();
//        } catch (Exception e) {
//            Log.d("CustomTypeAdapter", e.toString());
//        }
//
//        return list;
//    }
//}
