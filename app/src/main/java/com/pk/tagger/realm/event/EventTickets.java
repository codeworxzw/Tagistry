package com.pk.tagger.realm.event;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by uk on 05/02/2016.
 */
public class EventTickets extends RealmObject {

    private float lowest_price;
    private float commission;
    private float purchase_price;
    private float face_value;
    private int ticket_count;

    private String currency;

    private Date on_sale_date;

    public EventTickets(){}

    public float getLowest_price() {
        return lowest_price;
    }

    public void setLowest_price(float lowest_price) {
        this.lowest_price = lowest_price;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public float getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(float purchase_price) {
        this.purchase_price = purchase_price;
    }

    public float getFace_value() {
        return face_value;
    }

    public void setFace_value(float face_value) {
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
