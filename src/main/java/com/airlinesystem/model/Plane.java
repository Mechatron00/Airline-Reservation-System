package com.airlinesystem.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Plane implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String model;
    private int capacity;
    private String manufacturer;
    private String planeDescription;

    public Plane() {}

    public Plane(String model, int capacity, String manufacturer, String planeDescription) {
        this.model = model;
        this.capacity = capacity;
        this.manufacturer = manufacturer;
        this.planeDescription = planeDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return planeDescription;
    }

    public void setDescription(String description) {
        this.planeDescription= description;
    }
}