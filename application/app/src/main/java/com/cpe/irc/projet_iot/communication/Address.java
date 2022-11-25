package com.cpe.irc.projet_iot.communication;

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
}
