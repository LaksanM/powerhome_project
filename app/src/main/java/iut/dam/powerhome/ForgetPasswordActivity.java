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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ForgetPasswordActivity extends AppCompatActivity {
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Définir le contenu de l'activité avec le layout correspondant
        setContentView(R.layout.activity_forget_password);

        Button BackLoginBtn = findViewById(R.id.BackLoginBtn);
        Button ValiderBtn = findViewById(R.id.ValiderBtn);
        EditText EmailET = findViewById(R.id.EmailET);

        mQueue = Volley.newRequestQueue(this);

        ValiderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValiderBtn.setEnabled(false);

                String url = MainActivity.getAdresse()+"/forgetpassword.php?email=" + EmailET.getText().toString();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("ForgetPasswordActivity", "onResponse: " + response.toString());
                                try {
                                    String message = response.getString("information");
                                    Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    ValiderBtn.setEnabled(true);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        error.printStackTrace();
                        ValiderBtn.setEnabled(true);
                    }
                });
                mQueue.add(jsonObjectRequest);
            }
        });


        BackLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
