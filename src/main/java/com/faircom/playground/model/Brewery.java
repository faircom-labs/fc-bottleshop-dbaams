package com.faircom.playground.model;

public class Brewery {

    private int id;

    private String name;
    private String city;
    private String state;

    public String getCity() {

        return city;
    }

    public int getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public String getState() {

        return state;
    }

    public void setCity(String city) {

        this.city = city;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setState(String state) {

        this.state = state;
    }
}
