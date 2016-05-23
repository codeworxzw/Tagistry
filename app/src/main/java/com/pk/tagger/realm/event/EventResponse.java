package com.pk.tagger.realm.event;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Kieran on 23/05/2016.
 */
public class EventResponse extends RealmObject{
    public EventResponse() { }

    private RealmList<Event> docs;  // = new RealmList<>();
    private int total;
    private int limit;
    private int page;
    @PrimaryKey
    private int pages;

    public RealmList<Event> getDocs() {
        return docs;
    }

    public void setDocs(RealmList<Event> docs) {
        this.docs = docs;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
