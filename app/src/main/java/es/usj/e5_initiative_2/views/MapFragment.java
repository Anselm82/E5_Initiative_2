package es.usj.e5_initiative_2.views;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.usj.e5_initiative_2.NavigationActivity;
import es.usj.e5_initiative_2.R;
import es.usj.e5_initiative_2.data.DataHolder;
import es.usj.e5_initiative_2.location.LocationUSJProvider;
import es.usj.e5_initiative_2.model.Building;
import es.usj.e5_initiative_2.model.Facility;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final String TITLE = "MAP";
    private GoogleMap mMap;
    private Context mContext;
    private View view;
    private FloatingActionButton cameraFab;
    private LocationUSJProvider locationUSJProvider;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Set<KmlPolygon> polygons;
    private ClusterManager<Facility> mClusterManager;

    private static MapFragment instance;

    public static Fragment newInstance(Bundle data, Context context) {
        if (instance == null) {
            instance = new MapFragment();
            instance.polygons = new HashSet<>();
            instance.mContext = context;
            instance.locationUSJProvider = new LocationUSJProvider(context);
        }
        if (data != null) {
            instance.setArguments(data);
        }
        return instance;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        prepareMap();
        //retrieveFileFromResource();
        retrieveFileFromUrl();
    }

    private void prepareMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMap != null) {
            mMap.clear();
        }
        instance = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void retrieveFileFromResource() {
        try {
            final KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.campus, mContext);
            kmlLayer.addLayerToMap();
            prepareKml(kmlLayer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void retrieveFileFromUrl() {
        new DownloadKmlFile(getString(R.string.kml_url)).execute();
    }

    private void prepareKml(KmlLayer layer) {
        moveCameraToKml(layer);
        layer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                if(feature != null && feature.hasProperties()) {
                    //loadDetails(feature.getProperty("name"), feature.getProperty("description"));
                    loadBuilding(getBuildingForFeatureName(feature.getProperty("name")));
                }
            }
        });
        Iterable<KmlContainer> containers = (List) layer.getContainers();
        boolean isInside = false;
        for (KmlContainer container : containers) {
            Iterable<KmlContainer> containersInside = (List) container.getContainers();
            for (KmlContainer containerInside : containersInside) {
                Iterable<KmlPlacemark> placemarks = containerInside.getPlacemarks();
                for (KmlPlacemark placemark : placemarks) {
                    KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
                    polygons.add(polygon);
                    if(isUserInsideKmlPolygon(polygon)){
                        isInside = true;
                    }
                }
            }
        }
        setCameraButtonStatus(isInside);
        mClusterManager = new ClusterManager<>(getContext(), getMap());
        getMap().setOnCameraIdleListener(mClusterManager);
        List<Facility> facilities = (List<Facility>) DataHolder.getInstance().get(DataHolder.FACILITIES, List.class);
        mClusterManager.addItems(facilities);
    }

    private void loadBuilding(Building building) {
        if(building != null) {
            Fragment fragment = BuildingFragment.newInstance(building);
            ((NavigationActivity) getActivity()).loadFragment(fragment);
        }
    }

    private Building getBuildingForFeatureName(String name) {
        return (Building) DataHolder.getInstance().get(DataHolder.BUILDINGS, HashMap.class).get(name);
    }

    private void loadDetails(String title, String details) {
        ((NavigationActivity) getActivity()).loadDetail(title, details);
    }

    private void moveCameraToKml(KmlLayer kmlLayer) {
        KmlContainer container = kmlLayer.getContainers().iterator().next();
        container = container.getContainers().iterator().next();
        KmlPlacemark placemark = container.getPlacemarks().iterator().next();
        KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polygon.getOuterBoundaryCoordinates()) {
            builder.include(latLng);
        }
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, 1);
        mMap.moveCamera(cu);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 1000, null);
    }

    public boolean isUserInsideKmlPolygon(KmlPolygon kmlPolygon) {
        Location loc = DataHolder.getInstance().get(DataHolder.LOCATION, Location.class);
        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        boolean result = false;
        for (List<LatLng> geoObject : kmlPolygon.getGeometryObject()) {
            if (PolyUtil.containsLocation(latLng, geoObject, false)) {
                result = true;
            }
        }
        return result;
    }

    public void updatePosition(Location location) {
        DataHolder.getInstance().put(DataHolder.LOCATION, location);
        boolean result = false;
        for (KmlPolygon polygon : polygons) {
            if (isUserInsideKmlPolygon(polygon)) {
                result = true;
            }
        }
        setCameraButtonStatus(result);
    }

    public void setCameraButtonStatus(boolean result){
        if (result) {
            Toast.makeText(getContext(), R.string.inside_polygon_message, Toast.LENGTH_SHORT).show();
            cameraFab.setVisibility(View.VISIBLE);
            cameraFab.setEnabled(true);
        } else {
            cameraFab.setEnabled(false);
            cameraFab.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        registerForLocationUpdates();
    }

    @Override
    public void onStop() {
        unregisterForLocationUpdates();
        super.onStop();
    }

    @NonNull
    private FusedLocationProviderClient getFusedLocationProviderClient() {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }
        return fusedLocationProviderClient;
    }

    private void unregisterForLocationUpdates() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @SuppressLint("MissingPermission")
    private void registerForLocationUpdates() {
        FusedLocationProviderClient locationProviderClient = getFusedLocationProviderClient();
        LocationRequest locationRequest = LocationRequest.create();
        Looper looper = Looper.myLooper();
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, looper);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_map, container, false);
            setRetainInstance(true);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            cameraFab = (FloatingActionButton) view.findViewById(R.id.camera_fab);
            cameraFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationActivity)getActivity()).loadCamera();
                }
            });
            setCameraButtonStatus(false);
        }
        locationUSJProvider.rastreoGPS();
        return view;
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            updatePosition(lastLocation);
        }
    };

    private class DownloadKmlFile extends AsyncTask<String, Void, byte[]> {

        private final String mUrl;

        public DownloadKmlFile(String url) {
            mUrl = url;
        }

        protected byte[] doInBackground(String... params) {
            try {
                InputStream is =  new URL(mUrl).openStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                return buffer.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(byte[] byteArr) {
         try {
                KmlLayer kmlLayer = new KmlLayer(mMap, new ByteArrayInputStream(byteArr),
                        getActivity().getApplicationContext());
                kmlLayer.addLayerToMap();
                prepareKml(kmlLayer);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public GoogleMap getMap() {
        return mMap;
    }
}
