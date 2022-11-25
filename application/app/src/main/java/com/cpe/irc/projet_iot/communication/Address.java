package com.cpe.irc.projet_iot.communication;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Address {
    private String ip;
    private int port;

    public Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean hasIp() {
        return this.ip != null;
    }

    public boolean hasPort() {
        return this.port != 0;
    }

    public boolean hasValidIp(){
        try {
            InetAddress.getByName(this.ip);
        } catch (UnknownHostException e) {
            return false;
        }
        return true;
    }

    public boolean hasValidPort(){
        return this.port > 0 && this.port < 65535;
    }

    public String getIp() {
        return ip;
    }

    public InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getByName((java.lang.String) ip);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setIp(InetAddress ip){
        this.ip = (String) ip.getHostAddress();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static Address emptyAddress(){
        return new Address(null, 0);
    }

    @NonNull
    public String toString(){
        return this.ip + ":" + this.port;
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("ip", this.ip);
            json.put("port", this.port);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Address fromJson(JSONObject json) throws JSONException {
        return new Address(json.getString("ip"), json.getInt("port"));
    }

    public String toJsonString(){
        return this.toJson().toString();
    }

    public static Address fromJsonString(String json) {
        try {
            return Address.fromJson(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isLoaded(){
        return this.hasIp() && this.hasPort();
    }
}
