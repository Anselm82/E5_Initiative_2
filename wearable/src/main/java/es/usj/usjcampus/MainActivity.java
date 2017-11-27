package es.usj.usjcampus;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.FrameLayout;

/**
 * Clase que contiene la única y simple actividad que tendrá la parte wearable del proyecto.
 *
 * Created by Juan José Hernández Alonso on 24/11/17.
 */
public class MainActivity extends Activity {

    private NotificationManagerCompat mNotificationManagerCompat;
    private FrameLayout mMainFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        mMainFrameLayout = findViewById(R.id.mainFrameLayout);
    }
}
