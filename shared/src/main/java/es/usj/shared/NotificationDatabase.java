package es.usj.shared;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

/**
 * Clase que generará las notificaciones.
 */
public final class NotificationDatabase {

    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String TEXT = "text";

    /**
     * Método estático que generará una notificación de entrada a una de las zonas del campus.
     * @return USJCampusNotification con mensajes de bienvenida.
     */
    public static USJCampusNotification getUSJCampusEntranceNotification() {
        return USJCampusNotification.getInstance(new Bundle());
    }

    /**
     * Método estático que generará una notificación de entrada a una de las zonas del campus.
     * @return USJCampusNotification con mensajes de despedida.
     */
    public static USJCampusNotification getUSJCampusExitNotification() {
        Bundle bundle = new Bundle();
        bundle.putString(SUMMARY, "Come back soon!");
        bundle.putString(TEXT, "We hope that you have enjoyed your USJ Experiece! Come back soon!!");
        return USJCampusNotification.getInstance(bundle);
    }

    /**
     * Clase que prepara la notificación.
     */
    public static class USJCampusNotification extends NotificationData {

        private static USJCampusNotification INSTANCE = null;

        private String title;
        private String text;
        private String summary;

        private static synchronized USJCampusNotification getInstance(Bundle bundle) {
            if (INSTANCE == null) {
                INSTANCE = new USJCampusNotification(bundle);
            }
            return INSTANCE;
        }

        private USJCampusNotification(Bundle bundle) {
            // Standard Notification values:
            // Title for API <16 (4.0 and below) devices.
            mChannelName = mContentTitle = this.title = bundle.getString(TITLE, "USJ Campus");
            // Content for API <24 (4.0 and below) devices.
            mContentText = this.summary = bundle.getString(SUMMARY, "Share your experience!");
            mPriority = NotificationCompat.PRIORITY_DEFAULT;

            this.text = bundle.getString(TEXT, "Now you can take some photos of your Campus!!");

            // Notification channel values (for devices targeting 26 and above):
            mChannelId = "channel_reminder_1";
            mChannelDescription = "USJ Campus Notifications";
            mChannelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            mChannelEnableVibrate = false;
            mChannelLockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC;
        }

        public String getTitle() {
            return title;
        }

        public String getText() {
            return text;
        }

        public String getSummary() {
            return summary;
        }

        @Override
        public String toString() {
            return getTitle() + getText();
        }
    }

    /**
     * Clase que representa los datos necesarios para una notificación. Se mantiene la estructura
     * del ejemplo de la práctica 503.
     */
    public static abstract class NotificationData {

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