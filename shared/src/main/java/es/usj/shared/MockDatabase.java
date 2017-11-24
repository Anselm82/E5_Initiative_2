package es.usj.shared;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

public final class MockDatabase {

    public static USJCampusNotification getUSJCampusNotification() {
        return USJCampusNotification.getInstance();
    }

    public static class USJCampusNotification extends MockNotificationData {

        private static USJCampusNotification sInstance = null;

        private String mBigContentTitle;
        private String mBigText;
        private String mSummaryText;

        public static USJCampusNotification getInstance() {
            if (sInstance == null) {
                sInstance = getSync();
            }
            return sInstance;
        }

        private static synchronized USJCampusNotification getSync() {
            if (sInstance == null) {
                sInstance = new USJCampusNotification("USJ Campus", "USJ Campus Notification", "Now you can take some photos");
            }

            return sInstance;
        }

        private USJCampusNotification(String title, String summary, String text) {

            // Standard Notification values:
            // Title for API <16 (4.0 and below) devices.
            mContentTitle = mBigContentTitle = title;
            // Content for API <24 (4.0 and below) devices.
            mContentText = mSummaryText = summary;
            mPriority = NotificationCompat.PRIORITY_DEFAULT;

            mBigText = text;

            // Notification channel values (for devices targeting 26 and above):
            mChannelId = "channel_reminder_1";
            // The user-visible name of the channel.
            mChannelName = "USJ Campus";
            // The user-visible description of the channel.
            mChannelDescription = "USJ Campus Notifications";
            mChannelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            mChannelEnableVibrate = false;
            mChannelLockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC;
        }

        public String getBigContentTitle() {
            return mBigContentTitle;
        }

        public String getBigText() {
            return mBigText;
        }

        public String getSummaryText() {
            return mSummaryText;
        }

        @Override
        public String toString() {
            return getBigContentTitle() + getBigText();
        }
    }

    /**
     * Represents standard data needed for a Notification.
     */
    public static abstract class MockNotificationData {

        // Standard notification values:
        protected String mContentTitle;
        protected String mContentText;
        protected int mPriority;

        // Notification channel values (O and above):
        protected String mChannelId;
        protected CharSequence mChannelName;
        protected String mChannelDescription;
        protected int mChannelImportance;
        protected boolean mChannelEnableVibrate;
        protected int mChannelLockscreenVisibility;


        // Notification Standard notification get methods:
        public String getContentTitle() {
            return mContentTitle;
        }

        public String getContentText() {
            return mContentText;
        }

        public int getPriority() {
            return mPriority;
        }

        // Channel values (O and above) get methods:
        public String getChannelId() {
            return mChannelId;
        }

        public CharSequence getChannelName() {
            return mChannelName;
        }

        public String getChannelDescription() {
            return mChannelDescription;
        }

        public int getChannelImportance() {
            return mChannelImportance;
        }

        public boolean isChannelEnableVibrate() {
            return mChannelEnableVibrate;
        }

        public int getChannelLockscreenVisibility() {
            return mChannelLockscreenVisibility;
        }
    }
}