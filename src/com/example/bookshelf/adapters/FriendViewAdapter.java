package com.example.bookshelf.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.bookshelf.dependencies.BookshelfConstants;
import com.example.bookshelf.fragments.FriendRequestsFragment;
import com.example.bookshelf.fragments.FriendsFragment;

/**
 * Created by Steven on 21-6-2015.
 */
public class FriendViewAdapter extends FragmentStatePagerAdapter {
    Context context;

    public FriendViewAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new Fragment();
        if (i == 0) {
            fragment = new FriendsFragment();
        }
        if (i == 1) {
            fragment = new FriendRequestsFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return BookshelfConstants.PaGE_FRIENDS;
        }
        if (position == 1) {
            return BookshelfConstants.PAGE_FRIENDREQUESTS;
        }
        return BookshelfConstants.PAGE_UKNOWN;
    }
}
