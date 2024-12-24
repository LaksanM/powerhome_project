package iut.dam.powerhome;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import iut.dam.powerhome.fragments.HabitatFragment;

public class HabitatAdapter extends ArrayAdapter<Habitat> {
    HabitatFragment activity;
    List<Habitat> habitats;
    int itemResourceId;

    public HabitatAdapter(Context context, HabitatFragment activity, int itemResourceId, List<Habitat> habitats) {
        super(context,itemResourceId, habitats);
        this.activity = activity;
        this.habitats = habitats;
        this.itemResourceId = itemResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        Habitat habitat = habitats.get(position);
        List<Appliance> appliances = habitat.appliances;
        if(layout == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(itemResourceId, parent, false);
        }
        TextView residentName = (TextView) layout.findViewById(R.id.residentTV);
        TextView floorValue = (TextView) layout.findViewById(R.id.numberFloorTV);
        TextView equipment_label = (TextView) layout.findViewById(R.id.applianceTV);

        residentName.setText(habitat.residentName);
        floorValue.setText(String.valueOf(habitat.floor));
        equipment_label.setText(String.valueOf(appliances.size()) + " " + (appliances.size() == 1 ? "Equipment" : "Equipments"));


        int[] applianceImageViewIds = {
                R.id.appliance1IV,  // Index 0 pour l'ImageView correspondant à la machine à laver
                R.id.appliance2IV,  // Index 1 pour l'ImageView correspondant à l'aspirateur
                R.id.appliance3IV,  // Index 2 pour l'ImageView correspondant au climatiseur
                R.id.appliance4IV   // Index 3 pour l'ImageView correspondant au fer à repasser
        };

        for (int i = 0; i < appliances.size()  && i < applianceImageViewIds.length; i++) {
            Appliance appliance = appliances.get(i);
            ImageView applianceImageView = layout.findViewById(applianceImageViewIds[i]);
            applianceImageView.setImageResource(getImageResourceForAppliance(appliance));
            applianceImageView.setVisibility(View.VISIBLE);
        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.name + habitat.residentName , Toast.LENGTH_SHORT).show();
            }
        });

        return layout;
    }


    private int getImageResourceForAppliance(Appliance appliance) {
        switch (appliance.name) {
            case "Machine a laver":
                return R.drawable.ic_machine_a_laver;
            case "Aspirateur":
                return R.drawable.ic_aspirateur;
            case "Climatiseur":
                return R.drawable.ic_climatiseur;
            case "Fer a repasser":
                return R.drawable.ic_fer_a_repasser;
            default:
                return 0;
        }
    }
}
