package es.usj.e5_initiative_2.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.usj.e5_initiative_2.R;

/**
 * Created by Raúl Lapeña Martí
 * Class that defines a recycler view for the photo gallery
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> image_urls;

    public GalleryAdapter(Context context, ArrayList<String> image_urls)
    {
        this.context = context;
        this.image_urls = image_urls;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_cell, parent, false);
        return new GalleryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder holder, int position) {
        Picasso.with(context).load(image_urls.get(position)).noFade().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return image_urls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView img;

        ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.img);
        }
    }
}
