package es.usj.e5_initiative_2;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import es.usj.e5_initiative_2.notification.GlobalNotificationBuilder;
import es.usj.e5_initiative_2.views.DetailFragment;
import es.usj.e5_initiative_2.views.DevelopersFragment;
import es.usj.e5_initiative_2.views.MapFragment;
import es.usj.shared.NotificationDatabase;
import es.usj.shared.NotificationUtils;

/**
 * Clase con la Activity para la navegación de la aplicación.
 *
 * Created by Juan José Hernández Alonso on 07/11/17.
 */
public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int NOTIFICATION_ID = 888;
    private static final String DETAIL = "detail_tag";
    private static int view = 0;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadMap();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_map) {
            loadMap();
        } else if (id == R.id.nav_developers) {
            loadDevelopers();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Método para cargar el fragmento mapa.
     */
    public void loadMap() {
        MapFragment fragment = (MapFragment) MapFragment.newInstance(null, this);
        loadFragment(fragment);
    }

    /**
     * Método para cargar el fragmento desarrolladores.
     */
    public void loadDevelopers() {
        Fragment fragment = DevelopersFragment.newInstance();
        loadFragment(fragment);
    }

    /**
     * Método para cargar ña activity de camara.
     */
    public void loadCamera() {
        startActivity(new Intent(this, CameraActivity.class));
    }

    /**
     * Método para cargar el fragmento detalle. Perteneciente a la versión 1 de la aplicación.
     * Usar loadBuilding en su lugar.
     * @param title String título del fragmento.
     * @param data String HTML con los datos del detalle a visualizar.
     */
    @Deprecated
    public void loadDetail(String title, String data) {
        Fragment detailFragment = DetailFragment.newInstance(title, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_content, detailFragment, DETAIL);
        transaction.hide(MapFragment.newInstance(null, this));
        transaction.show(detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
    }

    /**
     * Método para cargar un fragmento genérico.
     */
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!(fragment instanceof MapFragment)) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.main_content, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    // BLOQUE PARA LAS NOTIFICACIONES DE LA APLICACIÓN

    private NotificationManagerCompat mNotificationManagerCompat;

    /**
     * Método que comprueba si las notificaciones están habilitadas y que las lanza.
     * @param entering Boolean que indica si el usuario está entrando o saliendo en la zona para
     *                 construir la notificación.
     */
    public void checkNotifications(boolean entering) {
        boolean areNotificationsEnabled = mNotificationManagerCompat.areNotificationsEnabled();

        if (!areNotificationsEnabled) {
            Snackbar snackbar = Snackbar
                    .make(navigationView
                            ,
                            R.string.notifications_needed,
                            Snackbar.LENGTH_LONG)
                    .setAction("ENABLE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openNotificationSettingsForApp();
                        }
                    });
            snackbar.show();
            return;
        }
        generateUSJNotification(entering);
    }

    /**
     * Método que genera una notificación cuando entramos o salimos de un edificio del campus.
     */
    private void generateUSJNotification(boolean entering) {

        NotificationDatabase.USJCampusNotification usjCampusNotification;
        if(entering)
            usjCampusNotification = NotificationDatabase.getUSJCampusEntranceNotification();
        else
            usjCampusNotification = NotificationDatabase.getUSJCampusExitNotification(getApplicationContext());

        String notificationChannelId =
                NotificationUtils.createNotificationChannel(this, usjCampusNotification);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(usjCampusNotification.getText())
                .setBigContentTitle(usjCampusNotification.getTitle())
                .setSummaryText(usjCampusNotification.getSummary());
        Intent notifyIntent = new Intent(this, SplashScreenActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(
                        getApplicationContext(), notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        Notification notification = notificationCompatBuilder
                .setStyle(bigTextStyle)
                .setContentTitle(usjCampusNotification.getContentTitle())
                .setContentText(usjCampusNotification.getContentText())
                .setSmallIcon(R.drawable.ic_alarm_white_48dp)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.mipmap.ic_launcher))
                .setContentIntent(notifyPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(usjCampusNotification.getPriority())
                .setVisibility(usjCampusNotification.getChannelLockscreenVisibility())
                .build();

        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    private void openNotificationSettingsForApp() {
        // Links to this app's notification settings.
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);
        startActivity(intent);
    }
}
