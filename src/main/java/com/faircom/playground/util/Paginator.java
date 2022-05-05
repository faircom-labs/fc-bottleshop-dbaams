package com.faircom.playground.util;

public class Paginator {

    private int start = 1;

    private int count = 10;

    public int getCount() {

        return count;
    }

    public int getStart() {

        return start;
    }

    public void setCount(int count) {

        this.count = count;
    }

    public void setStart(int start) {

        this.start = start;
    }
}
