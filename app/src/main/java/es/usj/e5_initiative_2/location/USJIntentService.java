package es.usj.e5_initiative_2.location;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.concurrent.TimeUnit;

import es.usj.e5_initiative_2.NavigationActivity;
import es.usj.e5_initiative_2.R;
import es.usj.shared.MockDatabase;

/**
 * Created by Anselm on 24/11/17.
 */

public class USJIntentService extends IntentService {

    private static final String TAG = "USJIntentService";

    private static final long SNOOZE_TIME = TimeUnit.SECONDS.toMillis(5);

    public USJIntentService() {
        super("USJIntentService");
    }

    private NotificationCompat.Builder recreateBuilderWithBigTextStyle() {

        // Get your data
        MockDatabase.USJCampusNotification usjCampusNotification = MockDatabase.getUSJCampusNotification();

        // Build the BIG_TEXT_STYLE
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(usjCampusNotification.getBigText())
                .setBigContentTitle(usjCampusNotification.getBigContentTitle())
                .setSummaryText(usjCampusNotification.getSummaryText());

        // Set up main Intent for notification
        Intent notifyIntent = new Intent(this, NavigationActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        // Build and issue the notification
        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(getApplicationContext());

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        notificationCompatBuilder
                .setStyle(bigTextStyle)
                .setContentTitle(usjCampusNotification.getContentTitle())
                .setContentText(usjCampusNotification.getContentText())
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.ic_alarm_white_48dp))
                .setContentIntent(notifyPendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(Notification.VISIBILITY_PUBLIC);

        return notificationCompatBuilder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //doNothing()
    }
}