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

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

import com.kyo.expandablelayout.ExpandableLayout;
import com.pk.tagger.ChromeTabsInterface;
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
    private ChromeTabsInterface chromeTabsListener;

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
                          Realm realm,
                          ChromeTabsInterface chromeTabsListener) {

        super(context, realmResults, automaticUpdate, animateResults);
        this.context = context;
        this.items = realmResults;
        this.clickListener = clickListener;
        this.expandListener = expandListener;
        this.expandState = expandState;
        this.myRealm = realm;
        this.chromeTabsListener = chromeTabsListener;
    }

    public class ViewHolder extends RealmViewHolder {
        public @Bind (R.id.expandablelayout) ExpandableLayout expandableLayout;
        @Bind (R.id.llexpand) LinearLayout llexpand;
        @Bind (R.id.llinfo) LinearLayout llinfo;
//        @Bind (R.id.artist_websites) LinearLayout artist_websites;
        @Bind (R.id.imageView_listings) ImageView listingsImage;
        @Bind (R.id.event_star) ImageView event_star;
        @Bind (R.id.textView_listings_title) TextView listingsTitle;
        @Bind (R.id.textView_listings_date) TextView listingsDate;
        @Bind (R.id.textView_listings_venue) TextView listingsVenue;
        @Bind (R.id.textView_listings_tickets) TextView listingsTickets;

        @Bind (R.id.textView_artist_title) TextView artist_title;
        @Bind (R.id.textView_artist_description) TextView artist_desc;
        @Bind (R.id.textView_artist_genre) TextView artist_genre;
//        @Bind (R.id.textView_artist_spotify) TextView artist_spotify;
//        @Bind (R.id.textView_artist_website_official) TextView artist_website_official;
//        @Bind (R.id.textView_artist_website_facebook) TextView artist_website_fb;
//        @Bind (R.id.textView_artist_website_twitter) TextView artist_website_twitter;
//        @Bind (R.id.textView_artist_website_wiki) TextView artist_website_wiki;
        @Bind (R.id.icon_official) ImageView artist_link_official;
        @Bind (R.id.icon_spotify) ImageView artist_link_spotify;
        @Bind (R.id.icon_fb) ImageView artist_link_fb;
        @Bind (R.id.icon_twitter) ImageView artist_link_twitter;
        @Bind (R.id.icon_wiki) ImageView artist_link_wiki;

        @Bind (R.id.textView_venue_title) TextView venue_title;
        @Bind (R.id.textView_venue_address) TextView venue_address;
        @Bind (R.id.textView_venue_website_sw) TextView venue_website_sw;
        @Bind (R.id.textView_venue_website_official) TextView venue_website_official;
        @Bind (R.id.textView_event_tickets_price) TextView event_tickets_price;
        @Bind (R.id.textView_event_tickets_buy) TextView event_tickets_buy;

        public ViewHolder(LinearLayout container) {
            super(container);
            ButterKnife.bind(this, container);
        }
        public void bind(final Event event) {

//            try{
//                if(event.getUrl()==null){           //TODO: needs to update to get latest ticket info, maybe a timestamp check to be added/separate api route for ticket info
//                    DatabaseStartServiceEvent.startActionDownload(context, event.getId(), "hello");
//                    Log.d("Adapter", "Requesting full data...");
//                } else {
////                    Log.d("Adapter", "Already has full data");
//                }
//            } catch (Exception e){
//                Log.d("Adapter", e.toString());
//            }

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
                    tickets = "Tickets from: £" + String.valueOf(Math.round(event.getPurchasePrice()));
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


//            switch(artist.getSw_genre_id()){
//                // pop & rock
//                case "10": llinfo.setBackgroundColor(context.getResources().getColor(R.color.poprock));
//                    break;
//                // hard rock & metal
//                case "11": llinfo.setBackgroundColor(context.getResources().getColor(R.color.hardrockmetal));
//                    break;
//                // indie & alternative
//                case "12": llinfo.setBackgroundColor(context.getResources().getColor(R.color.indiealternative));
//                    break;
//                // EDM & ibiza
//                case "14": llinfo.setBackgroundColor(context.getResources().getColor(R.color.edmibiza));
//                    break;
//                // EDM & ibiza
//                case "13": llinfo.setBackgroundColor(context.getResources().getColor(R.color.rnbhiphop));
//                    break;
//                default: llinfo.setBackgroundColor(0);
//            }

            try{
                if(artist.getSpotify_id().equals("Not found") || artist.getSpotify_id().equals("NA") || artist.getSpotify_id().isEmpty()){
//                    artist_spotify.setTextColor(Color.BLACK);
//                    artist_spotify.setText("Not found :<");
                } else {
                    String spotifyURL = "<a href='" + context.getResources().getString(R.string.spotify_url) + artist.getSpotify_id() + "'>Open in Spotify</a>";
//                    Log.d("spotifyURl:", spotifyURL);
//                    artist_spotify.setText(Html.fromHtml(spotifyURL));
//                    artist_spotify.setMovementMethod(LinkMovementMethod.getInstance());
                }
            } catch (Exception e) {
//                artist_spotify.setTextColor(Color.BLACK);
//                artist_spotify.setText("Not found :<");
            }
            artist_link_official.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (artist.getWebsite().getWebsite_official() != null) {
                            Toast.makeText(context, artist.getWebsite().getWebsite_official(), Toast.LENGTH_SHORT).show();
                            Log.d("Link", artist.getWebsite().getWebsite_official());
                            chromeTabsListener.openLink(artist.getWebsite().getWebsite_official());

                        } else {
                            Toast.makeText(context, "No link :(", Toast.LENGTH_SHORT).show();
                            Log.d("Adapter", "No url");
                        }
                    }catch(Exception e){
                        Log.d("Catch", e.toString());
                    }
                }
            });
            artist_link_fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (artist.getWebsite().getWebsite_fb() != null) {
                            Toast.makeText(context, artist.getWebsite().getWebsite_fb(), Toast.LENGTH_SHORT).show();
                            Log.d("Link", artist.getWebsite().getWebsite_fb());
                            chromeTabsListener.openLink(artist.getWebsite().getWebsite_fb());

                        } else {
                            Toast.makeText(context, "No link :(", Toast.LENGTH_SHORT).show();
                            Log.d("Adapter", "No link");
                        }
                    }catch(Exception e){
                        Log.d("Catch", e.toString());
                    }
                }
            });
            artist_link_twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (artist.getWebsite().getWebsite_twitter() != null) {
                            Toast.makeText(context, artist.getWebsite().getWebsite_twitter(), Toast.LENGTH_SHORT).show();
                            Log.d("Link", artist.getWebsite().getWebsite_twitter());
                            chromeTabsListener.openLink(artist.getWebsite().getWebsite_twitter());

                        } else {
                            Toast.makeText(context, "No link :(", Toast.LENGTH_SHORT).show();
                            Log.d("Adapter", "No link");
                        }
                    }catch(Exception e){
                        Log.d("Catch", e.toString());
                    }
                }
            });
            artist_link_wiki.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (artist.getWebsite().getWebsite_wiki() != null) {
                            Toast.makeText(context, artist.getWebsite().getWebsite_wiki(), Toast.LENGTH_SHORT).show();
                            Log.d("Link", artist.getWebsite().getWebsite_wiki());
                            chromeTabsListener.openLink(artist.getWebsite().getWebsite_wiki());

                        } else {
                            Toast.makeText(context, "No link :(", Toast.LENGTH_SHORT).show();
                            Log.d("Adapter", "No link");
                        }
                    }catch(Exception e){
                        Log.d("Catch", e.toString());
                    }
                }
            });

            venue_title.setText(venue.getName());

            StringBuilder sb =  new StringBuilder();
            String address;
            try {
                sb.append(venue.getLocation().getAddress_1());
                sb.append(", ");
            } catch(Exception e){Log.d("Address", "No address1");
            }
            try {
                sb.append(venue.getLocation().getAddress_2());
                sb.append(", ");
            } catch(Exception e){Log.d("Address", "No address2");
            }
            try {
                sb.append(venue.getLocation().getCity());
                sb.append(", ");
            } catch(Exception e){Log.d("Address", "No city");
            }
            try {
                sb.append(venue.getLocation().getPost_code());
            } catch(Exception e){Log.d("Address", "No post code");
            }

            address = sb.toString();

            venue_address.setText(address);

            venue_website_official.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (venue.getWebsite() != null) {
                            Toast.makeText(context, venue.getWebsite(), Toast.LENGTH_SHORT).show();
                            Log.d("Link", venue.getWebsite());
                            chromeTabsListener.openLink(venue.getWebsite());

                        } else {
                            Toast.makeText(context, "No link :(", Toast.LENGTH_SHORT).show();
                            Log.d("Adapter", "No link");
                        }
                    }catch(Exception e){
                        Log.d("Catch", e.toString());
                    }
                }
            });

            try{venue_website_official.setText(venue.getWebsite());
            } catch (Exception e) {venue_website_official.setText("N/A");}
            try{venue_website_sw.setText(venue.getSw_website());
            } catch (Exception e) {venue_website_sw.setText("N/A");}



            String tickets2 = "Tickets unavailable";

            try {
                if(event.getTickets().getTicket_count()!=0){
                    tickets2 = "Tickets from: £" + String.valueOf(Math.round(event.getPurchasePrice()));
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
                event_star.setImageResource(R.drawable.star_filled);
                Log.d("Starred", event.getArtist().getName());
            } else {
                event_star.setImageResource(R.drawable.star_outline);
            }
            event_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (Arrays.asList(user.getStarredEventsArray()).contains(event.getId())) {
                            event_star.setImageResource(R.drawable.star_outline);
                            myRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    user.removeStarredEvent(event.getId());
                                }
                            });
                        } else {
                            event_star.setImageResource(R.drawable.star_filled);
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
        View itemView = inflater.inflate(R.layout.listings_item_view_cardview,
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