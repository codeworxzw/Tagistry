
package com.pk.tagger.realm.event;


/**
 * Created by PK on 17/01/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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


public class EventsAdapter extends RealmBasedRecyclerViewAdapter<Event, EventsAdapter.ViewHolder> {
    private Context context;
    private static String TAG = EventsAdapter.class.getSimpleName();

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

        public void bind(final Event event, final OnItemClickListener listener) {
            listingsTitle.setText(event.getArtist().getName());
            //TODO: should probably parse date properly not just truncate string lol
            String date = event.getStartTime().getLocal().toString().substring(0,16);
            listingsDate.setText(date);
            listingsVenue.setText(event.getVenue().getName());
            String tickets = "Tickets Unavailable";
            try {
                if(event.getTickets().getTicket_count()!=0){
                    tickets = "Tickets from: £" + String.valueOf(event.getTickets().getPurchase_price());
                }
            } catch(Exception e){
                //Log.d(TAG, "No ticket price");
            }

            listingsTickets.setText(tickets);
            itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));

            String IMAGE_URL;
            IMAGE_URL = "http://a.stwv.im/filestore/season/image/pinkpop_002474_1_mainpicture.jpg";

            try{
                if(event.getImage_URL() != null) {
                    IMAGE_URL = event.getImage_URL();
                } else if (event.getArtist().getImage_URL() != null){
                    IMAGE_URL = event.getArtist().getImage_URL();
                }
            } catch (Exception e){
                Log.d(TAG, e.toString());
            }

            Picasso.with(context)
                    .load(IMAGE_URL)
                    .placeholder(R.drawable.note_listings)
                    .into(listingsImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(event);
                }
            });

        }
    }

    public EventsAdapter(
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

