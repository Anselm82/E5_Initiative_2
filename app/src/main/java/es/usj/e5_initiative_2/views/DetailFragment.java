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
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static DetailFragment detailFragment;
    private View rootView;
    private WebView wvDetail;
    private TextView tvTitle;
    private String data;
    private String title;

    public DetailFragment() {
    }

    public static Fragment newInstance(String title, String data) {
        if (detailFragment == null) {
            detailFragment =
                    new DetailFragment();
        }
        if (data != null) {
            detailFragment.data = data;
        }
        if (title != null) {
            detailFragment.title = title;
        }
        return detailFragment;
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
        detailFragment = null;
    }
}
