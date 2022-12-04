package com.cpe.irc.projet_iot.communication;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

/**
 * Classe runnable qui permet de recevoir les messages UDP
 */
public class Receiver extends Messager implements Runnable {

    public Receiver(BlockingQueue<Message> queue, int delay, int port) throws SocketException {
        super(queue);
        this.delay = delay;
        this.UDPSocket = new DatagramSocket(port);
    }

    public Receiver(BlockingQueue<Message> queue, int delay) throws SocketException {
        this(queue, delay, 10000);
    }

    @Override
    public void run() {
        try {
            boolean fin = false;
            while (!fin) {
                Message message = this.receive();
                if (message.msg.equals("fin")) {
                    fin = true;
                    Log.i("Receiver", " <-- " + this.uniqueId + " << Receiver: " + message.msg);
                } else if (!message.msg.equals("")) {
                    this.queue.put(message);
                    Log.i("Receiver", " <-- " + this.uniqueId + " << Receiver: " + message);
                }
            }
        } catch (InterruptedException ignored) {
            Log.i("Receiver", "Thread interrupted");
        } catch (IOException error) {
            Log.e("Receiver", "Error in run: " + error.getMessage());
        }
    }

    private Message receive() throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        this.UDPSocket.receive(datagramPacket);
        Log.i("Receiver", " --> " + this.uniqueId + " << Receiver: " + this.queue.peek());
        return Message.fromPacket(datagramPacket);
    }
}
