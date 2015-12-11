package com.example.bookshelf.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.*;
import com.example.BookShelf.R;
import com.example.bookshelf.connection.ConnectionManager;
import com.example.bookshelf.connection.RequestPackaging;
import com.example.bookshelf.dependencies.*;
import com.example.bookshelf.model.SaveSharedPreference;
import com.example.bookshelf.model.User;
import com.example.bookshelf.notification.GcmController;
import com.example.bookshelf.security.Security;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends CMActivity {

    CMTextField tfEmail;
    CMTextField tfPassword;
    Button btLogin;
    Button btRegister;
    ImageView img;
    ProgressBar pb;
    List<MyTask> tasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Register device by Google Cloud Messaging
        GcmController controller = new GcmController(this);
        controller.setRegId();

        tasks = new ArrayList<>(); // Initialize tasks list to keep track of background tasks

        // Create the layout
        RelativeLayout lOne = new RelativeLayout(this); // Create RelativeLayout
        lOne.setBackgroundColor(Color.WHITE);

        // Action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // Init Components

        // Text fields
        tfEmail = new CMTextField(this, 1, getString(R.string.email_textfield_hint));                                                                                  // E-mail field
        tfPassword = new CMTextField(this, 2, getString(R.string.password_textfield_hint), new PasswordTransformationMethod(), getString(R.string.required_label));                            // Password field

        // Buttons
        btLogin = new CMButton(this, 4, getString(R.string.login_button_label), btLoginHandler);                                                                   // Login button
        btRegister = new CMButton(this, 5, getString(R.string.registreren_button_label), btRegisterHandler);                                                           // Register button

        // Progressbar
        pb = new ProgressBar(this);
        pb.setId(6);
        pb.setVisibility(View.INVISIBLE);

        // Image
        img = new ImageView(this);
        img.setImageResource(R.drawable.login_logo);
        img.setId(7);

        // Details for different Components
        RelativeLayout.LayoutParams progressBarDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams emailDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams passwordDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams loginButtonDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams registerButtonDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams imageDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        // Progressbar details
        progressBarDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        progressBarDetails.addRule(RelativeLayout.CENTER_VERTICAL);

        // Email details
        emailDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        emailDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        emailDetails.setMargins(0, 0, 0, 50);
        emailDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Password details
        passwordDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        passwordDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        passwordDetails.addRule(RelativeLayout.BELOW, tfEmail.getId());
        passwordDetails.setMargins(0, 0, 0, 50);
        passwordDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Login Button details
        loginButtonDetails.addRule(RelativeLayout.BELOW, tfPassword.getId());
        loginButtonDetails.addRule(RelativeLayout.ALIGN_START, tfPassword.getId());
        loginButtonDetails.width = (int) ((getWidth() * 0.1) * 3.5);

        // Register button details
        registerButtonDetails.addRule(RelativeLayout.BELOW, tfPassword.getId());
        registerButtonDetails.addRule(RelativeLayout.ALIGN_END, tfPassword.getId());
        registerButtonDetails.width = (int) ((getWidth() * 0.1) * 3.5);

        // Image details
        imageDetails.addRule(RelativeLayout.ABOVE, tfEmail.getId());
        imageDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageDetails.addRule(RelativeLayout.CENTER_VERTICAL);

        // Add the views
        lOne.addView(tfEmail, emailDetails);
        lOne.addView(tfPassword, passwordDetails);
        lOne.addView(btLogin, loginButtonDetails);
        lOne.addView(btRegister, registerButtonDetails);
        lOne.addView(pb, progressBarDetails);
        lOne.addView(img, imageDetails);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.addView(lOne);
        setContentView(scrollView); // Set Main Interface for the activities

    }

    /**
     * Make a access request to restful
     *
     * @param uri
     * @param user
     * @throws JSONException
     */
    private void requestAccess(String uri, User user) throws JSONException {
        RequestPackaging p = new RequestPackaging();
        JSONObject object = new JSONObject();
        JSONArray jar = new JSONArray();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        object.put("query", "select"); // What kind of query
        object.put("method", "login"); // What kind of request

        // Start of the user credentials
        object.put("email", user.getEmail());
        object.put("password", user.getPasssword());
        jar.put(object); // add the json object to the json array
        p.setJarParams(jar); // move the list to packaging

        MyTask task = new MyTask();
        task.execute(p);
    }

    /**
     * Handler for the loginButton
     */
    View.OnClickListener btLoginHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            User user = new User();
            user.setEmail(tfEmail.getText().toString());
            user.setPasssword(Security.sha256(tfPassword.getText().toString()));

            try {
                requestAccess(BookshelfConstants.CONNECTION_URI, user); // Request access
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Handler for the registerButton
     */
    View.OnClickListener btRegisterHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(LoginActivity.this, UserRegisterActivity.class);
            LoginActivity.this.startActivity(myIntent);
            LoginActivity.this.finish();
        }
    };

    private class MyTask extends AsyncTask<RequestPackaging, String, String> {

        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0)
                pb.setVisibility(View.VISIBLE); // Set progressbar visibility
            tasks.add(this); // Add the task to the tasks list
        }

        @Override
        protected String doInBackground(RequestPackaging... params) {
            String content = ConnectionManager.getData(params[0]); // Get content from server/web
            return content; // Return content to calling scope
        }

        @Override
        protected void onPostExecute(String result) {
            tasks.remove(this); // Remove the task if complete
            if (tasks.size() == 0)
                pb.setVisibility(View.INVISIBLE); // Set progressbar visibility
            if (result == null) {
                Toast.makeText(LoginActivity.this, getString(R.string.connection_denied_error), Toast.LENGTH_LONG).show();
            } else {
                try {
                    wrapUpCall(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Clean up process method for the post execute block in the async task
         */
        private void wrapUpCall(String result) throws JSONException, UnsupportedEncodingException {
            QueryHandler queryHandler;
            JSONArray jsonArray = null;
            DataBundler bundler = null;
            jsonArray = new JSONArray(result);
            bundler = new DataBundler(jsonArray);

            queryHandler = new QueryHandler(bundler); // create new queryHandler

            if (queryHandler.select() == 1) {
                Intent credentials = new Intent(LoginActivity.this, BookshelfActivity.class); // create new intent
                SaveSharedPreference.setUserName(LoginActivity.this, bundler.getInnerObject()
                        .getString(BookshelfConstants.PREF_USER_NAME));
                SaveSharedPreference.setPassword(LoginActivity.this, bundler.getInnerObject()
                        .getString(BookshelfConstants.PREF_PASSWORD));
                SaveSharedPreference.setID(LoginActivity.this, bundler.getInnerObject()
                        .getString(BookshelfConstants.PREF_ID));
                SaveSharedPreference.setFirstName(LoginActivity.this, bundler.getInnerObject()
                        .getString(BookshelfConstants.PREF_FIRST_NAME));
                SaveSharedPreference.setLastName(LoginActivity.this, bundler.getInnerObject()
                        .getString(BookshelfConstants.PREF_LAST_NAME));
                SaveSharedPreference.setEmail(LoginActivity.this, bundler.getInnerObject()
                        .getString(BookshelfConstants.PREF_EMAIL));
                SaveSharedPreference.setHouseNumber(LoginActivity.this, bundler.getInnerObject()
                        .getString(BookshelfConstants.PREF_HOUSE_NUMBER));
                SaveSharedPreference.setZipCode(LoginActivity.this, bundler.getInnerObject()
                        .getString(BookshelfConstants.PREF_ZIP_CODE));
                startActivity(credentials); // Start new activity
                LoginActivity.this.finish(); // End the activity

            } else if (queryHandler.select() == 0) {
                Toast.makeText(LoginActivity.this, bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }
        }
    }
}