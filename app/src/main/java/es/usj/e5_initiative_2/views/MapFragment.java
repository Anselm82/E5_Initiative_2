package es.usj.e5_initiative_2.views;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import es.usj.e5_initiative_2.NavigationActivity;
import es.usj.e5_initiative_2.R;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final String TITLE = "MAP";
    private GoogleMap mMap;
    private Context mContext;
    private View view;

    private static MapFragment instance;

    public static Fragment newInstance(Bundle data, Context context) {
        if (instance == null) {
            instance = new MapFragment();
            instance.mContext = context;
        }
        if (data != null) {
            instance.setArguments(data);
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_map, container, false);
            setRetainInstance(true);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        prepareMap();
        retrieveFileFromResource();
    }

    private void prepareMap() {
        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {

            @Override
            public void onPolygonClick(Polygon polygon) {
                polygon.setClickable(true);

            }
        });
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

    public void retrieveFileFromResource() {
        try {
            final KmlLayer kmlLayer = new KmlLayer(getMap(), R.raw.campus, mContext);
            kmlLayer.addLayerToMap();
            moveCameraToKml(kmlLayer);
            kmlLayer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
                @Override
                public void onFeatureClick(Feature feature) {
                    loadDetails(feature.getProperty("name"), feature.getProperty("description"));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void loadDetails(String title, String details) {
        ((NavigationActivity)getActivity()).loadDetail(title, details);
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
        getMap().moveCamera(cu);
        getMap().animateCamera(CameraUpdateFactory.zoomTo(18), 1000, null);
    }

    public GoogleMap getMap() {
        return mMap;
    }
}
