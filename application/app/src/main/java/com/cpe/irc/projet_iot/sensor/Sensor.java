package com.cpe.irc.projet_iot.sensor;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Sensor implements Serializable {
    private String name;
    private String type;
    private String value;

    private boolean active;

    public Sensor(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.active = true;
    }

    public Sensor(String name, String type, String value, boolean active) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.active = active;
    }

    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getValue() {
        return value;
    }
    public boolean isActive() {
        return active;
    }

    public Sensor setName(String name) {
        this.name = name;
        return this;
    }
    public Sensor setType(String type) {
        this.type = type;
        return this;
    }
    public Sensor setValue(String value) {
        this.value = value;
        return this;
    }
    public Sensor setActive(boolean active) {
        this.active = active;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name +": "+ this.value;
    }
}
