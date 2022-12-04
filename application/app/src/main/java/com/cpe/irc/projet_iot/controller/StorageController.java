package com.cpe.irc.projet_iot.controller;

import com.cpe.irc.projet_iot.MainActivity;
import com.cpe.irc.projet_iot.communication.Address;
import com.cpe.irc.projet_iot.storage.Storage;

/**
 * Classe controller de stockage.
 * Gère le stockage des données.
 */
public class StorageController {
    public static final String ADDRESS_JSON = "address.json";
    protected Storage storage;

    /**
     * Constructeur du controller de stockage.
     * @param activity l'activité principale.
     */
    public StorageController(MainActivity activity) {
        this.storage = new Storage(activity);
    }

    /**
     * Sauvegarde l'adresse du serveur.
     * @param address l'adresse du serveur.
     */
    public void storeAddress(Address address) {
        this.storage.saveFile(ADDRESS_JSON, address.toJsonString());
    }

    /**
     * Récupère l'adresse du serveur.
     * @return l'adresse du serveur.
     */
    public Address loadAddress() {
        String json = this.storage.getFile(ADDRESS_JSON);
        return Address.fromJsonString(json);
    }

    /**
     * Vérifie si l'adresse du serveur est sauvegardée.
     * @return true si l'adresse est sauvegardée, false sinon.
     */
    public boolean hasSavedAddress() {
        return this.storage.hasFile(ADDRESS_JSON) && this.loadAddress().isLoaded();
    }
}
