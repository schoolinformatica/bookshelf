package com.example.bookshelf.connection;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jls on 5/22/15.
 */

// Used to package the data that will be sent to the webservice
public class RequestPackaging {

    private String uri;
    private String method = "GET";
    private Map<String, String> params = new HashMap<>();
    private JSONArray jarParams;


    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setParam(String key, String value) {
        this.params.put(key, value);
    }

    public void setJarParams(JSONArray array) {
        this.jarParams = array;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JSONArray getJarParams() {
        return jarParams;
    }

    public Map<String, String> getParams() {
        return params;
    }
    public String getMethod() {
        return method;
    }
    public String getUri() {
        return uri;
    }


    // Used to pass data for HTTP request
    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(params.get(key), "UTF-8"); // Use the key to retrieve value
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (sb.length() > 0) // Exec only if it has something inside
                sb.append("&");
            sb.append(key + "=" + value);
        }
        return sb.toString(); // Return StringBuilder Object as string
    }
}
