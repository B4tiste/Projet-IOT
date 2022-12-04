package com.cpe.irc.projet_iot.communication;

import java.net.DatagramSocket;
import java.util.concurrent.BlockingQueue;

/**
 * Base commune au système de réception et d'envoi
 */
public abstract class Messager {
    protected static int lastId = 0;
    protected BlockingQueue<Message> queue;
    protected int uniqueId;
    protected DatagramSocket UDPSocket;
    protected int delay;

    public static int incrementId() {
        return Messager.lastId++;
    }

    protected Messager(BlockingQueue<Message> queue) {
        this.uniqueId = Receiver.incrementId();
        this.queue = queue;
    }

    public Message getMessage() throws InterruptedException {
        return this.queue.take();
    }

    public void stop() {
        this.UDPSocket.close();
    }
}
