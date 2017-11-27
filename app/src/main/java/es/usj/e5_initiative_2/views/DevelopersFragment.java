package es.usj.e5_initiative_2.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import es.usj.e5_initiative_2.R;

/**
 * Clase que define el fragmento para la visualización de los perfiles de los desarrolladores.
 * Created by Juan José Hernández Alonso on 16/11/17.
 */
public class DevelopersFragment extends Fragment {

    private static DevelopersFragment INSTANCE;
    private static final int JUANJO = 0;
    private static final int RAUL = 1;
    private View rootView;
    private TextView tvName, tvSurname, tvEmail, tvTeam;
    private ImageButton imgJuanjo, imgRaul;

    /**
     * Método que proporciona la instancia singleton del fragmento. Debe usarse en lugar del
     * constructor.
     *
     * @return Fragment fragmento genérico.
     */
    public static Fragment newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DevelopersFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_developers, container, false);
            tvName = rootView.findViewById(R.id.tvName);
            tvSurname = rootView.findViewById(R.id.tvSurname);
            tvEmail = rootView.findViewById(R.id.tvEmail);
            tvTeam = rootView.findViewById(R.id.tvTeam);
            imgJuanjo = rootView.findViewById(R.id.imgJuanjo);
            imgRaul = rootView.findViewById(R.id.imgRaul);
            imgJuanjo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayDetails(JUANJO);
                }
            });
            imgRaul.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        displayDetails(RAUL);
                }
            });
            displayDetails(JUANJO);
        }
        return rootView;
    }

    /**
     * Método que enlaza los valores desde un xml dependiendo el desarrollador seleccionado.
     * @param developer int Valor que identifica al desarrollador.
     */
    private void displayDetails(int developer) {
        String  resourceName = developer == JUANJO ? "data_juanjo" : "data_raul";
        int id = getResources().getIdentifier(resourceName, "array", getActivity().getPackageName());
        String[] developerData = getResources().getStringArray(id);
        tvName.setText(developerData[0]);
        tvSurname.setText(developerData[1]);
        tvEmail.setText(developerData[2]);
        tvTeam.setText(developerData[3]);
    }
}
