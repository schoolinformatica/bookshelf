package com.example.bookshelf.model;

/**
 * Created by jls on 6/21/15.
 */
public class NavDrawerItem {
    public int icon;
    public String name;

    public NavDrawerItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }
}
