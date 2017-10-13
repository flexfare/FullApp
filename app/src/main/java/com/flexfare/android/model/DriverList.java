package com.flexfare.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kodenerd on 8/8/17.
 */

public class DriverList implements Parcelable {

    private String name, car_name, car_plate, location, driver_img, driver_phone;

    public DriverList(){}

    public DriverList(String name, String car_name, String car_plate, String location, String driver_img, String driver_phone) {
        this.name = name;
        this.car_name = car_name;
        this.car_plate = car_plate;
        this.location = location;
        this.driver_img = driver_img;
        this.driver_phone = driver_phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_plate() {
        return car_plate;
    }

    public void setCar_plate(String car_plate) {
        this.car_plate = car_plate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDriver_img() {
        return driver_img;
    }

    public void setDriver_img(String driver_img) {
        this.driver_img = driver_img;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.car_name);
        dest.writeString(this.car_plate);
        dest.writeString(this.driver_img);
        dest.writeString(this.driver_phone);
    }
    protected DriverList(Parcel in) {
        this.name = in.readString();
        this.car_name = in.readString();
        this.car_plate = in.readString();
        this.driver_img = in.readString();
        this.driver_phone = in.readString();
    }
    public static final Parcelable.Creator<DriverList> CREATOR = new Parcelable.Creator<DriverList>() {
        @Override
        public DriverList createFromParcel(Parcel source) {
            return new DriverList(source);
        }
        @Override
        public DriverList[] newArray(int size) {
            return new DriverList[size];
        }
    };
}

