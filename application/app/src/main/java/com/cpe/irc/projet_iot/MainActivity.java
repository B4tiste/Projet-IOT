package com.cpe.irc.projet_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.cpe.irc.projet_iot.sensor.Sensor;
import com.cpe.irc.projet_iot.sensor.SensorsAdapter;

public class MainActivity extends AppCompatActivity {

    protected Bundle instanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.instanceState = savedInstanceState;
        this.afterCreate();
    }

    protected void afterCreate() {
        this.linkSensorsList(this.loadSensorData());
    }

    // TODO: load data from server
    protected Sensor[] loadSensorData() {
        Sensor temp = new Sensor("Thermomètre", "temperature", "3°c");
        Sensor lum = new Sensor("Capteur de luminosité", "lumen", "600");
        Sensor humidite = new Sensor("Humidité", "humid", "60%");
        return new Sensor[]{temp, lum, humidite};
    }

    protected void linkSensorsList(Sensor[] sensors) {
        // get sensors_list view
        ListView sensors_list = findViewById(R.id.sensors_list);
        // create sensors list adapter to convert sensor objects to views
        SensorsAdapter sensorsListAdapter = new SensorsAdapter(this, sensors);
        // link sensor adapter to sensors list view
        sensors_list.setAdapter(sensorsListAdapter);
    }
}

