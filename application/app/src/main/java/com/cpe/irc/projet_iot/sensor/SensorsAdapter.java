package com.cpe.irc.projet_iot.sensor;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpe.irc.projet_iot.R;

/**
 *  Classe permettant de gérer l'affichage des capteurs dans la liste
 */
public class SensorsAdapter extends BaseAdapter {

    public static final String TEMPERATURE = "T";
    public static final String LUMINOSITY = "L";
    public static final String HUMIDITY = "H";

    private final Context context;
    private final Sensor[] sensors;
    private final LayoutInflater layoutInflater;

    /**
     * Constructeur de la classe
     * @param context Contexte de l'application
     * @param sensors Tableau de capteurs
     */
    public SensorsAdapter(Context context, Sensor[] sensors) {
        this.context = context;
        this.sensors = sensors;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    /**
     * Applique une action lorsque l'ordre de la liste est modifié
     * @param onDataSetChanged Action à effectuer
     * @return L'observer
     */
    public static DataSetObserver SensorsAdapterObserver(Runnable onDataSetChanged) {
        return new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                onDataSetChanged.run();
            }
        };
    }

    @Override
    public int getCount() {
        return this.sensors.length;
    }

    @Override
    public Sensor getItem(int position) {
        return this.sensors[position];
    }

    @Override
    public long getItemId(int position) {
        return this.sensors[position].hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.list_item_sensor_layout, container, false);
            holder = new ViewHolder();
            holder.sensorImgView = (ImageView) convertView.findViewById(R.id.sensor_img);
            holder.sensorNameView = (TextView) convertView.findViewById(R.id.sensor_name);
            holder.sensorValueView = (TextView) convertView.findViewById(R.id.sensor_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Sensor sensor = this.getItem(position);
        holder.sensorNameView.setText(sensor.getName());
        holder.sensorValueView.setText(sensor.getValue());

        this.setOrderFunction(position, convertView);

        int imageId = this.getMipmapResIdByName(sensor.getType());

        holder.sensorImgView.setImageResource(imageId);

        return convertView;
    }

    /**
     * Récupère l'id d'une image à partir de son nom
     * @param resName Nom de l'image
     * @return L'id de l'image
     */
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(this.getResNameForSensor(resName) , "drawable", pkgName);
        Log.i("SENSOR LIST VIEW", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    /**
     * Récupère le nom de l'image à partir du type de capteur
     * @param sensorType Type de capteur
     * @return Le nom de l'image
     */
    private String getResNameForSensor(String sensorType) {
        switch (sensorType) {
            case TEMPERATURE:
                return "device_thermostat";
            case LUMINOSITY:
                return "wb_sunny";
            case HUMIDITY:
                return "humidity_percentage";
            default:
                return "sensor";
        }
    }


    /**
     * Classe permettant de gérer l'ordres des éléments de la liste
     * @param id L'id de l'élément à bouger dans la liste
     * @param top position visée par l'élément
     */
    private void changeOrder(int id, boolean top) {
        Log.i("Change Order", "Button #" + id + " go " + (top ? "top" : "bottom"));
        Sensor toMove = this.sensors[id];
        if (top) {
            this.sensors[id] = this.sensors[id- 1];
            this.sensors[id- 1] = toMove;
        } else {
            this.sensors[id] = this.sensors[id+ 1];
            this.sensors[id+ 1] = toMove;
        }
        this.notifyDataSetChanged();
    }

    /**
     * Permet de lier les boutons de changement d'ordre à la fonction de changement d'ordre
     * @param id L'id de l'élément à bouger dans la liste
     * @param view L'élément de la liste
     */
    private void setOrderFunction(int id, View view) {
        View buttonUp = view.findViewById(R.id.button_up);
        View buttonDown = view.findViewById(R.id.button_down);
        if (id == 0) {
            buttonUp.setVisibility(View.INVISIBLE);
        } else if (id == this.getCount() - 1) {
            buttonDown.setVisibility(View.INVISIBLE);
        }
        buttonUp.setOnClickListener(v -> changeOrder(id, true));
        buttonDown.setOnClickListener(v -> changeOrder(id, false));
    }
}
