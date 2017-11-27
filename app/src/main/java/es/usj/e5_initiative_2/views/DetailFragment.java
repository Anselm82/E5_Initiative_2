package es.usj.e5_initiative_2.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import es.usj.e5_initiative_2.R;

/**
 * Clase utilizada en la versión 1 de la aplicación. Usar BuildingFragment en su lugar.
 *
 * Created by Juan José Hernández Alonso on 07/11/17.
 */
@Deprecated
public class DetailFragment extends Fragment {

    private static DetailFragment INSTANCE;

    private View rootView;
    private WebView wvDetail;
    private TextView tvTitle;
    private String data;
    private String title;

    public static Fragment newInstance(String title, String data) {
        if (INSTANCE == null) {
            INSTANCE =
                    new DetailFragment();
        }
        if (data != null) {
            INSTANCE.data = data;
        }
        if (title != null) {
            INSTANCE.title = title;
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        wvDetail = (WebView) rootView.findViewById(R.id.wvDetail);
        wvDetail.loadData(data, "text/html", "UTF-8");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        INSTANCE = null;
    }
}
