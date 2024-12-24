package iut.dam.powerhome.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import iut.dam.powerhome.LoginActivity;
import iut.dam.powerhome.MainActivity;
import iut.dam.powerhome.R;
import iut.dam.powerhome.RegisterHabitatActivity;


public class ModifierProfilFragment extends Fragment {
    private RequestQueue mQueue;

    public ModifierProfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_modifier_profil, container, false);
        mQueue = Volley.newRequestQueue(getContext());

        Button annuler_modification_profil_button = rootView.findViewById(R.id.annuler_modification_profil_button);
        Button confirmer_modification_profil_button = rootView.findViewById(R.id.confirmer_modification_profil_button);
        EditText modification_profil_nom = rootView.findViewById(R.id.modification_profil_nom);
        EditText modification_profil_prenom = rootView.findViewById(R.id.modification_profil_prenom);
        EditText modification_profil_email = rootView.findViewById(R.id.modification_profil_email);
        confirmer_modification_profil_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();

                String url = MainActivity.getAdresse()+"/modifyProfile.php?token=" + ((iut.dam.powerhome.MainActivity) getActivity()).getToken()+ "&nom=" + modification_profil_nom.getText().toString() + "&prenom=" + modification_profil_prenom.getText().toString() + "&email=" + modification_profil_email.getText().toString();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String error = response.getString("error");
                                    if (error.isEmpty()) {
                                        if (!modification_profil_nom.getText().toString().isEmpty()) {
                                            ((iut.dam.powerhome.MainActivity) getActivity()).setNom(modification_profil_nom.getText().toString());
                                        }
                                        if (!modification_profil_prenom.getText().toString().isEmpty()) {
                                            ((iut.dam.powerhome.MainActivity) getActivity()).setPrenom(modification_profil_prenom.getText().toString());
                                        }
                                        if (!modification_profil_email.getText().toString().isEmpty()) {
                                            ((iut.dam.powerhome.MainActivity) getActivity()).setEmail(modification_profil_email.getText().toString());
                                        }

                                        String success = response.getString("success");
                                        Toast.makeText(getContext(), success, Toast.LENGTH_SHORT).show();
                                        fragmentManager.popBackStack();

                                    } else {
                                        if (error.equals("Le token est invalide ou a déjà expiré! Veuillez vous reconnecter.")) {
                                            Intent intent = new Intent(getContext(), LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Modifersfsdf", "Erreur lors de la connexion à l'API");
                    }
                });
                mQueue.add(jsonObjectRequest);
            }
        });
        annuler_modification_profil_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, profileFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return rootView;
    }
}