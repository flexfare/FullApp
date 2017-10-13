package com.flexfare.android.model;

import java.io.Serializable;

/**
 * Created by Blessyn on 9/3/2017.
 */

public class Driver implements Serializable {

    private String name;
    private String car_type;
    private String registration_number;
    private String driver_number;
    private String about;
    private String carImg;
    private String driverImg;
    private String route;
    private String driver_type;

    public Driver() {}

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDriver_type() {
        return driver_type;
    }

    public void setDriver_type(String driver_type) {
        this.driver_type = driver_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getDriver_number() {
        return driver_number;
    }

    public void setDriver_number(String driver_number) {
        this.driver_number = driver_number;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCarImg() {
        return carImg;
    }

    public void setCarImg(String carImg) {
        this.carImg = carImg;
    }

    public String getDriverImg() {
        return driverImg;
    }

    public void setDriverImg(String driverImg) {
        this.driverImg = driverImg;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "name='" + name + '\'' +
                ", car_type='" + car_type + '\'' +
                ", registration_number='" + registration_number + '\'' +
                ", driver_number='" + driver_number + '\'' +
                ", about='" + about + '\'' +
                ", carImg='" + carImg + '\'' +
                ", driverImg='" + driverImg + '\'' +
                ", route='" + route + '\'' +
                ", driver_type='" + driver_type + '\'' +
                '}';
    }
}