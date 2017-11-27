package es.usj.shared;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

/**
 * Clase que generará las notificaciones.
 *
 * Created by Juan José Hernández Alonso on 24/11/17.
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
    public static USJCampusNotification getUSJCampusExitNotification(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(SUMMARY, context.getString(R.string.come_back));
        bundle.putString(TEXT, context.getString(R.string.exiting_msg));
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
                INSTANCE = new USJCampusNotification();
            }
            INSTANCE.install(bundle);
            return INSTANCE;
        }

        /**
         * Método que instala los valores de la notificación.
         * @param bundle Bundle con los valores.
         */
        private synchronized void install(Bundle bundle){
            mChannelName = mContentTitle = title = bundle.getString(TITLE, "USJ Campus");
            mContentText = summary = bundle.getString(SUMMARY, "Share your experience!");
            text = bundle.getString(TEXT, "Now you can take some photos of your Campus!!");
        }

        /**
         * Constructor por defecto que inicializa las propiedades comunes.
         */
        private USJCampusNotification() {
            mPriority = NotificationCompat.PRIORITY_DEFAULT;
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
            return getTitle() + " " + getText();
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