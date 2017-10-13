package com.flexfare.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flexfare.android.AsyncTask.ImageAsyncTask;
import com.flexfare.android.R;

/**
 * Created by kodenerd on 8/8/17.
 */

public class DriverViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public DriverViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }
    public void setName(String name) {
        TextView txt_name = (TextView) mView.findViewById(R.id.name);
        txt_name.setText(name);
    }
    public void setCar_name(String car_name) {
        TextView txt_car_name = (TextView) mView.findViewById(R.id.car_type);
        txt_car_name.setText(car_name);
    }
    public void setCar_plate(String car_plate) {
        TextView carPlate = (TextView) mView.findViewById(R.id.driver_plate);
        carPlate.setText(car_plate);
    }
    public void setLocation(String location) {
        TextView dri_loc = (TextView) mView.findViewById(R.id.driver_location);
        dri_loc.setText(location);
    }
    public void setDriver_img(String driver_img) {
        ImageView driverImg = (ImageView) mView.findViewById(R.id.photo);
        new ImageAsyncTask(driverImg).execute(driver_img);
    }
    public void setDriver_phone(String driver_phone) {
        ImageView driverPhone = (ImageView) mView.findViewById(R.id.dial);
        new ImageAsyncTask(driverPhone).execute(driver_phone);
    }
}
