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
        this.delay = 1;
        this.UDPSocket = new DatagramSocket();
        this.address = InetAddress.getByName(address);
        this.port = port;
    }

    @Override
    public void run() {
        try {
            boolean fin = false;
            while (!fin)
            {
                Message message = this.queue.take();
                if(message.msg.equals("fin")){
                    fin = true;
                } else {
                    this.send(message);
                    Log.i("Sender", "sending message");
                }
            }
        } catch (InterruptedException ignored) {
            Log.i("Receiver", "Thread interrupted");
        } catch (IOException error) {
            Log.e("Receiver", "Error in run: " + error.getMessage());
        }
    }

    private void send(Message message) throws IOException {
        this.UDPSocket.send(Message.toPacket(message, this.address, this.port));
        Log.i("Sender", " --> " + this.uniqueId + " >> Sender: " + message);
    }
}
