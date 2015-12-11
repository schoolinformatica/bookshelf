package com.example.bookshelf.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.bookshelf.dependencies.BookshelfConstants;
import com.example.bookshelf.fragments.BookshelfFragment;
import com.example.bookshelf.fragments.BorrowedBooksFragment;
import com.example.bookshelf.fragments.LoanedBooksFragment;


/**
 * Created by Steven on 15-6-2015.
 */
public class BookshelfViewAdapter extends FragmentStatePagerAdapter {
    Context context;

    public BookshelfViewAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new Fragment();
        if (i == 0) {
            fragment = new BookshelfFragment();
        }
        if (i == 1) {
            fragment = new BorrowedBooksFragment();
        }
        if (i == 2) {
            fragment = new LoanedBooksFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return BookshelfConstants.PAGE_TILE_MY_BOOKS;
        }
        if (position == 1) {
            return BookshelfConstants.PAGE_TILE_BORROWED_BOOKS;
        }
        if (position == 2) {
            return BookshelfConstants.PAGE_TILE_LENT_BOOKS;
        }
        return BookshelfConstants.PAGE_UKNOWN;
    }
}
