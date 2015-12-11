package com.example.bookshelf.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.*;
import com.example.BookShelf.R;
import com.example.bookshelf.fragments.BorrowBooksRequestsFragment;
import com.example.bookshelf.fragments.LoanBooksRequestsFragment;
import com.example.bookshelf.model.Book;
import com.example.bookshelf.model.BorrowedBook;
import com.example.bookshelf.model.Friend;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jls on 6/5/15.
 */
public class BookAdapter extends ArrayAdapter<Book> implements Filterable {

    public List<Book> orig;
    private List<Book> booklist;
    private final List<Book> selectedBooks = new ArrayList<>();
    private final List<Book> borrowBooks = new ArrayList<>();
    private final Context context;
    private final int resource;
    private SparseBooleanArray mSelectedItemsIds;

    private Fragment fragment = new Fragment();

    // for normal views
    public BookAdapter(Context context, int resource, List<Book> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.booklist = objects;
        this.mSelectedItemsIds = new SparseBooleanArray();
    }
    // for fragments
    public BookAdapter(Context context, int resource, List<Book> objects, Fragment fragment) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.booklist = objects;
        this.mSelectedItemsIds = new SparseBooleanArray();
        this.fragment = fragment;
    }


    // For ech item displayed this will be called
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);


        if (resource == R.layout.item_book_image) {
            Book book = booklist.get(position); // Pull book out of the booklist
            view = getViewBookshelf(view, book, position);
        }

        if (resource == R.layout.item_request) {
            BorrowedBook b = (BorrowedBook) booklist.get(position);
            Friend friend = b.getRenter();
            view = getViewBookLoanRequest(view, b, friend, position);
        }

        if(resource == R.layout.item_bookborrowrequest) {
            BorrowedBook b = (BorrowedBook) booklist.get(position);
            Friend friend = b.getRenter();
            view = getViewBookBorrowRequest(view, b, friend, position);
        }

        return view;
    }

    @Override
    public int getCount() {
        return booklist.size();
    }

    @Override
    public void remove(Book object) {
        booklist.remove(object);
        notifyDataSetChanged();
    }

    public List<Book> getBooklist() {
        return booklist;
    }

    public List<Book> getSelectedBooks() {
        return selectedBooks;
    }

    public void addSelectedBook(Book object) {
        selectedBooks.add(object);
    }

    public void addBorrowBooks(Book object) {borrowBooks.add(object);}

    public List<Book> getBorrowBooks() {return borrowBooks;}

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    private View getViewBookshelf(View view, Book book, int position) {
        // Display bookname in the TextView widget
        if (resource == R.layout.item_book_image) {
            TextView tv = (TextView) view.findViewById(R.id.textViewTwo);
            TextView tvsub = (TextView) view.findViewById(R.id.textViewThree);
            tv.setText(book.getTitle()); // Show the title of the book
            tv.setTextColor(Color.BLACK);
            tvsub.setText("Author: " + book.getAuthor());
            tvsub.setTextColor(Color.BLACK);

            if (mSelectedItemsIds.get(position)) {
                ImageView originalImage = (ImageView) view.findViewById(R.id.imageViewOne);
                ImageView newImage = (ImageView) view.findViewById(R.id.imageViewTwo);
                final float ROTATE_FROM = 0.0f;
                final float ROTATE_TO = -10.0f * 360.0f;// 3.141592654f * 32.0f;
                RotateAnimation r;
                r = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                newImage.setImageResource(R.drawable.mark_ico);
                r.setDuration((long) 2 * 500);
                r.setRepeatCount(0);
                newImage.startAnimation(r);
                originalImage.setVisibility(View.INVISIBLE); // Set original image invisible
                newImage.setVisibility(View.VISIBLE); // Load new Image
                view.setBackgroundColor(Color.rgb(219, 219, 219));
            } else {
                // Display Image in the TextView Widget
                if (!book.getImage().equals("") && book.getImage() != null) {
                    // Display book photo in ImageView
                    if (book.getBitmap() != null) { // Not in memory display
                        ImageView image = (ImageView) view.findViewById(R.id.imageViewOne);
                        image.setImageBitmap(book.getBitmap()); // Retrieve bitmap
                    } else { // Package it up and execute
                        BookAndView container = new BookAndView();
                        container.book = book;
                        container.view = view;

                        Imageloader loader = new Imageloader();
                        loader.execute(container);
                    }
                }
            }
        }
        return view;
    }

    private View getViewBookLoanRequest(View view, Book book, Friend friend, int position) {
        if (resource == R.layout.item_request) {
            ImageView iOne = (ImageView) view.findViewById(R.id.imageViewOne);
            TextView tOne = (TextView) view.findViewById(R.id.textViewOne);
            TextView tTwo = (TextView) view.findViewById(R.id.textViewTwo);
            Button bOne = (Button) view.findViewById(R.id.buttonOne);
            Button bTwo = (Button) view.findViewById(R.id.buttonTwo);

            tOne.setText(book.getTitle());
            tTwo.setText(friend.getFirstname() + " " + friend.getLastname());
            bOne.setBackground(context.getResources().getDrawable(R.drawable.approve_icon));
            bOne.setTag(position);
            bTwo.setBackground(context.getResources().getDrawable(R.drawable.deny_icon));
            bTwo.setTag(position);
            iOne.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_request));

            bOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    try {
                        LoanBooksRequestsFragment frag = (LoanBooksRequestsFragment) fragment;
                        frag.requestReplyRequests(position, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            bTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    try {
                        LoanBooksRequestsFragment frag = (LoanBooksRequestsFragment) fragment;
                        frag.requestReplyRequests(position, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private View getViewBookBorrowRequest(View view, Book book, Friend friend, int position) {
        if (resource == R.layout.item_bookborrowrequest) {
            ImageView iOne = (ImageView) view.findViewById(R.id.imageViewOne);
            TextView tOne = (TextView) view.findViewById(R.id.textViewOne);
            TextView tTwo = (TextView) view.findViewById(R.id.textViewTwo);
            Button bTwo = (Button) view.findViewById(R.id.buttonTwo);

            tOne.setText(book.getTitle());
            tTwo.setText(friend.getFirstname() + " " + friend.getLastname());
            bTwo.setBackground(context.getResources().getDrawable(R.drawable.deny_icon));
            bTwo.setTag(position);
            iOne.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_request));


            bTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=(Integer)v.getTag();
                    try {
                        BorrowBooksRequestsFragment frag = (BorrowBooksRequestsFragment) fragment;
                        frag.requestReplyRequest(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return view;
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    class BookAndView {
        public Book book;
        public View view;
        public Bitmap bitmap;
    }

    private class Imageloader extends AsyncTask<BookAndView, Void, BookAndView> {

        @Override
        protected BookAndView doInBackground(BookAndView... params) {
            BookAndView container = params[0]; // Get reference
            Book book = container.book; // Grab the book
            try {
                URLConnection connection;
                URL url = new URL(book.getImage());
                connection = url.openConnection();
                connection.connect();
                InputStream in = (InputStream) url.getContent(); // Retrieve content (BLOB)
                Bitmap bitmap = BitmapFactory.decodeStream(in); // Take Blob turn into object
                book.setBitmap(bitmap); // Add bitmap to book
                in.close(); // Close stream
                container.bitmap = bitmap; // Store the bitmap
                return container;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(BookAndView bookAndView) {
            ImageView image = (ImageView) bookAndView.view.findViewById(R.id.imageViewOne);
            image.setImageBitmap(bookAndView.bitmap); // Display bitmap
            bookAndView.book.setBitmap(bookAndView.bitmap); // Save for future use
        }
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Book> results = new ArrayList<>();
                if (orig == null)
                    orig = booklist;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Book g : orig) {
                            if (g.getTitle().toLowerCase().contains(constraint.toString()) || g.getAuthor().toLowerCase().contains(constraint.toString())){
                                results.add(g);
                            }
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                booklist = (ArrayList<Book>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
