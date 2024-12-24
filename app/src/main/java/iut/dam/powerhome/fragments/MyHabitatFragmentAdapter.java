package iut.dam.powerhome.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import iut.dam.powerhome.R;

public class MyHabitatFragmentAdapter extends ArrayAdapter<JSONObject> {

    private LayoutInflater inflater;

    public MyHabitatFragmentAdapter(Context context, List<JSONObject> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.myhabitat_item, parent, false);
            holder = new ViewHolder();
            holder.residentTextView = convertView.findViewById(R.id.residentObjet);
            holder.referenceTextView = convertView.findViewById(R.id.referanceObjet);
            holder.powerTextView = convertView.findViewById(R.id.numberFloorTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject item = getItem(position);
        if (item != null) {
            try {
                holder.residentTextView.setText(item.getString("name"));
                holder.referenceTextView.setText(item.getString("reference"));
                holder.powerTextView.setText(item.getInt("wattage") + " W");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView residentTextView;
        TextView applianceTextView;
        TextView referenceTextView;
        TextView powerTextView;
    }
}
