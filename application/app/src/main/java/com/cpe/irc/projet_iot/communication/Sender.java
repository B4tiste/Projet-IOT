package com.cpe.irc.projet_iot.communication;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;


public class Sender extends Messager implements Runnable {
    private final InetAddress address;
    private final int port;

    public Sender(BlockingQueue<Message> queue, String address, int port) throws SocketException, UnknownHostException {
        super(queue);
        this.UDPSocket = new DatagramSocket();
        this.address = InetAddress.getByName(address);
        this.port = port;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.send(this.queue.take());
            }
        } catch (InterruptedException | IOException ignored) {
        }
    }

    private void send(Message message) throws IOException {
        this.UDPSocket.send(Message.toPacket(message.msg.getBytes(), this.address, this.port));
        Log.i("Sender", " --> " + this.uniqueId + " >> Sender: " + message);
    }
}
