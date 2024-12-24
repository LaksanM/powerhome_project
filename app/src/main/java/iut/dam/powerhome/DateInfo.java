package iut.dam.powerhome;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DateInfo {
    private String id;
    private String begin;
    private String end;
    private String maxWattage;
    private int wattage;

    public DateInfo(String id, String begin, String end, String maxWattage, int wattage) throws JSONException, JSONException {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.maxWattage = maxWattage;
        this.wattage = wattage;
    }

    public String getId() {
        return id;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public String getMaxWattage() {
        return maxWattage;
    }

    public int getWattage() {
        return wattage;
    }
}
