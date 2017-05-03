package com.jopek.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Tomek on 2017-05-02.
 */
public class Registry {
    private StringProperty id;
    private StringProperty clientId;
    private StringProperty carId;
    private StringProperty date;
    private StringProperty rDate;

    public Registry() {
        this.id = new SimpleStringProperty();
        this.clientId = new SimpleStringProperty();
        this.carId = new SimpleStringProperty();
        this.date = new SimpleStringProperty();
        this.rDate = new SimpleStringProperty();
    }

    public Registry(StringProperty id, StringProperty clientId, StringProperty carId, StringProperty date, StringProperty rDate) {
        this.id = id;
        this.clientId = clientId;
        this.carId = carId;
        this.date = date;
        this.rDate = rDate;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getClientId() {
        return clientId.get();
    }

    public StringProperty clientIdProperty() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId.set(clientId);
    }

    public String getCarId() {
        return carId.get();
    }

    public StringProperty carIdProperty() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId.set(carId);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getrDate() {
        return rDate.get();
    }

    public StringProperty rDateProperty() {
        return rDate;
    }

    public void setrDate(String rDate) {
        this.rDate.set(rDate);
    }

    @Override
    public String toString() {
        return "\n" +
                getId() +
                "," +
                getClientId() +
                "," +
                getCarId() +
                "," +
                getDate() +
                "," +
                getrDate();
    }
}

