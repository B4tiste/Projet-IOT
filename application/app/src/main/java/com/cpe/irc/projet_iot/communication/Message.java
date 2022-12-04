package com.cpe.irc.projet_iot.communication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cpe.irc.projet_iot.data.Crypter;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Message {
    public String msg = "";
    private boolean isEncoded = false;

    public Message(String msg) {
        this.isEncoded = false;
        if(msg != null) {
            this.msg = msg;
        }
    }
    public Message(byte[] msg, int length) {
        this.isEncoded = true;
        if(msg != null) {
            this.msg = new String(msg, 0, length);
        }
    }

    public void encode() {
        if(!this.isEncoded) {
            this.msg = Crypter.encode(this.msg);
            this.isEncoded = true;
        }
    }

    public void decode() {
        if(this.isEncoded) {
            this.msg = Crypter.decode(this.msg);
            this.isEncoded = false;
        }
    }

    public static Message fromPacket(@NonNull DatagramPacket packet) {
        Message message = new Message(packet.getData(), packet.getLength());
        message.decode();
        return message;
    }

    public static DatagramPacket toPacket(Message message, InetAddress address, int port) {
        message.encode();
        byte[] msgInByte = message.msg.getBytes();
        return new DatagramPacket(msgInByte, msgInByte.length, address, port);
    }

    @NonNull
    public String toString() {
        return msg;
    }
}
