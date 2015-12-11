package com.example.bookshelf.googlebooks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import com.example.bookshelf.dependencies.BookshelfConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.bookshelf.model.Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.concurrent.ExecutionException;


/**
 * Created by Steven on 28-5-2015.
 */
@SuppressWarnings("ALL")
public class GoogleBookAPI {
    private Context context; //Used to get the context of the activity

    public GoogleBookAPI(Context mContext) {
        this.context = mContext;
    }

    // Received ISBN from Barcode Scanner. Send to GoogleBooks to obtain book information.
    class GoogleApiRequest extends AsyncTask<String, Object, Book> {
        String isbn;

        @Override
        protected void onPreExecute() {
            //Check if the device is connected to the internet
            if (!isNetworkConnected())
                cancel(true);
        }

        @Override
        protected Book doInBackground(String... isbn) {
            this.isbn = isbn[0];
            //If no internet available, cancel
            if (isCancelled()) {
                return null;
            }

            //String to the Google Book API
            final String apiUrlString = BookshelfConstants.ISBN_URL + this.isbn;

            try {
                HttpURLConnection connection = null;
                // Build Connection.
                try {
                    URL url = new URL(apiUrlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000); // 5 seconds
                    connection.setConnectTimeout(5000); // 5 seconds
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    connection.disconnect();
                    return null;
                }

                // Read data from response.
                StringBuilder builder = new StringBuilder();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = responseReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = responseReader.readLine();
                }
                String responseString = builder.toString();
                Log.d(getClass().getName(), "Response String: " + responseString);
                JSONObject responseJson = new JSONObject(responseString);

                // Close connection and return response code.
                connection.disconnect();
                return parseJSON(responseJson);

            } catch (SocketTimeoutException e) {
                Log.w(getClass().getName(), "Connection timed out. Returning null");
                return null;
            } catch (IOException e) {
                Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Book responseJson) {

        }


        /*
         * Method to check if device is connected with the internet
         */
        protected boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }

        /*
         * Method to parseBooks the JSON files into a Book class
         */
        protected Book parseJSON(JSONObject js) throws JSONException, UnsupportedEncodingException {
            Book book = new Book();

            //Set book values to make them show up in the JSON Object to prevent PHP errors
            book.setIsbn("");
            book.setPrint("");
            book.setCategory("");
            book.setAuthor("");
            book.setImage("");
            book.setSummary("");
            book.setLanguage("");
            book.setNote("");
            book.setPublicationDate("");
            if (js.has("items")) {
                JSONObject jo = js.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");
                JSONArray jar = (JSONArray) jo.getJSONArray("industryIdentifiers");
                JSONObject job = (JSONObject) jar.get(0);

                if (jo.has("authors")) {
                    String authors = "";
                    for (int i = 0; i < jo.getJSONArray("authors").length(); i++) {
                        authors += " ";
                        authors += jo.getJSONArray("authors").get(i);
                    }
                    book.setAuthor(authors);
                }

                if (jo.has("industryIdentifiers")) {
                    if (job.has("ISBN_13") && job.has("ISBN_10")) {
                        if (jo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("type").equals("ISBN_13"))
                            book.setIsbn(jo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier"));
                        if (jo.getJSONArray("industryIdentifiers").getJSONObject(1).getString("type").equals("ISBN_13"))
                            book.setIsbn(jo.getJSONArray("industryIdentifiers").getJSONObject(1).getString("identifier"));
                    } else
                        book.setIsbn(this.isbn);
                }

                if (jo.has("categories"))
                    book.setCategory(jo.getJSONArray("categories").getString(0));
                if (jo.has("language"))
                    book.setLanguage(jo.getString("language"));
                if (jo.has("imageLinks"))
                    if (jo.getJSONObject("imageLinks").has("smallThumbnail"))
                        book.setImage(URLEncoder.encode(jo.getJSONObject("imageLinks").getString("smallThumbnail"), "UTF-8"));
                if (jo.has("pageCount"))
                    book.setPages(jo.getString("pageCount"));
                if (jo.has("publishedDate"))
                    book.setPublicationDate(jo.getString("publishedDate"));
                if (jo.has("publisher"))
                    book.setPublisher(jo.getString("publisher"));
                if (jo.has("description"))
                    book.setSummary(jo.getString("description"));
                if (jo.has("title"))
                    book.setTitle(jo.getString("title"));


            }
            return book;

        }
    }

    /**
     * Method to search for a book by ISBN number
     */
    public Book getBookByISBN(String isbn) throws ExecutionException, InterruptedException {
        return new GoogleApiRequest().execute(isbn).get();
    }
}
