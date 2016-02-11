package com.pk.tagger.activity;

/**
 * Created by PK on 16/01/2016.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pk.tagger.R;
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
       // resetRealm();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tags, container, false);

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


    private void resetRealm() {
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(getContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.deleteRealm(realmConfig);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        myRealm.close();
        super.onDetach();
    }
}
