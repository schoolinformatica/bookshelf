package com.example.bookshelf.parser;

import android.util.Base64;
import com.example.bookshelf.model.Book;
import com.example.bookshelf.model.Friend;
import com.example.bookshelf.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jls on 5/22/15.
 */

// Used to parseBooks your JSON, Requires a JSON array with Objects
public class JSONParser {

    public static Book parseBook(JSONObject obj) throws JSONException, UnsupportedEncodingException {

        Book book = new Book(); // Create new Book

        book.setImage(obj.getString("image"));
        book.setRead(obj.getBoolean("read"));
        book.setAuthor(obj.getString("author"));
        book.setPrint(obj.getString("print"));
        book.setPublicationDate(obj.getString("yearOfRelease"));
        book.setIsbn(obj.getString("ISBN"));
        book.setNote(obj.getString("note"));
        book.setSummary(new String(Base64.decode(obj.getString("description"), Base64.DEFAULT), "UTF-8"));
        book.setTitle(new String(Base64.decode(obj.getString("title"), Base64.DEFAULT), "UTF-8"));
        book.setPages(obj.getString("pages"));
        book.setCategory(obj.getString("genre"));

        return book; // Return the book
    }

    public static Friend parseFriend(JSONObject job) throws JSONException {
        List<Friend> friends = new ArrayList<>();
        Friend f = new Friend();

        if(job.has("trusted"))
            f.setTrusted(job.getString("trusted"));
        f.setFirstname(job.getString("firstname"));
        f.setLastname(job.getString("lastname"));
        f.setId(job.getString("id"));
        f.setZip(job.getString("zipcode"));
        f.setEmail(job.getString("email"));
        f.setHouseNumber(job.getString("housenumber"));

        return f;
    }

    public static User parseUser(JSONObject job) throws JSONException {
        User u = new User();

        u.setFirstname(job.getString("firstname"));
        u.setLastname(job.getString("lastname"));
        u.setId(job.getString("id"));
        u.setZip(job.getString("zipcode"));
        u.setEmail(job.getString("email"));
        u.setHouseNumber(job.getString("housenumber"));

        return u;
    }
}
