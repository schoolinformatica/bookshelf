package com.example.bookshelf.model;

/**
 * Created by Steven on 15-6-2015.
 */
public class BorrowedBook extends Book {
    Friend renter; // The renter that has the book borrowed
    User owner; // The owner of the book
    String rentalDate; //Date when the book is borrowed


    public Friend getRenter() {
        return renter;
    }

    public void setRenter(Friend renter) {
        this.renter = renter;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public void setBook(Book b) {
        this.setIsbn(b.getIsbn());
        this.setAuthor(b.getAuthor());
        this.setBitmap(b.getBitmap());
        this.setImage(b.getImage());
        this.setPrint(b.getPrint());
        this.setSummary(b.getSummary());
        this.setCategory(b.getCategory());
        this.setPages(b.getPages());
        this.setTitle(b.getTitle());
    }
}