package com.example.bookshelf.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;
import com.example.BookShelf.R;
import com.example.bookshelf.adapters.BookshelfViewAdapter;
import com.example.bookshelf.adapters.NavigationAdapter;
import com.example.bookshelf.model.NavDrawerItem;
import com.example.bookshelf.model.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jls on 5/25/15.
 */

public class BookshelfActivity extends FragmentActivity {
    SearchView search;
    BookshelfViewAdapter adapter;
    ViewPager pager;
    ListView drawerList;
    DrawerLayout drawerLayout;
    NavigationAdapter secondAdapter;
    ActionBarDrawerToggle drawerToggle;
    String activityTitle;
    List<NavDrawerItem> navDrawerItemList;


    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        //Instantiate objects
        pager = (ViewPager) findViewById(R.id.viewPagerOne);
        adapter = new BookshelfViewAdapter(getSupportFragmentManager(), this);

        // Navigation drawer
        drawerList = (ListView) findViewById(R.id.navList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();

        //Set the actionbar details
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(SaveSharedPreference.getFirstName(BookshelfActivity.this).substring(0, 1).toUpperCase() +
                SaveSharedPreference.getFirstName(BookshelfActivity.this).substring(1) + getString(R.string.bookshelf_postfix));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(199, 92, 92)));
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //Pager must have an id
        pager.setId(20);


        //Make new PagerTabStrip (tabs in actionbar)
        PagerTabStrip strip = new PagerTabStrip(this);
        strip.setTabIndicatorColor(Color.WHITE);
        strip.setBackgroundDrawable(new ColorDrawable(Color.rgb(199, 92, 92)));

        ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
        layoutParams.height = ViewPager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewPager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP;


        pager.addView(strip, layoutParams);
        pager.setOffscreenPageLimit(2);

        //Set adapter to view
        pager.setAdapter(adapter);

        //Show content
        setContentView(drawerLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchview, menu);

        MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
        search = (SearchView) searchViewMenuItem.getActionView();
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) search.findViewById(searchImgId);
        v.setImageResource(R.drawable.search_ico);
        search.setBackgroundColor(Color.rgb(199, 92, 92));
        search.setIconified(true);


        return super.onCreateOptionsMenu(menu);
    }



    AdapterView.OnItemClickListener onItemClickHandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent;
            if (navDrawerItemList.get(position).getName().equals(getString(R.string.booklist_label))) {
                //nothing
            }

            if (navDrawerItemList.get(position).getName().equals(getString(R.string.friendlist_label))) {
                intent = new Intent(BookshelfActivity.this, FriendlistActivity.class);
                startActivity(intent);
            }

            if (navDrawerItemList.get(position).getName().equals(getString(R.string.request_book_label))) {
                intent = new Intent(BookshelfActivity.this, RequestBookActivity.class);
                startActivity(intent);
            }

            if (navDrawerItemList.get(position).getName().equals(getString(R.string.logout_label))) {
                SaveSharedPreference.logout(BookshelfActivity.this);
                intent = new Intent(BookshelfActivity.this, LoginActivity.class);
                startActivity(intent);
                BookshelfActivity.this.finish();
            }
        }
    };

    private void addDrawerItems() {
        fillNavDrawerList();
        secondAdapter = new NavigationAdapter(this, R.layout.drawer_list_item, navDrawerItemList);
        drawerList.setAdapter(secondAdapter);
        drawerList.setOnItemClickListener(onItemClickHandler);
    }

    /**
     * Fill up the navdrawer list
     */
    private void fillNavDrawerList() {
        navDrawerItemList = new ArrayList<>();
        navDrawerItemList.add(new NavDrawerItem(getString(R.string.booklist_label), R.drawable.icon_book));
        navDrawerItemList.add(new NavDrawerItem(getString(R.string.friendlist_label), R.drawable.friend_black));
        navDrawerItemList.add(new NavDrawerItem(getString(R.string.request_book_label), R.drawable.icon_request));
        navDrawerItemList.add(new NavDrawerItem(getString(R.string.logout_label), R.drawable.icon_logout));
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(getString(R.string.navigation_title));
                getActionBar().setIcon(getResources().getDrawable(R.drawable.icon_actionbar));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(activityTitle);
                getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public void onBackPressed() {
        if (search.getVisibility() == View.VISIBLE) {
            search.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Activate the navigation drawer toggle
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            // Highlight the selected item, update the title, and close the drawer
            // update selected item and title, then close the drawer
            drawerList.setItemChecked(position, true);


            String text = "menu click... should be implemented";
            Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();

        }
    }
}



