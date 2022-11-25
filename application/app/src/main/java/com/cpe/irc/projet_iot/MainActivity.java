package com.cpe.irc.projet_iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.cpe.irc.projet_iot.communication.Address;
import com.cpe.irc.projet_iot.controller.CommunicationController;
import com.cpe.irc.projet_iot.controller.StorageController;
import com.cpe.irc.projet_iot.controller.ViewController;
import com.cpe.irc.projet_iot.sensor.Sensor;

public class MainActivity extends AppCompatActivity {

    protected Bundle instanceState;
    protected Sensor[] sensors;
    protected Address address;

    protected ViewController viewController;
    protected CommunicationController communicationController;
    private StorageController storageController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.instanceState = savedInstanceState;
        this.viewController = new ViewController(this);
        this.communicationController = new CommunicationController();
        this.storageController = new StorageController(this);
        this.afterCreate();
    }

    protected void afterCreate() {
        this.fillOnStart();

        // on edit text change
        this.viewController.setIpPortView.setOnClickListener((v) -> this.registerIpPort());
    }


    protected void registerIpPort(){
        this.address = this.loadIpPort(this.viewController.ipView, this.viewController.portView);
        if (this.communicationController.checkIpPort(this.address)) {
            this.storageController.storeAddress(this.address);
            this.viewController.viewLoading(true);

            Log.i("IP", "IP: " + this.address.getIp());
            Log.i("PORT", "PORT: " + this.address.getPort());

            this.communicationController.setCommunicator(this.address);

            this.updateSensorsList();
        }
    }


    protected void updateSensorsList() {
        this.viewController.inParallel(
                () -> this.sensors = this.communicationController.loadSensorData(),
                () -> {
                    this.viewController.linkSensorsList(this.sensors);
                    this.viewController.updateLastUpdateView();
                    this.viewController.viewLoading(false);
                    if (this.viewController.hasLinkedSensorList()){
                        this.viewController.registerSensorsListObserver(()->this.communicationController.changeOrder(this.sensors));
                    }
                }
        );
    }

    @NonNull
    protected Address loadIpPort(EditText ip, EditText port) {
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

    @Override
    protected void onResume() {
        super.onResume();
        if (this.viewController.hasLinkedSensorList()) {
            this.viewController.viewLoading(true);
            this.updateSensorsList();
        }
    }

    protected void fillOnStart() {
        if (this.storageController.hasSavedAddress()) {
            this.address = this.storageController.loadAddress();
            this.viewController.fillIpPortView(this.address);
            this.registerIpPort();
        }
    }
}

