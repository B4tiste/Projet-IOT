package com.cpe.irc.projet_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cpe.irc.projet_iot.communication.Communicator;
import com.cpe.irc.projet_iot.communication.Message;
import com.cpe.irc.projet_iot.sensor.Sensor;
import com.cpe.irc.projet_iot.sensor.SensorsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    protected Bundle instanceState;
    protected Communicator communicator;
    protected Sensor[] sensors;
    protected String ip;
    protected int port;

    protected EditText ipView;
    protected EditText portView;
    protected Button setIpPortView;
    protected ProgressBar progressBarView;
    private ListView sensorListView;


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
        this.sensorListView = findViewById(R.id.sensors_list);
        this.ipView = this.findViewById(R.id.ip_address);
        this.portView = this.findViewById(R.id.port);
        this.setIpPortView = this.findViewById(R.id.set_ip_port);
        this.progressBarView = this.findViewById(R.id.progress_bar);
        // on edit text change
        this.setIpPortView.setOnClickListener((v) -> {
            this.loadIpPort(this.ipView, this.portView);
            if (this.checkIpPort()) {

                this.viewLoading(true);

                Log.i("IP", "IP: " + this.ip);
                Log.i("PORT", "PORT: " + this.port);

                this.communicator = Communicator.getCommunicator(this.ip, this.port);
                this.communicator.initiate();

                Thread communicationThread = new Thread(() -> {
                    this.sensors = this.loadSensorData();
                    this.runOnUiThread(() -> {
                        this.linkSensorsList(this.sensors);
                        this.viewLoading(false);
                    });
                });
                communicationThread.start();
            }
        });
    }

    protected void viewLoading(boolean loading)
    {
        this.sensorListView.setVisibility(loading ? View.GONE : View.VISIBLE);
        this.progressBarView.setVisibility(loading ? ProgressBar.VISIBLE : ProgressBar.INVISIBLE);
        this.ipView.setEnabled(!loading);
        this.portView.setEnabled(!loading);
        this.setIpPortView.setEnabled(!loading);
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
        Log.i("main", "Thread initiate");
        this.communicator.send("getValues()");

        Log.i("Main", "Loading datas");

        List<Message> messages = this.communicator.receive();

        for (Message message : messages) {
            Log.i("MESSAGE", message.toString());
        }

        Log.i("DATA", "Loading data");
        Log.i("DATA", "IP: " + this.ip);
        Log.i("DATA", "PORT: " + this.port);
        return new Sensor[]{
                new Sensor("Thermomètre", "temperature", "3°c"),
                new Sensor("Capteur de luminosité", "lumen", "600"),
                new Sensor("Humidité", "humid", "60%"),
                new Sensor("Chiantitude", "chiant", "T'es chiant"),
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

