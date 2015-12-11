package com.example.bookshelf.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.BookShelf.R;
import com.example.bookshelf.dependencies.CMActivity;
import com.example.bookshelf.model.Book;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Wesley de Roode on 19-6-2015.
 */
public class BookDetailsActivity extends CMActivity implements Serializable {
    Book book;
    ImageView ivCover;
    TextView tvTitle;
    TextView tvAuthor;
    TextView tvRelease;
    TextView tvGenre;
    TextView tvPages;
    TextView tvPrint;
    TextView tvISBN;
    TextView tvStatus; // uitgeleend ja of nee
    TextView tvSummary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details_activity);

        // Get passed intent
        Intent intent = getIntent();

        // Set object from passed intent
        book = (Book) intent.getSerializableExtra("book");

        // Image
        ivCover = (ImageView) findViewById(R.id.ivBook);

        // Text views
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvRelease = (TextView) findViewById(R.id.tvRelease);
        tvGenre = (TextView) findViewById(R.id.tvGenre);
        tvPages = (TextView) findViewById(R.id.tvPages);
        tvPrint = (TextView) findViewById(R.id.tvPrint);
        tvISBN = (TextView) findViewById(R.id.tvISBN);
        //tvStatus = (TextView) findViewById(R.id.tvStatus); uitgeleend ja/nee
        tvSummary = (TextView) findViewById(R.id.tvSummary);

        // Sets text views with data from the selected book
        setTextFields();

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(199, 92, 92)));
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

//         Buttons & Listeners
//        Button btBack = (Button) findViewById(R.id.btBack);
//        Button btLend = (Button) findViewById(R.id.btLend);
//
//        btBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        btLend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // todo: goto uitleningen
//            }
//        });

    }

    private void setTextFields() {
        if (book != null) {
            if (book.getImage() != null) {
                ImageDownloader id = new ImageDownloader();
                id.execute(book.getImage());
            }
            if (book.getTitle() != null) {
                tvTitle.setText(getString(R.string.title_prefix) + book.getTitle());
            } else {
                tvTitle.setText(getString(R.string.title_not_available_message));
            }
            if (book.getAuthor() != null) {
                tvAuthor.setText(getString(R.string.author_prefix) + book.getAuthor());
            } else {
                tvAuthor.setText(getString(R.string.author_not_available_message));
            }
            if (book.getPublicationDate() != null) {
                tvRelease.setText(getString(R.string.release_date_prefix) + book.getPublicationDate());
            } else {
                tvRelease.setText(getString(R.string.release_date_not_available_message));
            }
            if (book.getCategory() != null) {
                tvGenre.setText(getString(R.string.category_prefix) + book.getCategory());
            } else {
                tvGenre.setText(getString(R.string.category_not_available_message));
            }
            if (book.getCategory() != null) {
                tvPages.setText(getString(R.string.pages_prefix) + book.getPages());
            } else {
                tvPages.setText(getString(R.string.pages_not_available_message));
            }
            if (book.getPrint() != null) {
                tvPrint.setText(getString(R.string.print_prefx) + book.getPrint());
            } else {
                tvPrint.setText(getString(R.string.print_not_available_message));
            }
            if (book.getIsbn() != null) {
                tvISBN.setText(getString(R.string.isbn_prefix) + book.getIsbn());
            } else {
                tvISBN.setText(getString(R.string.isbn_not_available_message));
            }
            if (book.getSummary() != null) {
                tvSummary.setText(getString(R.string.summary_prefix) + "\n\n" + book.getSummary());
            } else {
                tvSummary.setText(getString(R.string.summary_not_available_message));
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.loading_book_failed_message),
                    Toast.LENGTH_LONG).show();
        }

    }

    private class ImageDownloader extends AsyncTask<String, Integer, Bitmap> {
        protected void onPreExecute() {
            //Setup is done here
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpCon =
                        (HttpURLConnection) url.openConnection();
                if (httpCon.getResponseCode() != 200)
                    throw new Exception("Failed to connect");
                InputStream is = httpCon.getInputStream();
                return BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                Log.e("Image", "Failed to load image", e);
            }
            return null;
        }

        protected void onProgressUpdate(Integer... params) {
            //Update a progress bar here, or ignore it, it's up to you
        }

        protected void onPostExecute(Bitmap img) {
            ImageView iv = (ImageView) findViewById(R.id.ivBook);
            if (iv != null && img != null) {
                iv.setVisibility(View.VISIBLE); // Added toggle for imageview
                iv.setImageBitmap(img);
            }
        }

        protected void onCancelled() {
        }
    }
}

