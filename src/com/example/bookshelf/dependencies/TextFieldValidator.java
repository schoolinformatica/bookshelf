package com.example.bookshelf.dependencies;

import android.widget.EditText;

/**
 * Created by Mert on 19-6-2015.
 */
public class TextFieldValidator {


    // Check for a valid email
    public static boolean isValidEmail(EditText field)
    {
        if (field != null && field.getText().toString().contains("@")) {
            return true;
        }
        return false;
    }

    // Check if all textfields are not empty
    public static boolean isValidUserInformation(EditText[] fields)
    {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField == null || currentField.getText().toString().length() <= 0) {
                return false;
            }
        }
        return true;
    }
}
