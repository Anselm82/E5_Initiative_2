package es.usj.e5_initiative_2.views;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import es.usj.e5_initiative_2.R;
import es.usj.e5_initiative_2.data.GalleryAdapter;
import es.usj.e5_initiative_2.data.JSONLoader;
import es.usj.e5_initiative_2.data.RESTRequest;

public class GalleryFragment extends Fragment {

    private static GalleryFragment INSTANCE;

    static ArrayList<String> image_urls;
    static String php_url = "http://ralamarti.tk/android/gallery/listFiles.php";
    int span_count = 2;

    public GalleryFragment() {}

    /**
     * Método que proporciona la instancia singleton del fragmento. Debe usarse en lugar del
     * constructor.
     *
     * @return Fragment fragmento genérico.
     */
    public static Fragment newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GalleryFragment();
            image_urls = new ArrayList<>();
            INSTANCE.init();
        }
        // Data for the recycler view
        return INSTANCE;
    }

    private void init() {
        new ImageRetrieveTask().execute();
    }

    public void onURLsObtained(ArrayList<String> retrievedUrls)
    {
        image_urls = new ArrayList<>();
        for(String url : retrievedUrls)
        {
            if(!url.equals("listFiles.php"))
                image_urls.add("http://ralamarti.tk/android/gallery/" + url);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(false);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), span_count));

        // 3. create an adapter
        GalleryAdapter mAdapter = new GalleryAdapter(getContext(), image_urls);

        // 4. set adapter
        recyclerView.setAdapter(mAdapter);

        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class ImageRetrieveTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            RESTRequest request = new RESTRequest(php_url);
            request.doGet();
            String json = request.getResponse();
            return JSONLoader.JSONToStringArrayList(json);
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

            super.onPostExecute(result);
            GalleryFragment.this.onURLsObtained(result);
        }
    }
}
