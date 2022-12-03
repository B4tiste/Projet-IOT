package com.cpe.irc.projet_iot.communication;

import androidx.annotation.NonNull;

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
        message.msg = message.decode();
        return message;
    }

    public static DatagramPacket toPacket(Message message, InetAddress address, int port) {
        byte[] msgInByte = message.encode().getBytes();
        return new DatagramPacket(msgInByte, msgInByte.length, address, port);
    }

    @NonNull
    public String toString() {
        return msg;
    }

    /**
     *  Encodé le message avec un césar de 3
     *
     * @return Le message encodé
     */
    public String encode() {
        return this.cesar(this.msg, 3);
    }

    /**
     *  Décodé le message avec un césar de 3
     *
     * @return Le message décoder
     */
    public String decode() {
        return this.cesar(this.msg, -3);
    }

    /**
     *
     */
    private String cesar(String msg, int decalage) {
        StringBuilder msgEncode = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            int charToInt = (int) msg.charAt(i) + decalage;
            char intToChar = (char) charToInt;
            msgEncode.append(intToChar);
        }
        return msgEncode.toString();
    }
}
