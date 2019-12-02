package com.ademkoc.haberler.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.ademkoc.haberler.NewsDetailActivity;
import com.ademkoc.haberler.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Adem on 12.06.2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Mesaj data içeriği: " + remoteMessage.getData());

            Map<String, String> stringMap = remoteMessage.getData();
            if(stringMap.containsKey("body")) {
                sendNotification(getResources().getString(R.string.app_name), stringMap.get("body"));
            } else {
                sendNotification(getResources().getString(R.string.app_name), String.valueOf(remoteMessage.getData()));
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Mesaj Notification Başlığı: "
                    + remoteMessage.getNotification().getTitle()
                    + " "
                    + "Mesaj Notification İçeriği: "
                    + remoteMessage.getNotification().getBody());

            sendNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }

    }

    private void sendNotification(String messageTitle, String messageBody) {

        long[] pattern = {500, 500};

        Intent i = new Intent(this, NewsDetailActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(messageBody)
                .setSmallIcon(R.drawable.app_icon)
                .setLargeIcon(bitmap)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setContentIntent(pi)
                .setVibrate(pattern)
                .setAutoCancel(true)
                .setSound(soundUri)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notification);

    }
}