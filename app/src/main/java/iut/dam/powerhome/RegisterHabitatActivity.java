package iut.dam.powerhome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterHabitatActivity extends AppCompatActivity {
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_habitat);

        mQueue = Volley.newRequestQueue(this);

        Button ValiderBtn = findViewById(R.id.ValiderBtn);
        Button RetourBtn = findViewById(R.id.RetourBtn);
        EditText EtageET = findViewById(R.id.EtageET);
        EditText SurfaceET = findViewById(R.id.SurfaceET);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String prenom = extras.getString("prenom");
            String nom = extras.getString("nom");
            String email = extras.getString("email");
            String password = extras.getString("password");
            String password2 = extras.getString("passwordc");


            ValiderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast t;

                    if (EtageET.getText().toString().isEmpty() || SurfaceET.getText().toString().isEmpty()) {
                        t = Toast.makeText(RegisterHabitatActivity.this, R.string.remplir_champs, Toast.LENGTH_SHORT);
                        t.show();
                    } else {
                        String url = MainActivity.getAdresse()+"/register.php?nom=" + nom + "&prenom=" + prenom + "&email=" + email + "&password=" + password + "&passwordc=" + password2 + "&etage=" + EtageET.getText().toString() + "&surface=" + SurfaceET.getText().toString();

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast t;
                                        try {
                                            String error = response.getString("error");
                                            if (error.isEmpty()) {
                                                Intent intent = new Intent(RegisterHabitatActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                String success = response.getString("success");
                                                t = Toast.makeText(RegisterHabitatActivity.this, success, Toast.LENGTH_SHORT);

                                            } else {
                                                t = Toast.makeText(RegisterHabitatActivity.this, error, Toast.LENGTH_SHORT);
                                            }
                                            t.show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LoginActivity", "Erreur lors de la connexion à l'API");
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                    }

                }
            });
        }
    }
}