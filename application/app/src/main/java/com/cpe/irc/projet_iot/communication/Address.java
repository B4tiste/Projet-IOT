package com.cpe.irc.projet_iot.communication;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Classe représentant une adresse IP et un port
 */
public class Address {
    private String ip;
    private int port;

    /**
     * Constructeur
     * @param ip l'adresse IP
     * @param port le port
     */
    public Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * L'adresse a une IP ?
     * @return True si l'adresse a une IP non nulle, False sinon
     */
    public boolean hasIp() {
        return this.ip != null;
    }

    /**
     * L'adresse a un port ?
     * @return True si l'adresse a un port differend de 0, False sinon
     */
    public boolean hasPort() {
        return this.port != 0;
    }

    /**
     * L'adresse IP est valide ?
     * @return True si l'adresse IP est valide, False sinon
     */
    public boolean hasValidIp(){
        try {
            InetAddress.getByName(this.ip);
        } catch (UnknownHostException e) {
            return false;
        }
        return true;
    }

    /**
     * Le port est valide ?
     * @return True si le port est valide, False sinon
     */
    public boolean hasValidPort(){
        return this.port > 0 && this.port < 65535;
    }

    public String getIp() {
        return ip;
    }

    public InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getByName((java.lang.String) ip);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setIp(InetAddress ip){
        this.ip = (String) ip.getHostAddress();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static Address emptyAddress(){
        return new Address(null, 0);
    }

    @NonNull
    public String toString(){
        return this.ip + ":" + this.port;
    }

    /**
     * Convertit l'adresse en JSON
     * @return L'adresse en JSON
     */
    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("ip", this.ip);
            json.put("port", this.port);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Convertit un JSON en adresse
     * @param json Le JSON à convertir
     * @return L'adresse
     */
    public static Address fromJson(JSONObject json) throws JSONException {
        return new Address(json.getString("ip"), json.getInt("port"));
    }

    /**
     * Retourne l'adresse au format json sous forme de chaine de caractère
     * @return L'adresse au format json sous forme de chaine de caractère
     */
    public String toJsonString(){
        return this.toJson().toString();
    }

    /**
     * Convertit une chaine de caractère au format json en adresse
     * @param json La chaine de caractère au format json
     * @return L'adresse
     */
    public static Address fromJsonString(String json) {
        try {
            return Address.fromJson(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Vérifie si l'adresse est rentrée
     * @return True si l'adresse est rentrée, False sinon
     */
    public boolean isLoaded(){
        return this.hasIp() && this.hasPort();
    }
}
