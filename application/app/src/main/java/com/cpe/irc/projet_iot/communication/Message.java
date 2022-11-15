package com.cpe.irc.projet_iot.communication;

import androidx.annotation.NonNull;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Message {
    final String msg;

    // A message to a worker.
    public Message(String msg) {
        this.msg = msg;
    }
    public static Message fromPacket(@NonNull DatagramPacket packet) {
        return new Message(new String(packet.getData(), 0, packet.getLength()));
    }

    public static DatagramPacket toPacket(byte[] data, InetAddress address, int port) {
        return new DatagramPacket(data, data.length, address, port);
    }

    @NonNull
    public String toString() {
        return msg;
    }
}
