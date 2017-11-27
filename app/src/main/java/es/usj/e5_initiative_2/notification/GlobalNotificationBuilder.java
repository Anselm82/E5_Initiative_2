package es.usj.e5_initiative_2.notification;

import android.support.v4.app.NotificationCompat;

/**
 * Clase para la gestión global de notificaciones. Extraída del ejemplo de la práctica 503.
 *
 * Created by Juan José Hernández Alonso on 24/11/17.
 */
public final class GlobalNotificationBuilder {

    private static NotificationCompat.Builder sGlobalNotificationCompatBuilder = null;

    /*
     * Empty constructor - We don't initialize builder because we rely on a null state to let us
     * know the Application's process was killed.
     */
    private GlobalNotificationBuilder() { }

    public static void setNotificationCompatBuilderInstance (NotificationCompat.Builder builder) {
        sGlobalNotificationCompatBuilder = builder;
    }

    public static NotificationCompat.Builder getNotificationCompatBuilderInstance(){
        return sGlobalNotificationCompatBuilder;
    }
}