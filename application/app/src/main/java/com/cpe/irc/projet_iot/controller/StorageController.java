package com.cpe.irc.projet_iot.controller;

import com.cpe.irc.projet_iot.MainActivity;
import com.cpe.irc.projet_iot.communication.Address;
import com.cpe.irc.projet_iot.storage.Storage;

public class StorageController {
    public static final String ADDRESS_JSON = "address.json";
    protected MainActivity activity;
    protected Storage storage;

    public StorageController(MainActivity activity) {
        this.activity = activity;
        this.storage = new Storage(activity);
    }

    public void storeAddress(Address address) {
        this.storage.saveFile(ADDRESS_JSON, address.toJsonString());
    }

    public Address loadAddress() {
        String json = this.storage.getFile(ADDRESS_JSON);
        return Address.fromJsonString(json);
    }

    public boolean hasSavedAddress() {
        return this.storage.hasFile(ADDRESS_JSON) && this.loadAddress().isLoaded();
    }
}
