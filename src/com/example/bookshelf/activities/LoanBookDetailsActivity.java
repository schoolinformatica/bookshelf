package com.example.bookshelf.activities;

import android.app.ActionBar;
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
import com.example.BookShelf.R;
import com.example.bookshelf.dependencies.CMActivity;
import com.example.bookshelf.model.BorrowedBook;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Steven on 21-6-2015.
 */
public class LoanBookDetailsActivity extends CMActivity {
    ImageView ivCover;
    TextView tvTitle;
    TextView tvAuthor;
    TextView tvRelease;
    TextView tvFriend;
    TextView tvDate;
    BorrowedBook borrowedBook;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrowed_book_activity);

        borrowedBook = (BorrowedBook)getIntent().getSerializableExtra("borrowedBook");

        ivCover = (ImageView) findViewById(R.id.ivBook);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvRelease = (TextView) findViewById(R.id.tvRelease);
        tvFriend = (TextView) findViewById(R.id.tvFriend);
        tvDate = (TextView) findViewById(R.id.tvDate);

        fillFields();

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(199, 92, 92)));
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

    }

    private void fillFields() {
        ImageDownloader id = new ImageDownloader();
        id.execute(borrowedBook.getImage());

        tvTitle.setText(borrowedBook.getTitle());
        tvAuthor.setText(borrowedBook.getAuthor());
        tvRelease.setText(borrowedBook.getPublicationDate());
        tvFriend.setText(borrowedBook.getRenter().getFirstname()+" "+borrowedBook.getRenter().getLastname());
        tvDate.setText(borrowedBook.getRentalDate());
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
