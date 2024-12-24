package iut.dam.powerhome;

import java.util.ArrayList;
import java.util.List;

public class Habitat {
    int id;
    String residentName;
    int floor;
    double area;
    List<Appliance> appliances;

    public Habitat(int id, String residentName, int floor, double area, List<Appliance> app) {
        this.id = id;
        this.residentName = residentName;
        this.floor = floor;
        this.area = area;
        this.appliances = app;

    }
}
