package com.example.bookshelf.notification;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


/**
 * Created by Steven on 20-6-2015.
 */
public class GcmController extends GcmListenerService {
    GoogleCloudMessaging gcm;
    Context context;
    static String regid;
    String PROJECT_NUMBER = "397261244151";

    public GcmController(Context context) {
        this.context = context;
    }

    public void setRegId() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                } catch (IOException ex) {
                    Log.e("Error", ex.getMessage());
                }
                return regid;
            }
        }.execute();
    }

    public String getKey() {
        return regid;
    }
}
