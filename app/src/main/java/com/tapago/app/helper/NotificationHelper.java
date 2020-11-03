package com.tapago.app.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tapago.app.R;


public class NotificationHelper extends ContextWrapper {

    private static final String CHANEL_ID = "com.tapago.app";
    private static final String CHANEL_NAME = "tapago";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannels();
    }

    /**
     * Registers notification channels, which can be used later by individual notifications.
     *
     * @param ctx The application context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notifyChannels = new NotificationChannel(CHANEL_ID, CHANEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        notifyChannels.enableLights(false);
        notifyChannels.enableVibration(true);
        notifyChannels.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManger().createNotificationChannel(notifyChannels);
    }

    /**
     * Get the notification manager.
     * <p>
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    public NotificationManager getManger() {
        if (manager == null)
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    /**
     * Get a notification of type 1
     * <p>
     * Provide the builder rather than the notification it's self as useful for making notification
     * changes.
     *
     * @param title the title of the notification
     * @param body  the body text for the notification
     * @return the builder as it keeps a reference to the notification (since API 24)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification(String title, String content, PendingIntent contentIntent,
                                                Uri soundUri) {
        return new Notification.Builder(getApplicationContext(), CHANEL_ID)
                .setContentText(content)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setVibrate(new long[]{500, 500})
                .setContentIntent(contentIntent)
                .setSmallIcon(getSmallIcon())
                .setLargeIcon(getLargeIcon());
    }


    /**
     * Get the small icon for this app
     *
     * @return The small icon resource id
     */
    private int getSmallIcon() {
        return R.mipmap.ic_launcher;
    }

    private Bitmap getLargeIcon() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }


}
