package com.example.bookshelf.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.BookShelf.R;
import com.example.bookshelf.adapters.BookAdapter;
import com.example.bookshelf.connection.ConnectionManager;
import com.example.bookshelf.connection.RequestPackaging;
import com.example.bookshelf.dependencies.BookshelfConstants;
import com.example.bookshelf.dependencies.DataBundler;
import com.example.bookshelf.dependencies.QueryHandler;
import com.example.bookshelf.model.Book;
import com.example.bookshelf.model.Friend;
import com.example.bookshelf.model.SaveSharedPreference;
import com.example.bookshelf.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Steven on 15-6-2015.
 */
public class FriendBookshelfActivity extends ListActivity {
    BookAdapter adapter;
    ListView list;
    List<Book> books;
    Friend friend;
    ProgressBar pb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = getListView();
        list.setBackgroundColor(Color.WHITE);
//        list.setOnItemLongClickListener(itemLongClickHandler);
        list.setMultiChoiceModeListener(multiChoiceHandler);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setOnItemClickListener(onItemClickHandler);

        friend = (Friend) getIntent().getSerializableExtra("friend");

        // Init Components

        pb = new ProgressBar(this); // Init the progressbar
        pb.setId(1); // Give pb an id
        pb.setVisibility(View.INVISIBLE); // Set progressbar visibility

        // Details for different Components
        RelativeLayout.LayoutParams progressBarDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        // Progressbar details
        progressBarDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        progressBarDetails.addRule(RelativeLayout.CENTER_VERTICAL);

        // Set the title for the actionbar
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(friend.getFirstname().substring(0, 1).toUpperCase() + friend.getFirstname()
                .substring(1) + getString(R.string.bookshelf_postfix));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(199, 92, 92)));
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        if (books == null) {
            try {
                requestData(BookshelfConstants.CONNECTION_URI);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            updateDisplay();
        }
    }

    AdapterView.OnItemClickListener onItemClickHandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Book book = books.get(position);
            Intent intent = new Intent(FriendBookshelfActivity.this, BookDetailsActivity.class);
            intent.putExtra("book", book);

            startActivity(intent);
        }
    };

    AbsListView.MultiChoiceModeListener multiChoiceHandler = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            // Get the total of checked items
            final int checkedCount = list.getCheckedItemCount();
            // Set the CAB title according to total checked items
            mode.setTitle(checkedCount + getString(R.string.selected_postfix));
            // Call to the toggle method of the adapter
            adapter.toggleSelection(position);
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.add(0, 0, 0, getString(R.string.borrow_book_label)).setIcon(R.drawable.borrow_icon)
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
                            Book selecteditem = adapter.getItem(selected.keyAt(i));
                            adapter.addBorrowBooks(selecteditem); // Add item to recently removed items list
                        }
                    }
                    // Close CAB
                    mode.finish();
                    //Insert data in database
                    try {
                        requestInsertBook(BookshelfConstants.CONNECTION_URI);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.removeSelection();
        }
    };

    /**
     * Updates the display
     */
    protected void updateDisplay() {
        // Use BookAdapter to display the data
        adapter = new BookAdapter(this, R.layout.item_book_image, books);
        setListAdapter(adapter); // assign the adapter to the listview
    }

    /**
     * Make a data request to restful
     * @param uri
     * @throws JSONException
     */
    private void requestData(String uri) throws JSONException {

        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        JSONArray jar = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("query", "select"); // What kind of query
        object.put("method", "book"); // What kind of request
        object.put("id", friend.getId()); // Pass the id for the user to the webservice
        jar.put(object);
        p.setJarParams(jar); // Add the param objects to the JSONArray

        MyTask task = new MyTask();
        task.execute(p);
    }

    /**
     * Make a book insert call to restful
     * @param uri
     * @throws JSONException
     */
    private void requestInsertBook(String uri) throws JSONException {
        for (Book b : adapter.getBorrowBooks()) {
            RequestPackaging p = new RequestPackaging();
            p.setMethod("POST"); // Set the HTTP REQUEST method
            p.setUri(uri); // Sets the URI
            JSONArray jar = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("query", "insert"); // What kind of query
            object.put("method", "addborrowbook"); // What kind of request
            object.put("friendid", friend.getId()); // Pass the id for the user to the webservice
            object.put("id", SaveSharedPreference.getID(FriendBookshelfActivity.this)); // Pass the id for the user to the webservice)
            object.put("note", "");
            object.put("isbn", b.getIsbn());
            object.put("renterid", SaveSharedPreference.getID(FriendBookshelfActivity.this));
            object.put("accepted", "FALSE");
            jar.put(object);
            p.setJarParams(jar); // Add the param objects to the JSONArray

            MyTask task = new MyTask();
            task.execute(p);
        }
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
                Toast.makeText(FriendBookshelfActivity.this, getString(R.string.connection_denied_error), Toast.LENGTH_LONG).show();
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
                Toast.makeText(FriendBookshelfActivity.this, bundler.getInnerObject()
                        .getString(BookshelfConstants.RESULT_KEY_MESSAGE), Toast.LENGTH_LONG).show();
            } else if (queryHandler.insert() == 0) {
                Toast.makeText(FriendBookshelfActivity.this, bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }

            if (queryHandler.select() == 1) {
                books = parse(bundler.getInnerArray()); // Parse data
                updateDisplay();
            } else if (queryHandler.select() == 0) {
                Toast.makeText(FriendBookshelfActivity.this, bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Parse the JSON for friends
         * @param jar
         * @return
         * @throws JSONException
         * @throws UnsupportedEncodingException
         */
        private List<Book> parse(JSONArray jar) throws JSONException, UnsupportedEncodingException {
            List<Book> books = new ArrayList<>();
            for (int i = 0; i < jar.length(); i++) {
                JSONObject job = (JSONObject) jar.get(i);
                Book book = JSONParser.parseBook(job);

                books.add(book);
            }
            return books;
        }
    }
}
