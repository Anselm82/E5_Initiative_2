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

/**
 * Created by  Raúl Lapeña Martí
 */
public class GalleryFragment extends Fragment {

    private static GalleryFragment INSTANCE;

    static ArrayList<String> image_urls;
    static String php_url = "http://ralamarti.tk/android/gallery/listFiles.php";
    int span_count = 2;
    RecyclerView recyclerView;
    View rootView;

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
        }
        // Data for the recycler view
        return INSTANCE;
    }


    public void onURLsObtained(ArrayList<String> retrievedUrls)
    {
        image_urls = new ArrayList<>();
        for(String url : retrievedUrls)
        {
            if(!url.equals("listFiles.php"))
                image_urls.add("http://ralamarti.tk/android/gallery/" + url);
        }

        // 1. get a reference to recyclerView
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        // 2. set layoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), span_count));

        // 3. create an adapter
        GalleryAdapter mAdapter = new GalleryAdapter(getContext(), image_urls);

        // 4. set adapter
        recyclerView.setAdapter(mAdapter);

        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new ImageRetrieveTask().execute();
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
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
