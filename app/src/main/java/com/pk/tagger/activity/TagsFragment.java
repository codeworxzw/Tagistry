package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pk.tagger.R;
import com.pk.tagger.realm.ResetRealm;
import com.pk.tagger.realm.TagData;
import com.pk.tagger.realm.TagDataAdapter;
//import com.pk.tagger.realm.TagDataAdapter;

//import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class TagsFragment extends Fragment {

    private Realm myRealm;

    public TagsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
       // resetRealm();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tags, container, false);
        Log.d("Realm Tag onCV", "open");

        myRealm = Realm.getInstance(getContext());
        RealmResults<TagData> toDoItems = myRealm
                .where(TagData.class)
                .findAll();
        TagDataAdapter toDoRealmAdapter =
                new TagDataAdapter(getContext(), toDoItems, true, true);
        RealmRecyclerView realmRecyclerView =
                (RealmRecyclerView) rootView.findViewById(R.id.realm_recycler_view);
        realmRecyclerView.setAdapter(toDoRealmAdapter);

        return rootView;
    }

    @Override
    public void onStart(){

        super.onStart();

    }

    @Override
    public void onDestroyView(){
        Log.d("Realm Tag onDV", "open");

        super.onDestroyView();
        myRealm.close();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item=menu.findItem(R.id.action_sync);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.action_clear_cache:
                myRealm.beginTransaction();
                myRealm.clear(TagData.class);
                myRealm.commitTransaction();

                return true;

            case R.id.action_sync:



                return false;

            default:
                return false;
                // /return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onStop() {
        super.onStop();

    }
}
