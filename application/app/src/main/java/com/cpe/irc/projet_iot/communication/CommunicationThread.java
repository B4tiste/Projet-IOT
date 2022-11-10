package com.cpe.irc.projet_iot.communication;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class CommunicationThread extends Thread {
    private DatagramSocket UDPSocket;
    private String message;
    private String ipSource;
    private String portSource;
    private InetAddress ipCible;
    private int portCible;

    public CommunicationThread() {}

    @Override
    public void run() {
        super.run();
    }

    
    public void sendMessage(String ipCible, int portCible, String message) throws SocketException {
        this.setIpCible(ipCible);
        this.setPortCible(portCible);
        this.setMessage(message);

        this.UDPSocket = new DatagramSocket();

        try {
            // create a datagram packet
            byte[] data = this.message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, this.ipCible, this.portCible);
            // send the packet
            UDPSocket.send(packet);

            Log.i("ComThread", "Send \"" + this.message + "\" to " + this.ipCible + ":" + this.portCible);
            Log.i("ComThread", "Send by" + UDPSocket.getLocalAddress() + " and port " + this.UDPSocket.getLocalPort());
        } catch (Exception e) {
            Log.e("ComThread", "error: " + e.getMessage());
        }
    }
    
    public void receiveMessage() {
        try {
            // create a datagram packet
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length, this.UDPSocket.getLocalAddress(), this.UDPSocket.getLocalPort());
            Log.i("ComThread", "Waiting for a packet on IP " + this.ipSource + " and port " + this.UDPSocket.getLocalPort());
            while (true) {
                // receive the packet
                UDPSocket.receive(packet);
                Log.i("ComThread" ,"Received a packet from IP " + packet.getAddress() + " and port " + packet.getPort());
                Log.i("ComThread" ,"Message: " + new String(packet.getData()));
                this.message = new String(packet.getData());
            }
        } catch (Exception e) {
            Log.e("ComThread", "error: " + e.getMessage());
        }
    }

    public void setIpSource(String ipSource) {
        this.ipSource = ipSource;
    }
    public void setPortSource(String portSource) {
        this.portSource = portSource;
    }
    public void setIpCible(String ipCible) {
        try {
            this.ipCible = InetAddress.getByName(ipCible);
        } catch (Exception e) {
            Log.e("ComThread", e.getMessage());
        }
    }
    public void setPortCible(int portCible) {
        this.portCible = portCible;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}
