package iut.dam.powerhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HabitatActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitat);

        listView = findViewById(R.id.habitatList);

        List<Habitat> items = new ArrayList<>();
        List<Appliance> appliances = new ArrayList<>();

        appliances.add(new Appliance(1, "Machine à laver", "Miele", 2000));
        appliances.add(new Appliance(2, "Aspirateur", "Dyson", 1000));
        items.add(new Habitat(1, "John Louis", 1, 50,appliances));

        List<Appliance> appliances2 = new ArrayList<>();
        appliances2.add(new Appliance(1, "Fer à repasser", "Miele", 2000));
        appliances2.add(new Appliance(2, "Climatiseur", "Dyson", 1000));

        items.add(new Habitat(2, "Jeanne Louis", 2, 60,appliances2));

        //HabitatAdapter adapter = new HabitatAdapter(this, R.layout.habitat_item, items);
        //listView.setAdapter(adapter);
    }
}
