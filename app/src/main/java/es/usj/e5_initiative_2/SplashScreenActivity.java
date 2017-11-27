package es.usj.e5_initiative_2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

/**
 * Clase que utiliza una librería para la configuración de un SplashScreen.
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this);
        config.withBackgroundColor(Color.WHITE);
        config.withLogo(R.drawable.universidad_san_jorge);
        config.withTargetActivity(NavigationActivity.class);
        config.withFooterText(getString(R.string.developers));
        config.getFooterTextView().setPadding(4, 4, 4, 4);
        config.withSplashTimeOut(2000);
        config.getFooterTextView().setTextColor(Color.DKGRAY);
        View view = config.create();
        setContentView(view);
    }

}
