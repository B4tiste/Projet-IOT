package com.cpe.irc.projet_iot.controller;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cpe.irc.projet_iot.MainActivity;
import com.cpe.irc.projet_iot.R;
import com.cpe.irc.projet_iot.communication.Address;
import com.cpe.irc.projet_iot.sensor.Sensor;
import com.cpe.irc.projet_iot.sensor.SensorsAdapter;

import java.text.DateFormat;
import java.util.Date;

public class ViewController {
    protected MainActivity activity;

    public EditText ipView;
    public EditText portView;
    public Button setIpPortView;
    public ProgressBar progressBarView;
    public ListView sensorListView;
    protected SensorsAdapter sensorsAdapter;
    public TextView lastUpdateView;

    public ViewController(MainActivity activity) {
        this.activity = activity;
        this.ipView = this.activity.findViewById(R.id.ip_address);
        this.portView = this.activity.findViewById(R.id.port);
        this.setIpPortView = this.activity.findViewById(R.id.set_ip_port);
        this.progressBarView = this.activity.findViewById(R.id.progress_bar);
        this.sensorListView = this.activity.findViewById(R.id.sensors_list);
        this.lastUpdateView = this.activity.findViewById(R.id.last_update);
    }

    // take two function in parameter
    public void inParallel(Runnable function, Runnable onUiThread) {
        Thread communicationThread = new Thread(() -> {
            function.run();
            this.activity.runOnUiThread(onUiThread);
        });
        communicationThread.start();
    }

    public void viewLoading(boolean loading) {
        Log.i("VIEW", "viewLoading: " + loading);
        if (loading) {
            this.sensorListView.setVisibility(View.GONE);
            this.progressBarView.setVisibility(ProgressBar.VISIBLE);
            this.ipView.setEnabled(false);
            this.portView.setEnabled(false);
            this.setIpPortView.setEnabled(false);
        } else {
            this.sensorListView.setVisibility(View.VISIBLE);
            this.progressBarView.setVisibility(ProgressBar.INVISIBLE);
            this.ipView.setEnabled(true);
            this.portView.setEnabled(true);
            this.setIpPortView.setEnabled(true);
        }
    }

    public void linkSensorsList(Sensor[] sensors) {
        // create sensors list adapter to convert sensor objects to views
        this.sensorsAdapter = new SensorsAdapter(this.activity, sensors);
        // link sensor adapter to sensors list view
        this.sensorListView.setAdapter(this.sensorsAdapter);
        // detect change on sensors list

    }

    public boolean hasLinkedSensorList(){
        return this.sensorsAdapter != null;
    }

    public void registerSensorsListObserver(Runnable onChange){
        this.sensorsAdapter.registerDataSetObserver(SensorsAdapter.SensorsAdapterObserver(onChange));
    }


    public void updateLastUpdateView() {
        Date now = new Date();
        DateFormat formatter = DateFormat.getDateTimeInstance();
        String lastUpdateText = this.activity.getString(R.string.last_update, formatter.format(now));
        this.lastUpdateView.setText(lastUpdateText);
        this.lastUpdateView.setVisibility(View.VISIBLE);
    }

    public void fillIpPortView(Address address) {
        this.ipView.setText(address.getIp());
        this.portView.setText(String.valueOf(address.getPort()));
    }
}
