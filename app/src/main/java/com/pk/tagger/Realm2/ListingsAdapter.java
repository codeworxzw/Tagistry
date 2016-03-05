
package com.pk.tagger.Realm2;


/**
 * Created by PK on 17/01/2016.
 */

 import android.content.Context;
 import android.graphics.Color;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.ImageView;


 import android.widget.LinearLayout;
 import android.widget.TextView;

 import io.realm.RealmBasedRecyclerViewAdapter;
 import io.realm.RealmResults;
 import io.realm.RealmViewHolder;

 import com.pk.tagger.R;
 import com.squareup.picasso.Picasso;


public class ListingsAdapter extends RealmBasedRecyclerViewAdapter<Event, ListingsAdapter.ViewHolder> {

    private Context context;

//  private final RealmResults<Event> items;
//  private AdapterView.OnItemClickListener listener;

//  public interface OnItemClickListener {
//      void onItemClick(Event item);
//  }

    public class ViewHolder extends RealmViewHolder {

        public ImageView listingsImage;
        public TextView listingsTitle;
        public TextView listingsDate;
        public TextView listingsVenue;
        public TextView listingsTickets;

        public ViewHolder(LinearLayout container) {
            super(container);
            this.listingsImage = (ImageView) container.findViewById(R.id.imageView_listings);
            this.listingsTitle = (TextView) container.findViewById(R.id.textView_listings_title);
            this.listingsDate = (TextView) container.findViewById(R.id.textView_listings_date);
            this.listingsVenue = (TextView) container.findViewById(R.id.textView_listings_venue);
            this.listingsTickets = (TextView) container.findViewById(R.id.textView_listings_tickets);
        }
    }

    public ListingsAdapter(
            Context context,
            RealmResults<Event> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {

        super(context, realmResults, automaticUpdate, animateResults);
        this.context = context;
//        this.items = realmResults;
//        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.listings_item_view, viewGroup, false);
        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final Event eventItem = realmResults.get(position);
        viewHolder.listingsTitle.setText(eventItem.getEventName());
        //TODO: should probably parse date properly not just truncate string lol
        String date = eventItem.getEventStartTime().getLocal().toString().substring(0,16);
        viewHolder.listingsDate.setText(date);
        viewHolder.listingsVenue.setText(eventItem.getEventVenue().getEventVenue_Name());
        String ticketsPlaceholder = "Min Price: £10";
        viewHolder.listingsTickets.setText(ticketsPlaceholder);
        viewHolder.itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));

        //String IMAGE_URL = eventItem.getEventImageURL();
        String IMAGE_URL = "http://a.stwv.im/filestore/season/image/pinkpop_002474_1_mainpicture.jpg";
        Picasso.with(context)
               .load(IMAGE_URL)
               .placeholder(R.drawable.jakebugg)
               .into(viewHolder.listingsImage);
    }
}

