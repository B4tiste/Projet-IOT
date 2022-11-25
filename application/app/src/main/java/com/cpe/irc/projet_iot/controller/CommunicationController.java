package com.cpe.irc.projet_iot.controller;

import android.util.Log;

import com.cpe.irc.projet_iot.communication.Address;
import com.cpe.irc.projet_iot.communication.Communicator;
import com.cpe.irc.projet_iot.communication.Message;
import com.cpe.irc.projet_iot.sensor.Sensor;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class CommunicationController {
    private Address address;
    private Communicator communicator;

    public void setCommunicator(Address address) {
        if (this.address == null || !this.address.getIp().equals(address.getIp()) || this.address.getPort() != address.getPort()){
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
        Log.i("main", "Thread initiate");
        this.communicator.send("getValues()");

        Log.i("Main", "Loading datas");

        List<Message> messages = this.communicator.receive();

        Sensor[] sensors = new Sensor[0];
        for (Message message : messages) {
            Log.i("MESSAGE", message.toString());
            try {
                JSONArray jSONArray = new JSONArray(message.toString());
                sensors = new Sensor[jSONArray.length()];
                for (int i = 0; i < jSONArray.length(); i++) {
                    sensors[i] = new Sensor(jSONArray.getJSONObject(i));
                }

                Log.i("MESSAGE", jSONArray.length() + "");
            } catch (JSONException e) {
                e.printStackTrace();
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
