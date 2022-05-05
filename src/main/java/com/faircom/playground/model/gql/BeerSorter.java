package com.faircom.playground.model.gql;

import com.faircom.playground.util.SortDirection;

public class BeerSorter {

    private SortDirection abv;
    private SortDirection ibu;

    public SortDirection getAbv() {

        return abv;
    }

    public SortDirection getIbu() {

        return ibu;
    }

    public void setAbv(SortDirection abv) {

        this.abv = abv;
    }

    public void setIbu(SortDirection ibu) {

        this.ibu = ibu;
    }
}
