package es.usj.usjcampus;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.wear.ambient.AmbientMode;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import es.usj.shared.MockDatabase;
import es.usj.shared.NotificationUtils;

public class MainActivity extends Activity implements
        AmbientMode.AmbientCallbackProvider {

    private static final String TAG = "MainActivity";

    public static final int NOTIFICATION_ID = 888;

    private static final String BIG_TEXT_STYLE = "BIG_TEXT_STYLE";

    private NotificationManagerCompat mNotificationManagerCompat;

    // Needed for {@link SnackBar} to alert users when {@link Notification} are disabled for app.
    private FrameLayout mMainFrameLayout;
    private WearableRecyclerView mWearableRecyclerView;
    private CustomRecyclerAdapter mCustomRecyclerAdapter;

    /**
     * Ambient mode controller attached to this display. Used by Activity to see if it is in
     * ambient mode.
     */
    private AmbientMode.AmbientController mAmbientController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        // Enables Ambient mode.
        mAmbientController = AmbientMode.attachAmbientSupport(this);

        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        mMainFrameLayout = findViewById(R.id.mainFrameLayout);
        mWearableRecyclerView = findViewById(R.id.recycler_view);
        mWearableRecyclerView.setEdgeItemsCenteringEnabled(true);

        // Customizes scrolling so items farther away form center are smaller.
        ScalingScrollLayoutCallback scalingScrollLayoutCallback =
                new ScalingScrollLayoutCallback();
        mWearableRecyclerView.setLayoutManager(
                new WearableLinearLayoutManager(this, scalingScrollLayoutCallback));

        // Improves performance because we know changes in content do not change the layout size of
        // the RecyclerView.
        mWearableRecyclerView.setHasFixedSize(true);

        // Specifies an adapter (see also next example).
        mCustomRecyclerAdapter = new CustomRecyclerAdapter(new Controller(this));

        mWearableRecyclerView.setAdapter(mCustomRecyclerAdapter);
    }

    /*
     * Generates a BIG_TEXT_STYLE Notification that supports both Wear 1.+ and Wear 2.0.
     *
     * IMPORTANT NOTE:
     * This method includes extra code to replicate Notification Styles behavior from Wear 1.+ and
     * phones on Wear 2.0, i.e., the notification expands on click. To see the specific code in the
     * method, search for "REPLICATE_NOTIFICATION_STYLE_CODE".
     *
     * Notification Styles behave slightly different on Wear 2.0 when they are launched by a
     * native/local Wear app, i.e., they will NOT expand when the user taps them but will instead
     * take the user directly into the local app for the richest experience. In contrast, a bridged
     * Notification launched from the phone will expand with the style details (whether there is a
     * local app or not).
     *
     * If you want to see the new behavior, please review the generateBigPictureStyleNotification()
     * and generateMessagingStyleNotification() methods.
     */
    private void generateBigTextStyleNotification() {

        Log.d(TAG, "generateBigTextStyleNotification()");

        MockDatabase.USJCampusNotification usjCampusNotification =
                MockDatabase.getUSJCampusNotification();

        String notificationChannelId =
                NotificationUtils.createNotificationChannel(this, usjCampusNotification);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(usjCampusNotification.getBigText())
                .setBigContentTitle(usjCampusNotification.getBigContentTitle())
                .setSummaryText(usjCampusNotification.getSummaryText());


        Intent mainIntent = new Intent(this, USJMainActivity.class);

        PendingIntent mainPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        // 5. Build and issue the notification.

        // Because we want this to be a new notification (not updating a previous notification), we
        // create a new Builder. Later, we use the same global builder to get back the notification
        // we built here for the snooze action, that is, canceling the notification and relaunching
        // it several seconds later.

        // Notification Channel Id is ignored for Android pre O (26).
        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(
                        getApplicationContext(), notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        notificationCompatBuilder
                .setStyle(bigTextStyle)
                .setContentTitle(usjCampusNotification.getContentTitle())
                .setContentText(usjCampusNotification.getContentText())
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.ic_alarm_white_48dp))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_red))
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(usjCampusNotification.getPriority())
                .setVisibility(usjCampusNotification.getChannelLockscreenVisibility());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            // Enables launching app in Wear 2.0 while keeping the old Notification Style behavior.
            NotificationCompat.Action mainAction = new NotificationCompat.Action.Builder(
                    R.drawable.ic_launcher,
                    "Open",
                    mainPendingIntent)
                    .build();

            notificationCompatBuilder.addAction(mainAction);

        } else {
            // Wear 1.+ still functions the same, so we set the main content intent.
            notificationCompatBuilder.setContentIntent(mainPendingIntent);
        }


        Notification notification = notificationCompatBuilder.build();

        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);
        finish();
    }

    /**
     * Helper method for the SnackBar action, i.e., if the user has this application's notifications
     * disabled, this opens up the dialog to turn them back on after the user requests a
     * Notification launch.
     * <p>
     * IMPORTANT NOTE: You should not do this action unless the user takes an action to see your
     * Notifications like this sample demonstrates. Spamming users to re-enable your notifications
     * is a bad idea.
     */
    private void openNotificationSettingsForApp() {
        // Links to this app's notification settings
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);
        startActivity(intent);
    }

    @Override
    public AmbientMode.AmbientCallback getAmbientCallback() {
        return new MyAmbientCallback();
    }

    private class MyAmbientCallback extends AmbientMode.AmbientCallback {
        /**
         * Prepares the UI for ambient mode.
         */
        @Override
        public void onEnterAmbient(Bundle ambientDetails) {
            super.onEnterAmbient(ambientDetails);
            Log.d(TAG, "onEnterAmbient() " + ambientDetails);
            // In our case, the assets are already in black and white, so we don't update UI.
        }

        /**
         * Restores the UI to active (non-ambient) mode.
         */
        @Override
        public void onExitAmbient() {
            super.onExitAmbient();
            Log.d(TAG, "onExitAmbient()");
            // In our case, the assets are already in black and white, so we don't update UI.
        }
    }

    public void itemSelected(String data) {

        Log.d(TAG, "itemSelected()");

        boolean areNotificationsEnabled = mNotificationManagerCompat.areNotificationsEnabled();

        // If notifications are disabled, allow user to enable.
        if (!areNotificationsEnabled) {
            // Because the user took an action to create a notification, we create a prompt to let
            // the user re-enable notifications for this application again.
            Snackbar snackbar = Snackbar
                    .make(
                            mMainFrameLayout,
                            "", // Not enough space for both text and action text.
                            Snackbar.LENGTH_LONG)
                    .setAction("Enable Notifications", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Links to this app's notification settings.
                            openNotificationSettingsForApp();
                        }
                    });
            snackbar.show();
            return;
        }
        generateBigTextStyleNotification();
    }
}
