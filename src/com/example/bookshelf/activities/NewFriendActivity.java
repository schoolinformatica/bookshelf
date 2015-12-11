package com.example.bookshelf.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.BookShelf.R;
import com.example.bookshelf.adapters.UserAdapter;
import com.example.bookshelf.connection.ConnectionManager;
import com.example.bookshelf.connection.RequestPackaging;
import com.example.bookshelf.dependencies.*;
import com.example.bookshelf.model.SaveSharedPreference;
import com.example.bookshelf.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Steven on 13-6-2015.
 */
public class NewFriendActivity extends CMActivity {
    List<User> users;
    RelativeLayout main;
    RelativeLayout rl;
    CMTextField tfemail;
    CMButton btnsearch;
    ListView listview;
    ArrayAdapter<User> adapter;
    User friendToAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate components
        rl = new RelativeLayout(this);
        tfemail = new CMTextField(this, 1, getString(R.string.email_or_username_textfield_hint));
        btnsearch = new CMButton(this, 2, getString(R.string.search_label), searchHandler);
        listview = new ListView(this);

        // Set details parent layout
        rl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rl.setBackgroundColor(Color.WHITE);
        rl.setId(3);

        // Create design components
        RelativeLayout.LayoutParams tfemailLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams btnsearchLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams listviewLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        // Add additional design components
        tfemailLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfemailLayout.setMargins(0, 20, 0, 50);
        tfemailLayout.width = (int) (0.9 * getWidth());
        tfemail.setPadding(20, 10, 20, 10);
        tfemail.setHeight(100);
        tfemail.setTextSize(20);

        btnsearchLayout.addRule(RelativeLayout.BELOW, tfemail.getId());
        btnsearchLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnsearchLayout.width = (int) (0.9 * getWidth());
        btnsearchLayout.setMargins(0, 0, 0, 50);

        listviewLayout.addRule(RelativeLayout.BELOW, btnsearch.getId());

        // Set the title for the actionbar
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(getString(R.string.add_friend_title));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(199, 92, 92)));
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        //Add views to layout
        rl.addView(tfemail, tfemailLayout);
        rl.addView(btnsearch, btnsearchLayout);
        rl.addView(listview, listviewLayout);

        //Set content view
        setContentView(rl);

        //Set listener to listview items
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View clickView,
                                    int position, long id) {
                friendToAdd = users.get(position);

                new AlertDialog.Builder(NewFriendActivity.this)
                        .setTitle(getString(R.string.add_friend_title))
                        .setMessage(getString(R.string.do_you_want_to_prefix) + friendToAdd.getFirstname() + " " + friendToAdd.getLastname() + getString(R.string.add_as_friend_postfix))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    requestAddFriend(BookshelfConstants.CONNECTION_URI);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                friendToAdd = null;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    // Search button
    View.OnClickListener searchHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                requestFriend(BookshelfConstants.CONNECTION_URI); // Go back to previous activity
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Updates the display
     */
    private void updateDisplay() {
        adapter = new UserAdapter(this, R.layout.item_addfriend, users);
        listview.setAdapter(adapter);
    }

    // Request for network data communication here
    // Pass in params/queries here
    private void requestFriend(String uri) throws JSONException {
        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        JSONArray jar = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("query", "select"); // What kind of query
        object.put("method", "findfriend"); // What kind of request
        object.put("searchname", tfemail.getText()); // Pass the id for the user to the webservice
        jar.put(object);
        p.setJarParams(jar); // Add the param objects to the JSONArray

        MyTask task = new MyTask();
        task.execute(p);
    }

    private void requestAddFriend(String uri) throws JSONException {
        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        JSONArray jar = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("query", "insert"); // What kind of query
        object.put("method", "addfriend"); // What kind of request
        object.put("id", SaveSharedPreference.getID(NewFriendActivity.this)); // Pass the id for the user to the webservice
        object.put("friendid", friendToAdd.getId());
        object.put("trusted", "true");
        jar.put(object);
        p.setJarParams(jar); // Add the param objects to the JSONArray

        MyTask task = new MyTask();
        task.execute(p);
    }

    private class MyTask extends AsyncTask<RequestPackaging, String, String> {


        @Override
        protected String doInBackground(RequestPackaging... params) {
            String content = ConnectionManager.getData(params[0]); // Get content from server/web
            return content; // Return content to calling scope (onPostExecute -> updateDisplay)
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(NewFriendActivity.this, getString(R.string.connection_denied_error), Toast.LENGTH_LONG).show();
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

            if (queryHandler.insert() == 1) {
                Toast.makeText(NewFriendActivity.this, getString(R.string.friend_added_message), Toast.LENGTH_LONG).show();
            } else if (queryHandler.insert() == 0) {
                Toast.makeText(NewFriendActivity.this, bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }

            if (queryHandler.select() == 1) {
                users = parseUser(bundler.getInnerArray());
                updateDisplay();
            } else if (queryHandler.select() == 0) {
                Toast.makeText(NewFriendActivity.this, bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Parse the JSON for Users
         * @param jar
         * @return
         * @throws JSONException
         */
        private List<User> parseUser(JSONArray jar) throws JSONException {
            List<User> users = new ArrayList<User>();
            for (int i = 0; i < jar.length(); i++) {
                User user = new User();
                JSONObject job = (JSONObject) jar.get(i);

                user.setEmail(job.getString(BookshelfConstants.USER_EMAIL));
                user.setId(job.getString(BookshelfConstants.USER_ID));
                user.setZip(job.getString(BookshelfConstants.USER_ZIP_CODE));
                user.setFirstname(job.getString(BookshelfConstants.USER_FIRST_NAME));
                user.setLastname(job.getString(BookshelfConstants.USER_LAST_NAME));
                user.setHouseNumber(job.getString(BookshelfConstants.USER_HOUSE_NUMBER));
                users.add(user);
            }
            return users;
        }
    }
}
