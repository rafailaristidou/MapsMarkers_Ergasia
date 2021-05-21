package com.example.mapsmarkers_ergasia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

public class popupactivity extends AppCompatActivity implements SensorEventListener {
    SensorManager DeviceSensorManager;
    Sensor pressure, light, amb_temp, rela_hum;
    Float timi_pressure, timi_light, timi_amb_temp, timi_rela_hum;
    Float timi_esthitira = null;
    TextView metrisis;
    Spinner spinneresthitires;
    Spinner spinnerxromata;
    String esthitiras;
    float xroma;
    String desc;
    TextView descedit;
    float[] color = new float[]{210, 240, 180, 120, 300, 30, 0, 270, 270, 60};//Pinakas gia antistixisi float timon xromaton gia Marker

    ArrayAdapter<CharSequence> adapteresthitira;
    ArrayAdapter<CharSequence> adapterxromata;

    double currentLon, currentLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_popupactivity);
        DeviceSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        //Gia anaktisi ton dedomeno tis Thesis apo to Main Activity
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        currentLat = bundle.getDouble("currentLat");
        currentLon = bundle.getDouble("currentLon");


        //      TextView Emfanisi Metriseon
        metrisis = (TextView) findViewById(R.id.metrisistxt);
        //      Spinner Epilogis Esthitira
        spinneresthitires = findViewById(R.id.esthitires);
        adapteresthitira = ArrayAdapter.createFromResource(this, R.array.esthitires, R.layout.support_simple_spinner_dropdown_item);
        adapteresthitira.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinneresthitires.setAdapter(adapteresthitira);
        spinneresthitires.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {//Kathe fora pou alazi i timi tou Spinner gia tin epilogi esthitira
                esthitiras = spinneresthitires.getItemAtPosition(position).toString();//Na perni tin timi tou epilegmenoy esthitira kai analoga me to ti epelexe
                // na elenxi an o esthitira arxika iparxi stin siskevi kai an iparxi na ton emfanizi


                if (esthitiras.equals("Βαρόμετρο")) {

                    if (timi_pressure != null) {
                        String mytext = Float.toString(timi_pressure);
                        timi_esthitira = timi_pressure;
                        metrisis.setText(mytext + " hPa");
                    } else {
                        metrisis.setText("Δεν υπάρχει Βαρόμετρο");
                    }
                }
                if (esthitiras.equals("Φωτόμετρο")) {
                    if (timi_light != null) {
                        String mytext = Float.toString(timi_light);
                        timi_esthitira = timi_light;
                        metrisis.setText(mytext + " lx");
                    } else {
                        metrisis.setText("Δεν υπάρχει Φωτόμετρο");
                    }
                }
                if (esthitiras.equals("Θερμόμετρο")) {
                    if (timi_amb_temp != null) {
                        String mytext = Float.toString(timi_amb_temp);
                        timi_esthitira = timi_amb_temp;
                        metrisis.setText(mytext + " °C");
                    } else {
                        metrisis.setText("Δεν υπάρχει Θερμόμετρο");
                    }
                }
                if (esthitiras.equals("Αισθητήρας Υγρασίας")) {
                    if (timi_rela_hum != null) {
                        String mytext = Float.toString(timi_rela_hum);
                        timi_esthitira = timi_rela_hum;
                        metrisis.setText(mytext + " °C");

                    } else {
                        metrisis.setText("Δεν υπάρχει Αισθητήρας Υγρασίας");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        //      Spinner Epilogis Xromatos

        spinnerxromata = findViewById(R.id.xromata);
        adapterxromata = ArrayAdapter.createFromResource(this, R.array.xromata, R.layout.support_simple_spinner_dropdown_item);
        adapterxromata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerxromata.setAdapter(adapterxromata);
        spinnerxromata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                xroma = color[position];//Antistixisisi tis timis position ston pinaka me tes float times ton xromaton


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


    }

    @Override
    protected void onPause() {//Na stamatane ta sensor na pernoun dedomena an stamata i efarmogi
        super.onPause();
        DeviceSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {//Na pernoun dedomena ta sensors otan sinexizi i efarmogi
        super.onResume();
        pressure = DeviceSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);//Sensor Varometrou
        light = DeviceSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);//Sensor Fotometrou
        amb_temp = DeviceSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);//Sensor Exoterikis thermokrasias
        rela_hum = DeviceSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);//Sensor Igrasias
        if (pressure != null) {
            DeviceSensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_UI);
        }
        if (light != null) {
            DeviceSensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_UI);
        }
        if (amb_temp != null) {
            DeviceSensorManager.registerListener(this, amb_temp, SensorManager.SENSOR_DELAY_UI);
        }
        if (rela_hum != null) {
            DeviceSensorManager.registerListener(this, rela_hum, SensorManager.SENSOR_DELAY_UI);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {//Otan allazoun oi times ton sensors
        switch (event.sensor.getType()) {
            case Sensor.TYPE_PRESSURE:
                timi_pressure = event.values[0];

                break;

            case Sensor.TYPE_LIGHT:
                timi_light = event.values[0];

                break;

            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                timi_amb_temp = event.values[0];

                break;

            case Sensor.TYPE_RELATIVE_HUMIDITY:
                timi_rela_hum = event.values[0];

                break;


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void closepopup(View v) {// klisimo tou popupactivity
        this.finish();
    }

    public void savetoFirestore(View v) {//Apothikefsi sto Firebase Ton timon Thesi,Xroma,Perigrafi,Timi Esthitira,Tipos Esthitira
        descedit = findViewById(R.id.dectxt);
        desc = descedit.getText().toString();
        GeoPoint currentloca = new GeoPoint(currentLat, currentLon);
        if (timi_esthitira == null) {
            timi_esthitira = 0f;
        }


        try {
            Marker savemarker = new Marker();//Dimiourgia Antikimenou Marker(Klasi p emis dimiourgiasame)
            savemarker.setColor(xroma);
            savemarker.setLocation(currentloca);
            savemarker.setTypeMetrisis(esthitiras);
            savemarker.setValueMetrisis(timi_esthitira);
            savemarker.setDesc(desc);
            MapsActivity.db.collection("MapsMarker")
                    .add(savemarker).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {//Epiveveosi Epitixias Apostolis Dedomenon
                    Toast.makeText(getBaseContext(), "Marker Added.", Toast.LENGTH_LONG).show();
                    finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Marker Fail.", Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}