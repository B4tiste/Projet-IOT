package com.cpe.irc.projet_iot.communication;

import androidx.annotation.NonNull;

import com.cpe.irc.projet_iot.data.Crypter;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Message {
    String msg = "";

    // A message to a worker.
    public Message(String msg) {
        if(msg != null) {
            this.msg = msg;
        }
    }

    public static Message fromPacket(@NonNull DatagramPacket packet) {
        Message message = new Message(new String(packet.getData(), 0, packet.getLength()));
        message.msg = Crypter.decode(message.msg);
        return message;
    }

    public static DatagramPacket toPacket(Message message, InetAddress address, int port) {
        byte[] msgInByte = Crypter.encode(message.msg).getBytes();
        return new DatagramPacket(msgInByte, msgInByte.length, address, port);
    }

    @NonNull
    public String toString() {
        return msg;
    }
}
