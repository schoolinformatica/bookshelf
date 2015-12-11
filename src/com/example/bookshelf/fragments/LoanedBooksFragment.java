package com.example.bookshelf.fragments;

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
import com.example.bookshelf.activities.BookDetailsActivity;
import com.example.bookshelf.activities.LoanBookDetailsActivity;
import com.example.bookshelf.adapters.BookAdapter;
import com.example.bookshelf.connection.ConnectionManager;
import com.example.bookshelf.connection.RequestPackaging;
import com.example.bookshelf.dependencies.BookshelfConstants;
import com.example.bookshelf.dependencies.DataBundler;
import com.example.bookshelf.dependencies.QueryHandler;
import com.example.bookshelf.model.Book;
import com.example.bookshelf.model.BorrowedBook;
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
 * Created by Steven on 17-6-2015.
 * Instances of this class are fragments representing a single object in our collection.
 */
public class LoanedBooksFragment extends Fragment {
    SearchView search;
    SwipeRefreshLayout swipe;
    ListView list;
    BookAdapter adapter;
    ProgressBar pb;
    List<MyTask> tasks;
    List<Book> booklist; // Fill this list with books
    List<Friend> friendlist;
    List<Book> borrowedBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //Instantiate lists
        tasks = new ArrayList<>();
        booklist = new ArrayList<>();
        friendlist = new ArrayList<>();
        borrowedBooks = new ArrayList<>();


        //Instantiate views
        list = new ListView(getActivity());
        swipe = new SwipeRefreshLayout(getActivity());

        //Initiate progressbar for loading
        pb = new ProgressBar(getActivity()); // Init the progressbar
        pb.setId(1); // Give pb an id
        pb.setVisibility(View.INVISIBLE); // Set progressbar visibility

        //Set details list
        list.setBackgroundColor(Color.WHITE);
        list.setMultiChoiceModeListener(multiChoiceHandler);
        list.setOnItemClickListener(onItemClickHandler);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        swipe.setOnRefreshListener(refreshListener);

        if (booklist.isEmpty()) {
            try {
                requestData(BookshelfConstants.CONNECTION_URI);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            updateDisplay();
        }

        swipe.addView(list);
        return swipe;
    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            try {
                requestData(BookshelfConstants.CONNECTION_URI);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    AdapterView.OnItemClickListener onItemClickHandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BorrowedBook book = (BorrowedBook)borrowedBooks.get(position);
            Intent intent = new Intent(LoanedBooksFragment.this.getActivity(), LoanBookDetailsActivity.class);
            intent.putExtra("borrowedBook", book);

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
            menu.add(0, 0, 0, getString(R.string.take_in_label)).setIcon(R.drawable.icon_receive).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
                            adapter.addSelectedBook(selecteditem); // Add item to recently removed items list
                        }
                    }
                    // Close CAB
                    mode.finish();
                    try {
                        requestInsertData(BookshelfConstants.CONNECTION_URI);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
        search = (SearchView) searchViewMenuItem.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    Filter filter = adapter.getFilter();
                    filter.filter(newText);
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Updates the display
     */
    protected void updateDisplay() {
        // Use BookAdapter to display the data
        if (borrowedBooks != null) {
            adapter = new BookAdapter(getActivity(), R.layout.item_book_image, borrowedBooks);
            list.setAdapter(adapter); // assign the adapter to the listview
        }
        swipe.setRefreshing(false);
    }

    private void requestInsertData(String uri) throws JSONException {
        List<Book> selectedBooks = adapter.getSelectedBooks();
        for (Book b : selectedBooks) {
            RequestPackaging p = new RequestPackaging();
            p.setMethod("POST"); // Set the HTTP REQUEST method
            p.setUri(uri); // Sets the URI
            JSONArray jar = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("query", "update"); // What kind of query
            object.put("method", "receiveborrowbook"); // What kind of request
            object.put("isbn", b.getIsbn());
            object.put("id", SaveSharedPreference.getID(getActivity())); // Pass the id for the user to the webservice
            jar.put(object);
            p.setJarParams(jar); // Add the param objects to the JSONArray

            MyTask task = new MyTask();
            task.execute(p);
        }
    }

    /**
     * Make request data call to restful
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
        object.put("method", "getloanedbooks"); // What kind of request
        object.put("id", SaveSharedPreference.getID(getActivity())); // Pass the id for the user to the webservice
        jar.put(object);
        p.setJarParams(jar); // Add the param objects to the JSONArray

        MyTask task = new MyTask();
        task.execute(p);
    }

    private class MyTask extends AsyncTask<RequestPackaging, String, String> {

        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0)
                pb.setVisibility(View.VISIBLE); // Set progressbar visibility
            tasks.add(this); // Add the task to the tasks list
        }

        @Override
        protected String doInBackground(RequestPackaging... params) {
            String content = ConnectionManager.getData(params[0]); // Get content from server/web
            return content; // Return content to calling scope (onPostExecute -> updateDisplay)
        }

        @Override
        protected void onPostExecute(String result) {
            tasks.remove(this); // Remove the task if complete
            if (tasks.size() == 0)
                pb.setVisibility(View.INVISIBLE); // Set progressbar visibility

            if (result == null) {
                Toast.makeText(getActivity(), "Cannot connect to the web service", Toast.LENGTH_LONG).show();
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
                requestData(BookshelfConstants.CONNECTION_URI);
            } else if (queryHandler.update() == 0) {
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

        private List<Book> parse(JSONArray jar) {
            List<Book> borrowedBooks = new ArrayList<>();
            try {
                for (int i = 0; i < jar.length(); i++) {
                    JSONObject job = (JSONObject) jar.get(i);
                    Book b = JSONParser.parseBook(job);
                    Friend f = JSONParser.parseFriend(job);
                    BorrowedBook bb = new BorrowedBook();
                    bb.setRenter(f);
                    bb.setRentalDate(job.getString("date"));
                    bb.setBook(b);


                    borrowedBooks.add(bb);
                }
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
            }
            return borrowedBooks;
        }

    }
}
