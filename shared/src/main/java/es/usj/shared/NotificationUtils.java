package es.usj.shared;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

/**
 * Clase con utilidades procedente de la tarea 503.
 *
 * Created by Juan José Hernández Alonso on 24/11/17.
 */
public class NotificationUtils {

    public static String createNotificationChannel(
            Context context,
            NotificationDatabase.NotificationData notificationData) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = notificationData.getChannelId();

            // The user-visible name of the channel.
            CharSequence channelName = notificationData.getChannelName();
            // The user-visible description of the channel.
            String channelDescription = notificationData.getChannelDescription();
            int channelImportance = notificationData.getChannelImportance();
            boolean channelEnableVibrate = notificationData.isChannelEnableVibrate();
            int channelLockscreenVisibility =
                    notificationData.getChannelLockscreenVisibility();

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel =
                    new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence. http://www.tech-recipes.com/rx/49586/how-do-i-connect-an-android-wear-emulator-to-a-real-phone/
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }


}