package com.pk.tagger.recyclerview;

/**
 * Created by PK on 27/02/2016.
 */
public class Genre {

    private String gName;

    public Boolean getgSelected() {
        return gSelected;
    }

    public void setgSelected(Boolean gSelected) {
        this.gSelected = gSelected;
    }

    private Boolean gSelected;

    public Genre(String name, Boolean selected) {
        gSelected = selected;
        gName = name;
    }

    public String getName() {
        return gName;
    }

    public void setName(String name) {

        gName = name;
    }


}
