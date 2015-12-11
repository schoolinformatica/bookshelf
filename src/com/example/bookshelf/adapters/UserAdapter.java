package com.example.bookshelf.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.BookShelf.R;
import com.example.bookshelf.fragments.FriendRequestsFragment;
import com.example.bookshelf.fragments.LoanBooksRequestsFragment;
import com.example.bookshelf.model.Friend;
import com.example.bookshelf.model.User;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 14-6-2015.
 */
public class UserAdapter extends ArrayAdapter<User> {
    Context context; // Context of the listview
    int resource; // Resource id
    List<User> user; // List with friends
    SparseBooleanArray selectedItemsIds;

    Fragment fragment = new Fragment();
    List<Friend> removedFriends = new ArrayList<Friend>();

    public UserAdapter(Context context, int resource, List<User> user) {
        super(context, resource, user);
        this.context = context;
        this.resource = resource;
        this.user = user;
        this.selectedItemsIds = new SparseBooleanArray();
    }

    public UserAdapter(Context context, int resource, List<User> user, Fragment fragment) {
        super(context, resource, user);
        this.context = context;
        this.resource = resource;
        this.user = user;
        this.selectedItemsIds = new SparseBooleanArray();
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);

        User friend = user.get(position);

        if (resource == R.layout.item_addfriend) {
            view = getSearchUserView(view, friend, position);
        }

        if (resource == R.layout.item_friend) {
           view = getFriendView(view, friend, position);
        }

        if (resource == R.layout.item_request) {
            view = getFriendRequestView(view, friend, position);
        }



        return view;
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            selectedItemsIds.put(position, value);
        else
            selectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public void cleanList() {
        selectedItemsIds = new SparseBooleanArray();
    }

    public List<Friend> getRemovedFriends() {
        return removedFriends;
    }

    public void addRemovedBooks(Friend object) {
        removedFriends.add(object);
    }


    public SparseBooleanArray getSelectedIds() {
        return selectedItemsIds;
    }


    private View getSearchUserView(View view, User friend, int position) {
        if (resource == R.layout.item_addfriend) {
            TextView tOne = (TextView) view.findViewById(R.id.textViewOne);
            TextView tTwo = (TextView) view.findViewById(R.id.textViewTwo);

            tOne.setText(friend.getFirstname() + " " + friend.getLastname());
            tOne.setTextColor(Color.BLACK);


            tTwo.setText(friend.getEmail());
            tTwo.setTextColor(Color.BLACK);

            Resources res = getContext().getResources();
            Drawable draw = res.getDrawable(R.drawable.friendtoadd);
            ImageView iOne = (ImageView) view.findViewById(R.id.imageViewOne);
            iOne.setImageDrawable(draw);

            if(selectedItemsIds.get(position)) {
                view.setBackgroundColor(Color.rgb(219, 219, 219));
            }
        }
        return view;
    }

    private View getFriendView(View view, User friend, int position) {
        if (resource == R.layout.item_friend) {
            TextView tOne = (TextView) view.findViewById(R.id.textViewOne);
            tOne.setText(friend.getFirstname() + " " + friend.getLastname());
            tOne.setTextColor(Color.BLACK);
            tOne.setTextSize(30);

            Resources res = getContext().getResources();
            Drawable draw = res.getDrawable(R.drawable.friend);
            ImageView iOne = (ImageView) view.findViewById(R.id.imageViewOne);
            iOne.setImageDrawable(draw);

            if(selectedItemsIds.get(position)) {
                view.setBackgroundColor(Color.rgb(219, 219, 219));
            }
        }
        return view;
    }

    private View getFriendRequestView(View view, User friend, int position) {
        if (resource == R.layout.item_request) {
            TextView tOne = (TextView) view.findViewById(R.id.textViewOne);
            TextView tTwo = (TextView) view.findViewById(R.id.textViewTwo);
            ImageView iOne = (ImageView) view.findViewById(R.id.imageViewOne);
            Button bOne = (Button) view.findViewById(R.id.buttonOne);
            Button bTwo = (Button) view.findViewById(R.id.buttonTwo);

            tOne.setText(friend.getFirstname() + " " + friend.getLastname());
            tTwo.setText(friend.getEmail());

            Resources res = getContext().getResources();
            Drawable draw = res.getDrawable(R.drawable.friend);
            iOne.setImageDrawable(draw);

            bOne.setBackground(context.getResources().getDrawable(R.drawable.approve_icon));
            bOne.setTag(position);
            bTwo.setBackground(context.getResources().getDrawable(R.drawable.deny_icon));
            bTwo.setTag(position);

            bOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    FriendRequestsFragment frag = (FriendRequestsFragment) fragment;
                    try {
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
                    FriendRequestsFragment frag = (FriendRequestsFragment) fragment;
                    try {
                        frag.requestReplyRequests(position, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return view;
    }

}
