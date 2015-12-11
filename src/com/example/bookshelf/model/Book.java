package com.example.bookshelf.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * This class represents the data object Book and holds all information about a specific book. *
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 7060210544600464481L;

    private String title;               // name of the book
    private String author;              // name of the author
    private String category;            // category of the book (fantasy/drama/sci-fi/etc)
    private String summary;             // summary of the book

    private String publisher;           // publisher of the book
    private String language;            // language of the book
    private String print;               // paperback OR hardback
    private String note;                // state of the book

    private String isbn;                // unique isbn number
    private String pages;               // number of pages

    private String publicationDate;     // date of publication

    private boolean read;               // read OR not
    private String image;               // URI to image
    private transient Bitmap bitmap;    // Bitmap for the image

    /* Default constructor*/
    public Book() {
    }

    /* Getters & Setters */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String format) {
        this.print = format;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Override the basic behavior
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Book) {
            Book book = (Book) o;
            return book.getTitle().equals(getTitle()) &&
                    book.getAuthor().equals(getAuthor()) &&
                    book.getIsbn().equals(getIsbn()) &&
                    book.getImage().equals(getImage()) &&
                    book.getCategory().equals(getCategory()) &&
                    book.getNote().equals(getNote()) &&
                    book.getLanguage().equals(getLanguage()) &&
                    book.getPages().equals(getPages()) &&
                    book.getPublicationDate().equals(getPublicationDate()) &&
                    book.getPrint().equals(getPrint()) &&
                    book.getSummary().equals(getSummary()) &&
                    book.getPublisher().equals(getPublisher());
        } else {
            return false;
        }
    }
}
