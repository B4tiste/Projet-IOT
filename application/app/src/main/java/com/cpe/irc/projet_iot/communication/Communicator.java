package com.cpe.irc.projet_iot.communication;

import android.util.Log;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Communicator {
    static private Communicator instance = null;

    private final BlockingQueue<Message> messagesToSend; // Setup the queue
    private final BlockingQueue<Message> messagesReceived; // Setup the queue
    private String ip;
    private int port;
    private Thread senderThread;
    private Thread receivedThread;
    private Sender sender;
    private Receiver receiver;

    public Communicator(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.messagesReceived = new ArrayBlockingQueue<>(10);
        this.messagesToSend = new ArrayBlockingQueue<>(10);
    }

    public static Communicator getCommunicator(Address address) {
        if (Communicator.instance == null) {
            Communicator.instance = new Communicator(address.getIp(), address.getPort());
        }
        return Communicator.instance;
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

    public boolean send(String msg) {
        try {
            this.messagesToSend.put(new Message(msg));
        } catch (InterruptedException e) {
            Log.e("COMMUNICATOR", "Error while sending message to server: " + e.getMessage());
            return false;
        }
        return true;
    }

    public List<Message> receive() {
        List<Message> msgs = new ArrayList<>();
        try {
            Message msg = this.messagesReceived.poll(30, TimeUnit.SECONDS);
            if(!msg.msg.equals(""))
            {
                msgs.add(msg);
            }
        } catch (NullPointerException | InterruptedException e) {
            Log.e("COMMUNICATOR", "Error while receiving message from server: " + e.getMessage());
        }
        return msgs;
    }

    public void initiate() {
        try {
        Log.i("Communicator", " --> " + " << Communicator: " + "Start communication");
            this.receiver = new Receiver(this.messagesReceived, 1);
            this.sender = new Sender(this.messagesToSend , this.ip, this.port);

            // Starting the threads
            this.receivedThread = new Thread(this.receiver);
            this.senderThread = new Thread(this.sender);


            receivedThread.start();
            senderThread.start();

        } catch (SocketException | UnknownHostException er) {
            er.printStackTrace();
        }
    }
}