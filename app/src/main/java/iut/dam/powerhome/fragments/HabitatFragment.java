package iut.dam.powerhome.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import iut.dam.powerhome.Appliance;
import iut.dam.powerhome.Habitat;
import iut.dam.powerhome.HabitatAdapter;
import iut.dam.powerhome.LoginActivity;
import iut.dam.powerhome.MainActivity;
import iut.dam.powerhome.R;
public class HabitatFragment extends Fragment {
    ListView listView;
    private RequestQueue mQueue;


    public HabitatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_habitat, container, false);
        listView = view.findViewById(R.id.ListHabitat);

        mQueue = Volley.newRequestQueue(getContext());

        String url = MainActivity.getAdresse()+"/getHabitats.php?token="+ ((MainActivity)getActivity()).getToken();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Habitat> items = new ArrayList<>();
                            JSONObject habitatd = response.getJSONObject(0);
                            String error = habitatd.optString("error", "");
                            if (!error.equals("")) {
                                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                            } else {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject habitat = response.getJSONObject(i);
                                    int floor = habitat.getInt("floor");
                                    int area = habitat.getInt("area");

                                    JSONArray infoArray = habitat.getJSONArray("info");
                                    String firstname = "";
                                    String lastname = "";
                                    if (infoArray.length() > 0) {
                                        JSONObject info = infoArray.getJSONObject(0);
                                        firstname = info.getString("firstname");
                                        lastname = info.getString("lastname");
                                    }

                                    List<Appliance> appliances = new ArrayList<>();
                                    JSONArray appliancesArray = habitat.getJSONArray("appliances");
                                    for (int j = 0; j < appliancesArray.length(); j++) {
                                        JSONObject appliance = appliancesArray.getJSONObject(j);
                                        int id = appliance.getInt("id");
                                        String name = appliance.getString("name");
                                        String reference = appliance.getString("reference");
                                        int wattage = appliance.getInt("wattage");
                                        appliances.add(new Appliance(id, name, reference, wattage));
                                    }


                                    items.add(new Habitat(1, firstname + " " + lastname, floor, area, appliances));
                                }

                                // Création et configuration de l'adaptateur
                                HabitatAdapter adapter = new HabitatAdapter(getContext(), HabitatFragment.this, R.layout.habitat_item, items);
                                listView.setAdapter(adapter);
                                Log.i("HabitatFragment", "Réponse JSON : " + response);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("HabitatFragment", "Erreur lors du traitement de la réponse JSON : " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("HabitatFragment", "Erreur lors de la récupération des données JSON : " + error.getMessage());
            }
        });

        mQueue.add(jsonArrayRequest);

        return view;
    }
}
