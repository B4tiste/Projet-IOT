package com.cpe.irc.projet_iot.controller;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cpe.irc.projet_iot.MainActivity;
import com.cpe.irc.projet_iot.R;
import com.cpe.irc.projet_iot.communication.Address;
import com.cpe.irc.projet_iot.sensor.Sensor;
import com.cpe.irc.projet_iot.sensor.SensorsAdapter;

import java.text.DateFormat;
import java.util.Date;

/**
 * Classe controller de la vue.
 * Gère les interactions entre l'utilisateur et l'application.
 */
public class ViewController {
    protected MainActivity activity;

    public EditText ipView;
    public EditText portView;
    public Button setIpPortView;
    public ProgressBar progressBarView;
    public ListView sensorListView;
    protected SensorsAdapter sensorsAdapter;
    public TextView lastUpdateView;
    public SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Récuperes les éléments important de la vue.
     * @param activity l'activité principale.
     */
    public ViewController(MainActivity activity) {
        this.activity = activity;
        this.ipView = this.activity.findViewById(R.id.ip_address);
        this.portView = this.activity.findViewById(R.id.port);
        this.setIpPortView = this.activity.findViewById(R.id.set_ip_port);
        this.progressBarView = this.activity.findViewById(R.id.progress_bar);
        this.sensorListView = this.activity.findViewById(R.id.sensors_list);
        this.lastUpdateView = this.activity.findViewById(R.id.last_update);
        this.swipeRefreshLayout = this.activity.findViewById(R.id.swipe_refresh_layout);
    }

    /**
     * Définis les actions à effectuer lors du refresh de la vue.
     * @param action l'action à effectuer.
     */
    public void setRefreshLayoutAction(Runnable action) {
        SwipeRefreshLayout swipeRefreshLayout = this.swipeRefreshLayout;
        this.swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    action.run();
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    /**
     * Permet d'exécuter une action sur l'UI après une première action.
     * Important pour les actions asynchrones.
     *
     * @param function La première action à effectuer.
     * @param onUiThread La seconde action à effectuer sur l'interface.
     */
    public void inParallel(Runnable function, Runnable onUiThread) {
        Thread communicationThread = new Thread(() -> {
            function.run();
            this.activity.runOnUiThread(onUiThread);
        });
        communicationThread.start();
    }

    /**
     * Affiche l'écran de chargement.
     * @param loading true pour afficher l'écran de chargement, false sinon.
     */
    public void viewLoading(boolean loading) {
        if (loading) {
            this.sensorListView.setVisibility(View.GONE);
            this.progressBarView.setVisibility(ProgressBar.VISIBLE);
            this.ipView.setEnabled(false);
            this.portView.setEnabled(false);
            this.setIpPortView.setEnabled(false);
            this.swipeRefreshLayout.setEnabled(false);
        } else {
            this.sensorListView.setVisibility(View.VISIBLE);
            this.progressBarView.setVisibility(ProgressBar.INVISIBLE);
            this.ipView.setEnabled(true);
            this.portView.setEnabled(true);
            this.setIpPortView.setEnabled(true);
            this.swipeRefreshLayout.setEnabled(true);
        }
    }

    /**
     * Lie les données des capteurs à la liste de la vue.
     * @param sensors les données des capteurs.
     */
    public void linkSensorsList(Sensor[] sensors) {
        // create sensors list adapter to convert sensor objects to views
        this.sensorsAdapter = new SensorsAdapter(this.activity, sensors);
        // link sensor adapter to sensors list view
        this.sensorListView.setAdapter(this.sensorsAdapter);
        // detect change on sensors list

    }

    /**
     * Vérifie si une liste de capteurs est définie.
     * @return true si une liste de capteurs est définie, false sinon.
     */
    public boolean hasLinkedSensorList(){
        return this.sensorsAdapter != null;
    }

    /**
     * Définie une action à effectuer lors du changement de la liste de capteurs.
     * @param onChange l'action à effectuer.
     */
    public void registerSensorsListObserver(Runnable onChange){
        this.sensorsAdapter.registerDataSetObserver(SensorsAdapter.SensorsAdapterObserver(onChange));
    }

    /**
     * Met à jour la date de la dernière mise à jour.
     */
    public void updateLastUpdateView() {
        Date now = new Date();
        DateFormat formatter = DateFormat.getDateTimeInstance();
        String lastUpdateText = this.activity.getString(R.string.last_update, formatter.format(now));
        this.lastUpdateView.setText(lastUpdateText);
        this.lastUpdateView.setVisibility(View.VISIBLE);
    }

    /**
     * Rentre les données de l'adresse dans les champs de la vue.
     * @param address l'adresse à afficher.
     */
    public void fillIpPortView(Address address) {
        this.ipView.setText(address.getIp());
        this.portView.setText(String.valueOf(address.getPort()));
    }
}
