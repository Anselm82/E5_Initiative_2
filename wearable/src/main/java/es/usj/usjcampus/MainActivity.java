package es.usj.usjcampus;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private NotificationManagerCompat mNotificationManagerCompat;
    private FrameLayout mMainFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        mMainFrameLayout = findViewById(R.id.mainFrameLayout);
    }
}
