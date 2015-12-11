package com.example.bookshelf.connection;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Steven on 19-5-2015.
 */

public class ConnectionManager {

//    Get data from remote
    public static String getData(RequestPackaging p) {

        BufferedReader reader = null;
        String uri = p.getUri();
        // Check if HTTP REQUEST method is GET
        if (p.getMethod().equals("GET"))
            uri += "?" + p.getEncodedParams(); // Append encoded params to the uri

        try {
            URL url = new URL(uri); // Init url object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Open up the connection
            conn.setRequestMethod(p.getMethod()); // Set the request Method

//            JSONObject json = new JSONObject(p.getParams()); // Turn map of params into JSON format
//            String params = "params=" + json.toString(); // Value to send to server (JSON FORMAT)

            String params = "params=" + p.getJarParams().toString();

            // Check if HTTP REQUEST method is POST
            if (p.getMethod().equals("POST")) {
                conn.setDoOutput(true); // OUTPUT content to body of the request
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream()); // Create writer Object that writes to the output stream and send info to connection
                // JSON REQUEST
                writer.write(params);
                // UNCOMMENT LINE BELOW IF YOU DON'T WANT TO USE JSON
//                writer.write(p.getEncodedParams()); // Add encoded params to the body of the request
                writer.flush(); // Anything written to memory flushed and send to server
                // REQUEST COMPLETED
            }

            StringBuilder sb = new StringBuilder(); // Create new Stringbuilder Object
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); // Read Inputstream of chars

            String line; // Store the line
            while ((line = reader.readLine()) != null) {
                sb.append(line + '\n'); // Append line to Stringbuilder

            }
            return sb.toString(); // Return the StringBuilder as String
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null)
                    reader.close(); // Close the reader Object
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    // Check if the network is available
    public static boolean isOnline(Context context) {
        ConnectivityManager conm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // Get the service
        NetworkInfo networkInfo = conm.getActiveNetworkInfo(); // Get the Info of the network from the service

        // Check Two states
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }
}
