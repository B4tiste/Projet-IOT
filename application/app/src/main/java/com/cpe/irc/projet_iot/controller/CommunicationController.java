package com.cpe.irc.projet_iot.controller;

import android.util.Log;

import com.cpe.irc.projet_iot.communication.Address;
import com.cpe.irc.projet_iot.communication.Communicator;
import com.cpe.irc.projet_iot.communication.Message;
import com.cpe.irc.projet_iot.sensor.Sensor;

import org.json.JSONArray;
import org.json.JSONException;

public class CommunicationController {
    private Address address;
    private Communicator communicator;

    public void setCommunicator(Address address) {
        if (
                this.address == null ||
                !this.address.getIp().equals(address.getIp()) ||
                this.address.getPort() != address.getPort())
        {
            this.address = address;
            this.communicator = Communicator.getCommunicator(this.address);
            this.communicator.initiate();
        }
    }

    public boolean checkIpPort(Address address) {
        if(!address.hasIp() || !address.hasPort()){
            Log.e("IP", "IP or port is null");
            return false;
        }
        if(!address.hasValidIp()){
            Log.e("IP", "IP is not valid");
            return false;
        }
        if(!address.hasValidPort()){
            Log.e("PORT", "Port is not valid");
            return false;
        }
        return true;
    }

    public Sensor[] loadSensorData() {
        this.communicator.send("getValues()");

        Message message = this.communicator.receive();

        Sensor[] sensors = new Sensor[0];
        while (message != null){
            if(message.msg.equals("ok")){
                message = this.communicator.receive();
            } else {
                Log.i("MESSAGE", message.toString());
                try {
                    JSONArray jSONArray = new JSONArray(message.toString());
                    sensors = new Sensor[jSONArray.length()];
                    for (int i = 0; i < jSONArray.length(); i++) {
                        sensors[i] = new Sensor(jSONArray.getJSONObject(i));
                    }

                    message = null;
                    Log.i("MESSAGE", jSONArray.length() + " sensors loaded");
                } catch (JSONException e) {
                    message = null;
                    Log.e("MESSAGE", "Error while parsing JSON: " + e.getMessage());
                }
            }

        }
        return sensors;
    }

    public void changeOrder(Sensor[] sensors){
        StringBuilder messageToSend = new StringBuilder("setOrder(");
        for (Sensor sensor : sensors) {
            messageToSend.append(sensor.getType()).append(",");
            Log.i("COMMUNICATION NEW ORDER", sensor + "");
        }
        messageToSend = new StringBuilder(messageToSend.substring(0, messageToSend.length() - 1) + ")");

        this.communicator.send(messageToSend.toString());
    }

}
