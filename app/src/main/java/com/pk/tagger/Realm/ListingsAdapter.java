
package com.pk.tagger.Realm;


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

  private final RealmResults<Event> items;
  private OnItemClickListener listener;

  public interface OnItemClickListener {
      void onItemClick(Event item);
  }

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

        public void bind(final Event eventItem, final OnItemClickListener listener) {
            listingsTitle.setText(eventItem.getEventPerformer().getName());
            //TODO: should probably parse date properly not just truncate string lol
            String date = eventItem.getEventStartTime().getLocal().toString().substring(0,16);
            listingsDate.setText(date);
            listingsVenue.setText(eventItem.getEventVenue().getName());
            String tickets = "Tickets Unavailable";
            if(eventItem.getEventTickets().getLowest_price() != 0){
                tickets = "Tickets from: £" + String.valueOf(eventItem.getEventTickets().getLowest_price());
            }

            listingsTickets.setText(tickets);
            itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));

            String IMAGE_URL;
            IMAGE_URL = "http://a.stwv.im/filestore/season/image/pinkpop_002474_1_mainpicture.jpg";

            if(eventItem.getEventImageURL() != null) {
                IMAGE_URL = eventItem.getEventImageURL();
            } else if (eventItem.getEventPerformer().getImage_URL() != null){
                IMAGE_URL = eventItem.getEventPerformer().getImage_URL();
            }
            Picasso.with(context)
                    .load(IMAGE_URL)
                    .placeholder(R.drawable.katie_melua)
                    .into(listingsImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(eventItem);
                }
            });

        }
    }

    public ListingsAdapter(
            Context context,
            RealmResults<Event> realmResults,
            boolean automaticUpdate,
            boolean animateResults,
            OnItemClickListener listener) {

        super(context, realmResults, automaticUpdate, animateResults);
        this.context = context;
        this.items = realmResults;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.listings_item_view, viewGroup, false);
        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.bind(items.get(position), listener);
    }
}

