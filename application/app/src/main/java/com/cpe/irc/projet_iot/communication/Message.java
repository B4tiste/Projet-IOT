package com.cpe.irc.projet_iot.communication;

import androidx.annotation.NonNull;

public class Message {
    final String msg;

    // A message to a worker.
    public Message(String msg) {
        this.msg = msg;
    }

    @NonNull
    public String toString() {
        return msg;
    }
}
