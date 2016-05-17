package com.pk.tagger.realm.event;


/**
 * Created by PK on 17/01/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

import com.kyo.expandablelayout.ExpandableLayout;
import com.pk.tagger.Fx;
import com.pk.tagger.R;
import com.pk.tagger.realm.artist.Artist;
import com.pk.tagger.realm.venue.Venue;
import com.squareup.picasso.Picasso;


public class EventsAdapter extends RealmBasedRecyclerViewAdapter<Event, EventsAdapter.ViewHolder> {
    private Context context;
    private RealmResults<Event> items;
    public SparseBooleanArray expandState;
    private View.OnClickListener clickListener;
    private ExpandableLayout.OnExpandListener expandListener;
    private Realm myRealm;


    public interface OnClickListener {
        void onClick(View v);

    }

    public interface OnExpandListener {
        void onToggle(ExpandableLayout view, View child,
                      boolean isExpanded);

        void onExpandOffset(ExpandableLayout view, View child,
                            float offset, boolean isExpanding);
    }

    public EventsAdapter (Context context,
                             RealmResults<Event> realmResults,
                             boolean automaticUpdate,
                             boolean animateResults,
                             View.OnClickListener clickListener,
                             ExpandableLayout.OnExpandListener expandListener,
                             SparseBooleanArray expandState) {

        super(context, realmResults, automaticUpdate, animateResults);
        this.context = context;
        this.items = realmResults;
        this.clickListener = clickListener;
        this.expandListener = expandListener;
        this.expandState = expandState;

    }

    public class ViewHolder extends RealmViewHolder {
        public ExpandableLayout expandableLayout;
        public LinearLayout llexpand, toggle_artist_desc, toggle_artist_websites, artist_websites;
        public ImageView listingsImage, artist_desc_expand,artist_websites_expand;
        public TextView listingsTitle;
        public TextView listingsDate;
        public TextView listingsVenue;
        public TextView listingsTickets;
        public TextView artist_title, artist_desc, artist_genre, artist_spotify, artist_website_official, artist_website_fb, artist_website_twitter, artist_website_wiki;

        public ViewHolder(LinearLayout container) {
            super(container);
            this.expandableLayout = (ExpandableLayout) container.findViewById(R.id.expandablelayout);
            this.llexpand = (LinearLayout) container.findViewById(R.id.llexpand);
            this.listingsImage = (ImageView) container.findViewById(R.id.imageView_listings);
            this.listingsTitle = (TextView) container.findViewById(R.id.textView_listings_title);
            this.listingsDate = (TextView) container.findViewById(R.id.textView_listings_date);
            this.listingsVenue = (TextView) container.findViewById(R.id.textView_listings_venue);
            this.listingsTickets = (TextView) container.findViewById(R.id.textView_listings_tickets);
            this.artist_title = (TextView) container.findViewById(R.id.textView_artist_title);
            this.artist_desc = (TextView) container.findViewById(R.id.textView_artist_description);
            this.toggle_artist_desc = (LinearLayout) container.findViewById(R.id.toggle_artist_desc);
            this.toggle_artist_websites = (LinearLayout) container.findViewById(R.id.toggle_artist_websites);

            this.artist_desc_expand = (ImageView) container.findViewById(R.id.artist_description_expand);
            this.artist_websites_expand = (ImageView) container.findViewById(R.id.artist_websites_expand);

            this.artist_genre = (TextView) container.findViewById(R.id.textView_artist_genre);
            this.artist_spotify = (TextView) container.findViewById(R.id.textView_artist_spotify);
            this.artist_websites = (LinearLayout) container.findViewById(R.id.artist_websites);
            this.artist_website_official = (TextView) container.findViewById(R.id.textView_artist_website_official);
            this.artist_website_fb = (TextView) container.findViewById(R.id.textView_artist_website_facebook);
            this.artist_website_twitter = (TextView) container.findViewById(R.id.textView_artist_website_twitter);
            this.artist_website_wiki = (TextView) container.findViewById(R.id.textView_artist_website_wiki);

        }
        public void bind(final Event event) {

            myRealm = Realm.getDefaultInstance();
            final Artist artist = myRealm
                    .where(Artist.class)
                    .equalTo("id", event.getArtist().getId())
                    .findFirst();
            Log.d("Artist", artist.toString());
            final Venue venue = myRealm
                    .where(Venue.class)
                    .equalTo("id", event.getVenue().getId())
                    .findFirst();
            Log.d("Venue", venue.toString());

            listingsTitle.setText(event.getArtist().getName());
            //TODO: should probably parse date properly not just truncate string lol
            String date = event.getStartTime().getLocal().toString().substring(0,16);
            listingsDate.setText(date);
            listingsVenue.setText(event.getVenue().getName());
            String tickets = "Tickets Unavailable";
            try {
                if(event.getTickets().getTicket_count()!=0){
                    tickets = "Tickets from: £" + String.valueOf(event.getPurchasePrice());
                }
            } catch(Exception e){
                //Log.d(TAG, "No ticket price");
            }

            listingsTickets.setText(tickets);
            itemView.setBackgroundColor(Color.parseColor("#e6e6e6"));

            artist_title.setText(artist.getName());
            artist_desc.setVisibility(View.GONE);
            artist_desc.setText(artist.getDescription());
            artist_websites.setVisibility(View.GONE);
            toggle_artist_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(artist_desc.isShown()){
                        Fx.slide_up(context, artist_desc);              //slide up animation not working
                        artist_desc.setVisibility(View.GONE);
                        artist_desc_expand.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    }
                    else{
                        artist_desc.setVisibility(View.VISIBLE);
                        Fx.slide_down(context, artist_desc);
                        artist_desc_expand.setImageResource(R.drawable.ic_expand_less_black_24dp);
                    }
                }
            });
            toggle_artist_websites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(artist_websites.isShown()){
                        Fx.slide_up(context, artist_websites);              //slide up animation not working
                        artist_websites.setVisibility(View.GONE);
                        artist_websites_expand.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    }
                    else{
                        artist_websites.setVisibility(View.VISIBLE);
                        Fx.slide_down(context, artist_websites);
                        artist_websites_expand.setImageResource(R.drawable.ic_expand_less_black_24dp);
                    }
                }
            });

            String IMAGE_URL = "";
//            IMAGE_URL = "http://a.stwv.im/filestore/season/image/pinkpop_002474_1_mainpicture.jpg";

            try{
                if(event.getImage_URL() != null) {
                    IMAGE_URL = event.getImage_URL();
                } else if (event.getArtist().getImage_URL() != null){
                    IMAGE_URL = event.getArtist().getImage_URL();
                }
            } catch (Exception e){
                Log.d("Bind", e.toString());
            }

            Picasso.with(context)
                    .load(IMAGE_URL)
                    .placeholder(R.drawable.note_listings)
                    .into(listingsImage);
            myRealm.close();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup parent,
                                              int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent
                .getContext());
        View itemView = inflater.inflate(R.layout.listings_item_view,
                parent, false);
        ViewHolder holder = new ViewHolder((LinearLayout)itemView);
        holder.llexpand.setOnClickListener(clickListener);
        holder.llexpand.setTag(holder);
        holder.expandableLayout.setTag(holder);
        holder.expandableLayout.setExpanded(false);
        holder.expandableLayout.setOnExpandListener(expandListener);
        return holder;

    }

    @Override
    public void onBindRealmViewHolder(ViewHolder holder,
                                      int position) {
        holder.bind(items.get(position));
        holder.expandableLayout.setExpanded(expandState.get(position), false);

    }


}
