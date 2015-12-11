package com.example.bookshelf.dependencies;

/**
 * Created by jls on 6/19/15.
 */

/**
 * Collection of globally used constants
 */
public class BookshelfConstants {

    // Networking related Constants
    public final static String CONNECTION_URI = "http://dbcon.stevenschenk.nl/queryHandler.php";

    // GoogleBookApi related Constants
    public final static String ISBN_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    // DataBundler related Constants
    public final static String RESULT_KEY_SUCCESS = "succeed";
    public final static String RESULT_KEY_QUERY = "query";
    public final static String RESULT_KEY_RESULT = "result";
    public final static String RESULT_KEY_ERROR = "error";
    public final static String RESULT_KEY_MESSAGE = "message";
    public final static String RESULT_KEY_ONE = "1";
    public final static String RESULT_KEY_ZERO = "0";

    // Query related Constants
    public final static String QUERY_INSERT = "insert";
    public final static String QUERY_SELECT = "select";
    public final static String QUERY_UPDATE = "update";
    public final static String QUERY_DELETE = "delete";

    // Scanner related Constants
    public final static String ISBN_PREFIX1 = "978";
    public final static String ISBN_PREFIX2 = "979";

    // Adapter related Constants
    public final static String PAGE_TILE_MY_BOOKS = "MIJN BOEKEN";
    public final static String PAGE_TILE_BORROWED_BOOKS = "GELEENDE BOEKEN";
    public final static String PAGE_TILE_LENT_BOOKS = "UITGELEENDE BOEKEN";
    public final static String PAGE_UKNOWN = "ONBEKEND";
    public final static String PAGE_LOANS = "UITLENINGEN";
    public final static String PAGE_LOAN = "LENINGEN";
    public final static String PaGE_FRIENDS = "VRIENDEN";
    public final static String PAGE_FRIENDREQUESTS = "VRIENDSCHAPVERZOEKEN";

    // Ordering related Constants
    public final static String ORDER_ASC = "asc";
    public final static String ORDER_DESC = "desc";

    // User related Constants
    public final static String USER_EMAIL = "email";
    public final static String USER_ID = "id";
    public final static String USER_FIRST_NAME = "firstname";
    public final static String USER_HOUSE_NUMBER = "housenumber";
    public final static String USER_LAST_NAME = "lastname";
    public final static String USER_ZIP_CODE = "zipcode";
    public final static String USER_TRUST = "trusted";
    public final static String USER_FRIEND_ID = "friendID";

    // SharedPreferences related Constants
    public final static String PREF_USER_NAME = "username";
    public final static String PREF_PASSWORD = "password";
    public final static String PREF_ID = "id";
    public final static String PREF_FIRST_NAME = "firstname";
    public final static String PREF_LAST_NAME = "lastname";
    public final static String PREF_EMAIL = "email";
    public final static String PREF_HOUSE_NUMBER = "housenumber";
    public final static String PREF_ZIP_CODE = "zipcode";
}
