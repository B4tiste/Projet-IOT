package com.cpe.irc.projet_iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cpe.irc.projet_iot.communication.Address;
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
    protected Sensor[] sensors;
    protected Address address;

    protected ViewController viewController;
    protected CommunicationController communicationController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.instanceState = savedInstanceState;
        this.viewController = new ViewController(this);
        this.communicationController = new CommunicationController();
        this.afterCreate();
    }

    protected void afterCreate() {
        this.registerIpPort();
    }


    protected void registerIpPort(){
        // on edit text change
        this.viewController.setIpPortView.setOnClickListener((v) -> {
            this.address = this.loadIpPort(this.viewController.ipView, this.viewController.portView);
            if (this.communicationController.checkIpPort(this.address)) {

                this.viewController.viewLoading(true);

                Log.i("IP", "IP: " + this.address.getIp());
                Log.i("PORT", "PORT: " + this.address.getPort());

                this.communicationController.setCommunicator(this.address);

                this.viewController.inParallel(
                        () -> {
                            this.sensors = this.communicationController.loadSensorData();
                        },
                        () -> {
                            this.linkSensorsList(this.sensors);
                            this.viewController.viewLoading(false);
                        }
                );
            }
        });
    }

    @NonNull
    private Address loadIpPort(EditText ip, EditText port) {
        String toCheckIp;
        try {
            toCheckIp = ip.getText().toString();
        } catch (Exception e) {
            Log.e("IP", "Error while loading ip");
            return Address.emptyAddress();
        }
        int toCheckPort;
        try {
            toCheckPort = Integer.parseInt(port.getText().toString());
        } catch (Exception e) {
            Log.e("PORT", "Error while loading port");
            return Address.emptyAddress();
        }

        return new Address(toCheckIp, toCheckPort);
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

