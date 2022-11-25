package com.cpe.irc.projet_iot;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cpe.irc.projet_iot.sensor.Sensor;
import com.cpe.irc.projet_iot.sensor.SensorsAdapter;

public class ViewController {
    protected MainActivity activity;

    public EditText ipView;
    public EditText portView;
    public Button setIpPortView;
    public ProgressBar progressBarView;
    public ListView sensorListView;

    public ViewController(MainActivity activity) {
        this.activity = activity;
        this.ipView = this.activity.findViewById(R.id.ip_address);
        this.portView = this.activity.findViewById(R.id.port);
        this.setIpPortView = this.activity.findViewById(R.id.set_ip_port);
        this.progressBarView = this.activity.findViewById(R.id.progress_bar);
        this.sensorListView = this.activity.findViewById(R.id.sensors_list);
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
        SensorsAdapter sensorsListAdapter = new SensorsAdapter(this.activity, sensors);
        // link sensor adapter to sensors list view
        this.sensorListView.setAdapter(sensorsListAdapter);
    }
}
