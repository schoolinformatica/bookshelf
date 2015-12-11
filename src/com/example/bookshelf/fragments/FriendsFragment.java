package com.example.bookshelf.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.bookshelf.model.Friend;
import com.example.bookshelf.model.SaveSharedPreference;
import com.example.bookshelf.model.User;
import com.example.bookshelf.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 21-6-2015.
 */
public class FriendsFragment extends Fragment {
    SwipeRefreshLayout swipe;
    UserAdapter adapter; // Adapter used for ListView
    ListView list;
    List<User> friends; // List with friends
    ProgressBar pb; // Progressbar showed when loading
    Friend friend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipe = new SwipeRefreshLayout(getActivity());

        // Set the listview background
        list = new ListView(getActivity());
        list.setBackgroundColor(Color.WHITE);
        list.setMultiChoiceModeListener(multiChoiceHandler);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        //Initiate progressbar
        pb = new ProgressBar(getActivity()); // Init the progressbar
        pb.setId(1); // Give pb an id
        pb.setVisibility(View.INVISIBLE); // Set progressbar visibility

        //Get friends
        if (friends == null) {
            try {
                requestFriendData(BookshelfConstants.CONNECTION_URI);
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

        swipe.setOnRefreshListener(refreshListener);

        //Set listener to listview items
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View clickView,
                                    int position, long id) {
                friend = (Friend) friends.get(position);
                Intent intent = new Intent(getActivity(), FriendBookshelfActivity.class);
                intent.putExtra("friend", friend);
                startActivity(intent);
            }
        });

        swipe.addView(list);

        return swipe;
    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            try {
                requestFriendData(BookshelfConstants.CONNECTION_URI);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    AbsListView.MultiChoiceModeListener multiChoiceHandler = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            // Get the total of checked items
            final int checkedCount = list.getCheckedItemCount();
            // Set the CAB title according to total checked items
            mode.setTitle(checkedCount + " Selected");
            adapter.toggleSelection(position);
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.add(0, 0, 0, "Delete friend").setIcon(R.drawable.delete_icon)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case 0:
                    // Call to getselectedId's method listViewAdapter
                    SparseBooleanArray selected = adapter.getSelectedIds();
                    // Get all selected id's through iteration
                    for (int i = (selected.size() - 1); i >= 0; i--) {
                        if (selected.valueAt(i)) {
                            Friend selecteditem = (Friend) adapter.getItem(selected.keyAt(i));
//                            // Remove selected items following the ids
//                            adapter.remove(selecteditem); // Remove the item
                            adapter.addRemovedBooks(selecteditem); // Add item to recently removed items list
                        }
                    }
                    //Clean selected list
                    adapter.cleanList();
                    // Close CAB
                    mode.finish();

                    requestDeleteFriends(BookshelfConstants.CONNECTION_URI);

                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        try {
            requestFriendData(BookshelfConstants.CONNECTION_URI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.clear();
        }
        menu.add(0, 0, 0, getString(R.string.add_friend_label)).setIcon(R.drawable.friendadd)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Updates the display
     */
    private void updateDisplay() {
        if (friends != null) {
            adapter = new UserAdapter(getActivity(), R.layout.item_friend, friends);
            list.setAdapter(adapter);
        }
        swipe.setRefreshing(false);
    }

    /**
     * Delete friends
     *
     * @param uri
     */
    private void requestDeleteFriends(String uri) {
        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        JSONArray jar = new JSONArray();
        List<Friend> deleteFriendList = adapter.getRemovedFriends(); // Get the list of removed books
        if (!deleteFriendList.isEmpty()) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.delete_prefix) + deleteFriendList.size() + " vrienden")
                    .setMessage(getString(R.string.delete_friend_message))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            for (Friend f : deleteFriendList) {
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("query", "delete"); // What kind of query
                                    object.put("method", "deletefriend"); // What kind of request
                                    object.put("id", SaveSharedPreference.getID(getActivity())); // Pass the id for the user to the webservice
                                    object.put("friendid", f.getId());
                                    jar.put(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            p.setJarParams(jar); // Add the param objects to the JSONArray

                            MyTask task = new MyTask();
                            task.execute(p);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            adapter.getRemovedFriends().clear();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    /**
     * Make friend select call to restful
     *
     * @param uri
     * @throws JSONException
     */
    private void requestFriendData(String uri) throws JSONException {

        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        JSONArray jar = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("query", "select"); // What kind of query
        object.put("method", "friend"); // What kind of request
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
            QueryHandler queryHandler;
            JSONArray jsonArray = null;
            DataBundler bundler = null;
            jsonArray = new JSONArray(result);
            bundler = new DataBundler(jsonArray);

            queryHandler = new QueryHandler(bundler); // create new queryHandler

            if (queryHandler.delete() == 1) {
                requestFriendData(BookshelfConstants.CONNECTION_URI);
            } else if (queryHandler.insert() == 0) {
                Toast.makeText(getActivity(), bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }

            if (queryHandler.select() == 1) {
                friends = userParser(bundler.getInnerArray());
                updateDisplay();
            } else if (queryHandler.select() == 0) {
                friends = userParser(bundler.getInnerArray());
                updateDisplay();
            }
        }

        /**
         * Parse JSON for User
         *
         * @param jarr
         * @return
         */
        private List<User> userParser(JSONArray jarr) {
            List<User> friends = new ArrayList<>();

            for (int i = 0; i < jarr.length(); i++) {
                try {
                    JSONObject job = (JSONObject) jarr.get(i);
                    Friend friend = new Friend();
                    friend = JSONParser.parseFriend(job);

                    friends.add(friend);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return friends;
        }
    }
}
