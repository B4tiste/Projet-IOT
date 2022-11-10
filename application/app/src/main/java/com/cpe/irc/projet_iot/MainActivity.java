package com.cpe.irc.projet_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cpe.irc.projet_iot.communication.Communicator;
import com.cpe.irc.projet_iot.communication.OnChangeLoadIpPort;
import com.cpe.irc.projet_iot.sensor.Sensor;
import com.cpe.irc.projet_iot.sensor.SensorsAdapter;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    protected Bundle instanceState;
    protected String ip;
    protected int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.instanceState = savedInstanceState;
        this.afterCreate();
    }

    protected void afterCreate() {
        this.registerIpPort();
    }


    protected void registerIpPort(){
        EditText ip = this.findViewById(R.id.ip_address);
        EditText port = this.findViewById(R.id.port);
        Button setIpPort = this.findViewById(R.id.set_ip_port);
        // on edit text change
        setIpPort.setOnClickListener((v) -> {
            this.loadIpPort(ip, port);
        });
    }

    public void loadIpPort(EditText ip, EditText port) {
        String toCheckIp;
        try {
            toCheckIp = ip.getText().toString();
        } catch (Exception e) {
            Log.e("IP", "Error while loading ip");
            return;
        }
        int toCheckPort;
        try {
            toCheckPort = Integer.parseInt(port.getText().toString());
        } catch (Exception e) {
            Log.e("PORT", "Error while loading port");
            return;
        }

        this.ip = toCheckIp;
        this.port = toCheckPort;

        if (this.checkIpPort()) {
            Log.i("IP", "IP: " + this.ip);
            Log.i("PORT", "PORT: " + this.port);
            this.linkSensorsList(this.loadSensorData());
        }
    }

    private boolean checkIpPort() {
        if(this.ip == null || this.port == 0){
            Log.e("IP", "IP or port is null");
            return false;
        }
        Pattern ipPattern = Pattern.compile('^' + Patterns.IP_ADDRESS.pattern() + '$');
        if(!ipPattern.matcher(this.ip).matches()){
            Log.e("IP", "IP is not valid");
            return false;
        }
        if(this.port < 0 || this.port > 65535){
            Log.e("PORT", "Port is not valid");
            return false;
        }
        return true;
    }


    // TODO: load data from server
    protected Sensor[] loadSensorData() {
        Communicator communicator = new Communicator(this.ip, this.port);
        communicator.sendMessage("GET_SENSORS");
        Log.i("COMMUNICATOR", "Message Sent");

//        communicator.run();
//        String response = communicator.receiveMessage();


        Log.i("DATA", "Loading data");
        Log.i("DATA", "IP: " + this.ip);
        Log.i("DATA", "PORT: " + this.port);
        return new Sensor[]{
                new Sensor("Thermomètre", "temperature", "3°c"),
                new Sensor("Capteur de luminosité", "lumen", "600"),
                new Sensor("Humidité", "humid", "60%"),
        };
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

