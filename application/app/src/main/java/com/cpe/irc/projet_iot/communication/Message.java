package com.cpe.irc.projet_iot.communication;

import androidx.annotation.NonNull;

import com.cpe.irc.projet_iot.data.Crypter;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Classe représentant un message à envoyer ou reçu
 */
public class Message {
    public String msg = "";
    private boolean isEncoded;

    /**
     * Constructeur de la classe à partir d'un message non encodé
     * @param msg le message à envoyer
     */
    public Message(String msg) {
        this.isEncoded = false;
        if(msg != null) {
            this.msg = msg;
        }
    }

    /**
     * Constructeur de la classe à partir d'un message encodé
     * @param msg le message reçu
     */
    public Message(byte[] msg, int length) {
        this.isEncoded = true;
        if(msg != null) {
            this.msg = new String(msg, 0, length);
        }
    }

    /**
     * Permet d'encoder le message
     */
    public void encode() {
        if(!this.isEncoded) {
            this.msg = Crypter.encode(this.msg);
            this.isEncoded = true;
        }
    }

    /**
     * Permet de décoder le message
     */
    public void decode() {
        if(this.isEncoded) {
            this.msg = Crypter.decode(this.msg);
            this.isEncoded = false;
        }
    }

    /**
     * Créé un message à partir d'un DatagramPacket
     * @param packet le DatagramPacket à convertir
     * @return le message créé
     */
    public static Message fromPacket(@NonNull DatagramPacket packet) {
        Message message = new Message(packet.getData(), packet.getLength());
        message.decode();
        return message;
    }

    /**
     * Créé un DatagramPacket à partir du message
     * @param message le message à convertir
     * @param address l'adresse du destinataire
     * @param port le port du destinataire
     * @return le DatagramPacket créé
     */
    public static DatagramPacket toPacket(Message message, InetAddress address, int port) {
        message.encode();
        byte[] msgInByte = message.msg.getBytes();
        return new DatagramPacket(msgInByte, msgInByte.length, address, port);
    }

    /**
     * @return le message enregistré
     */
    @NonNull
    public String toString() {
        return msg;
    }
}
