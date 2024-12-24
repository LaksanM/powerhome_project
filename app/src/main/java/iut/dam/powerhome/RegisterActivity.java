package iut.dam.powerhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Button registerBtn = findViewById(R.id.registerBtn);
        EditText prenom = findViewById(R.id.sPrenom);
        EditText nom = findViewById(R.id.sNom);
        EditText email = findViewById(R.id.sEmail);
        EditText password = findViewById(R.id.sPassword);
        EditText password2 = findViewById(R.id.sPasswordC);
        Button loginBtn = findViewById(R.id.gotologin);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast t;

                if (prenom.getText().toString().isEmpty() || nom.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || password2.getText().toString().isEmpty()) {
                    t = Toast.makeText(RegisterActivity.this, R.string.remplir_champs, Toast.LENGTH_SHORT);
                    t.show();
                }else
                {
                    if (!password.getText().toString().equals(password2.getText().toString())) {
                        t = Toast.makeText(RegisterActivity.this, R.string.erreur_mdp, Toast.LENGTH_SHORT);
                        t.show();
                    }else {
                        Intent intent = new Intent(RegisterActivity.this, RegisterHabitatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("prenom", prenom.getText().toString());
                        bundle.putString("nom", nom.getText().toString());
                        bundle.putString("email", email.getText().toString());
                        bundle.putString("password", password.getText().toString());
                        bundle.putString("passwordc", password2.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}