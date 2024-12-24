package iut.dam.powerhome.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import iut.dam.powerhome.LoginActivity;
import iut.dam.powerhome.R;
import iut.dam.powerhome.RegisterActivity;
import iut.dam.powerhome.RegisterHabitatActivity;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView prenom = rootView.findViewById(R.id.profil_prenom);
        TextView nom = rootView.findViewById(R.id.profil_nom);
        TextView email = rootView.findViewById(R.id.profil_email);
        Button logout = rootView.findViewById(R.id.profil_deconnexion);
        Button modifier = rootView.findViewById(R.id.profil_modifier);

        prenom.setText(((iut.dam.powerhome.MainActivity) getActivity()).getPrenom());
        nom.setText(((iut.dam.powerhome.MainActivity) getActivity()).getNom());
        email.setText(((iut.dam.powerhome.MainActivity) getActivity()).getEmail());


        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifierProfilFragment modifierProfilFragment = new ModifierProfilFragment();

                FragmentManager fragmentManager = getParentFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.fl_container, modifierProfilFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((iut.dam.powerhome.MainActivity) getActivity()).setToken("");
                ((iut.dam.powerhome.MainActivity) getActivity()).setEmail("");
                ((iut.dam.powerhome.MainActivity) getActivity()).setPassword("");
                ((iut.dam.powerhome.MainActivity) getActivity()).setNom("");
                ((iut.dam.powerhome.MainActivity) getActivity()).setPrenom("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });
        return rootView;
    }

}