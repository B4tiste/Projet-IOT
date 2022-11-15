package com.cpe.irc.projet_iot.communication;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;


public class Receiver extends Messager implements Runnable {
    private final int delay;

    public Receiver(BlockingQueue<Message> queue, int delay) throws SocketException {
        super(queue);
        this.delay = delay;
        // port between 49152 and 65535
        this.UDPSocket = new DatagramSocket(49152 + (int)(Math.random() * ((65535 - 49152) + 1)));
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(this.delay * 1000L);
                this.queue.put(this.receive());
            }
        } catch (InterruptedException | IOException ignored) {
        }
    }

    private Message receive() throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        UDPSocket.receive(datagramPacket);
        Log.i("Receiver", "#" + this.uniqueId + " >> Create a new message!");
        return Message.fromPacket(datagramPacket);
    }
}
