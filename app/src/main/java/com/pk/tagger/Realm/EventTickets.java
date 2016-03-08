package com.pk.tagger.Realm;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventTickets extends RealmObject {

    private int lowest_price;
    private int commission;
    private int purchase_price;
    private int face_value;
    private int ticket_count;

    private String currency;

    private Date on_sale_date;

    public EventTickets(){}

    public int getLowest_price() { return lowest_price; }

    public void setLowest_price(int lowest_price) {
        this.lowest_price = lowest_price;
    }

    public int getCommission() { return commission; }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getPurchase_price() { return purchase_price; }

    public void setPurchase_price(int purchase_price) {
        this.purchase_price = purchase_price;
    }

    public int getFace_value() { return face_value; }

    public void setFace_value(int face_value) {
        this.face_value = face_value;
    }

    public int getTicket_count() { return ticket_count; }

    public void setTicket_count(int ticket_count) {
        this.ticket_count = ticket_count;
    }

    public String getCurrency() { return currency; }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getOn_sale_date() {
        return on_sale_date;
    }

    public void setOn_sale_date(Date on_sale_date) {
        this.on_sale_date = on_sale_date;
    }
}
