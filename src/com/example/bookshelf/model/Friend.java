package com.example.bookshelf.model;

/**
 * Created by Steven on 13-6-2015.
 */
public class Friend extends User {
    private String trusted;
    public String getTrusted() {
        return trusted;
    }

    public void setTrusted(String trusted) {
        this.trusted = trusted;
    }

    public String toString() {
        return "Friend(firstname="+this.getFirstname()+", lastname="+this.getLastname()+", zip="+this.getZip()+", " +
                "housnumber="+this.getHouseNumber()+", email="+this.getEmail()+", id="+this.getId()+", trusted=" + this.getTrusted() +
                ")";
    }
}
