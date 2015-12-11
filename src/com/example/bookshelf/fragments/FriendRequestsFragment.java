package com.example.bookshelf.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.*;
import com.example.BookShelf.R;
import com.example.bookshelf.activities.FriendBookshelfActivity;
import com.example.bookshelf.activities.NewFriendActivity;
import com.example.bookshelf.adapters.UserAdapter;
import com.example.bookshelf.connection.ConnectionManager;
import com.example.bookshelf.connection.RequestPackaging;
import com.example.bookshelf.dependencies.BookshelfConstants;
import com.example.bookshelf.dependencies.DataBundler;
import com.example.bookshelf.dependencies.QueryHandler;
import com.example.bookshelf.model.BorrowedBook;
import com.example.bookshelf.model.Friend;
import com.example.bookshelf.model.SaveSharedPreference;
import com.example.bookshelf.model.User;
import com.example.bookshelf.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 21-6-2015.
 */
public class FriendRequestsFragment extends Fragment {
    UserAdapter adapter; // Adapter used for ListView
    ListView list;
    List<User> friends; // List with friends
    ProgressBar pb; // Progressbar showed when loading
    Friend friend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the listview background
        list = new ListView(getActivity());
        list.setBackgroundColor(Color.WHITE);


        //Initiate progressbar
        pb = new ProgressBar(getActivity()); // Init the progressbar
        pb.setId(1); // Give pb an id
        pb.setVisibility(View.INVISIBLE); // Set progressbar visibility

        //Get friends
        if (friends == null) {
            try {
                requestGetRequests(BookshelfConstants.CONNECTION_URI);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            updateDisplay();
        }

        // Progressbar details
        RelativeLayout.LayoutParams progressBarDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        progressBarDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        progressBarDetails.addRule(RelativeLayout.CENTER_VERTICAL);

        //To make actionbar menu appear
        setHasOptionsMenu(true);

        return list;
    }


    /**
     * Updates the display
     */
    private void updateDisplay() {
        adapter = new UserAdapter(getActivity(), R.layout.item_request, friends, this);
        list.setAdapter(adapter);
    }


    /**
     * Make request replies request call to restful
     *
     * @param position
     * @param b
     * @throws JSONException
     */
    public void requestReplyRequests(int position, boolean b) throws JSONException {
        User friend = friends.get(position);
        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(BookshelfConstants.CONNECTION_URI); // Sets the URI
        JSONArray jar = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("query", "update"); // What kind of query
        object.put("method", "replyfriendrequest"); // What kind of request
        object.put("id", SaveSharedPreference.getID(getActivity())); // Pass the id for the user to the webservice
        object.put("friendid", friend.getId());
        if (b)
            object.put("accepted", "TRUE");
        else
            object.put("accepted", "FALSE");
        jar.put(object);
        p.setJarParams(jar); // Add the param objects to the JSONArray

        MyTask task = new MyTask();
        task.execute(p);
    }

    /**
     * Make request get requests call to restful
     *
     * @param uri
     * @throws JSONException
     */
    public void requestGetRequests(String uri) throws JSONException {
        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        JSONArray jar = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("query", "select"); // What kind of query
        object.put("method", "getfriendrequests"); // What kind of request
        object.put("id", SaveSharedPreference.getID(getActivity())); // Pass the id for the user to the webservice
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
            Log.d("result", result);
            QueryHandler queryHandler;
            JSONArray jsonArray = null;
            DataBundler bundler = null;
            jsonArray = new JSONArray(result);
            bundler = new DataBundler(jsonArray);
            queryHandler = new QueryHandler(bundler); // create new queryHandler

            if (queryHandler.select() == 1) {
                friends = userParser(bundler.getInnerArray());
                updateDisplay();
            } else if (queryHandler.select() == 0) {
                friends = new ArrayList<>();
                updateDisplay();
                Toast.makeText(getActivity(), bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }

            if (queryHandler.update() == 1) {
                requestGetRequests(BookshelfConstants.CONNECTION_URI);
            } else if (queryHandler.update() == 0) {
                Toast.makeText(getActivity(), bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Parse JSON for User
         * @param jarr
         * @return
         */
        private List<User> userParser(JSONArray jarr) {
            List<User> friends = new ArrayList<>();

            for (int i = 0; i < jarr.length(); i++) {
                try {
                    JSONObject job = (JSONObject) jarr.get(i);
                    Friend friend = JSONParser.parseFriend(job);

                    friends.add(friend);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return friends;
        }
    }
}
