package iut.dam.powerhome.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ArrayAdapter;


import java.util.Locale;

import iut.dam.powerhome.R;


public class PreferencesFragment extends Fragment {

    private Switch NotifBtn;
    private TextView EtatNotif;
    private Spinner languageSpinner;
    private SharedPreferences sharedPreferences;

    public PreferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);

        NotifBtn = view.findViewById(R.id.NotifBtn);
        EtatNotif = view.findViewById(R.id.EtatNotif);
        languageSpinner = view.findViewById(R.id.languageSpinner);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        NotifBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                EtatNotif.setText("Activé");
            } else {
                EtatNotif.setText("Désactivé");
            }
        });

        String[] languages = getResources().getStringArray(R.array.languages);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, languages);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languageSpinner.setAdapter(adapter);

        // Get the saved language from SharedPreferences
        String savedLanguage = sharedPreferences.getString("selected_language", "");

        // Set the selected language in the spinner
        if (!savedLanguage.isEmpty()) {
            int index = adapter.getPosition(savedLanguage);
            if (index != -1) {
                languageSpinner.setSelection(index);
            }
        }

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = languages[position];
                setAppLanguage(selectedLanguage);

                // Save the selected language to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_language", selectedLanguage);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void setAppLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Refresh the current fragment
        getParentFragmentManager().beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }
}