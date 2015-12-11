package com.example.bookshelf.dependencies;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by jls on 6/5/15.
 */
public abstract class CMActivity extends Activity {

    public int getWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public int getHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }
}
