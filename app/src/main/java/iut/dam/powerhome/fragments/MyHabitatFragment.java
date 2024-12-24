package iut.dam.powerhome.fragments;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import iut.dam.powerhome.DateInfo;
import iut.dam.powerhome.MainActivity;
import iut.dam.powerhome.R;

public class MyHabitatFragment extends Fragment {

    ListView listView;

    private int maconsomation;
    private RequestQueue mQueue;

    List<String> nomsAppareils = new ArrayList<>();

    List<Integer> wattAppereils = new ArrayList<>();
    List<Integer> idsAppareils = new ArrayList<>();
    private boolean isFirstSelection = true;

    
    private String DateTime = null;
    public MyHabitatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Mon Habitats");

        View rootView = inflater.inflate(R.layout.fragment_myhabitat, container, false);
        listView = rootView.findViewById(R.id.myhabitatlist);

        TextView consomation = rootView.findViewById(R.id.iConsomationPerso);

        // Créer une liste d'éléments
        List<JSONObject> items = new ArrayList<>();


        mQueue = Volley.newRequestQueue(getContext());

        String url = MainActivity.getAdresse()+"/myhabitats.php?token="+ ((MainActivity)getActivity()).getToken();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject habitatd = response.getJSONObject(0);
                            String error = habitatd.optString("error", "");
                            if (!error.equals("")) {
                                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                            } else {
                                JSONObject habitat = response.getJSONObject(response.length() - 1);
                                Log.i("MyHabitatFragment", "Habitat : " + habitat.toString());
                                JSONArray appliancesArray = habitat.getJSONArray("appliances");
                                for (int j = 0; j < appliancesArray.length(); j++) {
                                    JSONObject appliance = appliancesArray.getJSONObject(j);
                                    maconsomation += appliance.getInt("wattage");
                                    items.add(appliance);

                                    nomsAppareils.add(appliance.getString("name"));
                                    wattAppereils.add(appliance.getInt("wattage"));
                                    idsAppareils.add(appliance.getInt("id"));
                                }
                                consomation.setText(maconsomation + " W");
                                MyHabitatFragmentAdapter adapter = new MyHabitatFragmentAdapter(getActivity(), items);
                                listView.setAdapter(adapter);

                                Button addAppareil = rootView.findViewById(R.id.addAnAppliance);
                                Button reserveCreneau = rootView.findViewById(R.id.reserveCreneau);
                                addAppareil.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AjouterAppareilDialog();
                                    }
                                });

                                reserveCreneau.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String url = MainActivity.getAdresse() + "/getTime_slot.php?token=" + ((MainActivity) getActivity()).getToken();

                                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                                                new Response.Listener<JSONArray>() {
                                                    @Override
                                                    public void onResponse(JSONArray response) {
                                                        ReserveCreneauDialog(response);
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                    }
                                                });

                                        mQueue.add(jsonArrayRequest);
                                    }
                                });
                                }

                            } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("MyHabitatFragment", "Erreur lors du traitement de la réponse JSON : " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("MyHabitatFragment", "Erreur lors de la récupération des données JSON : " + error.getMessage());
            }
        });

        mQueue.add(jsonArrayRequest);


        return rootView;
    }


    private void AjouterAppareilDialog(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.ajoutappareil, null);

        Button close = view.findViewById(R.id.addAppareilBtnAnnuler);
        Button add = view.findViewById(R.id.addAppareilBtnValider);
        Spinner textspinner = view.findViewById(R.id.spinnerAppliance);
        EditText textwattage = view.findViewById(R.id.editTextWatt);
        EditText reference = view.findViewById(R.id.editTextReference);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MainActivity.getAdresse()+"/addAppareil.php?name=" + textspinner.getSelectedItem().toString() + "&reference=" + reference.getText().toString() + "&wattage=" + textwattage.getText()+ "&idhabitat=" + ((MainActivity)getActivity()).getIdhabitat();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            Toast t = null;

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                      String Information = response.getString("Info");

                                        t = Toast.makeText(v.getContext(), Information, Toast.LENGTH_SHORT);
                                        t.show();
                                        dialog.dismiss();
                                    requireActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fl_container, new MyHabitatFragment())
                                            .commit();



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                mQueue.add(jsonObjectRequest);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void ReserveCreneauDialog(JSONArray response) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.reservation, null);

        Button close = view.findViewById(R.id.buttonFermer);
        Button add = view.findViewById(R.id.buttonReserver);
        Spinner textspinnerReservation = view.findViewById(R.id.spinnerReserver);
        Spinner textspinnerDate = view.findViewById(R.id.spinnerDate);
        TextView textWattageActuel = view.findViewById(R.id.textViewWattageActuel);
        TextView textWattageMax = view.findViewById(R.id.textViewWattageMax);

        List<DateInfo> dateInfoList = new ArrayList<>();
        String Date = null;

        try {
            JSONArray jsonArray = response;

            ArrayList<String> datesList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String begin = jsonObject.getString("begin");
                String end = jsonObject.getString("end");
                String maxWattage = jsonObject.getString("max_wattage");
                int wattageActuel = jsonObject.getInt("wattage");
                datesList.add(id + " | " + begin + " - " + end);

                DateInfo dateInfo = new DateInfo(id, begin, end, maxWattage, wattageActuel);
                dateInfoList.add(dateInfo);
            }


            ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, datesList);
            dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            textspinnerDate.setAdapter(dateAdapter);
            textspinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (isFirstSelection) {
                        isFirstSelection = false;
                        return;
                    }

                        DateInfo dateInfo = dateInfoList.get(position);
                        textWattageActuel.setText("Wattage actuel : " + dateInfo.getWattage());
                        textWattageMax.setText("Wattage max : " + dateInfo.getMaxWattage());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date beginDate = null;
                        try {
                            beginDate = sdf.parse(dateInfo.getBegin());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        Date endDate = null;
                        try {
                            endDate = sdf.parse(dateInfo.getEnd());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        Calendar beginCalendar = Calendar.getInstance();
                        beginCalendar.setTime(beginDate);
                        int beginYear = beginCalendar.get(Calendar.YEAR);
                        int beginMonth = beginCalendar.get(Calendar.MONTH);
                        int beginDay = beginCalendar.get(Calendar.DAY_OF_MONTH);
                        int beginHour = beginCalendar.get(Calendar.HOUR_OF_DAY);
                        int beginMinute = beginCalendar.get(Calendar.MINUTE);

                        Calendar endCalendar = Calendar.getInstance();
                        endCalendar.setTime(endDate);
                        int endYear = endCalendar.get(Calendar.YEAR);
                        int endMonth = endCalendar.get(Calendar.MONTH);
                        int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
                        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
                        int endMinute = endCalendar.get(Calendar.MINUTE);

                        Calandrier(beginYear, endYear, beginMonth, endMonth, beginDay, endDay, beginHour, endHour, beginMinute, endMinute);
                    }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.reservation_spinner, nomsAppareils) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.spinner_item_text);
                String appareil = nomsAppareils.get(position);
                int wattage = wattAppereils.get(position);
                String textToShow = appareil + " - " + wattage + " W";
                textView.setText(textToShow);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.spinner_item_text);
                String appareil = nomsAppareils.get(position);
                int wattage = wattAppereils.get(position);
                String textToShow = appareil + " - " + wattage + " W";
                textView.setText(textToShow);
                return view;
            }
        };
        textspinnerReservation.setAdapter(adapter);
        final int[] idAppareilSelectionne = new int[1];
        textspinnerReservation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                idAppareilSelectionne[0] = idsAppareils.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (DateTime == null) {
                        Toast.makeText(getContext(), R.string.selecttionDate, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (wattAppereils.get(textspinnerReservation.getSelectedItemPosition()) + dateInfoList.get(textspinnerDate.getSelectedItemPosition()).getWattage() > Integer.parseInt(dateInfoList.get(textspinnerDate.getSelectedItemPosition()).getMaxWattage())) {
                        Toast.makeText(getContext(), R.string.maxPuissance, Toast.LENGTH_SHORT).show();
                        return;
                    }


                    String url = MainActivity.getAdresse()+"/addReservation.php?appliance_id=" + idAppareilSelectionne[0] + "&time_slot_id=" + dateInfoList.get(textspinnerDate.getSelectedItemPosition()).getId()+ "&date=" + DateTime;

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                Toast t = null;

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String Information = response.getString("Info");

                                        t = Toast.makeText(v.getContext(), Information, Toast.LENGTH_SHORT);
                                        t.show();
                                        dialog.dismiss();
                                        isFirstSelection = true;
                                        DateTime = null;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    mQueue.add(jsonObjectRequest);
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    isFirstSelection = true;
                    DateTime = null;
                }
            });
    }

    private void Calandrier(int yearsmin, int yearsmax, int monthmin, int monthmax, int daymin, int daymax, int hourmin, int hourmax, int minutemin, int minutemax){

        final String[] formattedDateTime = {null};

        Calendar minDateTime = Calendar.getInstance();
        minDateTime.set(yearsmin, monthmin, daymin-1, hourmin, minutemin, 0);
        long minDateTimeMillis = minDateTime.getTimeInMillis();

        Calendar maxDateTime = Calendar.getInstance();
        maxDateTime.set(yearsmax, monthmax, daymax, hourmax, minutemax, 0);
        long maxDateTimeMillis = maxDateTime.getTimeInMillis();

        MaterialDatePicker.Builder<Long> dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTitleText("Sélectionner la date");

        CalendarConstraints.DateValidator validatorMinDate = DateValidatorPointForward.from(minDateTimeMillis);

        CalendarConstraints.DateValidator validatorMaxDate = DateValidatorPointBackward.before(maxDateTimeMillis);

        CalendarConstraints.DateValidator compositeValidator = new CalendarConstraints.DateValidator() {
            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(@NonNull Parcel dest, int flags) {

            }
            @Override
            public boolean isValid(long date) {
                return date >= minDateTimeMillis && date <= maxDateTimeMillis;
            }
        };

        dateBuilder.setCalendarConstraints(new CalendarConstraints.Builder()
                .setValidator(compositeValidator)
                .build());

        MaterialDatePicker<Long> datePicker = dateBuilder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            long selectedDate = selection;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = sdf.format(new Date(selectedDate));

            MaterialTimePicker.Builder timeBuilder = new MaterialTimePicker.Builder();
            timeBuilder.setTimeFormat(TimeFormat.CLOCK_24H);
            timeBuilder.setTitleText("Sélectionner l'heure");

            MaterialTimePicker timePicker = timeBuilder.build();

            timePicker.addOnPositiveButtonClickListener(view -> {
                int selectedHour = timePicker.getHour();
                int selectedMinute = timePicker.getMinute();

                Calendar selectedDateTime = Calendar.getInstance();
                selectedDateTime.setTimeInMillis(selectedDate);
                selectedDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                selectedDateTime.set(Calendar.MINUTE, selectedMinute);

                if (selectedDateTime.getTimeInMillis() < minDateTimeMillis+TimeUnit.DAYS.toMillis(1)  || selectedDateTime.getTimeInMillis() > maxDateTimeMillis) {
                        Toast.makeText(getContext(), R.string.dateSelect, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.dateReserv + formattedDate + " " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
                    SimpleDateFormat dateTimes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                     DateTime = dateTimes.format(selectedDateTime.getTime());
                }
            });

            timePicker.show(requireActivity().getSupportFragmentManager(), "TIME_PICKER");
        });

        datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");
    }






}
