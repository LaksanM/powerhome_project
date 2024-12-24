package iut.dam.powerhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

import iut.dam.powerhome.fragments.HabitatFragment;
import iut.dam.powerhome.fragments.MyHabitatFragment;
import iut.dam.powerhome.fragments.NotificationsFragment;
import iut.dam.powerhome.fragments.PreferencesFragment;
import iut.dam.powerhome.fragments.ProfileFragment;
import iut.dam.powerhome.fragments.ModifierProfilFragment;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerDL;
    NavigationView navNV;
    ActionBarDrawerToggle toggle;
    FragmentManager fm;

    private String email = "";
    private String password = "";

    private String token = "";

    private String nom = "";

    private String prenom = "";

    private int idhabitat = 0;

    private static String adresse = "http://192.168.1.10/powerhome_server";//"http://172.20.10.8/powerhome_server";

    public static String getAdresse() {
        return adresse;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public void setIdhabitat(int idhabitat) {
        this.idhabitat = idhabitat;
    }

    public int getIdhabitat() {
        return idhabitat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerDL = findViewById(R.id.dl_drawer);
        navNV = findViewById(R.id.nv_nav);

        toggle = new ActionBarDrawerToggle(this, drawerDL, R.string.drawer_open, R.string.drawer_close);
        drawerDL.addDrawerListener(toggle);
        toggle.syncState();

        fm = getSupportFragmentManager();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String token = extras.getString("token");
            String firstName = extras.getString("firstName");
            String lastName = extras.getString("lastName");
            String email = extras.getString("email");
            String password = extras.getString("password");
            int idhabitat = extras.getInt("idHabitat");

            setEmail(email);
            setPassword(password);
            setToken(token);
            setNom(lastName);
            setPrenom(firstName);
            setIdhabitat(idhabitat);

        }

        navNV.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_first) {
                    fm.beginTransaction()
                            .replace(R.id.fl_container, new HabitatFragment())
                            .commit();
                    setTitle("List des habitats");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else if (itemId == R.id.nav_second) {
                    fm.beginTransaction()
                            .replace(R.id.fl_container, new MyHabitatFragment())
                            .commit();
                    setTitle("Mon habitat");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else if (itemId == R.id.nav_third) {
                    fm.beginTransaction()
                            .replace(R.id.fl_container, new NotificationsFragment())
                            .commit();
                    setTitle("Mes notifications");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else if (itemId == R.id.nav_fourth) {
                    fm.beginTransaction()
                            .replace(R.id.fl_container, new PreferencesFragment())
                            .commit();
                    setTitle("Mes préférences");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else if (itemId == R.id.nav_fifth) {
                    fm.beginTransaction()
                            .replace(R.id.fl_container, new ProfileFragment())
                            .commit();
                    setTitle("Profile");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else if (itemId == R.id.nav_sixth) {
                    AboutDialog();
                } else if (itemId == R.id.nav_seventh) {
                    setNom("");
                    setPrenom("");
                    setToken("");
                    setEmail("");
                    setPassword("");
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                drawerDL.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navNV.getMenu().performIdentifierAction(R.id.nav_first, 0);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item);
    }

    private void AboutDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.apropos, null);

        Button close = view.findViewById(R.id.aboutclosebtn);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}