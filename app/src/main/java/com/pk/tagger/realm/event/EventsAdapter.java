package com.pk.tagger.realm.event;


/**
 * Created by PK on 17/01/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import com.pk.tagger.realm.user.User;
import com.pk.tagger.realm.venue.Venue;
import com.squareup.picasso.Picasso;

import java.util.Arrays;


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
                             SparseBooleanArray expandState,
                          Realm realm) {

        super(context, realmResults, automaticUpdate, animateResults);
        this.context = context;
        this.items = realmResults;
        this.clickListener = clickListener;
        this.expandListener = expandListener;
        this.expandState = expandState;
        this.myRealm = realm;
    }

    public class ViewHolder extends RealmViewHolder {
        public ExpandableLayout expandableLayout;
        public LinearLayout llexpand, artist_websites;

        public ImageView listingsImage, event_star;
        public TextView listingsTitle, listingsDate, listingsVenue, listingsTickets;
        public TextView artist_title, artist_desc, artist_genre, artist_spotify, artist_website_official, artist_website_fb, artist_website_twitter, artist_website_wiki;
        public TextView venue_title, venue_address, venue_website_sw, venue_website_official;
        public TextView event_tickets_price, event_tickets_buy;

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
            this.artist_genre = (TextView) container.findViewById(R.id.textView_artist_genre);
            this.artist_spotify = (TextView) container.findViewById(R.id.textView_artist_spotify);
            this.artist_websites = (LinearLayout) container.findViewById(R.id.artist_websites);
            this.artist_website_official = (TextView) container.findViewById(R.id.textView_artist_website_official);
            this.artist_website_fb = (TextView) container.findViewById(R.id.textView_artist_website_facebook);
            this.artist_website_twitter = (TextView) container.findViewById(R.id.textView_artist_website_twitter);
            this.artist_website_wiki = (TextView) container.findViewById(R.id.textView_artist_website_wiki);
            this.venue_title = (TextView) container.findViewById(R.id.textView_venue_title);
            this.venue_address = (TextView) container.findViewById(R.id.textView_venue_address);
            this.venue_website_sw = (TextView) container.findViewById(R.id.textView_venue_website_sw);
            this.venue_website_official = (TextView) container.findViewById(R.id.textView_venue_website_official);
            this.event_tickets_price = (TextView) container.findViewById(R.id.textView_event_tickets_price);
            this.event_tickets_buy = (TextView) container.findViewById(R.id.textView_event_tickets_buy);
            this.event_star = (ImageView) container.findViewById(R.id.event_star);

        }
        public void bind(final Event event) {

//            myRealm = Realm.getDefaultInstance();
            final Artist artist = myRealm
                    .where(Artist.class)
                    .equalTo("id", event.getArtist().getId())
                    .findFirst();
//            Log.d("Artist", artist.toString());
            final Venue venue = myRealm
                    .where(Venue.class)
                    .equalTo("id", event.getVenue().getId())
                    .findFirst();
//            Log.d("Venue", venue.toString());

            String IMAGE_URL = "";

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

            //Expanded layout stuff set below
            artist_title.setText(artist.getName());
            artist_desc.setText(artist.getDescription());
            artist_genre.setText("Sw Genre ID: " +artist.getSw_genre_id());     //replace with actual genre names when we get them (waiting updated api)

            try{
                if(artist.getSpotify_id().equals("Not found") || artist.getSpotify_id().equals("NA") || artist.getSpotify_id().isEmpty()){
                    artist_spotify.setTextColor(Color.BLACK);
                    artist_spotify.setText("Not found :<");
                } else {
                    String spotifyURL = "<a href='" + context.getResources().getString(R.string.spotify_url) + artist.getSpotify_id() + "'>Open in Spotify</a>";
//                    Log.d("spotifyURl:", spotifyURL);
                    artist_spotify.setText(Html.fromHtml(spotifyURL));
                    artist_spotify.setMovementMethod(LinkMovementMethod.getInstance());
                }
            } catch (Exception e) {
                artist_spotify.setTextColor(Color.BLACK);
                artist_spotify.setText("Not found :<");
            }
            try {artist_website_official.setText(artist.getWebsite().getWebsite_official());
            } catch (Exception e) {//Log.d("Website", "No official website found");
            }
            try {artist_website_fb.setText(artist.getWebsite().getWebsite_fb());
            } catch (Exception e) {//Log.d("Website", "No facebook website found");
            }
            try {artist_website_twitter.setText(artist.getWebsite().getWebsite_twitter());
            } catch (Exception e) {//Log.d("Website", "No twitter website found");
            }
            try{artist_website_wiki.setText(artist.getWebsite().getWebsite_wiki());
            } catch (Exception e) {//Log.d("Website", "No wiki website found");
            }

            venue_title.setText(venue.getName());

            try{venue_website_official.setText(venue.getWebsite());
            } catch (Exception e) {venue_website_official.setText("N/A");}
            try{venue_website_sw.setText(venue.getSw_website());
            } catch (Exception e) {venue_website_sw.setText("N/A");}

            String address, address1, address2, city, post_code;
            address = address1 = address2 = city = post_code = "";
            try {address1 = venue.getLocation().getAddress_1() + "\n";
            } catch(Exception e){//Log.d("Address", "No address1");
            }
            try {address2 = venue.getLocation().getAddress_2() +"\n";
            } catch(Exception e){//Log.d("Address", "No address2");
            }
            try {city = venue.getLocation().getCity() +"\n";
            } catch(Exception e){//Log.d("Address", "No city");
            }
            try {post_code = venue.getLocation().getPost_code();
            } catch(Exception e){//Log.d("Address", "No post code");
            }

            address = address1 + address2 + city + post_code;

            venue_address.setText(address);

            String tickets2 = "Tickets unavailable";

            try {
                if(event.getTickets().getTicket_count()!=0){
                    tickets2 = "Tickets from: £" + String.valueOf(event.getPurchasePrice());
                    event_tickets_price.setText(tickets2);
                    event_tickets_buy.setVisibility(View.VISIBLE);
                }else {
                    event_tickets_price.setText(tickets2);
                    event_tickets_buy.setVisibility(View.GONE);
                }
            } catch(Exception e){Log.d("Catch tickets", "No tickets");}

            event_tickets_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (event.getUrl() != null) {
                            Toast.makeText(context, "Show me dem tickets!", Toast.LENGTH_SHORT).show();
                            Log.d("Buying", event.getUrl());
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUrl()));
                            context.startActivity(browserIntent);
                        } else {
                            Toast.makeText(context, "No ticket link :(", Toast.LENGTH_SHORT).show();
                            Log.d("Else", event.getUrl());
                        }
                    }catch(Exception e){
                        Log.d("Catch", e.toString());
                    }
                }
            });

            final User user = myRealm.where(User.class).findFirst();
            //check if user has saved event before
            if (Arrays.asList(user.getStarredEventsArray()).contains(event.getId())) {
                event_star.setImageResource(R.drawable.ic_star_black_36dp);
                Log.d("Starred", event.getArtist().getName());
            } else {
                event_star.setImageResource(R.drawable.ic_star_border_black_36dp);
            }
            event_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (Arrays.asList(user.getStarredEventsArray()).contains(event.getId())) {
                            event_star.setImageResource(R.drawable.ic_star_border_black_36dp);
                            myRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    user.removeStarredEvent(event.getId());
                                }
                            });

                        } else {
                            event_star.setImageResource(R.drawable.ic_star_black_36dp);
                            myRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    user.addStarredEvent(event.getId());
                                }
                            });
                        }
                    }catch(Exception e){
                        Log.d("Catch", e.toString());
                    }
                }
            });
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