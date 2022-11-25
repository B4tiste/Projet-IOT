package com.cpe.irc.projet_iot.sensor;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Sensor implements Serializable {
    private final int id;
    private String name;
    private String type;
    private String value;

    private boolean active;

    public Sensor(int id, String name, String type, String value, boolean active) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.active = active;
    }

    public Sensor(int id, String name, String type, String value) {
        this(id, name, type, value, false);
    }

    public Sensor(JSONObject jsonObject) {
        this(jsonObject.optInt("id"), jsonObject.optString("name"), jsonObject.optString("type"), jsonObject.optString("value"), jsonObject.optBoolean("active"));
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

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", this.id);
            jsonObject.put("name", this.name);
            jsonObject.put("type", this.type);
            jsonObject.put("value", this.value);
            jsonObject.put("active", this.active);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String toJsonString() {
        return this.toJSON().toString();
    }
}
