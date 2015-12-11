package com.example.bookshelf.dependencies;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

/**
 * Customized button to match design
 */
public class CMButton extends Button {

    /**
     * Default constructor
     *
     * @param context  activity identifier
     * @param text     displayed on button
     * @param listener listener / handler
     * @param id       button identifier
     */
    public CMButton(Context context, int id, String text, OnClickListener listener) {
        super(context);

        this.setBackgroundColor(Color.rgb(199, 92, 92));
        this.setTextColor(Color.rgb(255, 255, 255));
        this.setText(text);
        this.setOnClickListener(listener);
        this.setId(id);
    }

}