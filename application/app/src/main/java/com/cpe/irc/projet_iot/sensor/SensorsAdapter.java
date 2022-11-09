package com.cpe.irc.projet_iot.sensor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpe.irc.projet_iot.R;

public class SensorsAdapter extends BaseAdapter {

    public static final String TEMPERATURE = "temperature";
    public static final String LUMINOSITY = "lumen";
    public static final String HUMIDITY = "humid";

    private final Context context;
    private final Sensor[] sensors;
    private final LayoutInflater layoutInflater;

    public SensorsAdapter(Context context, Sensor[] sensors) {
        this.context = context;
        this.sensors = sensors;
        this.layoutInflater = LayoutInflater.from(this.context);
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
            convertView = this.layoutInflater.inflate(R.layout.list_item_sensor_layout, null);
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

        int imageId = this.getMipmapResIdByName(sensor.getType());

        holder.sensorImgView.setImageResource(imageId);

        return convertView;
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(this.getResNameForSensor(resName) , "drawable", pkgName);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

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
}
