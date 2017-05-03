package com.jopek.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Tomek on 2017-05-02.
 */
public class Car {
    private StringProperty id;
    private StringProperty producer;
    private StringProperty model;
    private StringProperty productionYear;
    private StringProperty plates;
    private StringProperty flag;

    public Car() {
        this.id = new SimpleStringProperty();
        this.producer = new SimpleStringProperty();
        this.model = new SimpleStringProperty();
        this.productionYear = new SimpleStringProperty();
        this.plates = new SimpleStringProperty();
        this.flag = new SimpleStringProperty();
        this.flag.set("NIE");
    }

    public Car(StringProperty id, StringProperty producer, StringProperty model, StringProperty productionYear, StringProperty plates, StringProperty flag) {
        this.id = id;
        this.producer = producer;
        this.model = model;
        this.productionYear = productionYear;
        this.plates = plates;
        this.flag = flag;
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

    public String getProducer() {
        return producer.get();
    }

    public StringProperty producerProperty() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer.set(producer);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public String getProductionYear() {
        return productionYear.get();
    }

    public StringProperty productionYearProperty() {
        return productionYear;
    }

    public void setProductionYear(String productionYear) {
        this.productionYear.set(productionYear);
    }

    public String getPlates() {
        return plates.get();
    }

    public StringProperty platesProperty() {
        return plates;
    }

    public void setPlates(String plates) {
        this.plates.set(plates);
    }

    public String getFlag() {
        return flag.get();
    }

    public StringProperty flagProperty() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag.set(flag);
    }

    @Override
    public String toString() {
        return "\n" +
                getId() +
                "," +
                getProducer() +
                "," +
                getModel() +
                "," +
                getProductionYear() +
                "," +
                getPlates() +
                "," + getFlag();
    }
}