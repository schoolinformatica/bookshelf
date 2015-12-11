package com.example.bookshelf.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.example.BookShelf.R;
import com.example.bookshelf.dependencies.CMActivity;
import com.example.bookshelf.dependencies.CMButton;
import com.example.bookshelf.dependencies.CMTextField;
import com.example.bookshelf.model.Book;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class NewBookActivity extends CMActivity {

    CMTextField tfIsbn;
    CMTextField tfTitle;
    CMTextField tfAuthor;
    CMTextField tfYearOfRelease;
    CMTextField tfGenre;
    CMTextField tfDescription;
    CMTextField tfPrint;
    CMTextField tfPages;
    CMTextField tfImage;
    CMButton btNext;
    ImageView img;
    Book book;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable scroll
        ScrollView scroll = new ScrollView(this);

        // Remove action bar
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(199, 92, 92)));
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        //Layout One
        RelativeLayout lOne = new RelativeLayout(this);
        lOne.setBackgroundColor(Color.WHITE);

        // Layout Two
        RelativeLayout lTwo = new RelativeLayout(this);                                                  // Create relativeLayout
        lTwo.setBackgroundColor(Color.WHITE);

        // Text fields
        tfIsbn = new CMTextField(this, 1, getString(R.string.isbn_textfield_hint));                                                       // ISBN number
        tfTitle = new CMTextField(this, 2, getString(R.string.title_textfield_hint));                    // Title of the book
        tfAuthor = new CMTextField(this, 3, getString(R.string.author_textfield_hint));                  // Author of the book
        tfYearOfRelease = new CMTextField(this, 4, getString(R.string.year_of_release_textfield_hint));  // Year the book was released
        tfGenre = new CMTextField(this, 5, getString(R.string.category_textfield_hint));
        tfDescription = new CMTextField(this, 6, getString(R.string.summary_textfield_hint));
        tfPrint = new CMTextField(this, 7, getString(R.string.print_textfield_hint));
        tfPages = new CMTextField(this, 8, getString(R.string.pages_textfield_hint));
        tfImage = new CMTextField(this, 9, getString(R.string.image_textfield_hint));

        // Buttons
        btNext = new CMButton(this, 10, getString(R.string.next_button_label), btVerderHandler);

        // Image
        img = new ImageView(this);
        img.setImageResource(R.drawable.book_ico);
        img.setId(13);

        RelativeLayout.LayoutParams tfIsbnDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfImageDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfTitleDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfAuthorDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfYearOfReleaseDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfGenreDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfDescriptionDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfPrintDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfPagesDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams imageDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams btNextDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams lTwoDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );


        // Image details
        imageDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        imageDetails.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // ISBN details
        tfIsbnDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfIsbnDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfIsbnDetails.addRule(RelativeLayout.BELOW, img.getId());
        tfIsbnDetails.setMargins(0, 50, 0, 50);
        tfIsbnDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Title details
        tfTitleDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfTitleDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfTitleDetails.addRule(RelativeLayout.BELOW, tfIsbn.getId());
        tfTitleDetails.setMargins(0, 0, 0, 50);
        tfTitleDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Author details
        tfAuthorDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfAuthorDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfAuthorDetails.addRule(RelativeLayout.BELOW, tfTitle.getId());
        tfAuthorDetails.setMargins(0, 0, 0, 50);
        tfAuthorDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Publication year details
        tfYearOfReleaseDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfYearOfReleaseDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfYearOfReleaseDetails.addRule(RelativeLayout.BELOW, tfAuthor.getId());
        tfYearOfReleaseDetails.setMargins(0, 0, 0, 50);
        tfYearOfReleaseDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Genre details
        tfGenreDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfGenreDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfGenreDetails.addRule(RelativeLayout.BELOW, tfYearOfRelease.getId());
        tfGenreDetails.setMargins(0, 0, 0, 50);
        tfGenreDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Description details
        tfDescriptionDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfDescriptionDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfDescriptionDetails.addRule(RelativeLayout.BELOW, tfGenre.getId());
        tfDescriptionDetails.setMargins(0, 0, 0, 50);
        tfDescriptionDetails.width = (int) ((getWidth() * 0.1) * 7.5);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(150000); // Make it really really long
        tfDescription.setFilters(FilterArray); // set the filter

        // Print details
        tfPrintDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfPrintDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfPrintDetails.addRule(RelativeLayout.BELOW, tfDescription.getId());
        tfPrintDetails.setMargins(0, 0, 0, 50);
        tfPrintDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Pages details
        tfPagesDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfPagesDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfPagesDetails.addRule(RelativeLayout.BELOW, tfPrint.getId());
        tfPagesDetails.setMargins(0, 0, 0, 50);
        tfPagesDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Image url details
        tfImageDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfImageDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfImageDetails.addRule(RelativeLayout.BELOW, tfPages.getId());
        tfImageDetails.setMargins(0, 0, 0, 50);
        tfImageDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Next button details
        btNextDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btNextDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        btNextDetails.addRule(RelativeLayout.BELOW, tfImage.getId());
        btNextDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Layout two details
        lTwoDetails.addRule(RelativeLayout.CENTER_IN_PARENT);

        // Add the views
        lTwo.addView(img, imageDetails);
        lTwo.addView(tfIsbn, tfIsbnDetails);
        lTwo.addView(tfTitle, tfTitleDetails);
        lTwo.addView(tfAuthor, tfAuthorDetails);
        lTwo.addView(tfYearOfRelease, tfYearOfReleaseDetails);
        lTwo.addView(tfGenre, tfGenreDetails);
        lTwo.addView(tfDescription, tfDescriptionDetails);
        lTwo.addView(tfPrint, tfPrintDetails);
        lTwo.addView(tfPages, tfPagesDetails);
        lTwo.addView(tfImage, tfImageDetails);
        lTwo.addView(btNext, btNextDetails);

        lOne.addView(lTwo, lTwoDetails);
        scroll.addView(lOne);
        setContentView(scroll);
    }

    /**
     * Button next listener.
     */
    View.OnClickListener btVerderHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (readFields()) {
                Intent data = new Intent();
                data.putExtra("book", book);
                setResult(RESULT_OK, data);
                finish();
            } else {
                Toast.makeText(NewBookActivity.this, getString(R.string.required_field_message), Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * Read put fields and checks for field values
     */
    private boolean readFields() {
        if (!tfAuthor.getText().toString().equals("") &&
                !tfIsbn.getText().toString().equals("") && !tfTitle.getText().toString().equals("")) {
            book = new Book(); // Init book
            book.setIsbn(tfIsbn.getText().toString());
            book.setTitle(tfTitle.getText().toString());
            book.setAuthor(tfAuthor.getText().toString());
            book.setPublicationDate(tfYearOfRelease.getText().toString());
            book.setCategory(tfGenre.getText().toString());
            book.setSummary(tfDescription.getText().toString());
            book.setPrint(tfPrint.getText().toString());
            book.setPages(tfPages.getText().toString());
            try {
                book.setImage(URLEncoder.encode(tfImage.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
