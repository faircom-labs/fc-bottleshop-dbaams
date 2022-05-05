package com.faircom.playground.model;

public class Beer {

    private int id;
    private int ibu;
    private int breweryId;

    private float abv;
    private float ounces;

    private String name;
    private String style;

    public float getAbv() {

        return abv;
    }

    public int getBreweryId() {

        return breweryId;
    }

    public int getIbu() {

        return ibu;
    }

    public int getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public float getOunces() {

        return ounces;
    }

    public String getStyle() {

        return style;
    }

    public void setAbv(float abv) {

        this.abv = abv;
    }

    public void setBreweryId(int breweryId) {

        this.breweryId = breweryId;
    }

    public void setIbu(int ibu) {

        this.ibu = ibu;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setOunces(float ounces) {

        this.ounces = ounces;
    }

    public void setStyle(String style) {

        this.style = style;
    }
}
