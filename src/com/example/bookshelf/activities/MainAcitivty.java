package com.example.bookshelf.activities;

import android.content.Intent;
import android.os.Bundle;
import com.example.bookshelf.dependencies.CMActivity;
import com.example.bookshelf.model.SaveSharedPreference;

/**
 * Created by jls on 6/10/15.
 */
public class MainAcitivty extends CMActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        redirect(); // Redirect activity
    }

    private void redirect() {
        // Check if the user preferences are known
        Intent intent;
        if (SaveSharedPreference.getUserName(MainAcitivty.this).length() != 0 &&
                SaveSharedPreference.getPassword(MainAcitivty.this).length() != 0 &&
                SaveSharedPreference.getID(MainAcitivty.this).length() != 0) {
            intent = new Intent(MainAcitivty.this, BookshelfActivity.class);
            startActivity(intent);
            MainAcitivty.this.finish();
        } else {
            intent = new Intent(MainAcitivty.this, LoginActivity.class);
            startActivity(intent);
            MainAcitivty.this.finish();
        }
    }
}
