package com.example.bookshelf.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.BookShelf.R;
import com.example.bookshelf.connection.ConnectionManager;
import com.example.bookshelf.connection.RequestPackaging;
import com.example.bookshelf.dependencies.*;
import com.example.bookshelf.model.User;
import com.example.bookshelf.notification.GcmController;
import com.example.bookshelf.security.Security;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class UserRegisterActivity extends CMActivity {

    CMTextField tfUsername;
    CMTextField tfFirstName;
    CMTextField tfLastName;
    CMTextField tfHouseNumber;
    CMTextField tfZip;
    CMTextField tfEmail;
    CMTextField tfPassword;
    CMButton btConfirm;
    CMButton btCancel;
    ImageView img;
    List<MyTask> tasks;
    ProgressBar pb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tasks = new ArrayList<>(); // Initialize tasks list to keep track of background tasks

        // Enable scroll
        ScrollView scroll = new ScrollView(this);

        // Remove action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        //Layout One
        RelativeLayout lOne = new RelativeLayout(this);
        lOne.setBackgroundColor(Color.WHITE);

        // Layout Two
        RelativeLayout lTwo = new RelativeLayout(this);                                                                  // Create relativeLayout
        lTwo.setBackgroundColor(Color.WHITE);

        // Set Background color layout

        // Text fields
        tfUsername = new CMTextField(this, 1, getString(R.string.username_textfield_hint), getString(R.string.required_label));                                              // Username
        tfFirstName = new CMTextField(this, 2, getString(R.string.firstname_textfield_hint), getString(R.string.required_label));                                                   // First name
        tfLastName = new CMTextField(this, 3, getString(R.string.lastname_textfield_hint), getString(R.string.required_label));                                                  // Last name
        tfHouseNumber = new CMTextField(this, 4, getString(R.string.housenumber_textfield_hint));                                                          // House number
        tfZip = new CMTextField(this, 5, getString(R.string.zip_textfield_hint), getString(R.string.postcode_label));                                                        // Postal code
        tfEmail = new CMTextField(this, 6, getString(R.string.email_textfield_hint), getString(R.string.email_label));                                                           // E-mail
        tfPassword = new CMTextField(this, 8, getString(R.string.password_textfield_hint), new PasswordTransformationMethod(), getString(R.string.required_label));              // Password

        // Buttons
        btConfirm = new CMButton(this, 10, getString(R.string.confirm_button_label), confirmHandler); // Confirm button
        btCancel = new CMButton(this, 11, getString(R.string.cancel_button_label), cancelHandler);    // Cancel button

        // Image
        img = new ImageView(this);
        img.setImageResource(R.drawable.new_user);
        img.setId(12);

        // Progressbar
        pb = new ProgressBar(this);
        pb.setId(13);
        pb.setVisibility(View.INVISIBLE);

        // Details for different Components
        RelativeLayout.LayoutParams progressBarDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfUsernameDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfFirstNameDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        RelativeLayout.LayoutParams tfLastNameDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfHouseNumberDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfZipDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfEmailDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams tfPasswordDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams btConfirmDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams btCancelDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams lTwoDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams imageDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        // Image details
        imageDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        imageDetails.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // Username details
        tfUsernameDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfUsernameDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfUsernameDetails.addRule(RelativeLayout.BELOW, img.getId());
        tfUsernameDetails.setMargins(0, 0, 0, 50);
        tfUsernameDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // First name details
        tfFirstNameDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfFirstNameDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfFirstNameDetails.addRule(RelativeLayout.BELOW, tfUsername.getId());
        tfFirstNameDetails.setMargins(0, 0, 0, 50);
        tfFirstNameDetails.width = (int) ((getWidth() * 0.1) * 7.5);


        // Last name details
        tfLastNameDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfLastNameDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfLastNameDetails.addRule(RelativeLayout.BELOW, tfFirstName.getId());
        tfLastNameDetails.setMargins(0, 0, 0, 50);
        tfLastNameDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // House number details
        tfHouseNumberDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfHouseNumberDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfHouseNumberDetails.addRule(RelativeLayout.BELOW, tfLastName.getId());
        tfHouseNumberDetails.setMargins(0, 0, 0, 50);
        tfHouseNumberDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Zip details
        tfZipDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfZipDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfZipDetails.addRule(RelativeLayout.BELOW, tfHouseNumber.getId());
        tfZipDetails.setMargins(0, 0, 0, 50);
        tfZipDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Email details
        tfEmailDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfEmailDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfEmailDetails.addRule(RelativeLayout.BELOW, tfZip.getId());
        tfEmailDetails.setMargins(0, 0, 0, 50);
        tfEmailDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Password details
        tfPasswordDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tfPasswordDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        tfPasswordDetails.addRule(RelativeLayout.BELOW, tfEmail.getId());
        tfPasswordDetails.setMargins(0, 0, 0, 50);
        tfPasswordDetails.width = (int) ((getWidth() * 0.1) * 7.5);

        // Confirm button details
        btConfirmDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btConfirmDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        btConfirmDetails.addRule(RelativeLayout.BELOW, tfPassword.getId());
        btConfirmDetails.addRule(RelativeLayout.ALIGN_START, tfPassword.getId());
        btConfirmDetails.width = (int) ((getWidth() * 0.1) * 3.5);
        btConfirmDetails.setMargins(0, 0, 0, 75);

        // Cancel button details
        btCancelDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btCancelDetails.addRule(RelativeLayout.CENTER_VERTICAL);
        btCancelDetails.addRule(RelativeLayout.BELOW, tfPassword.getId());
        btCancelDetails.addRule(RelativeLayout.ALIGN_END, tfPassword.getId());
        btCancelDetails.width = (int) ((getWidth() * 0.1) * 3.5);
        btCancelDetails.setMargins(0, 0, 0, 75);

        // Progressbar details
        progressBarDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        progressBarDetails.addRule(RelativeLayout.CENTER_VERTICAL);

        // Layout two details
        lTwoDetails.addRule(RelativeLayout.CENTER_IN_PARENT);

        // Add the views
        lTwo.addView(img, imageDetails);
        lTwo.addView(tfUsername, tfUsernameDetails);
        lTwo.addView(tfFirstName, tfFirstNameDetails);
        lTwo.addView(tfLastName, tfLastNameDetails);
        lTwo.addView(tfHouseNumber, tfHouseNumberDetails);
        lTwo.addView(tfZip, tfZipDetails);
        lTwo.addView(tfEmail, tfEmailDetails);
        lTwo.addView(tfPassword, tfPasswordDetails);
        lTwo.addView(btConfirm, btConfirmDetails);
        lTwo.addView(btCancel, btCancelDetails);

        lOne.addView(lTwo, lTwoDetails);
        scroll.setFillViewport(true);
        scroll.addView(lOne);
        setContentView(scroll); // Set Main Interface for the activities
    }


    // Handler for the confirm button
    View.OnClickListener confirmHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean fieldsOK = TextFieldValidator.isValidUserInformation(new EditText[]{tfUsername, tfFirstName, tfLastName, tfHouseNumber, tfZip, tfEmail, tfPassword}); // validate following fields
            boolean emailOK = TextFieldValidator.isValidEmail(tfEmail);
            if (fieldsOK && emailOK) {
                User user = new User();
                user.setUsername(tfUsername.getText().toString());
                user.setFirstname(tfFirstName.getText().toString());
                user.setLastname(tfLastName.getText().toString());
                user.setHouseNumber(tfHouseNumber.getText().toString());
                user.setZip(tfZip.getText().toString());
                user.setEmail(tfEmail.getText().toString());
                user.setPasssword(Security.sha256(tfPassword.getText().toString()));

                try {
                    requestRegister(BookshelfConstants.CONNECTION_URI, user); // Request Register
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.invalid_textfields_message), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };


    /**
     * Handler for the cancel button
     */
    View.OnClickListener cancelHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserRegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };

    /**
     * Make request register call to restful
     *
     * @param uri
     * @param user
     * @throws JSONException
     */
    private void requestRegister(String uri, User user) throws JSONException {
        GcmController controller = new GcmController(getBaseContext());
        RequestPackaging p = new RequestPackaging();
        JSONObject object = new JSONObject();
        JSONArray jar = new JSONArray();
        p.setMethod("POST"); // Set the HTTP REQUEST method
        p.setUri(uri); // Sets the URI
        object.put("query", "insert"); // What kind of query
        object.put("method", "register"); // What kind of request

        // Start of the user credentials
        object.put("GCMkey", controller.getKey());
        object.put("email", user.getEmail());
        object.put("password", user.getPasssword());
        object.put("username", user.getUsername());
        object.put("firstname", user.getFirstname());
        object.put("lastname", user.getLastname());
        object.put("zip", user.getZip());
        object.put("housenumber", user.getHouseNumber());

        jar.put(object); // add the json object to the json array
        p.setJarParams(jar); // move the list to packaging

        MyTask task = new MyTask();
        task.execute(p);
    }

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
            return content; // Return content to calling scope (onPostExecute -> updateDisplay)
        }

        @Override
        protected void onPostExecute(String result) {
            tasks.remove(this); // Remove the task if complete
            if (tasks.size() == 0)
                pb.setVisibility(View.INVISIBLE); // Set progressbar visibility
            if (result == null) {
                Toast.makeText(UserRegisterActivity.this, getString(R.string.connection_denied_error), Toast.LENGTH_LONG).show();
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
            Log.d("result", result);
            QueryHandler queryHandler;
            JSONArray jsonArray = null;
            DataBundler bundler = null;
            jsonArray = new JSONArray(result);
            bundler = new DataBundler(jsonArray);

            queryHandler = new QueryHandler(bundler); // create new queryHandler

            if (queryHandler.insert() == 1) {
                Intent credentials = new Intent(UserRegisterActivity.this, LoginActivity.class);
                startActivity(credentials); // Start new activity
                Toast.makeText(UserRegisterActivity.this, bundler.getInnerObject()
                        .getString(BookshelfConstants.RESULT_KEY_MESSAGE), Toast.LENGTH_LONG).show();
                UserRegisterActivity.this.finish(); // End the activity
            } else if (queryHandler.insert() == 0) {
                Toast.makeText(UserRegisterActivity.this, bundler.getOuterObject()
                        .getString(BookshelfConstants.RESULT_KEY_ERROR), Toast.LENGTH_LONG).show();
            }
        }
    }
}