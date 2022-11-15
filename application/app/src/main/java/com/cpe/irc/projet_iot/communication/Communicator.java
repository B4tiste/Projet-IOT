package com.cpe.irc.projet_iot.communication;

import android.util.Log;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Communicator {
    private final BlockingQueue<Message> messages; // Setup the queue
    private String ip;
    private int port;

    public Communicator(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.messages = new ArrayBlockingQueue<>(10);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void communicate() throws SocketException, UnknownHostException, InterruptedException {
        Receiver receiver = new Receiver(this.messages, 0);
        Sender sender = new Sender(this.messages , this.ip, this.port);

        // Starting the threads
        Thread receivedThread = new Thread(receiver);
        Thread senderThread = new Thread(sender);

        receivedThread.start();
        senderThread.start();

        // Waiting for the threads to finish and get the results
        try {
            receivedThread.join();
            senderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Printing the results
        Log.i("Communicator", "Received: " + receiver.getMessage());
    }
}