package com.faircom.playground.util;

public class SortField {

    private String name;

    private SortDirection dir;

    public SortField(String name, SortDirection dir) {

        this.name = name;
        this.dir = dir;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public SortDirection getDir() {

        return dir;
    }

    public void setDir(SortDirection dir) {

        this.dir = dir;
    }
}
