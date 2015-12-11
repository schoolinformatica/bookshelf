package com.example.bookshelf.dependencies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jls on 6/19/15.
 */

/**
 * Grab the JSONObject data from the results of the restful service
 */
public class DataBundler {

    JSONObject outerObject;
    JSONObject innerObject;
    JSONArray innerArray;
    JSONArray outerArray;

    public DataBundler(JSONArray json) {
        try {
            this.outerObject = (JSONObject) json.get(0); // Grab the object in the json array
            this.innerObject = (JSONObject) (new JSONArray(outerObject.getString(BookshelfConstants.RESULT_KEY_RESULT))).get(0); // Grab the object from the nested array
            this.innerArray =  new JSONArray(outerObject.getString(BookshelfConstants.RESULT_KEY_RESULT));
            this.outerArray = json;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getInnerObject() {
        return innerObject;
    }

    public JSONObject getOuterObject() {
        return outerObject;
    }

    public JSONArray getInnerArray() {
        return innerArray;
    }

    public JSONArray getOuterArray() {
        return outerArray;
    }
}
