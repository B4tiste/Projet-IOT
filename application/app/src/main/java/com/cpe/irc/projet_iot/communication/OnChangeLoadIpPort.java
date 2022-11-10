package com.cpe.irc.projet_iot.communication;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.cpe.irc.projet_iot.MainActivity;

public class OnChangeLoadIpPort implements TextWatcher {
    private final MainActivity mainActivity;
    private final EditText ip;
    private final EditText port;

    public OnChangeLoadIpPort(MainActivity mainActivity, EditText ip, EditText port) {
        this.mainActivity = mainActivity;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.mainActivity.loadIpPort(this.ip, this.port);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
