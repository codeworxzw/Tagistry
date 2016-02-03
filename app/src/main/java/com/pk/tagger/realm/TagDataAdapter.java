/*
package com.pk.tagger.realm;

*/
/**
 * Created by PK on 17/01/2016.
 *//*

 import android.content.Context;
 import android.view.View;
import android.view.ViewGroup;
 import android.widget.FrameLayout;
 import android.widget.TextView;

 import io.realm.RealmBasedRecyclerViewAdapter;
 import io.realm.RealmResults;
 import io.realm.RealmViewHolder;

 import com.pk.tagger.R;


public class TagDataAdapter
        extends RealmBasedRecyclerViewAdapter<TagData, TagDataAdapter.ViewHolder> {

    public class ViewHolder extends RealmViewHolder {

        public TextView todoTextView;
        public ViewHolder(FrameLayout container) {
            super(container);
            this.todoTextView = (TextView) container.findViewById(R.id.todo_text_view);
        }
    }

    public TagDataAdapter(
            Context context,
            RealmResults<TagData> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.to_do_item_view, viewGroup, false);
        ViewHolder vh = new ViewHolder((FrameLayout) v);
        return vh;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final TagData toDoItem = realmResults.get(position);
        viewHolder.todoTextView.setText(toDoItem.toString());

    }
}
*/
