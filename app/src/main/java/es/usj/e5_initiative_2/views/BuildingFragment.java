package es.usj.e5_initiative_2.views;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.usj.e5_initiative_2.R;
import es.usj.e5_initiative_2.model.Building;
import es.usj.e5_initiative_2.model.Facility;

/**
 * Fragmento con el detalle de un edificio. Se mostrará al hacer click sobre la zona KML.
 *
 * Created by Juan José Hernández Alonso on 16/11/17.
 */
public class BuildingFragment extends Fragment {

    private static BuildingFragment INSTANCE;
    private Building building;
    private ImageView imageView;
    private SeekBar seekBar;
    private TextView header, tvInfo, tvSchedule;
    private ListView lvFacilities;
    private TabHost tabHost;

    /**
     * Método que proporciona la instancia singleton del fragmento. Debe usarse en lugar del
     * constructor.
     *
     * @param building Building Objeto con los datos a mostrar.
     * @return Fragment fragmento genérico.
     */
    public static Fragment newInstance(Building building) {
        if (INSTANCE == null) {
            INSTANCE =
                    new BuildingFragment();
        }
        if (building != null) {
            INSTANCE.building = building;
        }
        return INSTANCE;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_building, container, false);
        header = view.findViewById(R.id.tvBuildingName);
        seekBar = view.findViewById(R.id.seekBarImages);
        imageView = view.findViewById(R.id.imgBuilding);
        tabHost = view.findViewById(R.id.tabHost);
        tvInfo = view.findViewById(R.id.tvInfo);
        tvSchedule = view.findViewById(R.id.tvSchedule);
        lvFacilities = view.findViewById(R.id.lvFacilities);
        install();
        return view;
    }

    /**
     * Método que "instala" los valores en los componentes UI.
     */
    private void install(){
        installImages();
        installTabs();
        installBuilding();
    }

    /**
     * Método que instala los valores generales de un edificio.
     */
    private void installBuilding() {
        header.setText(building.getName());
        tvInfo.setText(building.getDescription());
        tvSchedule.setText(building.getSchedule());
        List<Facility> facilities = building.getFacilities();
        Facility[] values = new Facility[facilities.size()];
        facilities.toArray(values);
        ArrayAdapter<Facility> adapter = new ArrayAdapter<Facility>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
        lvFacilities.setAdapter(adapter);
        lvFacilities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Facility itemValue = (Facility) lvFacilities.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(itemValue.getTitle());
                builder.setMessage(itemValue.getSnippet());
                builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * Método que instala las tabs para cada categoria: información general, instalaciones y horario.
     */
    private void installTabs() {
        tabHost.setup();
        TabHost.TabSpec spec = tabHost.newTabSpec(getResources().getString(R.string.information_title));
        spec.setContent(R.id.info_tab);
        spec.setIndicator(getResources().getString(R.string.information_title));
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(getResources().getString(R.string.facilities_title));
        spec.setContent(R.id.facilities_tab);
        spec.setIndicator(getResources().getString(R.string.facilities_title));
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(getResources().getString(R.string.schedule_title));
        spec.setContent(R.id.schedule_tab);
        spec.setIndicator(getResources().getString(R.string.schedule_title));
        tabHost.addTab(spec);
        tabHost.setCurrentTab(0);
    }

    /**
     * Metodo que carga las imagenes en el componente y configura la barra de búsqueda. Usamos la
     * librería Picasso para la descarga asíncrona desde la URL y la carga en el componente
     * ImageView.
     */
    private void installImages() {
        String url;
        if(building.getImages().size() == 1){
            url = building.getImages().get(0);
            if(url.endsWith("null")) {
                Picasso.with(getContext()).load(R.drawable.no_image_available).into(imageView);
            } else {
                Picasso.with(getContext()).load(url).into(imageView);
            }
        } else if(building.getImages().size() > 1) {
            seekBar.setVisibility(View.VISIBLE);
            seekBar.setMax(building.getImages().size()-1);
            url = building.getImages().get(0);
            Picasso.with(getContext()).load(url).into(imageView);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    String image = building.getImages().get(progress);
                    Picasso.with(getContext()).load(image).noFade().into(imageView);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }
}
