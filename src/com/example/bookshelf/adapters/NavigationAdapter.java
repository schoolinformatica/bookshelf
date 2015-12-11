package com.example.bookshelf.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.BookShelf.R;
import com.example.bookshelf.model.NavDrawerItem;

import java.util.List;

/**
 * Created by jls on 6/21/15.
 */
public class NavigationAdapter extends ArrayAdapter<NavDrawerItem> {
    private final Context context;
    private final int layoutResourceId;
    private List<NavDrawerItem> data = null;

    public NavigationAdapter(Context context, int layoutResourceId, List<NavDrawerItem> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View v = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageView = (ImageView) v.findViewById(R.id.navDrawerImageView);
        TextView textView = (TextView) v.findViewById(R.id.navDrawerTextView);

        textView.setTextColor(Color.BLACK);
//        textView.setClickable(true);
        NavDrawerItem choice = data.get(position);

        imageView.setImageResource(choice.icon);
        textView.setText(choice.name);

        return v;
    }
}
