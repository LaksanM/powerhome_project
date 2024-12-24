package iut.dam.powerhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private RequestQueue mQueue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView register = findViewById(R.id.textLinkLogin);
        Button loginBtn = findViewById(R.id.loginBtn);
        EditText emailET = findViewById(R.id.emailET);
        EditText passwordET = findViewById(R.id.passwordET);
        TextView passwordforget = findViewById(R.id.passwordforget);


        mQueue = Volley.newRequestQueue(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Log.i("LoginActivity", "Bouton login clické !");

                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                String url = MainActivity.getAdresse()+"/login.php?email=" + email + "&password=" + password;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            Toast t = null;

                            @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String firstName = response.getString("firstname");
                            String lastName = response.getString("lastname");
                            String token = response.getString("token");
                            int idhabitat = response.getInt("habitat_id");
                            Boolean Connected = response.getBoolean("Connected");

                            if (Connected) {
                                t = Toast.makeText(v.getContext(), R.string.authen_reussi, Toast.LENGTH_SHORT);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("token", token);
                                bundle.putString("firstName", firstName);
                                bundle.putString("lastName", lastName);
                                bundle.putString("email",email);
                                bundle.putString("password", password);
                                bundle.putInt("idHabitat", idhabitat);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                t = Toast.makeText(v.getContext(), R.string.toast_errorconnexion, Toast.LENGTH_SHORT);
                            }
                            t.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("LoginActivity", "Erreur2 : " + e.getMessage());
                        }

                    }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.i("LoginActivity", "Erreur1 : " + error.getMessage());
                        }
                    });
                mQueue.add(jsonObjectRequest);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        passwordforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}