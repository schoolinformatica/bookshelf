package com.example.bookshelf.model;

import java.io.Serializable;

/**
 * Created by jls on 5/14/15.
 */

// Used for storing user/account specific information
public class User implements Serializable {
    private String username;
    private String firstname;
    private String lastname;
    private String passsword;       //Make sure to put the String trough a hash before storing here
    private String zip;             // Zip code a.k.a postcode in dutch
    private String houseNumber;
    private String email;
    private String telephone;
    private String Id;

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPasssword() {
        return passsword;
    }

    public String getZip() {
        return zip;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
