package com.cpe.irc.projet_iot.communication;

import android.util.Log;

import java.net.SocketException;
import java.net.UnknownHostException;
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
        Communicator.instance.setAddress(address);
        return Communicator.instance;
    }

    private void setAddress(Address address) {
        this.ip = address.getIp();
        this.port = address.getPort();
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

    public Message receive() {
        Message msg = null;
        try {
            msg = this.messagesReceived.poll(15, TimeUnit.SECONDS);
        } catch (NullPointerException | InterruptedException e) {
            Log.e("COMMUNICATOR", "Error while receiving message from server: " + e.getMessage());
        }
        return msg;
    }

    public void initiate() {
        this.stop();
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

    private void stop() {
        try {
            if (this.sender != null) {
                this.sender.stop();
            }
            if (this.receiver != null) {
                this.receiver.stop();
            }
            if(this.receivedThread != null && this.receivedThread.isAlive()){
                this.receivedThread.interrupt();
            }
            if(this.senderThread != null && this.senderThread.isAlive()){
                this.senderThread.interrupt();
            }
        } catch (NullPointerException e) {
            Log.e("COMMUNICATOR", "Error while stopping communication: " + e.getMessage());
        }
    }
}