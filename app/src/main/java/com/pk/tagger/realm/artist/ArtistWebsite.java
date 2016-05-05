package com.pk.tagger.realm.artist;

import io.realm.RealmObject;

/**
 * Created by Kieran on 05/05/2016.
 */
public class ArtistWebsite extends RealmObject {

    private String website_official;
    private String website_fb;
    private String website_twitter;
    private String website_wiki;


    public String getWebsite_official() {
        return website_official;
    }

    public void setWebsite_official(String website_official) {
        this.website_official = website_official;
    }

    public String getWebsite_fb() {
        return website_fb;
    }

    public void setWebsite_fb(String website_fb) {
        this.website_fb = website_fb;
    }

    public String getWebsite_twitter() {
        return website_twitter;
    }

    public void setWebsite_twitter(String website_twitter) {
        this.website_twitter = website_twitter;
    }

    public String getWebsite_wiki() {
        return website_wiki;
    }

    public void setWebsite_wiki(String website_wiki) {
        this.website_wiki = website_wiki;
    }

}
