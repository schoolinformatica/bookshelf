package com.example.bookshelf.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.*;
import com.example.BookShelf.R;
import com.example.bookshelf.activities.BookDetailsActivity;
import com.example.bookshelf.activities.NewBookActivity;
import com.example.bookshelf.adapters.BookAdapter;
import com.example.bookshelf.connection.ConnectionManager;
import com.example.bookshelf.connection.RequestPackaging;
import com.example.bookshelf.dependencies.BookshelfConstants;
import com.example.bookshelf.dependencies.DataBundler;
import com.example.bookshelf.dependencies.ListViewSorter;
import com.example.bookshelf.dependencies.QueryHandler;
import com.example.bookshelf.googlebooks.GoogleBookAPI;
import com.example.bookshelf.model.Book;
import com.example.bookshelf.model.SaveSharedPreference;
import com.example.bookshelf.parser.JSONParser;
import com.googlezxing.integration.android.IntentIntegrator;
import com.googlezxing.integration.android.IntentResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Steven on 15-6-2015.
 * Instances of this class are fragments representing a single object in our collection.
 */
public class BookshelfFragment extends Fragment {
    SwipeRefreshLayout swipe;
    RelativeLayout rl;
    ListView list;
    SearchView search;
    BookAdapter adapter;
    ProgressBar pb;
    List<MyTask> tasks;
    List<Book> booklist; // Fill this list with books
    List<Book> tempBookList; // Temporary list of books to be passed to the web service
    List<Book> deleteBookList;
    DrawerLayout drawer;

    @SuppressLint("NewApi")

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rl = new RelativeLayout(getActivity());
        list = loadFragment();
        swipe = new SwipeRefreshLayout(getActivity());


        RelativeLayout.LayoutParams pSwipe = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pSwipe.addRule(RelativeLayout.BELOW, 1);

        swipe.setOnRefreshListener(refreshListener);

        swipe.addView(list);
        rl.addView(swipe, pSwipe);

        return rl;
    }

    /**
     * Loads the current activity
     *
     * @return
     */
    private ListView loadFragment() {
        //Instantiate lists
        tasks = new ArrayList<>();
        tempBookList = new ArrayList<>();
        deleteBookList = new ArrayList<>();

        //Instantiate listview
        list = new ListView(getActivity());

        //Initiate progressbar for loading
        pb = new ProgressBar(getActivity()); // Init the progressbar
        pb.setId(1); // Give pb an id
        pb.setVisibility(View.INVISIBLE); // Set progressbar visibility

        //Set details list
        list.setBackgroundColor(Color.WHITE);
        list.setMultiChoiceModeListener(multiChoiceHandler);
        list.setOnItemClickListener(onItemClickHandler);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setTextFilterEnabled(true);

        try {
            requestData(BookshelfConstants.CONNECTION_URI);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
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
            Book book = booklist.get(position);
            Intent intent = new Intent(BookshelfFragment.this.getActivity(), BookDetailsActivity.class);
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
            menu.add(0, 0, 0, "Delete book").setIcon(R.drawable.delete_icon)
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
//                            // Remove selected items following the ids
//                            adapter.remove(selecteditem); // Remove the item
                            adapter.addSelectedBook(selecteditem); // Add item to recently removed items list
                        }
                    }
                    // Close CAB
                    mode.finish();
                    try {
                        requestDeleteData(BookshelfConstants.CONNECTION_URI);
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

        menu.add(0, 0, 0, getString(R.string.add_book_label)).setIcon(R.drawable.add_book)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 1, 0, getString(R.string.scan_book_label)).setIcon(R.drawable.camera_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

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
            case 0: // Go to BooksToAddActivity (NOTE: Will result in redirect to NewBookActivity)
                intent = new Intent(getActivity(), NewBookActivity.class);
                startActivityForResult(intent, 1);
//                finish();
                return true;
            case 1: // Scan book
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Triggered after the scan completed.
     * Triggered after the manual input is completed.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) { // Request code for manual adding of books
            if (resultCode == getActivity().RESULT_OK) {
                Book book = (Book) intent.getSerializableExtra("book");
                tempBookList.add(book); // Add book to tempBookList
                try {
                    requestInsertData(BookshelfConstants.CONNECTION_URI);
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), getString(R.string.book_adding_error),
                            Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                // Not successful
            }
        }

        /**
         * Code for the scanning results
         */
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            if (scanContent != null) {
                if (isISBN(scanContent, getActivity())) { // Check if scanContent equals valid ISBN
                    GoogleBookAPI api = new GoogleBookAPI(getActivity());
                    try {
                        Book book = api.getBookByISBN(scanContent); // assign values to tempbook
                        if (book == null) {
                        } else if (checkBook(book)) { // Check validity of tempbook
                            tempBookList.add(book); // Add Book to the tempBookList
                            requestInsertData(BookshelfConstants.CONNECTION_URI);
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.no_valid_book_data_available_message)
                                    , Toast.LENGTH_LONG).show();
                        }
                    } catch (ExecutionException | InterruptedException | JSONException e) {
                        Toast.makeText(getActivity(), getString(R.string.book_adding_error),
                                Toast.LENGTH_LONG).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            } else {
            }
        }
    }

    /**
     * Checks if the inputted EAN13 equals a valid ISBN.
     *
     * @param ean from user input
     * @return true if ean equals PREFIX1 or PREFIX2
     */
    protected boolean isISBN(String ean, Context context) {
        boolean check = false;
        try {
            String str = ean.substring(0, 3); // Get prefix
            if (str.equals(BookshelfConstants.ISBN_PREFIX1) || str.equals(BookshelfConstants.ISBN_PREFIX2)) {
                check = true;
            }
        } catch (StringIndexOutOfBoundsException e) {
            Toast.makeText(BookshelfFragment.this.getActivity(),
                    getString(R.string.isbn_invalid_message),
                    Toast.LENGTH_LONG).show();
        }
        return check;
    }

    /**
     * Checks the book for the required data
     */
    private boolean checkBook(Book b) {
        if (!b.getIsbn().equals("") && !b.getAuthor().equals("") &&
                !b.getTitle().equals("")) {
            return true;
        }
        return false;
    }


    /**
     * Updates the display
     */
    protected void updateDisplay() {
        // Use BookAdapter to display the data
        if (booklist != null) {
            adapter = new BookAdapter(getActivity(), R.layout.item_book_image, booklist);
            list.setAdapter(adapter); // assign the adapter to the listview
        }
        swipe.setRefreshing(false);
    }

    /**
     * Make insert data call to restful
     *
     * @param uri
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    private void requestInsertData(String uri) throws JSONException, UnsupportedEncodingException {

        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        JSONArray jar = new JSONArray();
        for (Book b : tempBookList) {
            JSONObject object = new JSONObject();
            object.put("query", "insert"); // What kind of query
            object.put("method", "book"); // What kind of request
            object.put("image", b.getImage());
            object.put("id", SaveSharedPreference.getID(getActivity())); // Pass the id for the user to the webservice
            object.put("title", Base64.encodeToString(b.getTitle().getBytes("UTF-8"), Base64.DEFAULT));
            object.put("author", b.getAuthor());
            object.put("summary", Base64.encodeToString(b.getSummary().getBytes("UTF-8"), Base64.DEFAULT));
            object.put("category", b.getCategory());
            object.put("publisher", b.getPublisher());
            object.put("language", b.getLanguage());
            object.put("print", b.getPrint());
            object.put("note", b.getNote());
            object.put("pages", b.getPages());
            object.put("isbn", b.getIsbn());
            object.put("read", String.valueOf(b.isRead()));
            object.put("publicationDate", b.getPublicationDate());
            jar.put(object);
        }
        p.setJarParams(jar); // Add the param objects to the JSONArray

        MyTask task = new MyTask();
        task.execute(p);
    }

    /**
     * Make a delete request call to restful
     *
     * @param uri
     * @throws JSONException
     */
    private void requestDeleteData(String uri) throws JSONException {
        RequestPackaging p = new RequestPackaging();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        JSONArray jar = new JSONArray();
        deleteBookList = adapter.getSelectedBooks(); // Get the list of removed books
        if (!deleteBookList.isEmpty()) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.delete_prefix) + deleteBookList.size() + getString(R.string.book_postfix))
                    .setMessage(getString(R.string.delete_books_message))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            for (Book b : deleteBookList) {
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("query", "delete"); // What kind of query
                                    object.put("method", "book"); // What kind of request
                                    object.put("id", SaveSharedPreference.getID(getActivity())); // Pass the id for the user to the webservice
                                    object.put("isbn", b.getIsbn());
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
                            deleteBookList.clear();
                            adapter.getSelectedBooks().clear();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    /**
     * Make a request data call to restful
     *
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

            if (queryHandler.insert() == 1) {
                Toast.makeText(getActivity(), bundler.getInnerObject()
                        .getString(BookshelfConstants.RESULT_KEY_MESSAGE), Toast.LENGTH_LONG).show();
                tempBookList.clear(); // Clear the tempBooklist
                requestData(BookshelfConstants.CONNECTION_URI);
            } else if (queryHandler.insert() == 0) {
                Toast.makeText(getActivity(), bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }

            if (queryHandler.select() == 1) {
                booklist = parse(bundler.getInnerArray()); // Parse data
                ListViewSorter.sortBookByTitle(booklist, BookshelfConstants.ORDER_ASC);
                updateDisplay();
            } else if (queryHandler.select() == 0) {
                Toast.makeText(getActivity(), bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }

            if (queryHandler.delete() == 1) {
                deleteBookList.clear(); // Clear the deleteBookList
                requestData(BookshelfConstants.CONNECTION_URI);
                updateDisplay();
                Toast.makeText(getActivity(), bundler.getInnerObject()
                        .getString(BookshelfConstants.RESULT_KEY_MESSAGE), Toast.LENGTH_LONG).show();
            } else if (queryHandler.delete() == 0) {
                Toast.makeText(getActivity(), bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Parse JSON for Books
         *
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
