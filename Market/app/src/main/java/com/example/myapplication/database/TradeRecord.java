package com.example.myapplication.database;

import org.litepal.crud.LitePalSupport;

public class TradeRecord extends LitePalSupport {
    private String date; //交易时间
    private User buyer;
    private User seller;
    private String note; //交易备注

    public TradeRecord() {

    }

    public String getDate() {
        return date;
    }

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
