package com.jopek.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Tomek on 2017-05-02.
 */
public class Client {
    private StringProperty id;
    private StringProperty surname;
    private StringProperty name;
    private StringProperty city;
    private StringProperty postalCode;
    private StringProperty phone;
    private StringProperty email;

    public Client() {
        this.id = new SimpleStringProperty();
        this.surname = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.city = new SimpleStringProperty();
        this.postalCode = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public Client(StringProperty id, StringProperty surname, StringProperty name, StringProperty city, StringProperty postalCode, StringProperty phone, StringProperty email) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.email = email;
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

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    @Override
    public String toString() {

        return "\n" +
                getId() +
                "," +
                getSurname() +
                "," +
                getName() +
                "," +
                getCity() +
                "," +
                getPostalCode() +
                "," +
                getPhone() +
                "," +
                getEmail();
    }
}
