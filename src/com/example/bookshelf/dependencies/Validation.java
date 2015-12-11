package com.example.bookshelf.dependencies;

import java.util.regex.Pattern;

public class Validation {

    // Regular expressions
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String POSTALCODE_REGEX = "^[1-9][0-9]{3}[\\s]?[A-Za-z]{2}$";
    private static final String ISBN_REGEX = "^(97(8|9))?\\d{9}(\\d|X)$";

    // Error messages
    private static String REQUIRED_MSG = "Vereist!";
    private static String EMAIL_MSG = "Ongeldig E-mailadres!";
    private static String POSTAL_CODE_MSG = "Ongeldige postcode!";
    private static String ISBN_MSG = "Ongeldige ISBN!";

    // Check email
    public static boolean checkEmailAddress(CMTextField editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // Check postal code
    public static boolean checkPostalCode(CMTextField editText, boolean required) {
        return isValid(editText, POSTALCODE_REGEX, POSTAL_CODE_MSG, required);
    }

    // Check ISBN
    public static boolean checkISBN(CMTextField editText, boolean required) {
        return isValid(editText, ISBN_REGEX, ISBN_MSG, required);
    }

    // Check if textfield has text
    public static boolean hasText(CMTextField editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(CMTextField editText, String regex, String errorMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if (required && !hasText(editText)) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errorMsg);
            return false;
        }

        return true;
    }

}

