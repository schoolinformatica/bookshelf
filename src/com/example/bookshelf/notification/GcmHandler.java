package com.example.bookshelf.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import com.example.BookShelf.R;
import com.example.bookshelf.activities.BookshelfActivity;
import com.example.bookshelf.activities.FriendlistActivity;
import com.example.bookshelf.activities.RequestBookActivity;
import com.google.android.gms.drive.internal.GetDriveIdFromUniqueIdentifierRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Random;

/**
 * Created by Steven on 20-6-2015.
 */
public class GcmHandler extends IntentService {
    String message;
    String title;
    String type;

    private Handler handler;

    public GcmHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.

        message = intent.getExtras().getString("message");
        title = intent.getExtras().getString("title");
        type = intent.getExtras().getString("type");

        showNotification();

        GcmReceiver.completeWakefulIntent(intent);

    }

    public void showNotification() {
        Intent resultIntent = new Intent();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Create new notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon_actionbar)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});


        /**
         * When clicked on the notification, this intent will be put on top of the stack.
         * First make new intent
         * Then put on top of new stack
         */
        if (type.equals("friend")) {
            resultIntent = new Intent(this, FriendlistActivity.class);
        }
        if (type.equals("book")) {
            resultIntent = new Intent(this, RequestBookActivity.class);
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(BookshelfActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Show the notification
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        mNotificationManager.notify(m, mBuilder.build());
    }

}
