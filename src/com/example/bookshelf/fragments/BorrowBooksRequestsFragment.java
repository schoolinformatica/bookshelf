package com.example.bookshelf.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.BookShelf.R;
import com.example.bookshelf.activities.BookDetailsActivity;
import com.example.bookshelf.adapters.BookAdapter;
import com.example.bookshelf.connection.ConnectionManager;
import com.example.bookshelf.connection.RequestPackaging;
import com.example.bookshelf.dependencies.BookshelfConstants;
import com.example.bookshelf.dependencies.DataBundler;
import com.example.bookshelf.dependencies.QueryHandler;
import com.example.bookshelf.model.*;
import com.example.bookshelf.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 20-6-2015.
 * Instances of this class are fragments representing a single object in our collection.
 */
public class BorrowBooksRequestsFragment extends Fragment {
    ListView list;
    BookAdapter adapter;
    ProgressBar pb;
    List<Book> borrowedBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ListView(getActivity());
        list.setBackgroundColor(Color.WHITE);
        borrowedBooks = new ArrayList<>();

        //Initiate progressbar
        pb = new ProgressBar(getActivity()); // Init the progressbar
        pb.setId(1); // Give pb an id
        pb.setVisibility(View.INVISIBLE); // Set progressbar visibility

        // Progressbar details
        RelativeLayout.LayoutParams progressBarDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        progressBarDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        progressBarDetails.addRule(RelativeLayout.CENTER_VERTICAL);


        if (borrowedBooks.isEmpty()) {
            try {
                requestRequests(BookshelfConstants.CONNECTION_URI);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            updateDisplay();
        }
        return list;
    }

    /**
     * Updates the display
     */
    private void updateDisplay() {
        if (borrowedBooks != null) {
            adapter = new BookAdapter(getActivity(), R.layout.item_bookborrowrequest, borrowedBooks, this);
            list.setAdapter(adapter);
        }
    }

    /**
     * Make a request request call to restful
     *
     * @param uri
     * @throws JSONException
     */
    private void requestRequests(String uri) throws JSONException {
        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        JSONArray jar = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("query", "select"); // What kind of query
        object.put("method", "getborrowbookrequest"); // What kind of request
        object.put("id", SaveSharedPreference.getID(getActivity())); // Pass the id for the user to the webservice
        jar.put(object);
        p.setJarParams(jar); // Add the param objects to the JSONArray

        MyTask task = new MyTask();
        task.execute(p);
    }

    /**
     * Make a reply request call to restful
     *
     * @param position
     * @throws JSONException
     */
    public void requestReplyRequest(int position) throws JSONException {
        BorrowedBook borrowedBook = (BorrowedBook) borrowedBooks.get(position);
        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(BookshelfConstants.CONNECTION_URI); // Sets the URI
        JSONArray jar = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("query", "update"); // What kind of query
        object.put("method", "replyborrowedbook"); // What kind of request
        object.put("friendid", SaveSharedPreference.getID(getActivity())); // Pass the id for the user to the webservice
        object.put("id", borrowedBook.getRenter().getId());
        object.put("isbn", borrowedBooks.get(position).getIsbn());
        object.put("accepted", "FALSE");
        object.put("replied", "TRUE");
        jar.put(object);
        p.setJarParams(jar); // Add the param objects to the JSONArray

        MyTask task = new MyTask();
        task.execute(p);
    }

    private class MyTask extends AsyncTask<RequestPackaging, String, String> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE); // Set progressbar visibility
        }

        @Override
        protected String doInBackground(RequestPackaging... params) {
            String content = ConnectionManager.getData(params[0]); // Get content from server/web
            return content; // Return content to calling scope (onPostExecute -> updateDisplay)
        }

        @Override
        protected void onPostExecute(String result) {
            pb.setVisibility(View.INVISIBLE); // Set progressbar visibility

            if (result == null) {
                Toast.makeText(getActivity(), getString(R.string.connection_denied_error), Toast.LENGTH_LONG).show();
            } else {
                try {
                    wrapUpCall(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Clean up process method for the post execute block in the async task
         */
        private void wrapUpCall(String result) throws JSONException, UnsupportedEncodingException {
            QueryHandler queryHandler;
            JSONArray jsonArray = null;
            DataBundler bundler = null;
            jsonArray = new JSONArray(result);
            bundler = new DataBundler(jsonArray);

            queryHandler = new QueryHandler(bundler); // create new queryHandler

            if (queryHandler.update() == 1) {
                requestRequests(BookshelfConstants.CONNECTION_URI);
            } else if (queryHandler.insert() == 0) {
                Toast.makeText(getActivity(), bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }

            if (queryHandler.select() == 1) {
                borrowedBooks = parse(bundler.getInnerArray());
                updateDisplay();
            } else if (queryHandler.select() == 0) {
                borrowedBooks = new ArrayList<>();
                updateDisplay();
                Toast.makeText(getActivity(), bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Parse JSON for BorrowedBooks
         *
         * @param jar
         * @return
         */
        private List<Book> parse(JSONArray jar) {
            List<Book> borrowedBooks = new ArrayList<>();
            try {
                for (int i = 0; i < jar.length(); i++) {
                    JSONObject job = (JSONObject) jar.get(i);
                    Book b = JSONParser.parseBook(job);
                    Friend f = JSONParser.parseFriend(job);
                    User u = JSONParser.parseUser(job);
                    BorrowedBook bb = new BorrowedBook();
                    bb.setRenter(f);
                    bb.setBook(b);
                    bb.setOwner(u);
                    borrowedBooks.add(bb);
                }
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
            }
            return borrowedBooks;
        }

    }
}
